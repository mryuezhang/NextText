package com.example.yue.nexttext.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.yue.nexttext.DataType.MessageWrapper
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_edit_message.*

/**
 * Created by yue on 2017-10-28.
 */
class EditMessageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_message)

        val messageWrapper: MessageWrapper = intent.getParcelableExtra<MessageWrapper>(Utilities.EDIT_DATA)
        textView2.text = messageWrapper.message._to
        textView3.text = messageWrapper.message._content
    }

    companion object {
        fun getStartActivityIntent(context: Context) =
                Intent(context, EditMessageActivity::class.java)
    }
}