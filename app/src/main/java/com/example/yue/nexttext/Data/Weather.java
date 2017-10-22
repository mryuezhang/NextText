package com.example.yue.nexttext.Data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by jamesmulvenna on 2017-10-13.
 */

//yahoo api

public class Weather implements Parcelable {
    private String city, countryCode, host;

    public Weather(String newCity, String newCountryCode) {
        this.city = newCity;
        this.countryCode = newCountryCode;
        //url checks what the city in country's current text condition is (Sunny, rainy, windy, cloudy, etc.)
        this.host = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition.code%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + this.city + "%2C%20" + this.countryCode + "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
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

    public String getCity() {
        return this.city;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getHost() {
        return this.host;
    }

    public void setCity(String newCity) {
        this.city = newCity;
    }

    public void setCountryCode(String newCountryCode) {
        this.countryCode = newCountryCode;
    }

    public void setHost(String newHost) {
        this.host = newHost;
    }

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

        /*
        0	tornado
        1	tropical storm
        2	hurricane
        3	severe thunderstorms
        4	thunderstorms
        5	mixed rain and snow
        6	mixed rain and sleet
        7	mixed snow and sleet
        8	freezing drizzle
        9	drizzle
        10	freezing rain
        11	showers
        12	showers
        13	snow flurries
        14	light snow showers
        15	blowing snow
        16	snow
        17	hail
        18	sleet
        19	dust
        20	foggy
        21	haze
        22	smoky
        23	blustery
        24	windy
        25	cold
        26	cloudy
        27	mostly cloudy (night)
        28	mostly cloudy (day)
        29	partly cloudy (night)
        30	partly cloudy (day)
        31	clear (night)
        32	sunny
        33	fair (night)
        34	fair (day)
        35	mixed rain and hail
        36	hot
        37	isolated thunderstorms
        38	scattered thunderstorms
        39	scattered thunderstorms
        40	scattered showers
        41	heavy snow
        42	scattered snow showers
        43	heavy snow
        44	partly cloudy
        45	thundershowers
        46	snow showers
        47	isolated thundershowers
        */


        public int getCondition () throws IOException, JSONException {
            JSONObject json = readJsonFromUrl(this.getHost());
            int conditionCode = json.getInt("code");

            return conditionCode;
        }


        @Override
        public int describeContents () {
            return 0;
        }

        @Override
        public void writeToParcel (Parcel parcel,int i){
            parcel.writeString(city);
            parcel.writeString(countryCode);
            parcel.writeString(host);
        }
    }