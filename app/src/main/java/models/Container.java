package models;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by M1027519 on 3/27/2015.
 */
public class Container extends Item{
    private ArrayList<Item> items;
    public Container(Context context,String name,String shortDescIn){
        super(name,0,0,context,shortDescIn);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

}
