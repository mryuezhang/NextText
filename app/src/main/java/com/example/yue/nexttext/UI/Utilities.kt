package com.example.yue.nexttext.UI

import android.app.Activity
import android.app.AlertDialog
import android.icu.text.SimpleDateFormat
import android.support.v4.content.ContextCompat
import com.example.yue.nexttext.R
import java.util.regex.Pattern

/**
 * Created by yue on 2017-10-28.
 */
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

        val dateFormat = SimpleDateFormat("EEE, MMMM, d, yyyy")
        val timeFormat = SimpleDateFormat("hh:mm a")
        val timeFormat_24_hour = SimpleDateFormat("HH:mm")

        fun reverseDateFormat_YEAR(string: String): Int = string.split(",")[3].trim().toInt()

        fun reverseDateFormat_MONTH(string: String): Int = when(string.split(",")[1].trim()){
                "January" ->  1
                "February" -> 2
                "March" -> 3
                "April" -> 4
                "May" -> 5
                "June" -> 6
                "July" -> 7
                "August" -> 8
                "September" -> 9
                "October" -> 10
                "November" -> 11
                else -> 12
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
                AlertDialog.Builder(activity).setMessage("Picked date has passed")
                        .setPositiveButton("Ok") { d, _ -> d.cancel() }
                        .create()
            } else {
                AlertDialog.Builder(activity).setMessage("Picked time has passed")
                        .setPositiveButton("Ok") { d, _ -> d.cancel() }
                        .create()
            }
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity.applicationContext, R.color.colorPrimary))
            }

            return alertDialog
        }
    }
}