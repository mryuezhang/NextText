package com.example.yue.nexttext.Data

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by yue on 2017-10-26.
 * A class can be used to hold either for SMS or Email data
 *
 */
class Message() : Parcelable {
    var from: String? = null
    var password: String? = null
    var to: String? = null
    var subject: String? = null
    var message: String? = null

    constructor(parcel: Parcel) : this() {
        from = parcel.readString()
        password = parcel.readString()
        to = parcel.readString()
        subject = parcel.readString()
        message = parcel.readString()
    }

    // For email
    // TODO: Actual constructor shouldn't take null value, they are here just because the whole project isn't complete
    constructor(_from: String?, _password: String?, newTo: String?, _subject: String?, _message: String?) : this() {
        this.from = _from
        this.password = _password
        this.to = newTo
        this.subject = _subject
        this.message = _message
    }

    constructor(_to: String, _subject: String, _message: String) : this() {
        this.to = _to
        this.subject = _subject
        this.message = _message
    }

    // For sms
    constructor(_to: String, _message: String) : this() {
        this.to = _to
        this.message = _message
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(from)
        parcel.writeString(password)
        parcel.writeString(to)
        parcel.writeString(subject)
        parcel.writeString(message)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Message> {

        override fun createFromParcel(parcel: Parcel): Message = Message(parcel)

        override fun newArray(size: Int): Array<Message?> = arrayOfNulls(size)
    }
}