package models;

import android.content.Context;

/**
 * Created by M1027519 on 3/31/2015.
 */
public class Weapon extends Item {

    public Weapon(String name,Integer damage,Context context,String shortDesc){
        super(name,damage,0,context,shortDesc);
    }


}
