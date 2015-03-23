package models;

import android.util.Log;

/**
 * Created by M1027519 on 1/14/2015.
 */
public class Actions {
    private final String TAG = getClass().getSimpleName();
    private Character character;

    public Actions(Character characterIn){
        character = characterIn;
    }

    public void Swing(){
        Log.d(TAG,"The character swings");
    }
    public void Cast(){
        Log.d(TAG,"The character cast a spell");
    }
    public void Run(){
        Log.d(TAG,"The character runs");
    }
    public void Walk(){
        Log.d(TAG,"The character walks");
    }

}
