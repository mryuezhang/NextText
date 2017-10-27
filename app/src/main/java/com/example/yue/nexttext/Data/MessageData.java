package com.example.yue.nexttext.Data;


import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
/**
 * Created by jamesmulvenna on 2017-10-13.
 */

public class MessageData implements Parcelable {
    public static final Creator<MessageData> CREATOR = new Creator<MessageData>() {
        @Override
        public MessageData createFromParcel(Parcel in) {
            return new MessageData(in);
        }

        @Override
        public MessageData[] newArray(int size) {
            return new MessageData[size];
        }
    };
    private Message message;
    private Time time;
    private Location location;
    private Weather weather;
    private int id;
    //FORMAT: Feb 27, 2012 5:41:23 PM
    private String currentTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

    public MessageData(Message newMessage, Time newTime, int newId) {
        this.message = newMessage;
        this.time = newTime;
        this.id = newId;
    }

    public MessageData(Message newMessage, Location newLocation, int newId) {
        this.message = newMessage;
        this.location = newLocation;
        this.id = newId;
    }

    public MessageData(Message newMessage, Weather newWeather, int newId) {
        this.message = newMessage;
        this.weather = newWeather;
        this.id = newId;
    }

    public MessageData(Message newMessage, Time newTime) {
        this.message = newMessage;
        this.time = newTime;
        this.id = 0;
    }

    public MessageData(Message newMessage, Location newLocation) {
        this.message = newMessage;
        this.location = newLocation;
        this.id = 0;
    }

    public MessageData(Message newMessage, Weather newWeather) {
        this.message = newMessage;
        this.weather = newWeather;
        this.id = 0;
    }

    protected MessageData(Parcel in) {
        message = in.readParcelable(Message.class.getClassLoader());
        time = in.readParcelable(Time.class.getClassLoader());
        weather = in.readParcelable(Weather.class.getClassLoader());
        id = in.readInt();
        currentTime = in.readString();
    }

    public Message getMessage() {return this.message; }
    public void setMessage(Message newMessage) { this.message = newMessage; }
    public Time getTime() { return this.time; }
    public void setTime(Time newTime) { this.time = newTime; }
    public Location getLocation() { return this.location; }
    public void setLocation(Location newLocation) { this.location = newLocation; }
    public Weather getWeather() { return this.weather; }
    public void setWeather(Weather newWeather) { this.weather = newWeather; }
    public String getCurrentTime() { return this.currentTime; }
    public int getId() { return this.id; }
    public void setId(int newId) { this.id = newId; }

    @Override
    public String toString() {
        return "id = " + id + " - To: "+ message.getTo() + "  Message: " + message.getMessage();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(message, i);
        parcel.writeParcelable(time, i);
        parcel.writeParcelable(weather, i);
        parcel.writeInt(id);
        parcel.writeString(currentTime);
    }
}
