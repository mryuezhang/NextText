package com.example.yue.nexttext.UI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.yue.nexttext.Database.MessageManager
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

/**
 * Created by yue on 2017-09-27.
 */
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    //private var message_ArrayList = ArrayList<Message>()
    private var messageManger: MessageManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageManger = MessageManager(applicationContext)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        fab.setOnClickListener { createNewMessage() }

        //prepareDummyData()
        messageManger?.prepareData()
        setupMessageList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1->{
                when(resultCode){
                    Activity.RESULT_OK -> receiveMessageAndUpdateListView(data)
                    //Activity.RESULT_CANCELED -> cancel(data)
                }
            }
        }
    }

    //MARK: Action menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    //MARK: private methods
    /*
    private fun prepareDummyData(){
        message_ArrayList.add(0, Message("Happy Birthday", "Happy Birthday bro!!!!"))
        message_ArrayList.add(0, Message("It's gonna rain today", "The weather channel said it's gonna rain today, bring an umbrella or rain coat."))
        message_ArrayList.add(0, Message("I will be there in 5 mins", "I'm close, get ready soon"))
        message_ArrayList[0].setTime()
        message_ArrayList[1].setLocation()
        message_ArrayList[2].setWeather()
    }
    */

    private fun setupMessageList(){
        if(!messageManger!!.isEmpty){
            val messageListAdapter = MessageListAdapter(applicationContext, messageManger!!.loadDataTbl_Message_Data())
            messageList.adapter = messageListAdapter
            //messageList.setOnItemClickListener { adapterView, view, i, l -> editMessage(i) }
        }
    }

    /*
    private fun editMessage(i: Int){
        val intent = MessageConfigureActivity_Old.getStartActivityIntent(this)
        intent.putExtra("selected_message", message_ArrayList[i])
        message_ArrayList.removeAt(i)
        startActivityForResult(intent, 1)
    }
    */


    private fun receiveMessageAndUpdateListView(data: Intent?){
        val message: Message? = data?.getParcelableExtra<Message>("message")
        if(message == null){
            Log.e(TAG, "Received MessageData object is Null!")
        }
        else {
            //message_ArrayList.add(0,message)
            setupMessageList()
        }
    }

    private fun cancel(data: Intent?){
        receiveMessageAndUpdateListView(data)
        Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
        setupMessageList()
    }

    private fun createNewMessage(){
        //startActivityForResult(MessageConfigureActivity_Old.getStartActivityIntent(this), 1)
        startActivity(MessageConfigureActivity.getStartActivityIntent(this))
    }
}


