package com.example.yue.nexttext.Data;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by jamesmulvenna on 2017-09-28.
 */

public class Message implements Parcelable {
    private String from, password, to, subject, message;

    public Message() {}

    public Message(String from, String password, String to, String subject, String message){
        this.from = from;
        this.password = password;
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    protected Message(Parcel in) {
        from = in.readString();
        to = in.readString();
        subject = in.readString();
        message = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getFrom(){ return from; }
    public void setFrom(String newFrom) { this.from = newFrom; }
    public String getPassword(){ return password; }
    public void setPassword(String newPassword) { this.password = newPassword; }
    public String getTo(){ return to; }
    public void setTo(String newTo) { this.to = newTo; }
    public String getSubject(){ return subject; }
    public void setSubject(String newSubject) { this.subject = newSubject; }
    public String getMessage(){ return message; }
    public void setMessage(String newMessage) { this.message = newMessage; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(to);
        parcel.writeString(subject);
        parcel.writeString(message);
    }
}
