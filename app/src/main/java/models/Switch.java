package models;

import android.content.Context;

/**
 * Created by M1027519 on 3/23/2015.
 */
public class Switch extends Item {
    private boolean on;
    private Context mContext;
    public Switch(boolean on, Context context) {
        super("Light Switch",0,0,context);
        this.mContext = context;
        this.on = on;
    }

    public boolean getIsOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
