package com.example.yue.nexttext.UI

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import com.example.yue.nexttext.Data.MessageData
import com.example.yue.nexttext.Database.MessageManager
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.empty_view_for_list_view.*


/**
 * Created by yue on 2017-09-27.
 * Main screen
 */
class MainActivity : AppCompatActivity() {
    private var messageManager: MessageManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageManager = MessageManager(this)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        fab.setOnClickListener { startActivityForResult(MessageConfigureActivity.getStartActivityIntent(this),
                Utilities.MESSAGECONFIGUREACTIVITY_REQUEST_CODE) }

        messageManager?.prepareData()
        setupMessageList()
    }

    /*
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
    */

    //MARK: Action menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.action_settings -> true
        R.id.action_delete_all_messages -> {
            deleteAllMessagesEverywhere()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    //MARK: private methods
    private fun setupMessageList(){
        //set up a additional view for listview when it's empty
        val emptyView = layoutInflater.inflate(R.layout.empty_view_for_list_view, null, false)
        addContentView(emptyView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        messageList.emptyView = emptyView_for_ListView

        //inflate the list with data from database when database is not empty
        if(!messageManager!!.isEmpty){
            val messageListAdapter = MessageListAdapter(baseContext, messageManager!!.allMessages as ArrayList<MessageData>)
            messageList.adapter = messageListAdapter
            //messageList.setOnItemClickListener { adapterView, view, i, l -> editMessage(i) }
            messageList.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
            messageList.setMultiChoiceModeListener(object : AbsListView.MultiChoiceModeListener{
                override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                    when(p1?.itemId){
                        R.id.action_delete_select_message -> {
                            //Toast.makeText(applicationContext, "Deleted!", Toast.LENGTH_SHORT).show()
                            val builder: AlertDialog.Builder  = AlertDialog.Builder(this@MainActivity)

                            if (p0?.title.toString().toInt() == 1){
                                builder.setMessage("Delete this message?")
                            }else{
                                builder.setMessage("Delete these messages?")
                            }

                            builder.setNegativeButton("No") { d, _ -> d.cancel() }.
                                    setPositiveButton("Yes") { _, _ ->
                                        val selectedViews = messageListAdapter.getSelectedIds()
                                        (selectedViews.size()-1 downTo 0)
                                                .filter { selectedViews.valueAt(it) }
                                                .map { messageListAdapter.getItem(selectedViews.keyAt(it)) }
                                                .forEach { deleteMessageEverywhere(it) }
                                        selectedViews.clear()

                                        p0?.finish()
                                     }
                            builder.create().show()
                            return true
                        }
                        else -> return false
                    }
                }

                override fun onItemCheckedStateChanged(p0: ActionMode?, p1: Int, p2: Long, p3: Boolean) {
                    val checkedCount: Int = messageList.checkedItemCount
                    p0?.title = checkedCount.toString()
                    messageListAdapter.toggleSelection(p1)
                }

                override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                    val menuInflater = p0?.menuInflater
                    menuInflater?.inflate(R.menu.menu_multi_selected, p1)
                    return true
                }

                override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean = false

                override fun onDestroyActionMode(p0: ActionMode?) {
                    messageListAdapter.removeSelection()
                }
            })
        }
    }

    private fun deleteAllMessagesEverywhere(){
        messageManager?.deleteAllMessages()
        (messageList.adapter as MessageListAdapter).deleteAll()
    }

    private fun deleteMessageEverywhere(messageData: MessageData){
        messageManager?.deleteMessageById(messageData.id)
        (messageList.adapter as MessageListAdapter).delete(messageData)
    }

    /*
    private fun editMessage(i: Int){
        val intent = MessageConfigureActivity_Old.getStartActivityIntent(this)
        intent.putExtra("selected_message", message_ArrayList[i])
        message_ArrayList.removeAt(i)
        startActivityForResult(intent, 1)
    }
    */

    /*
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
    */

    /*
    private fun cancel(data: Intent?){
        receiveMessageAndUpdateListView(data)
        Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
        setupMessageList()
    }
    */
}


