package com.example.yue.nexttext.Data;

import android.os.Parcelable;

/**
 * Created by jamesmulvenna on 2017-10-13.
 */

public abstract class MessageCondition implements Parcelable{
    Time time;
    Location location;
    Weather weather;
    Boolean timeOn;
    Boolean locationOn;
    Boolean weatherOn;
    int id;


}
