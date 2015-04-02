package models;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by M1027519 on 1/14/2015.
 */
public class Actions {
    private final String TAG = getClass().getSimpleName();
    private Character character;
    private Context mContext;
    private ArrayList<Item> items;
    public Actions(Character characterIn, Context context, ArrayList<Item>itemsIn){
        character = characterIn;
        mContext = context;
        items = itemsIn;
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

    public String LookAt(String thing){
        String look = "Look at";
        StringBuilder stringBuilder = new StringBuilder(look);
        stringBuilder = stringBuilder.append(thing);
        String target = stringBuilder.substring(6,stringBuilder.length());
        for (Item item : items){
            if (target.equalsIgnoreCase(item.getItemName())){
                Log.d("TAG","Desc is "+item.getItemDesc());
                return item.getItemDesc();
            }
        }
        return null;
    }

}
