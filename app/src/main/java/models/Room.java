package models;

import android.content.Context;

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
    private String mDesc;
    private String mTitle;
    private int roomID;
    private boolean isLighted;
    private Room leftRoom;
    private Room rightRoom;
    private Room prevRoom;
    private Room nextRoom;
    private ArrayList<String> mRoomIsLightedDesc;
    public Room(){}
    //TODO: finish JSONLoader util class
    public Room( String title, String desc, int roomIDIn,boolean isLightedIn){
        this.mTitle = title;
        this.roomID = roomIDIn;
        this.isLighted = isLightedIn;
        this.characters = new ArrayList<Character>();
        this.items = new ArrayList<Item>();
        this.mRoomIsLightedDesc = new ArrayList<String>();
//        try {
//            JSONObject obj = new JSONObject(loadJSONFromAsset(getApplicationContext(),"rooms.json"));
//            JSONArray jsonArray = obj.getJSONArray("Rooms");
//            for (int i=0; i < jsonArray.length(); i++){
//                roomDesc = jsonArray.getJSONObject(i);
//                entrance = new Room("Entrance", roomDesc.getString("Entrance"),0,false);
//
//                hallway = new Room("Hall Way",roomDesc.getString("Main_Hallway"),1,false);
//                kitchen = new Room("Kitchen", roomDesc.getString("Kitchen"),2,false);
//                livingRoom = new Room("Living Room", roomDesc.getString("Living_Room"),3,false);
//                diningRoom = new Room("Dining Room", roomDesc.getString("Dining_Room"),4,false);
//                diningRoom.setItems(furniture);
//            }
//
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
        for (int i=0; i < 2;i++){
            mRoomIsLightedDesc.add(desc);
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
        return mDesc;
    }
    @RoomInfo(setDesc = "Room Desc")
    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
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

    public boolean isLighted() {
        return isLighted;
    }

    public void setLighted(boolean isLighted) {
        this.isLighted = isLighted;
    }

    public ArrayList<String> getmRoomIsLightedDesc() {
        return mRoomIsLightedDesc;
    }

    public void setmRoomIsLightedDesc(ArrayList<String> mRoomIsLightedDesc) {
        this.mRoomIsLightedDesc = mRoomIsLightedDesc;
    }
}
