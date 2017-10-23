package com.example.yue.nexttext.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by jamesmulvenna on 2017-09-28.
 */

public class Time implements Parcelable {
    private String date, time;
    private int type, status;

    public Time(String newDate, String newTime, int newType, int newStatus) {
        this.date = newDate;
        this.time = newTime;
        this.type = newType;
        this.status = newStatus;
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
        return this.date;
    }

    public void setDate(String newDate) {
        this.date = newDate;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String newTime) {
        this.time = newTime;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int newType) {
        this.type = newType;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int newStatus) {
        this.status = newStatus;
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
