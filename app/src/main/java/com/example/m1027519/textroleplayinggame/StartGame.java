//Use a AsyncTask for callback to run the timer and send updates to the main thread - done


package com.example.m1027519.textroleplayinggame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import models.*;
import models.Character;


public class StartGame extends Activity {
    private ScrollView gameOutputScroller;
    private TextView gameOutput;
    private String name;
    private String profession;
    private int intelligence;
    private int wisdom;
    private int strength;
    private int dexterity;
    private int constitution;
    private int hp;
    private int mana;

    private Timer timer;
    static final String TAG = StartGame.class.getSimpleName();

    private Button backButton;
    private Button forwardButton;
    private Button leftButton;
    private Button rightButton;
    private Button hitButton;
    private Button lookButton;
    private EditText gameInputField;
    private String lookTarget;
    private String prevEnemyRoom;
    private String prevEnemy2Room;
    private String inputCommandText;
    private HashMap combatHits;

    private Room entrance;
    private Room hallway;
    private Room kitchen;
    private Room livingRoom;
    private Room diningRoom;

    private Handler handler;
    private Enemy enemy;

    private Character player;
    private Container backpack;
    private Weapon sword;
    //private Enemy enemy2;

    private Actions actions;
    private String gameInput;
    private Switch Light_switch;



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

        gameOutputScroller = (ScrollView)findViewById(R.id.game_log_scroller);
        gameInputField = (EditText)findViewById(R.id.game_input_field);
        gameInputField.setText("");
        gameOutput = (TextView)findViewById(R.id.game_output_view);
        handler = new Handler();
        initRooms();

        //Initial message to user
        gameOutput.setText("Welcome " + name + ".\n" + "You are going to help me tell my story." + "\n" + entrance.getmDesc());

        //Create character and set the starting room
        player = new Character(name,1,constitution,intelligence,wisdom,strength,dexterity,profession);//get args from bundle
        player.setCurrentRoom(entrance);
        player.getCurrentRoom().getCharacters().add(player);

        //Set starting inventory
        backpack = new Container(this,"Backpack");
        sword = new Weapon("Sword",10,this);

        player.getItemsInInv().add(backpack);
        player.getItemsInInv().add(sword);

        initButtonActions();

        //Set timer to start AI
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
               CustomAsyncTask enemyMoveTask = new CustomAsyncTask();
               enemyMoveTask.execute();
            }
        },1000,5000);

        ArrayList<Item> allItems = new ArrayList<Item>();
        allItems.add(backpack);
        allItems.add(sword);
        allItems.add(Light_switch);
        actions = new Actions(player,this,allItems);
    }

    /**
     *
     * @return
     */
    public String getGameInput(){
        return gameInput;
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    /**
     *
     * @param command
     */
    public void doActionFOrCommandString(String command){
        actions.LookAt(command);
//        if (command.equalsIgnoreCase("look")){
//            lookAround();
//
//        }else if (command.equalsIgnoreCase("flip switch") || (command.equalsIgnoreCase("turn on switch"))){
//            Log.d("TAG", "flipping switch");
//            Room current = player.getCurrentRoom();
//            if (current.getLightSwitch() != null){
//                if (!current.getLightSwitch().getIsOn()){
//                    current.setLighted(true);
//                    current.getLightSwitch().setOn(true);
//                    showUpdatedContent("Turned on light switch");
//                    showUpdatedContent(current.getmDesc());
//                    Log.d("TAG", "turning on switch");
//                }else{
//                    current.setLighted(false);
//                    current.getLightSwitch().setOn(false);
//                    showUpdatedContent(current.getmDesc());
//                    showUpdatedContent("Turned off light switch");
//                    Log.d("TAG", "turning off switch");
//                }
//            }else{
//                showUpdatedContent("There is no light switch here");
//            }
//        }else if(command.equalsIgnoreCase("look at")){
//            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//            alertBuilder.setTitle("Look at what?");
//            View alertView = new View(this);
//            alertBuilder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            }).setPositiveButton("Ok",new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            }).create();
//            alertBuilder.show();
//        }else{
//            showUpdatedContent("Current commands are 'flip switch' or 'turn on switch' ");
//        }
    }

    /**
     *
     */
    public void lookAround(){
        Room current = player.getCurrentRoom();
        String status = current.isLighted() ? "isLighted" : "Not lighted";
        Log.d("TAG","Current is "+ status);
        showUpdatedContent(current.getmDesc());
        if (current.isLighted()){
            for (int i=0;i<current.getCharacters().size();i++){
                showUpdatedContent(current.getCharacters().get(i).getName()+" is here");
            }
            for(int i=0;i<current.getItems().size();i++){
                showUpdatedContent(current.getItems().get(i).getItemName());
            }
        }
        for (int i=0; i < current.getItems().size();i++){
            if (current.getItems().get(i).equals(Light_switch)){
                Switch theSwitch = (Switch)current.getItems().get(i);
                String switchState = theSwitch.getIsOn()?"on":"off";
                showUpdatedContent("There is a light switch here and it is "+switchState);
            }
        }
        if (current.getLightSwitch()!=null){
            String switchState = current.getLightSwitch().getIsOn()?"on":"off";
            showUpdatedContent("There is a light switch here and it is "+switchState);
        }
        Log.d("TAG", "num enemies"+current.getEnemies().size());
        showEnemies();
    }
    /**
     *
     */
    public void showEnemies(){
        Room current = player.getCurrentRoom();
        if (current.getEnemies().size() > 0){
            hitButton.setVisibility(View.VISIBLE);
            for (int i=0; i < current.getEnemies().size(); i++){
                showUpdatedContent(current.getCharacters().get(i).getName()+" is here");
            }
        }else{
            hitButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     *
     */
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

    /**
     *
     */
    public void initRooms(){
        Light_switch = new Switch(false,getApplicationContext());
        entrance = new Room("Entrance", "Entrance",0,false,getApplicationContext());
        entrance.setLightSwitch(Light_switch);

        hallway = new Room("Hall Way","Main_Hallway",1,false,getApplicationContext());
        kitchen = new Room("Kitchen", "Kitchen",2,false,getApplicationContext());
        livingRoom = new Room("Living Room","Living_Room",3,false,getApplicationContext());
        diningRoom = new Room("Dining Room","Dining_Room",4,false,getApplicationContext());
        createMap();
        generateEnemies();
    }


    /**
     *
     */
    private void dismissKeyboard() {
        gameInput = gameInputField.getText().toString();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    /**
     *
     */
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

    /**
     *
     * @param character
     */
    public void navigateCharacterLeft(Character character){
        Room current = character.getCurrentRoom();
        if (current.getLeftRoom()!=null){
            current.getCharacters().remove(character);
            character.setCurrentRoom(current.getLeftRoom());
            current = character.getCurrentRoom();
            current.getCharacters().add(character);
            lookAround();
        }else{
            lookAround();
            showEndOfRooms();
        }
    }
    /**
     *
     * @param character
     */
    public void navigateCharacterRight(Character character){
        Room current = character.getCurrentRoom();
        if (current.getRightRoom()!=null){
            current.getCharacters().remove(character);
            character.setCurrentRoom(current.getRightRoom());
            current = character.getCurrentRoom();
            current.getCharacters().add(character);
            lookAround();
        }else{
            lookAround();
            showEndOfRooms();
        }
    }

    /**
     *
     * @param character
     */
    public void navigateCharacterForward(Character character){
        Room current = character.getCurrentRoom();
        if (current.getNextRoom()!=null){
            current.getCharacters().remove(character);
            character.setCurrentRoom(current.getNextRoom());
            current = character.getCurrentRoom();
            current.getCharacters().add(character);
            lookAround();
        }else{
            lookAround();
            showEndOfRooms();
        }
    }

    /**
     *
     * @param character
     */
    public void navigateCharacterBack(Character character){
        Room current = character.getCurrentRoom();
        if (current.getPrevRoom()!=null){
            current.getCharacters().remove(character);
            character.setCurrentRoom(current.getPrevRoom());
            current = character.getCurrentRoom();
            current.getCharacters().add(character);
            lookAround();
        }else{
            lookAround();
            showEndOfRooms();
        }
    }


    /**
     *
     */
    public void showEndOfRooms(){
        showUpdatedContent("You see a wall.");
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

    /**
     *
     * @param content
     */
    public void showUpdatedContent(String content){
        gameOutput.setText(gameOutput.getText() + "\n" + content);
        gameOutputScroller.post(new Runnable() {
            @Override
            public void run() {
                gameOutputScroller.fullScroll(View.FOCUS_DOWN);
            }
        });

    }

    /**
     *
     */
    public void generateEnemies(){
        enemy = new Enemy("BadGuy",1,10,10,10,10,10,"BadGuyClass");
        //enemy2 = new Enemy("BadGuy2",1,10,10,10,10,10,"BadBuyClass");

        enemy.setCurrentRoom(hallway);
        //enemy2.setCurrentRoom(kitchen);

        hallway.getEnemies().add(enemy);
        //kitchen.getEnemies().add(enemy2);

    }

    /**
     *
     * @param rollHit
     * @param player
     * @param target
     */
    public void doEmemyHit(int rollHit, Character player, Enemy target){
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


    /**
     *
     * @param rollHit
     * @param target
     * @return
     */
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

    /**
     *
     *
     */
    public void showEnemymovement(Enemy npc){
        Room current = npc.getCurrentRoom();
        Log.d(TAG,npc.getName()+" is in "+npc.getCurrentRoom().getmTitle());
        if (current.isLighted()){
            if (!npc.getCurrentRoom().getmTitle().equalsIgnoreCase(prevEnemyRoom) && player.getCurrentRoom().getmTitle().equalsIgnoreCase(prevEnemyRoom)){
                showUpdatedContent(npc.getName() + " just left.");
            }
            if (player.getCurrentRoom().getmTitle().equalsIgnoreCase(npc.getCurrentRoom().getmTitle())&&npc.getCurrentRoom().getmTitle().equalsIgnoreCase(prevEnemyRoom)){
                showUpdatedContent(npc.getName() + " is here and"+npc.randomizeNPCActions());
            }
            if (!npc.getCurrentRoom().getmTitle().equalsIgnoreCase(prevEnemyRoom)&&player.getCurrentRoom().getmTitle().equalsIgnoreCase(enemy.getCurrentRoom().getmTitle())){
                showUpdatedContent(npc.getName() + " just walked in.");
            }
        }


    }

    /**
     *
     */
    public void displayHPandMana(){
        showUpdatedContent(enemy.getName() + " HP: " + enemy.getHitpts());
        showUpdatedContent("Your HP:" + player.getHitpts());
        showUpdatedContent("Your Mana:"+player.getMana());
    }

    /**
     *
     * @param target
     */
    public void doCombat(final Enemy target){
        showUpdatedContent("You swing at " + target.getName());
        Random random = new Random();
        int playerDice = random.nextInt(5 - 0)+0;
        final int enemyDice = random.nextInt(5-0)+0;
        HitPhrases pHitPhrase = doHit(playerDice,target);
        displayHPandMana();
        if(target.getCurrentRoom().getmTitle().equalsIgnoreCase(player.getCurrentRoom().getmTitle())){
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
                    doEmemyHit(enemyDice, player, target);
                }
            }, 200);
        }
    }



    /**
     *
     */
    private class CustomAsyncTask extends AsyncTask<Void,Void,Void>{

        boolean containsEnemy;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prevEnemyRoom = enemy.getCurrentRoom().getmTitle();
            //prevEnemy2Room = enemy2.getCurrentRoom().getmTitle();

            //startEnemyNavigationLogic(enemy);
            //startEnemyNavigationLogic(enemy2);
            enemy.startEnemyNavigationLogic();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showEnemymovement(enemy);
            //showEnemymovement(enemy2);
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
