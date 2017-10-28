package com.example.yue.nexttext.DataType

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by yue on 2017-10-28.
 */
data class Message(var _from: String?,
              var _password: String?,
              var _to: String,
              var _subject: String?,
              var _content: String): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    constructor(_to: String, _content: String) : this(null, null, _to, null, _content)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_from)
        parcel.writeString(_password)
        parcel.writeString(_to)
        parcel.writeString(_subject)
        parcel.writeString(_content)
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