package com.example.yue.nexttext.UI

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity

/**
 * Created by yue on 2017-10-05.
 */
class NewEmailActivity: AppCompatActivity() {

    companion object {
        fun getStartActivityIntent(context: Context) =
                Intent(context, NewEmailActivity::class.java)
    }
}