package com.example.yue.nexttext.UI

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.support.v4.content.ContextCompat
import android.text.Html
import com.example.yue.nexttext.R
import java.util.*
import java.util.regex.Pattern


class Utilities {

    companion object {
        val MESSAGE_CONFIGURE_ACTIVITY_REQUEST_CODE = 121
        val MESSAGE_CONFIRMATION_ACTIVITY_REQUEST_CODE = 122
        val EDIT_MESSAGE_ACTIVITY_REQUEST_CODE = 123
        val INCOMPLETE_DATA = "IncompleteMessageDataObject"
        val COMPLETE_DATA = "CompleteMessageDataObject"
        val EDIT_DATA = "EditDataObject"
        val EDITED_DATA = "EditedDataObject"
        val DATE = "DATE"
        val TIME = "TIME"

        private val dateFormat = SimpleDateFormat("EEE, MMMM, d, yyyy")
        private val timeFormat = SimpleDateFormat("hh:mm a")
        private val timeFormat_24_hour = SimpleDateFormat("HH:mm")

        fun formatDate(p0: Calendar): String = dateFormat.format(p0)

        fun formatTime(p0: Calendar, p1: Context): String =
                if (android.text.format.DateFormat.is24HourFormat(p1)) timeFormat_24_hour.format(p0)
                else timeFormat.format(p0)

        fun parseDate(p0: String): Date = dateFormat.parse(p0)

        fun parseTime(p0: String, p1: Context): Date =
                if (android.text.format.DateFormat.is24HourFormat(p1)) timeFormat_24_hour.parse(p0)
                else timeFormat.parse(p0)

        fun isSameDate(p0: Calendar, p1: Date): Boolean =
                dateFormat.format(p0) == dateFormat.format(p1)

        /**
         * returns 0 if they're the same, -1 if p0 is after p1, 1 if p0 is before p1
         */
        fun compareTime(p0: Calendar, p1: Date): Int {
            return when {
                timeFormat_24_hour.format(p0) == timeFormat_24_hour.format(p1) -> 0
                timeFormat_24_hour.format(p0) > timeFormat_24_hour.format(p1) -> -1
                else -> 1
            }
        }


        fun reverseDateFormat_YEAR(string: String): Int = string.split(",")[3].trim().toInt()

        fun reverseDateFormat_MONTH(string: String): Int = when(string.split(",")[1].trim()){
                "January" ->  0
                "February" -> 1
                "March" -> 2
                "April" -> 3
                "May" -> 4
                "June" -> 5
                "July" -> 6
                "August" -> 7
                "September" -> 8
                "October" -> 9
                "November" -> 10
                else -> 11
        }

        fun reverseDateFormat_DAY(string: String): Int = string.split(",")[2].trim().toInt()

        fun reverseTimeFormat_HOUR(string: String): Int = string.split(":")[0].trim().toInt()

        fun reverseTimeFormat_MINUTE(string: String): Int = string.split(":")[1].split(" ")[0].toInt()

        fun isEmailValid(email: String): Boolean {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun emptyRecipientDialog(activity: Activity): AlertDialog{
            val alertDialog = AlertDialog.Builder(activity)
                    .setMessage("Recipient can't be empty")
                    .setPositiveButton("Ok") { d, _ -> d.cancel() }.create()

            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity.applicationContext,R.color.colorPrimary))
            }

            return alertDialog
        }

        fun invalidEmailAddressAlertDialog(activity: Activity, invalidAddress: String): AlertDialog{
            val alertDialog = AlertDialog.Builder(activity)
                    .setMessage("The address <$invalidAddress> is invalid")
                    .setPositiveButton("Ok") { d, _ -> d.cancel() }.create()

            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity.applicationContext, R.color.colorPrimary))
            }

            return alertDialog
        }

        fun invalidTimeTriggerAlertDialog(activity: Activity, type: String): AlertDialog{
            val alertDialog = if (type == DATE) {
                val string = "Picked <b>date</b> has passed"

                AlertDialog.Builder(activity).setMessage(Html.fromHtml(string, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH))
                        .setPositiveButton("Ok") { d, _ -> d.cancel() }
                        .create()
            } else {
                val string = "Picked <b>time</b> has passed"
                AlertDialog.Builder(activity).setMessage(Html.fromHtml(string, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH))
                        .setPositiveButton("Ok") { d, _ -> d.cancel() }
                        .create()
            }
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity.applicationContext, R.color.colorPrimary))
            }

            return alertDialog
        }

        fun avoidCurrentTimeAlertDialog(activity: Activity, invalidAddress: String): AlertDialog{
            val alertDialog = AlertDialog.Builder(activity)
                    .setMessage("Avoid using current time")
                    .setPositiveButton("Ok") { d, _ -> d.cancel() }.create()

            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity.applicationContext, R.color.colorPrimary))
            }

            return alertDialog
        }
    }
}