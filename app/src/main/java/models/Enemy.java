package models;

import android.util.Log;

import java.util.Random;

/**
 * Created by M1027519 on 2/23/2015.
 */
public class Enemy extends Character {
    private Integer hp, mana, level;
    private String mDescription;
    public Enemy(String nameIn, Integer levelIn, Integer strengthIn, Integer intelligenceIn,Integer dexterityIn, Integer wisdomIn, Integer constitutionIn, String playerClassIn){
        super(nameIn,levelIn,strengthIn,intelligenceIn,dexterityIn,wisdomIn,constitutionIn,playerClassIn);
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {

        this.hp = hp;
    }

    @Override
    public Integer getMana() {
        return mana;
    }

    public void setMana(Integer mana) {
        this.mana = mana;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     *
     *
     */
    private void navigateEnemyLeft(){
        Room room = getCurrentRoom();
        if (room.getLeftRoom() != null){
            room.getEnemies().remove(this);
            this.setCurrentRoom(room.getLeftRoom());
            room = this.getCurrentRoom();
            room.getEnemies().add(this);
        }
    }
    /**
     *
     *
     */
    private void navigateEnemyRight(){
        Room room = getCurrentRoom();
        if (room.getRightRoom() != null){
            room.getEnemies().remove(this);
            this.setCurrentRoom(room.getRightRoom());
            room = this.getCurrentRoom();
            room.getEnemies().add(this);
        }
    }
    /**
     *
     *
     */
    private void navigateEnemyForward(){
        Room room = getCurrentRoom();
        if (room.getNextRoom() != null){
            room.getEnemies().remove(this);
            this.setCurrentRoom(room.getNextRoom());
            room = this.getCurrentRoom();
            room.getEnemies().add(this);
        }
    }
    /**
     *
     *
     */
    private void navigateEnemyBack(){
        Room room = getCurrentRoom();
        if (room.getPrevRoom() != null){
            room.getEnemies().remove(this);
            this.setCurrentRoom(room.getPrevRoom());
            room = this.getCurrentRoom();
            room.getEnemies().add(this);
        }
    }

    /**
     *
     *
     */
    public void startEnemyNavigationLogic(){
        Random randomDir = new Random();
        int enemyDir = randomDir.nextInt(4-0);
        if (!this.isDead()){
            switch (enemyDir){
                case 0:
                    navigateEnemyForward();
                    break;
                case 1:
                    navigateEnemyBack();
                    break;
                case 2:
                    navigateEnemyRight();
                    break;
                case 3:
                    navigateEnemyLeft();
                    break;
            }
        }
    }

    public String randomizeNPCActions(){
        Random randomAction = new Random();
        String actionString="";
        int npcAction = randomAction.nextInt(2-0);
        switch (npcAction){
            case 0:
                actionString = " glares at you.";
                break;
            case 1:
                actionString = " laughs out loud to their self.";
                break;
            case 2:
                actionString = " twiddles their thumbs.";
                break;
        }
        return actionString;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}
