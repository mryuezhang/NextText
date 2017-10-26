package com.example.yue.nexttext.Data

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by yue on 2017-10-26.
 */
class MessageData() : Parcelable {
    var message: Message? = null
    var time: Time? = null
    var location: Location? = null
    var weather: Weather? = null
    var id: Int = 0
    //FORMAT: Feb 27, 2012 5:41:23 PM
    var currentTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

    constructor(parcel: Parcel) : this() {
        message = parcel.readParcelable(Message::class.java.classLoader)
        time = parcel.readParcelable(Time::class.java.classLoader)
        weather = parcel.readParcelable(Weather::class.java.classLoader)
        id = parcel.readInt()
        currentTime = parcel.readString()
    }

    constructor(_message: Message) : this() {
        this.message = _message
        this.id = 0
    }

    constructor(_message: Message, _id: Int) : this() {
        this.message = _message
        this.id = _id
    }

    constructor(newMessage: Message, newLocation: Location, newId: Int) : this() {
        this.message = newMessage
        this.location = newLocation
        this.id = newId
    }

    constructor(newMessage: Message, newWeather: Weather, newId: Int) : this() {
        this.message = newMessage
        this.weather = newWeather
        this.id = newId
    }

    constructor(newMessage: Message, newTime: Time) : this() {
        this.message = newMessage
        this.time = newTime
        this.id = 0
    }

    constructor(newMessage: Message, newLocation: Location) : this() {
        this.message = newMessage
        this.location = newLocation
        this.id = 0
    }

    constructor(newMessage: Message, newWeather: Weather) : this() {
        this.message = newMessage
        this.weather = newWeather
        this.id = 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(message, flags)
        parcel.writeParcelable(time, flags)
        parcel.writeParcelable(weather, flags)
        parcel.writeInt(id)
        parcel.writeString(currentTime)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<MessageData> {
        override fun createFromParcel(parcel: Parcel): MessageData = MessageData(parcel)

        override fun newArray(size: Int): Array<MessageData?> = arrayOfNulls(size)
    }
}