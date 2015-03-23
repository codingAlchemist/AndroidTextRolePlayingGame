package com.example.m1027519.textroleplayinggame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONException;

import java.util.List;
import static com.orm.query.Condition.prop;
import models.Character;


public class ConfirmCharacterActivity extends DialogFragment {
    private TextView mCharacterName;
    private final String TAG = "ConfirmCharacterActivity:";
    private Intent intent;
    private Button startGameButton;
    private String name;
    private String profession;
    private Character character;
    private int intelligence;
    private int wisdom;
    private int strength;
    private int dexterity;
    private int constitution;
    private int hp;
    private int mana;

    public ConfirmCharacterActivity newInstance(){
        ConfirmCharacterActivity df = new ConfirmCharacterActivity();
        Bundle args = new Bundle();
        Log.d(TAG,"char name " + name);
        args.putString("char_name",name );
        args.putString("char_class",profession );
        args.putInt("int", intelligence);
        args.putInt("wis", wisdom);
        args.putInt("str", strength);
        args.putInt("dex", dexterity);
        args.putInt("con", constitution);
        args.putInt("hp",hp);
        args.putInt("mana",mana);


        df.setArguments(args);
        return df;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.activity_confirm_character,null);
        intent = new Intent();
        populateFields(dialog);

        builder.setView(dialog);
        builder.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveCharacter();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        populateFields(dialog);


        return builder.create();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void populateFields(View view){
        Bundle bundle = getArguments();
        name = bundle.getString("char_name");
        profession = bundle.getString("char_class");

        intelligence = bundle.getInt("int");
        wisdom = bundle.getInt("wis");
        strength = bundle.getInt("str");
        dexterity = bundle.getInt("dex");
        constitution = bundle.getInt("con");

        hp = bundle.getInt("hp");
        mana = bundle.getInt("mana");
        character = new Character(name,
                strength,
                intelligence,
                dexterity,
                wisdom,
                constitution,
                profession,
                hp,
                mana,false);
        TextView name_field = (TextView)view.findViewById(R.id.character_name);
        name_field.setText(name);
        TextView mana_field = (TextView)view.findViewById(R.id.character_mana);
        TextView hp_field = (TextView)view.findViewById(R.id.character_hp);

        TextView str_field = (TextView)view.findViewById(R.id.strength_stat);
        TextView int_field = (TextView)view.findViewById(R.id.intelligence_stat);


    }

    public void saveCharacter(){
        character.save();
    }


}
