package com.example.yue.nexttext.Data;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
/**
 * Created by jamesmulvenna on 2017-10-13.
 */

public class MessageData implements Parcelable {
    Message message;
    private Time time;
    private Location location;
    private Weather weather;
    int id;
    //FORMAT: Feb 27, 2012 5:41:23 PM
    private String currentTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

    public MessageData(Message newMessage, Time newTime, int id) {
        this.message = newMessage;
        this.time = newTime;
        this.id = id;
    }

    public MessageData(Message newMessage, Location newLocation, int id) {
        this.message = newMessage;
        this.location = newLocation;
        this.id = id;
    }

    public MessageData(Message newMessage, Weather newWeather, int id) {
        this.message = newMessage;
        this.weather = newWeather;
        this.id = id;
    }

    public MessageData(Message newMessage, Time newTime) {
        this.message = newMessage;
        this.time = newTime;
        id= 0;
    }

    public MessageData(Message newMessage, Location newLocation) {
        this.message = newMessage;
        this.location = newLocation;
        id= 0;
    }

    public MessageData(Message newMessage, Weather newWeather) {
        this.message = newMessage;
        this.weather = newWeather;
        id= 0;
    }

    protected MessageData(Parcel in) {
        message = in.readParcelable(Message.class.getClassLoader());
        time = in.readParcelable(Time.class.getClassLoader());
        weather = in.readParcelable(Weather.class.getClassLoader());
        id = in.readInt();
        currentTime = in.readString();
    }

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

    public Message getMessage() {return message; }
    public Time getTime() { return time; }
    public Location getLocation() { return location; }
    public Weather getWeather() { return weather; }
    public String getCurrentTime() { return currentTime; }
    public int getId() { return id; }

    public void setMessage(Message newMessage) { this.message = newMessage; }
    public void setTime(Time newTime) { time = newTime; }
    public void setLocation(Location newLocation) { location = newLocation; }
    public void setWeather(Weather newWeather) { weather = newWeather; }
    public void setId(int newId) { id = newId; }

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
