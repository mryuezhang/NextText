package com.example.yue.nexttext.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_message_configure_old.*

/**
 * Created by yue on 2017-09-27.
 */
class MessageConfigureActivity_Old : AppCompatActivity() {

    private val TAG = "MessageConfigActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_configure_old)

        //if user choose to edit an existing message, then load the message
        val selected_message: Message? = intent.getParcelableExtra("selected_message")
        if( selected_message != null) {
            title = "Edit"
            editText_messageTitile.setText(selected_message.getTitle(), TextView.BufferType.EDITABLE)
            editText_messageContent.setText(selected_message.getContent(), TextView.BufferType.EDITABLE)
            Log.i(TAG, selected_message.getBase())
            when(selected_message.getBase()){
                "time" -> radioGroup_messageBase.check(R.id.radioButton_time)
                "location" -> radioGroup_messageBase.check(R.id.radioButton_location)
                "weather" -> radioGroup_messageBase.check(R.id.radioButton_weather)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            2 ->{
                when(resultCode){
                    Activity.RESULT_OK -> receiveAndForwardMessage(data)
                    Activity.RESULT_CANCELED -> Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //MARK: Action Menu methods
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_message_configure, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save_new_message_configure -> { submitMessageConfigure() }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    //MARK: Private methods
    private fun submitMessageConfigure(){
        val message = Message(editText_messageTitile.text.toString(), editText_messageContent.text.toString())
        val intent = MessageConfirmationActivity.getStartActivityIntent(this)
        intent.putExtra("message", message)
        setResult(Activity.RESULT_OK, intent)
        startActivityForResult(intent, 2)
    }

    /**
     * This method is used to receive a Message object from MessageConfirmationActivity and forward it to MainActivity
     */
    private fun receiveAndForwardMessage(data: Intent?){
        val message: Message? = data?.getParcelableExtra("message")
        if (message == null) {
            Log.e(this.TAG, "Received Message object is Null!")
        } else {
            val intent = Intent()
            intent.putExtra("message", message)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun cancelConfigure(){
        val selected_message: Message? = intent.getParcelableExtra("selected_message")
        if( selected_message != null) {
            val intent = Intent()
            intent.putExtra("message", selected_message)
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        } else{
            Log.e(this.TAG, "Received Message object is Null!")
        }
    }

    companion object {
        fun getStartActivityIntent(context: Context) =
                Intent(context, MessageConfigureActivity_Old::class.java)
    }
}