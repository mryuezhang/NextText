package com.example.yue.nexttext.UI.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by jamesmulvenna on 2017-09-28.
 */

public class Time implements Parcelable {
    private String date;
    private String time;
    private int type;
    private int status;

    public Time(String date, String time, int type, int status) {
        this.date = date;
        this.time = time;
    }

    public Time() {}

    protected Time(Parcel in) {
        date = in.readString();
        time = in.readString();
        type = in.readInt();
        status = in.readInt();
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getTimer(){

        String AM = "AM";
        String parseAM[] = time.split(":");

        int hour = Integer.parseInt(parseAM[0]);
        int minute = Integer.parseInt(parseAM[1]);

        if(hour > 12){
            hour -= 12;
            AM = "PM";
        }


        return hour + ":" + minute + AM;
    }

    @Override
    public String toString() { return date + " " + getTimer(); }

    public Calendar getCalendar(){
        Calendar calendar = Calendar.getInstance();

        //get date
        String parseDate[] = date.split("/");

        int day = Integer.parseInt(parseDate[0]);
        int month = Integer.parseInt(parseDate[1])-1;
        int year = Integer.parseInt(parseDate[2]);

        int hour;
        int minute;

        String parseAM[] = time.split(":");

        hour = Integer.parseInt(parseAM[0]);
        minute = Integer.parseInt(parseAM[1]);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        System.out.println(calendar.getTime().toString());

        return calendar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeInt(type);
        parcel.writeInt(status);
    }
}
