//Use a AsyncTask for callback to run the timer and send updates to the main thread - done


package com.example.m1027519.textroleplayinggame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import models.*;
import models.Character;


public class StartGame extends Activity implements Runnable {

    TextView gameOutput;
    ArrayList<Room> rooms;
    ArrayList<Item> furniture;
    ArrayList<Item> weapons;
    String name;
    String profession;
    private int intelligence;
    private int wisdom;
    private int strength;
    private int dexterity;
    private int constitution;
    int hp;
    int mana;

    private Timer timer;
    static final String TAG = StartGame.class.getSimpleName();

    private Button backButton;
    private Button forwardButton;
    private Button leftButton;
    private Button rightButton;
    private Button hitButton;
    private Button lookButton;
    private EditText gameInputField;

    private String prevEnemyRoom;
    private String prevEnemy2Room;
    private String inputCommandText;
    int scroll_amount = 0;
    HashMap combatHits;

    Room entrance;
    Room hallway;
    Room kitchen;
    Room livingRoom;
    Room diningRoom;

    Handler handler;
    Character enemy;
    Character player;
    Character enemy2;

    private String gameInput;

    //Enemy enemy;
    @Override
    public void run() {

    }

    public static enum HitPhrases{
        MISS,
        WEAK,
        MEDIUM,
        HARD,
        EXTREMELYHARD,
        BONESHATTERING,
        DECAPITATING
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        Intent intent = getIntent();
        intent.getExtras();
        name = intent.getStringExtra("char_name");
        Log.d(TAG,"Name: "+name);
        List<Character> list = Character.findWithQuery(Character.class, "Select * from Character where name = ?",name);

        for (int i=0; i < list.size(); i++){
            profession = list.get(i).getPlayerClass();
            intelligence = list.get(i).getIntelligence();
            wisdom = list.get(i).getWisdom();
            strength = list.get(i).getStrength();
            dexterity = list.get(i).getDexterity();
            constitution = list.get(i).getConstitution();

            hp = list.get(i).getHitpts();
            mana = list.get(i).getMana();
        }

        rooms = new ArrayList<Room>();
        furniture = new ArrayList<Item>();
        weapons = new ArrayList<Item>();

        gameInputField = (EditText)findViewById(R.id.game_input_field);
        gameInputField.setText("");

        gameOutput = (TextView)findViewById(R.id.game_output_view);
        handler = new Handler();
        initRooms();

        gameOutput.setText("Welcome " + name + ".\n" + "You are going to help me tell my story." + "\n" + entrance.getmDesc());

        //Create character and set the starting room
        player = new Character(name,constitution,intelligence,wisdom,strength,dexterity,profession,hp,mana,false);//get args from bundle
        player.setCurrentRoom(entrance);
        player.getCurrentRoom().getCharacters().add(player);

        initButtonActions();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
               CustomAsyncTask enemyMoveTask = new CustomAsyncTask();
               enemyMoveTask.execute();
            }
        },1000,5000);
    }
    public String getGameInput(){
        return gameInput;
    }
    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    public String loadJSONFromAsset(Context context,String jsonFile){
        String json = null;
        try{
            InputStream is = context.getAssets().open(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public void lookAround(){
        Room current = player.getCurrentRoom();
        showUpdatedContent(current.getmDesc());
        for (int i=0;i<current.getCharacters().size();i++){
            showUpdatedContent(current.getCharacters().get(i).getName()+" is here");
        }
        for(int i=0;i<current.getItems().size();i++){
            showUpdatedContent(current.getItems().get(i).getItemName());
        }
    }
    public void createMap(){
        entrance.setNextRoom(hallway);
        entrance.setPrevRoom(null);
        entrance.setRightRoom(null);
        entrance.setLeftRoom(null);

        hallway.setPrevRoom(entrance);
        hallway.setNextRoom(null);
        hallway.setLeftRoom(null);
        hallway.setRightRoom(kitchen);


        kitchen.setPrevRoom(null);
        kitchen.setNextRoom(diningRoom);
        kitchen.setLeftRoom(hallway);
        kitchen.setRightRoom(null);

        diningRoom.setRightRoom(null);
        diningRoom.setPrevRoom(kitchen);
        diningRoom.setNextRoom(null);
        diningRoom.setLeftRoom(null);
    }
    public void initRooms(){

        JSONObject roomDesc = null;
        JSONObject lighted = null;
        JSONObject dark = null;
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(getApplicationContext(),"rooms.json"));
            JSONArray jsonArray = obj.getJSONArray("Rooms");
            for (int i=0; i < jsonArray.length(); i++){
                roomDesc = jsonArray.getJSONObject(i);
                entrance = new Room("Entrance", roomDesc.getString("Entrance"),0,false);

                hallway = new Room("Hall Way",roomDesc.getString("Main_Hallway"),1,false);
                kitchen = new Room("Kitchen", roomDesc.getString("Kitchen"),2,false);
                livingRoom = new Room("Living Room", roomDesc.getString("Living_Room"),3,false);
                diningRoom = new Room("Dining Room", roomDesc.getString("Dining_Room"),4,false);
                diningRoom.setItems(furniture);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        createMap();

        rooms.add(entrance);
        rooms.add(hallway);
        rooms.add(kitchen);
        rooms.add(livingRoom);
        rooms.add(diningRoom);

        for(int i=0;i<initItems().size();i++){
            if(initItems().get(i).isFurniture()){
                furniture.add(initItems().get(i));
            }else if(initItems().get(i).isWeapon()){
                weapons.add(initItems().get(i));
            }
        }

        generateEnemies();
    }
    //Test room occupancy
    public void testRoomCharCount(){
        Log.d(TAG,"\n");
        for(int i=0; i < rooms.size();i++){
           Log.d(TAG,"Room "+rooms.get(i).getmTitle()+" has "+rooms.get(i).getCharacters().size()+" chars");
        }
    }

    public ArrayList<Item> initItems(){


        ArrayList<Item> itemList = new ArrayList<Item>();
        try{
            JSONObject obj = new JSONObject(loadJSONFromAsset(getApplicationContext(),"items.json"));
            JSONArray itemArray = obj.getJSONArray("Items");
            for (int i=0; i < itemArray.length();i++){
                JSONObject theItem = itemArray.getJSONObject(i);
                String itemName = theItem.getString("Item_name");
                String itemDesc = theItem.getString("Item_desc");
                String itemType = theItem.getString("Item_type");
                Integer damage = theItem.getInt("Damage");
                Integer capacity = theItem.getInt("Capacity");
                Item item = new Item(itemName,itemDesc,itemType,damage,capacity);
                itemList.add(item);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return itemList;
    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void initButtonActions(){
        forwardButton = (Button)findViewById(R.id.forward_button);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { navigateCharacterForward(player);}
        });
        backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateCharacterBack(player);
            }
        });
        rightButton = (Button)findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateCharacterRight(player);
            }
        });
        leftButton = (Button)findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateCharacterLeft(player);
            }
        });
        lookButton = (Button)findViewById(R.id.lookButton);
        lookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookAround();
            }
        });
        hitButton = (Button)findViewById(R.id.hitButton);
        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCombat(enemy);
            }
        });
        hitButton.setVisibility(View.INVISIBLE);
        gameInputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    dismissKeyboard();
                    inputCommandText = gameInputField.getText().toString();
                    doActionFOrCommandString(inputCommandText);
                    Log.d(TAG,inputCommandText);
                    gameInputField.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    public void navigateCharacterLeft(Character character){
        Room current = character.getCurrentRoom();
        if (current.getLeftRoom()!=null){
            current.getCharacters().remove(character);
            character.setCurrentRoom(current.getLeftRoom());
            current = character.getCurrentRoom();
            current.getCharacters().add(character);
            if (!character.isEnemy()){
                showUpdatedContent(character.getCurrentRoom().getmDesc());
            }
        }else{
            if (!character.isEnemy()){
                showEndOfRooms();
            }
        }
        if (!character.isEnemy()){
            showEnemies();
        }
        testRoomCharCount();
    }

    public void navigateCharacterRight(Character character){
        Room current = character.getCurrentRoom();
        if (current.getRightRoom()!=null){
            current.getCharacters().remove(character);
            character.setCurrentRoom(current.getRightRoom());
            current = character.getCurrentRoom();
            current.getCharacters().add(character);
            if (!character.isEnemy()){
                showUpdatedContent(character.getCurrentRoom().getmDesc());
            }
        }else{
            if (!character.isEnemy()){
                showEndOfRooms();
            }
        }
        if (!character.isEnemy()){
            showEnemies();
        }
        testRoomCharCount();
    }

    public void navigateCharacterForward(Character character){
        Room current = character.getCurrentRoom();
        if (current.getNextRoom()!=null){
            current.getCharacters().remove(character);
            character.setCurrentRoom(current.getNextRoom());
            current = character.getCurrentRoom();
            if (!character.isEnemy()){
                showUpdatedContent(character.getCurrentRoom().getmDesc());
            }
            current.getCharacters().add(character);
        }else{
            if (!character.isEnemy()){
                showEndOfRooms();
            }
        }
        if (!character.isEnemy()){
            showEnemies();
        }
        testRoomCharCount();
    }
    public void navigateCharacterBack(Character character){
        Room current = character.getCurrentRoom();
        if (current.getPrevRoom()!=null){
            current.getCharacters().remove(character);
            character.setCurrentRoom(current.getPrevRoom());
            if (!character.isEnemy()){
                showUpdatedContent(character.getCurrentRoom().getmDesc());
            }

            current = character.getCurrentRoom();
            current.getCharacters().add(character);
        }else{
            if (!character.isEnemy()){
                showEndOfRooms();
            }

        }
        if (!character.isEnemy()){
            showEnemies();
        }
       testRoomCharCount();
    }

    public void showEndOfRooms(){
        showUpdatedContent("You see a wall.");
    }

    public void showEnemies(){
        Room current = player.getCurrentRoom();
        for (int i=0;i<current.getCharacters().size();i++){
            if (current.getCharacters().get(i).isEnemy()){
                showUpdatedContent(current.getCharacters().get(i).getName()+" is here");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    public void showUpdatedContent(String content){
        gameOutput.setMovementMethod(new ScrollingMovementMethod());
        gameOutput.setText(gameOutput.getText() + "\n" + content);

        if (gameOutput.getLineCount() > 11){
            scroll_amount = scroll_amount + gameOutput.getLineHeight();
            gameOutput.scrollTo(0,scroll_amount);
        }
    }

    public void generateEnemies(){
        enemy = new Character("BadGuy",10,10,10,10,10,"",100,100,true);
        enemy2 = new Character("BadGuy2",10,10,10,10,10,"",100,100,true);

        enemy.setCurrentRoom(hallway);
        enemy2.setCurrentRoom(kitchen);

        hallway.getCharacters().add(enemy);
        kitchen.getCharacters().add(enemy2);

    }
    public void doEmemyHit(int rollHit, Character player, Character target){
        switch(rollHit){
            case 0:
                showUpdatedContent(target.getName() + " misses!");
                break;
            case 1:
                player.setHitpts(player.getHitpts()-2);
                showUpdatedContent(target.getName()+ " scratched you!");
                break;
            case 2:
                showUpdatedContent(target.getName()+" slapped you!");
                player.setHitpts(player.getHitpts()-3);
                break;
            case 3:
                player.setHitpts(player.getHitpts()-4);
                showUpdatedContent(target.getName()+" punched you!");
                break;
            case 4:
                player.setHitpts(player.getHitpts()-5);
                showUpdatedContent(target.getName()+" clobbered you!");
                break;
            case 5:
                player.setHitpts(player.getHitpts()-6);
                showUpdatedContent(target.getName()+"Yells Shoryuken!!");
                showUpdatedContent(target.getName()+"Yells Shoryuken!!");
                showUpdatedContent(target.getName()+"Yells Shoryuken!!");
                break;
            case 6:
                player.setHitpts(player.getHitpts()-9);
                showUpdatedContent(enemy.getName()+" Sent you flying!");
                break;
        }
    }
    public HitPhrases doHit(int rollHit, Character target){
        HitPhrases hitPhrase = HitPhrases.values()[rollHit];
        switch (hitPhrase){
            case MISS:
                showUpdatedContent("You pathetic fool! You missed!");
                break;
            case WEAK:
                showUpdatedContent("Cant you hit harder than that wussy!");
                showUpdatedContent("You hit " + target.getName());
                target.setHitpts(target.getHitpts()-2);
                break;
            case MEDIUM:
                showUpdatedContent("Now you starting to hit like you mean it");
                showUpdatedContent("You hit " + target.getName());
                target.setHitpts(target.getHitpts()-3);
                break;
            case HARD:
                showUpdatedContent("You put some testosterone in that one!");
                showUpdatedContent("You hit " + target.getName());
                target.setHitpts(target.getHitpts()-6);
                break;
            case EXTREMELYHARD:
                showUpdatedContent("Wow, Keep it up!");
                showUpdatedContent("You hit " + target.getName());
                target.setHitpts(target.getHitpts()-7);
                break;
            case BONESHATTERING:
                showUpdatedContent("Boneshattering!!!");
                showUpdatedContent("You hit " + target.getName());
                target.setHitpts(target.getHitpts()-9);
                break;
            default:
                break;
        }
        return hitPhrase;
    }
    private String randomizeNPCActions(){
        Random randomAction = new Random();
        String actionString="";
        int npcAction = randomAction.nextInt(2-0);
        switch (npcAction){
            case 0:
                actionString = " glares at you.";
                break;
            case 1:
                actionString = " laughs out loud to their self.";
                break;
            case 2:
                actionString = " twiddles their thumbs.";
                break;
        }
        return actionString;
    }
    private void startEnemyNavigationLogic(Character enemy){
        Random randomDir = new Random();
        int enemyDir = randomDir.nextInt(4-0);
        if (!enemy.isDead()){
            switch (enemyDir){
                case 0:
                    navigateCharacterForward(enemy);
                    break;
                case 1:
                    navigateCharacterBack(enemy);
                    break;
                case 2:
                    navigateCharacterRight(enemy);
                    break;
                case 3:
                    navigateCharacterLeft(enemy);
                    break;
            }
        }
    }
    public void showEnemymovement(Character character){
        Log.d(TAG,character.getName()+" is in "+character.getCurrentRoom().getmTitle());
        if (!character.getCurrentRoom().getmTitle().equalsIgnoreCase(prevEnemyRoom) && player.getCurrentRoom().getmTitle().equalsIgnoreCase(prevEnemyRoom)){
            showUpdatedContent(character.getName() + " just left.");
        }
        if (player.getCurrentRoom().getmTitle().equalsIgnoreCase(character.getCurrentRoom().getmTitle())&&character.getCurrentRoom().getmTitle().equalsIgnoreCase(prevEnemyRoom)){
            showUpdatedContent(character.getName() + " is here and"+randomizeNPCActions());
        }
        if (!character.getCurrentRoom().getmTitle().equalsIgnoreCase(prevEnemyRoom)&&player.getCurrentRoom().getmTitle().equalsIgnoreCase(enemy.getCurrentRoom().getmTitle())){
            showUpdatedContent(character.getName() + " just walked in.");
        }

    }
    public void displayHPandMana(){
        showUpdatedContent(enemy.getName()+ " HP: " + enemy.getHitpts());
        showUpdatedContent("Your HP:" + player.getHitpts());
        showUpdatedContent("Your Mana:"+player.getMana());
    }
    public void doCombat(final Character target){
        showUpdatedContent("You swing at " + target.getName());
        Random random = new Random();
        int playerDice = random.nextInt(5 - 0)+0;
        final int enemyDice = random.nextInt(5-0)+0;
        HitPhrases pHitPhrase = doHit(playerDice,target);
        displayHPandMana();
        if (pHitPhrase != HitPhrases.MISS){
            target.setHitpts(target.getHitpts()-10);
            Log.d(TAG, "player roll "+playerDice);
            Log.d(TAG, "enemy hp "+target.getHitpts());
            if (target.getHitpts() <= 0){
                showUpdatedContent("You killed "+ target.getName());
                target.setDead(true);
            }
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               doEmemyHit(enemyDice,player,target);
            }
        },200);
    }
    public void doActionFOrCommandString(String command){

        if (command.equalsIgnoreCase("look around")){
            lookAround();
        }
    }
    private class CustomAsyncTask extends AsyncTask<Void,Void,Void>{

        boolean containsEnemy;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prevEnemyRoom = enemy.getCurrentRoom().getmTitle();
            prevEnemy2Room = enemy2.getCurrentRoom().getmTitle();

            startEnemyNavigationLogic(enemy);
            startEnemyNavigationLogic(enemy2);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showEnemymovement(enemy);
            showEnemymovement(enemy2);
            Room currentRoom = player.getCurrentRoom();
            for(int i=0; i < currentRoom.getCharacters().size();i++){
                if (currentRoom.getCharacters().get(i).isEnemy()){
                    hitButton.setVisibility(View.VISIBLE);
                }else{
                    hitButton.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }



}
