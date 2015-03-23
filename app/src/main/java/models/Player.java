package models;

/**
 * Created by M1027519 on 2/23/2015.
 */
public class Player extends Character {

    public Player(String nameIn, Integer strengthIn, Integer intelligenceIn,Integer dexterityIn, Integer wisdomIn, Integer constitutionIn, String playerClassIn){
        int hp = (strengthIn*10) + constitutionIn;
        int mana = (intelligenceIn * 10) + wisdomIn;
        Character character = new Character(nameIn,strengthIn,intelligenceIn,dexterityIn,wisdomIn,constitutionIn,playerClassIn,hp,mana,false);
    }
}
