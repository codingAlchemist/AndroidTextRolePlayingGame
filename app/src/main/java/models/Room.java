package models;

import android.content.Context;
import android.util.Log;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by M1027519 on 1/14/2015.
 */
public class Room extends SugarRecord<Room>{
    private ArrayList<Item> items;
    private ArrayList<Character> characters;
    private ArrayList<Enemy> enemies;
    private String mDesc;
    private String mTitle;
    private int roomID;
    private boolean isLighted;
    private boolean hasLightSwitch;
    private String mRoomIsLightedDesc;
    private String mRoomIsDarkDesc;
    private Room leftRoom;
    private Room rightRoom;
    private Room prevRoom;
    private Room nextRoom;
    private Context mContext;
    private Switch lightSwitch;
    public Room(){}
    //TODO: finish JSONLoader util class
    public Room( String title,String room, int roomIDIn,boolean isLightedIn, Context contextIn){
        this.mContext = contextIn;
        this.mTitle = title;
        this.roomID = roomIDIn;
        this.isLighted = isLightedIn;
        this.characters = new ArrayList<Character>();
        this.enemies = new ArrayList<Enemy>();
        this.items = new ArrayList<Item>();
        this.hasLightSwitch = false;
        JSONObject roomDesc;
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(mContext,"rooms.json"));
            JSONArray jsonArray = obj.getJSONArray("Rooms");
            for (int i=0; i < jsonArray.length(); i++){
                roomDesc = jsonArray.getJSONObject(i);
                mRoomIsLightedDesc = roomDesc.getJSONArray(room).getJSONObject(0).getString("Lighted");
                mRoomIsDarkDesc = roomDesc.getJSONArray(room).getJSONObject(0).getString("Dark");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
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
    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getmDesc() {
        if (isLighted){
            return mRoomIsLightedDesc;
        }
        return mRoomIsDarkDesc;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Room getLeftRoom() {
        return leftRoom;
    }

    public void setLeftRoom(Room leftRoom) {
        this.leftRoom = leftRoom;
    }

    public Room getRightRoom() {
        return rightRoom;
    }

    public void setRightRoom(Room rightRoom) {
        this.rightRoom = rightRoom;
    }

    public Room getPrevRoom() {
        return prevRoom;
    }

    public void setPrevRoom(Room prevRoom) {
        this.prevRoom = prevRoom;
    }

    public Room getNextRoom() {
        return nextRoom;
    }

    public void setNextRoom(Room nextRoom) {
        this.nextRoom = nextRoom;
    }

    public ArrayList<Item> getItems() {

        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;

    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public boolean isLighted() {
        return isLighted;
    }

    public void setLighted(boolean isLighted) {
        this.isLighted = isLighted;
    }

    public void setmRoomIsLightedDesc(String mRoomIsLightedDesc) {
        this.mRoomIsLightedDesc = mRoomIsLightedDesc;
    }

    public Switch getLightSwitch() {
        return lightSwitch;
    }

    public void setLightSwitch(Switch lightSwitch) {
        this.lightSwitch = lightSwitch;
    }

    public String getmRoomIsLightedDesc() {
        return mRoomIsLightedDesc;
    }

    public String getmRoomIsDarkDesc() {
        return mRoomIsDarkDesc;
    }

    public void setmRoomIsDarkDesc(String mRoomIsDarkDesc) {
        this.mRoomIsDarkDesc = mRoomIsDarkDesc;
    }
}
