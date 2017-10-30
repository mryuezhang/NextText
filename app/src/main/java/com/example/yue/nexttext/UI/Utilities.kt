package com.example.yue.nexttext.UI

import android.icu.text.SimpleDateFormat

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

        val dateFormat = SimpleDateFormat("EEE, MMMM, d, yyyy")
        val timeFormat = SimpleDateFormat("h:mm a")
    }
}