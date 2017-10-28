package com.example.yue.nexttext.UI

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.ListView
import com.example.yue.nexttext.DataType.MessageWrapper
import com.example.yue.nexttext.Database.MessageManager
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_message_list.*
import kotlinx.android.synthetic.main.app_bar_message_list.*
import kotlinx.android.synthetic.main.content_message_list.*

class MessageListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var messageManager: MessageManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ -> startCreatingNewMessage() }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        //by default the app should show all messages at first time
        if (savedInstanceState == null) {
            setTitle("All Messages")
            nav_view.setCheckedItem(R.id.nav_all_messages)
        }

        messageManager = MessageManager(applicationContext)
        setupMessageList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Utilities.MESSAGE_CONFIGURE_ACTIVITY_REQUEST_CODE->{
                when(resultCode){
                    Activity.RESULT_OK -> receiveMessageAndUpdateListView(data)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.message_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.developer_add_dummy_data -> {
                messageManager!!.prepareData()
                refreshMessgeList()
                return true
            }
            R.id.developer_delete_all_messages ->{
                deleteAllMessagesEverywhere()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_all_messages -> {
                refreshMessgeList()
            }
            R.id.nav_sms_only -> {
                setupMessageList_SMSOnly()
            }
            R.id.nav_email_only -> {
                setupMessageList_EmailsOnly()
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    //MARK: private methods
    private fun setupMessageList(){
        message_list.emptyView = emptyView_for_ListView

        val messageListAdapter = MessageListAdapter(baseContext, messageManager!!.allMessages)
        message_list.adapter = messageListAdapter

        message_list.setOnItemClickListener { _, _, i, _ -> editMessage(i) }

        message_list.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL

        setupMultiChoiceListener(messageListAdapter)

        (message_list.adapter as MessageListAdapter).notifyDataSetChanged()
    }

    private fun setupMultiChoiceListener(messageListAdapter: MessageListAdapter) {
        message_list.setMultiChoiceModeListener(object : AbsListView.MultiChoiceModeListener {
            override fun onItemCheckedStateChanged(p0: ActionMode?, p1: Int, p2: Long, p3: Boolean) {
                val checkedCount: Int = message_list.checkedItemCount
                p0?.title = checkedCount.toString()
                messageListAdapter.toggleSelection(p1)
            }

            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                val menuInflater = p0?.menuInflater
                menuInflater?.inflate(R.menu.menu_list_view_multiselect, p1)
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean = false

            override fun onDestroyActionMode(p0: ActionMode?) {
                messageListAdapter.removeSelection()
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                when (p1?.itemId) {
                    R.id.action_delete_select_message -> {
                        //Toast.makeText(applicationContext, "Deleted!", Toast.LENGTH_SHORT).show()
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MessageListActivity)

                        if (p0?.title.toString().toInt() == 1) {
                            builder.setMessage("Delete this message?")
                        } else {
                            builder.setMessage("Delete these messages?")
                        }

                        builder.setNegativeButton("No") { d, _ -> d.cancel() }.
                                setPositiveButton("Yes") { _, _ ->
                                    val selectedViews = messageListAdapter.getSelectedIds()
                                    (selectedViews.size() - 1 downTo 0)
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
        })
    }

    private fun deleteAllMessagesEverywhere(){
        messageManager?.deleteAllMessages()
        (message_list.adapter as MessageListAdapter).deleteAll()
    }

    private fun deleteMessageEverywhere(messageWrapper: MessageWrapper){
        messageManager?.deleteMessageById(messageWrapper.id)
        (message_list.adapter as MessageListAdapter).delete(messageWrapper)
    }

    private fun refreshMessgeList(){
        toolbar.title = "All Messages"
        emptyView_text.text = resources.getString(R.string.empty_view_default_text)
        (message_list.adapter as MessageListAdapter).setMessageList(messageManager!!.allMessages)
    }

    private fun setupMessageList_SMSOnly(){
        toolbar.title = "SMS"
        emptyView_text.text = resources.getString(R.string.empty_view_no_sms)
        (message_list.adapter as MessageListAdapter).setMessageList(messageManager!!.allSMS)
    }

    private fun setupMessageList_EmailsOnly(){
        toolbar.title = "Email"
        emptyView_text.text = resources.getString(R.string.empty_view_no_email)
        (message_list.adapter as MessageListAdapter).setMessageList(messageManager!!.allEmails)
    }

    private fun editMessage(position: Int){
        val messageWrapper: MessageWrapper = message_list.getItemAtPosition(position) as MessageWrapper
        val mIntent: Intent = EditMessageActivity.getStartActivityIntent(applicationContext)
        mIntent.putExtra(Utilities.EDIT_DATA, messageWrapper)
        startActivityForResult(mIntent, Utilities.EDIT_MESSAGE_ACTIVITY_REQUEST_CODE)
    }

    private fun startCreatingNewMessage(){
        startActivityForResult(MessageConfigureActivity.getStartActivityIntent(applicationContext), Utilities.MESSAGE_CONFIGURE_ACTIVITY_REQUEST_CODE)
    }

    private fun receiveMessageAndUpdateListView(data: Intent?){
        val receivedCompleteData = data?.getParcelableExtra<MessageWrapper>(Utilities.COMPLETE_DATA)
        if(receivedCompleteData == null){
            Log.e("MainActivity", "Received MessageData object is Null!")
        }
        else {
            messageManager?.addMessage(receivedCompleteData)
            (message_list.adapter as MessageListAdapter).add(receivedCompleteData)
        }
    }
}
