package com.example.yue.nexttext.UI.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jamesmulvenna on 2017-10-18.
 */

public class EmailAccount implements Parcelable {
    private String username, password;

    public EmailAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }


    protected EmailAccount(Parcel in) {
    }

    public static final Creator<EmailAccount> CREATOR = new Creator<EmailAccount>() {
        @Override
        public EmailAccount createFromParcel(Parcel in) {
            return new EmailAccount(in);
        }

        @Override
        public EmailAccount[] newArray(int size) {
            return new EmailAccount[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
