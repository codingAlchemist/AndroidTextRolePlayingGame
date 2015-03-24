package models;

/**
 * Created by M1027519 on 2/23/2015.
 */
public class Enemy extends Character {
    private Integer hp, mana, level;
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
}
