package com.example.yue.nexttext.UI.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jamesmulvenna on 2017-10-13.
 */

//yahoo api

public class Weather implements Parcelable {
    String city;
    String countryCode;
    String host;

    public Weather(String thisCity, String thisCountryCode, String thisHost){
        city = thisCity;
        countryCode = thisCountryCode;
        //url should be = select item.condition.temp from weather.forecast where woeid in (select woeid from geo.places(1) where text="ottawa, ca")
        host = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition.temp%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + city + "%2C%20" + countryCode + "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    }

    protected Weather(Parcel in) {
        city = in.readString();
        countryCode = in.readString();
        host = in.readString();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    public String getCity() { return this.city; }
    public String getCountryCode() { return this.countryCode; }
    public String getHost() { return this.host; }

    public void setCity(String newCity) { this.city = newCity; }
    public void setCountryCode(String newCountryCode) { this.countryCode = newCountryCode; }
    public void setHost(String newHost) { this.host = newHost; }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public int getTemperature(String thisHost) throws IOException, JSONException {
        JSONObject json = readJsonFromUrl(host);
        String temp = json.toString();
        int result = Integer.parseInt(temp);
        return result;
    }

    //if false then trivial to understand its less
    public boolean isGreater(int originalTemperature, String originalHost) throws IOException, JSONException {
        int currentTemperature = getTemperature(originalHost);

        if (currentTemperature >= originalTemperature){
            return true;
        }

        return false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(city);
        parcel.writeString(countryCode);
        parcel.writeString(host);
    }
}
