package models;

/**
 * Created by M1027519 on 2/23/2015.
 */
public class Enemy extends Character {
    private int hp, mana;
    private Character character;
    public Enemy(String nameIn, Integer strengthIn, Integer intelligenceIn,Integer dexterityIn, Integer wisdomIn, Integer constitutionIn, String playerClassIn){
        character = new Character(nameIn,strengthIn,intelligenceIn,dexterityIn,wisdomIn,constitutionIn,playerClassIn,hp,mana,true);
        character.setHitpts(character.getLevel()*10);
        character.setMana(character.getLevel()*10);
    }
}
