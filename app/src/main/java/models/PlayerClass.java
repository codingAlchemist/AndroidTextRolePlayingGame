package models;

/**
 * Created by M1027519 on 1/14/2015.
 */
public class PlayerClass {

    private boolean isFighter;
    private boolean isMage;
    private boolean isDruid;

    public PlayerClass(String playerClass){
        if (playerClass.equalsIgnoreCase("mage")){
            isMage = true;
        }else if (playerClass.equalsIgnoreCase("fighter")){
            isFighter = true;
        }else if (playerClass.equalsIgnoreCase("druid")){
            isDruid = true;
        }
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
}
