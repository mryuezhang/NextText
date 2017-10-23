package com.example.yue.nexttext.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jamesmulvenna on 2017-09-28.
 */

public class PhoneData implements Parcelable {
    private String name, phone;
    private int id;

    public PhoneData() {}

    public PhoneData(String newName, String newPhone){
        this.name = newName;
        this.phone = newPhone;
    }

    public PhoneData(int newId, String newName, String newPhone){
        this.id = newId;
        this.name = newName;
        this.phone = newPhone;
    }

    private PhoneData(Parcel in) {
        name = in.readString();
        phone = in.readString();
        id = in.readInt();
    }

    public static final Creator<PhoneData> CREATOR = new Creator<PhoneData>() {
        @Override
        public PhoneData createFromParcel(Parcel in) {
            return new PhoneData(in);
        }

        @Override
        public PhoneData[] newArray(int size) {
            return new PhoneData[size];
        }
    };

    public int getID() { return this.id; }
    public void setID(int newId) { this.id = newId; }
    public String getName(){ return this.name; }
    public void setName(String newName){ this.name = newName; }
    public String getPhone(){ return this.phone; }
    public void setPhone(String newPhone){ this.phone = newPhone; }

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
        parcel.writeInt(id);
    }
}
