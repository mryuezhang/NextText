package com.example.yue.nexttext.Core.DataType;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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

    public int maskConditon(int conditonCode) {
        int maskedCondition = 0;

                //SUNNY
                if (conditonCode == 32 || conditonCode == 34 || conditonCode == 36){
                    maskedCondition = 1;
                }
                //CLOUDS
                else if (conditonCode == 26 || conditonCode == 27 || conditonCode == 28 || conditonCode == 29 || conditonCode == 30 || conditonCode == 44){
                    maskedCondition = 2;
                }
                //WINDY
                else if (conditonCode == 24){
                    maskedCondition = 3;
                }
                //RAIN
                else if (conditonCode == 5 || conditonCode == 6 || conditonCode == 7 || conditonCode == 8 || conditonCode == 9 || conditonCode == 10 || conditonCode == 11 || conditonCode == 12){
                    maskedCondition = 4;
                }
                //THUNDERSTORMS
                else if (conditonCode == 1 || conditonCode == 2 || conditonCode == 3 || conditonCode == 4 || conditonCode == 37 || conditonCode == 38 || conditonCode == 39 || conditonCode == 45 || conditonCode == 47){
                    maskedCondition = 5;
                }
                //HAIL
                else if (conditonCode == 17 || conditonCode == 18 || conditonCode == 35){
                    maskedCondition = 6;
                }
                //SNOW
                else if (conditonCode == 13 || conditonCode == 14 || conditonCode == 15 || conditonCode == 16 || conditonCode == 41 || conditonCode == 42 || conditonCode == 43 || conditonCode == 46){
                    maskedCondition = 7;
                }
                //DOESN'T MATTER
                else {
                    maskedCondition = 0;
                }


        return maskedCondition;
    }


    public int getConditionCode() throws IOException, JSONException {
        JSONObject json = readJsonFromUrl(this.getHost());
        String conditionCode = json.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("code");
        Log.d("Condition code from weather object: ", conditionCode);

        Integer conditionCodeFinal = maskConditon(Integer.parseInt(conditionCode));

        return conditionCodeFinal;
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

    /*
Condition codes:
What we need:
1 Sunny
2 Clouds
3 Windy
4 Rain
5 Thunderstorms
6 Hail
7 Snow

SUNNY
32	sunny
34	fair (day)
36	hot

CLOUDS
26	cloudy
27	mostly cloudy (night)
28	mostly cloudy (day)
29	partly cloudy (night)
30	partly cloudy (day)
44	partly cloudy

WIND
24	windy

RAIN
5	mixed rain and snow
6	mixed rain and sleet
7	mixed snow and sleet
8	freezing drizzle
9	drizzle
10	freezing rain
11	showers
12	showers
40	scattered showers

THUNDERSTORM
1	tropical storm
2	hurricane
3	severe thunderstorms
4	thunderstorms
37	isolated thunderstorms
38	scattered thunderstorms
39	scattered thunderstorms
45	thundershowers
47	isolated thundershowers

HAIL
17	hail
18	sleet
35	mixed rain and hail

SNOW
13	snow flurries
14	light snow showers
15	blowing snow
16	snow
41	heavy snow
42	scattered snow showers
43	heavy snow
46	snow showers
     */
}
