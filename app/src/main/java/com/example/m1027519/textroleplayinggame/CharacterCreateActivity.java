package com.example.m1027519.textroleplayinggame;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.Random;

import models.Character;


public class CharacterCreateActivity extends Activity {
    protected final String TAG = "My Tag";
    boolean dialogExists = false;
    private Spinner playerClassSpinner;
    private CustomSpinnerAdapater adapater;
    private Button rerollButton;
    private Button saveButon;
    private Button startGameButton;
    private EditText nameInput;
    private TextView hitPoints;
    private Character character;
    private Intent intent;


    private int intelligence;
    private int wisdom;
    private int strength;
    private int dexterity;
    private int constitution;
    private int hp;
    private int mana;

    ConfirmCharacterActivity confirmCharacterDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_char);
        final String TAG = "ConfirmCharacterActivity:";

        intent = new Intent(this, StartGame.class);
        playerClassSpinner = (Spinner)findViewById(R.id.player_class_spinner);
        nameInput = (EditText)findViewById(R.id.character_name_edit_text);
        hitPoints = (TextView)findViewById(R.id.hit_points);
        adapater = new CustomSpinnerAdapater(getResources().getStringArray(R.array.player_classes));
        confirmCharacterDialog = new ConfirmCharacterActivity();
        playerClassSpinner.setAdapter(adapater);
        playerClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getAdapter().getItem(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rerollButton = (Button)findViewById(R.id.reroll_button);
        rerollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollForStats();
            }
        });



        rollForStats();
        saveButon = (Button)findViewById(R.id.save_character_button);
        saveButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passData();
                if (!dialogExists){
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    confirmCharacterDialog.show(ft,"dialog");
                    dialogExists = true;
                }else {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.remove(confirmCharacterDialog);
                    dialogExists = false;
                }

            }
        });
        startGameButton = (Button)findViewById(R.id.start_game_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
            }
        });

       nameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if (actionId == EditorInfo.IME_ACTION_DONE){
                   dismissKeyboard();
                   intent.putExtra("char_name",nameInput.getText().toString().toLowerCase());
                   return true;
               }
               return false;
           }
       });
    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void saveCharacter(){
        character = new Character(nameInput.getText().toString(),
                strength,
                intelligence,
                dexterity,
                wisdom,
                constitution,
                playerClassSpinner.getSelectedItem().toString(),
                hp,
                mana,false);
        character.setExp(0);
        character.setLevel(0);
        character.save();

    }

    public void rollForStats(){


        Random random = new Random();

        intelligence = random.nextInt(10-0)+0;
        wisdom = random.nextInt(10-0)+0;
        strength = random.nextInt(10-0)+0;
        dexterity = random.nextInt(10-0)+0;
        constitution = random.nextInt(10-0)+0;

        hp = (strength*10) + constitution;
        mana = (intelligence * 10) + wisdom;

        TextView intTV = (TextView)findViewById(R.id.intelligence_stat);
        TextView wisTV = (TextView)findViewById(R.id.wisdom_stat);
        TextView strTV = (TextView)findViewById(R.id.strength_stat);
        TextView dexTV = (TextView)findViewById(R.id.dexterity_stat);
        TextView conTV = (TextView)findViewById(R.id.constitution_stat);

        TextView hpTV = (TextView)findViewById(R.id.hit_points);
        TextView manaTV = (TextView)findViewById(R.id.mana_points);

        intTV.setText(Integer.toString(intelligence));
        wisTV.setText(Integer.toString(wisdom));
        strTV.setText(Integer.toString(strength));
        dexTV.setText(Integer.toString(dexterity));
        conTV.setText(Integer.toString(constitution));

        hpTV.setText(Integer.toString(hp));
        manaTV.setText(Integer.toString(mana));

    }

    public void passData(){
        Bundle args = new Bundle();

        args.putString("char_name",nameInput.getText().toString().toLowerCase() );
        args.putString("char_class",playerClassSpinner.getSelectedItem().toString() );
        args.putInt("int", intelligence);
        args.putInt("wis", wisdom);
        args.putInt("str", strength);
        args.putInt("dex", dexterity);
        args.putInt("con", constitution);
        args.putInt("hp",hp);
        args.putInt("mana",mana);

        confirmCharacterDialog.setArguments(args);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomSpinnerAdapater extends BaseAdapter{
        private String[] mList;
        public CustomSpinnerAdapater(String[] list){
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.length;
        }

        @Override
        public String getItem(int position) {
            return mList[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Context context = getApplicationContext();
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_spinner_item,null);
            TextView className = (TextView)convertView.findViewById(R.id.player_class_title);
            className.setText(mList[position]);
            return convertView;
        }
    }
}
