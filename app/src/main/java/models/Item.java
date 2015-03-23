package models;

import com.orm.SugarRecord;

/**
 * Created by M1027519 on 1/14/2015.
 */
public class Item extends SugarRecord<Item>{

    private String itemName;
    private boolean isInventoryItem;
    private boolean isWeapon;
    private boolean isContainer;
    private boolean isFurniture;
    private String isConsumable;
    private String itemDesc;
    private int damage;
    private int capacity;

    public Item(String itemNameIn,String itemDescIn, String type, Integer damageIn,Integer capacityIn){
        itemName = itemNameIn;
        itemDesc = itemDescIn;
        if (type.equalsIgnoreCase("Furniture")){
            isContainer = false;
            isInventoryItem = false;
            isFurniture = true;
            isWeapon = false;
        }else if(type.equalsIgnoreCase("Armor")){
            isContainer = false;
            isInventoryItem = true;
            isFurniture = false;
            isWeapon = false;
        }else if(type.equalsIgnoreCase("Weapon")){
            isContainer = false;
            isInventoryItem = true;
            isFurniture = false;
            isWeapon = true;
        }else if(type.equalsIgnoreCase("Food")){
            isContainer = false;
            isInventoryItem = true;
            isFurniture = false;
            isWeapon = false;
        }else if(type.equalsIgnoreCase("Container")){
            isContainer = true;
            isInventoryItem = true;
            isFurniture = false;
            isWeapon = false;
        }
        damage = damageIn;
        capacity = capacityIn;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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
}
