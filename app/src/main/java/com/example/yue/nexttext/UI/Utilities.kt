package com.example.yue.nexttext.UI

import com.example.yue.nexttext.Data.MessageData

/**
 * Created by yue on 2017-10-21.
 * A class holds some values that are being used in multiple classes
 */
abstract class Utilities {

    companion object {
        val MESSAGE_CONFIGURE_ACTIVITY_REQUEST_CODE = 121
        val MESSAGE_CONFIRMATION_ACTIVITY_REQUEST_CODE = 122
        val INCOMPLETE_DATA = "IncompleteMessageDataObject"
        val COMPLETE_DATA = "CompleteMessageDataObject"

        fun isEmail(messageData: MessageData): Boolean =
                messageData.message?.to!!.contains("@")
    }

}