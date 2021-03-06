package models;

import android.content.Context;
import android.util.Log;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by M1027519 on 1/14/2015.
 */
public class Item extends SugarRecord<Item>{

    private String ItemName;
    private boolean isInventoryItem;
    private boolean isWeapon;
    private boolean isContainer;
    private boolean isFurniture;
    private String isConsumable;
    private boolean isSwitch;
    private String ItemType;
    private Context mContext;
    private String itemDesc;
    private int damage;
    private int capacity;
    private String shortDesc;
    private String longDesc;

    public Item(String nameToGet, Integer damageIn,Integer capacityIn, Context context){
        mContext = context;
        JSONObject itemObj;
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(mContext,"items.json"));
            JSONArray jsonArray = obj.getJSONArray("Items");
            for (int i=0; i < jsonArray.length();i++){
                itemObj = jsonArray.getJSONObject(i);
                ItemName = itemObj.getJSONArray(nameToGet).getJSONObject(0).getString("Item_name");
                ItemType = itemObj.getJSONArray(nameToGet).getJSONObject(0).getString("Item_type");
                Log.d("TAG", "Item name is "+ItemName);
                Log.d("TAG", "Item type is "+ItemType);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        damage = damageIn;
        capacity = capacityIn;
    }


    public Item(String nameToGet, Integer damageIn,Integer capacityIn, Context context,String sDesc){
        mContext = context;
        damage = damageIn;
        capacity = capacityIn;
        shortDesc = sDesc;
        ItemName = nameToGet;
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

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        this.ItemName = itemName;
    }

    public boolean isInventoryItem() {
        return isInventoryItem;
    }

    public void setInventoryItem(boolean isInventoryItem) {
        this.isInventoryItem = isInventoryItem;
    }

    public boolean isContainer() {
        return isContainer;
    }

    public void setContainer(boolean isContainer) {
        this.isContainer = isContainer;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public boolean isFurniture() {
        return isFurniture;
    }

    public void setFurniture(boolean isFurniture) {
        this.isFurniture = isFurniture;
    }

    public boolean isWeapon() {
        return isWeapon;
    }

    public void setWeapon(boolean isWeapon) {
        this.isWeapon = isWeapon;
    }

    public String getIsConsumable() {
        return isConsumable;
    }

    public void setIsConsumable(String isConsumable) {
        this.isConsumable = isConsumable;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean isSwitch) {
        this.isSwitch = isSwitch;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }
}
