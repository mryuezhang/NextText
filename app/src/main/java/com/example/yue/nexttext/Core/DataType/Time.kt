package com.example.yue.nexttext.Core.DataType

import android.icu.util.Calendar
import android.os.Parcel
import android.os.Parcelable
import com.example.yue.nexttext.UI.Utilities

class Time(val date : String,
           val time: String): Parcelable {

    fun getCalendar(): Calendar{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, Utilities.reverseDateFormat_YEAR(this.date))
        calendar.set(Calendar.MONTH, Utilities.reverseDateFormat_MONTH(this.date))
        calendar.set(Calendar.DAY_OF_MONTH, Utilities.reverseDateFormat_DAY(this.date))
        calendar.set(Calendar.HOUR_OF_DAY, Utilities.reverseTimeFormat_HOUR(this.time))
        calendar.set(Calendar.MINUTE, Utilities.reverseTimeFormat_MINUTE(this.time))
        return calendar
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Time> {
        override fun createFromParcel(parcel: Parcel): Time {
            return Time(parcel)
        }

        override fun newArray(size: Int): Array<Time?> {
            return arrayOfNulls(size)
        }
    }
}
