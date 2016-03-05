package com.lapdict.lap089.lapdict;

import android.graphics.drawable.Drawable;
import android.widget.Spinner;

/**
 * Created by user on 5/23/2015.
 */
public class Setting {

    private Drawable Image;
    private  String Name="";

    /*********** Set Methods ******************/

    Setting(Drawable image,String name)
    {
        this.Image=image;
        this.Name= name;
    }

    public void setImage(Drawable Image)
    {
        this.Image = Image;
    }

    public void setName(String Name)
    {
        this.Name = Name;
    }

    /*********** Get Methods ****************/


    public Drawable getImage()
    {
        return this.Image;
    }

    public String getName()
    {
        return this.Name;
    }
}