package com.example.yue.nexttext.DataType

import android.icu.util.Calendar
import android.os.Parcel
import android.os.Parcelable
import com.example.yue.nexttext.Core.DataType.Time
import com.example.yue.nexttext.Core.DataType.Weather

class MessageWrapper(var message: Message,
                     var timeTrigger: Time?,
                     var weatherTrigger: Weather?,
                     var locationTrigger: String?,
                     var id: Int) : Parcelable{

    var createdTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Message::class.java.classLoader),
            parcel.readParcelable(Time::class.java.classLoader),
            parcel.readParcelable(Weather::class.java.classLoader),
            parcel.readString(),
            parcel.readInt()) {
        createdTime = parcel.readString()
    }


    constructor(message: Message):this(message, null, null, null, 0)

    constructor(message: Message, queried_id_from_data_base: Int): this(message, null, null, null, queried_id_from_data_base)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(message, flags)
        parcel.writeParcelable(timeTrigger, flags)
        parcel.writeParcelable(weatherTrigger, flags)
        parcel.writeString(locationTrigger)
        parcel.writeInt(id)
        parcel.writeString(createdTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageWrapper> {
        override fun createFromParcel(parcel: Parcel): MessageWrapper {
            return MessageWrapper(parcel)
        }

        override fun newArray(size: Int): Array<MessageWrapper?> {
            return arrayOfNulls(size)
        }
    }

}