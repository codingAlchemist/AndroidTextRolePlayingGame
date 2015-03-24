package models;

import android.util.Log;


import com.orm.SugarRecord;

import java.util.ArrayList;


public class Character extends SugarRecord<Character>{

    private String name;
    private String playerClass;
    private int exp;
    private boolean isFighter;
    private boolean isMage;
    private boolean isDruid;
    private boolean isEnemy;
    private boolean isDead;
    private boolean inCombat;
    private Integer strength;
    private Integer dexterity;
    private Integer intelligence;
    private Integer wisdom;
    private Integer constitution;
    private Integer mana;
    private Integer hitpts;
    private Room currentRoom;
    private ArrayList<Item> itemsInInv;
    private Integer level;
    public Character(String nameIn, Integer levelIn, Integer strengthIn, Integer intelligenceIn, Integer dexterityIn, Integer wisdomIn, Integer constitutionIn,String playerClassIn){
        super();
        name = nameIn;
        strength = strengthIn;
        dexterity = dexterityIn;
        intelligence = intelligenceIn;
        wisdom = wisdomIn;
        constitution = constitutionIn;

        playerClass = playerClassIn;

        level = levelIn;
        isDead = false;
        inCombat = false;

        hitpts = (constitution *10) + strength + level;
        mana = (intelligence * 10) + wisdom + level;

        itemsInInv = new ArrayList<Item>();
    }
    public Character(){
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getConstitution() {
        return constitution;
    }

    public void setConstitution(Integer constitution) {
        this.constitution = constitution;
    }

    public Integer getDexterity() {
        return dexterity;
    }

    public void setDexterity(Integer dexterity) {
        this.dexterity = dexterity;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getWisdom() {
        return wisdom;
    }

    public void setWisdom(Integer wisdom) {
        this.wisdom = wisdom;
    }

    public void setPlayerClass(String playerClassIn){
        playerClass = playerClassIn;

    }

    public String getPlayerClass() {
        return playerClass;
    }

    public Integer getHitpts() {
        return hitpts;
    }

    public void setHitpts(Integer hitpts) {
        this.hitpts = hitpts;
    }

    public Integer getMana() {
        return mana;
    }

    public void setMana(Integer mana) {
        this.mana = mana;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean isEnemy) {
        this.isEnemy = isEnemy;
    }

    public ArrayList<Item> getItemsInInv() {
        return itemsInInv;
    }

    public void setItemsInInv(ArrayList<Item> itemsInInv) {
        this.itemsInInv = itemsInInv;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    public boolean isInCombat() {
        return inCombat;
    }

    public void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public boolean isFighter() {
        return isFighter;
    }

    public void setFighter(boolean isFighter) {
        this.isFighter = isFighter;
    }

    public boolean isMage() {
        return isMage;
    }

    public void setMage(boolean isMage) {
        this.isMage = isMage;
    }

    public boolean isDruid() {
        return isDruid;
    }

    public void setDruid(boolean isDruid) {
        this.isDruid = isDruid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
