package com.example.yue.nexttext.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jamesmulvenna on 2017-09-28.
 */

public class Sms extends MessageCondition implements Parcelable {
    private String name, phone, message;
    private int id;

    public Sms() {}

    public Sms(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public Sms(int id, String name, String phone, String message){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.message = message;
    }

    protected Sms(Parcel in) {
        name = in.readString();
        phone = in.readString();
        message = in.readString();
        id = in.readInt();
    }

    public static final Creator<Sms> CREATOR = new Creator<Sms>() {
        @Override
        public Sms createFromParcel(Parcel in) {
            return new Sms(in);
        }

        @Override
        public Sms[] newArray(int size) {
            return new Sms[size];
        }
    };

    public int getID() { return id; }
    public void setID(int newId) { this.id = newId; }
    public String getName(){ return name; }
    public void setName(String newName){this.name = newName; }
    public String getPhone(){ return phone; }
    public void setPhone(String newPhone){this.phone = newPhone; }
    public String getMessage(){ return message; }
    public void setMessage(String newMessage) { this.message = newMessage; }
    public Time getTime() { return time; }
    public void setTime(Time newTime) { time = newTime;}
    public Location getLocation(){return location; }
    public void setLocation(Location newLocation) { location = newLocation; }
    public Weather getWeather(){return weather; }
    public void setWeather(Weather newWeather) { weather = newWeather; }

    @Override
    public String toString() {
        return name + "\n["+ phone +"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(message);
        parcel.writeInt(id);
    }
}
