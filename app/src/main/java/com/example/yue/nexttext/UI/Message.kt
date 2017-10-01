package com.example.yue.nexttext.UI

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by yue on 2017-09-27.
 */
class Message(private val title:String,
              private val content:String): Parcelable {

    //Only one of the following variable can hold data while other must be null
    private var time: Int? = null
    private var location: Int? = null
    private var weather: Int? = null

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
        time = parcel.readValue(kotlin.Int::class.java.classLoader) as? Int
        location = parcel.readValue(kotlin.Int::class.java.classLoader) as? Int
        weather = parcel.readValue(kotlin.Int::class.java.classLoader) as? Int
    }

    fun getTitle():String = this.title
    fun getContent():String = this.content

    fun setTime(){
        this.time = 1
        this.location = null
        this.weather = null
    }

    fun setLocation(){
        this.time = null
        this.location = 1
        this.weather = null
    }

    fun setWeather(){
        this.time = null
        this.location = null
        this.weather = 1
    }

    fun getBase(): String{
        if (this.time == 1) return "time"
        else if (this.location == 1 ) return "location"
        else if (this.weather == 1) return "weather"
        else return "false"

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeValue(time)
        parcel.writeValue(location)
        parcel.writeValue(weather)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }
}