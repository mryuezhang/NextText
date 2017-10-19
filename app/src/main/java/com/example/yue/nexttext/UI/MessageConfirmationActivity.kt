package com.example.yue.nexttext.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_message_confirmation.*

/**
 * Created by yue on 2017-09-27.
 */
class MessageConfirmationActivity : AppCompatActivity() {

    val TAG = "MessageConActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_confirmation)
    }

    //MARK: Action Menu methods
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_message_confirmation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_confirm_new_message -> confirmMessage()
            R.id.action_cancel_confirm_new_message -> cancelConfirmation()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    //MARK: Private methods
    private fun confirmMessage(){
        val received_Message: Message? = intent.getParcelableExtra("message")
        if(received_Message == null){
            Log.e(TAG, "Received MessageCondition object is Null!")
        }
        else{
            val intent = Intent()
            intent.putExtra("message", received_Message)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun cancelConfirmation(){
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    companion object {
        fun getStartActivityIntent(context: Context) =
                Intent(context, MessageConfirmationActivity::class.java)
    }
}