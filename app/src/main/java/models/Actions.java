package models;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by M1027519 on 1/14/2015.
 */
public class Actions {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList<Item> items;
    public Actions(Context context, ArrayList<Item>itemsIn){
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
            Log.d("TAG", "item names are "+item.getItemName());
            if (target.equalsIgnoreCase(item.getItemName())){
                Log.d("TAG","Desc is "+item.getItemDesc());
                return item.getItemDesc();
            }
        }
        return null;
    }

    public void flipSwitch(Switch theSwitch){
        if (theSwitch.getIsOn()){
            theSwitch.setOn(false);
        }else{
            theSwitch.setOn(true);
        }
    }
    public String getActionCommand(String command,Character player){
        String output = "";
        StringBuilder stringBuilder = new StringBuilder(output);
        if (command.equalsIgnoreCase("Look")||command.equalsIgnoreCase("Look around")){
            Log.d(TAG,"Looking around");
            stringBuilder.append(lookAround(player));
            return output;
        }

        if (command.equalsIgnoreCase("flip switch")||command.equalsIgnoreCase("turn on switch")){
            Log.d(TAG,"flipping on switch");
            Room room = player.getCurrentRoom();
            if (room.getLightSwitch() != null){
                Switch currentSwitch = room.getLightSwitch();
                flipSwitch(currentSwitch);
            }
            return output;
        }
        return null;
    }
    /**
     *
     */
    public String lookAround(Character player){
        Room current = player.getCurrentRoom();
        String status = current.isLighted() ? "isLighted" : "Not lighted";
        Log.d("TAG","Current is "+ status);
        String output = current.getmDesc();
        StringBuilder stringBuilder = new StringBuilder(output);
        if (current.isLighted()){
            for (int i=0;i<current.getCharacters().size();i++){
                stringBuilder.append(current.getCharacters().get(i).getName()+" is here\n");
            }
            for(int i=0;i<current.getItems().size();i++){
                stringBuilder.append(current.getItems().get(i).getItemName());
            }
        }

        if (current.getLightSwitch()!=null){
            String switchState = current.getLightSwitch().getIsOn()?"on":"off";
            stringBuilder.append("There is a light switch here and it is "+switchState+"\n");
        }
        Log.d("TAG", "num enemies"+current.getEnemies().size());
        showEnemies(current);
        return output;
    }
    /**
     *
     */
    public ArrayList<Enemy> showEnemies(Room current){
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        for (int i=0; i < current.getEnemies().size(); i++){
            enemies.add(current.getEnemies().get(i));
        }
        return enemies;
    }
}
