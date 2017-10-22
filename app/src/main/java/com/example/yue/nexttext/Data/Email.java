package com.example.yue.nexttext.Data;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by jamesmulvenna on 2017-09-28.
 */

public class Email extends MessageCondition implements Parcelable {
    private String from, to, subject, message;
    private int id;

    public Email(int id, String from, String to, String subject, String message) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    public Email() {
    }

    protected Email(Parcel in) {
        from = in.readString();
        to = in.readString();
        subject = in.readString();
        message = in.readString();
        id = in.readInt();
    }

    public static final Creator<Email> CREATOR = new Creator<Email>() {
        @Override
        public Email createFromParcel(Parcel in) {
            return new Email(in);
        }

        @Override
        public Email[] newArray(int size) {
            return new Email[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int newId) {
        id = newId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String newFrom) {
        this.to = newFrom;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String newTo) {
        this.to = newTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String newSubject) {
        this.subject = newSubject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String newMessage) {
        this.message = newMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(to);
        parcel.writeString(subject);
        parcel.writeString(message);
        parcel.writeInt(id);
    }
}
