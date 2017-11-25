package com.example.yue.nexttext.UI

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import android.widget.Toast
import com.example.yue.nexttext.Core.Database.MessageManager
import com.example.yue.nexttext.Core.SendReceiveService.AlarmReceiver
import com.example.yue.nexttext.Core.Utility.Constants
import com.example.yue.nexttext.DataType.MessageWrapper
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_message_list.*
import kotlinx.android.synthetic.main.app_bar_message_list.*
import kotlinx.android.synthetic.main.content_message_list.*
import java.util.*


class MessageListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var messageManager: MessageManager? = null
    private var alarmManger: AlarmManager? = null

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
            title = "All Messages"
            nav_view.setCheckedItem(R.id.nav_all_messages)
        }

        messageManager = MessageManager(applicationContext)
        setupMessageList()

        alarmManger = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Utilities.MESSAGE_CONFIGURE_ACTIVITY_REQUEST_CODE -> {
                when(resultCode){
                    Activity.RESULT_OK -> receiveMessageAndUpdateListView(data)
                }
            }
            Utilities.EDIT_MESSAGE_ACTIVITY_REQUEST_CODE -> {
                when(resultCode){
                    Activity.RESULT_OK -> receiveEditedMessageAndUpdateListView(data)
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

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchMenuItem = menu.findItem(R.id.search)

        val searchView = searchMenuItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                (message_list.adapter as MessageListAdapter).filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (message_list.adapter as MessageListAdapter).filter.filter(newText)
                return true
            }
        })

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                searchView.clearFocus()
                findViewById<View>(R.id.emptyView_text).visibility = View.VISIBLE
                findViewById<View>(R.id.no_result_empty_view).visibility = View.INVISIBLE
                message_list.emptyView = emptyView_for_ListView
                when(toolbar.title){
                    "SMS" -> setupMessageList_SMSOnly()
                    "Email" -> setupMessageList_EmailsOnly()
                    "Email Settings" -> setupMessageList_EmailSettings();
                    else -> refreshMessgeList()
                }
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                findViewById<View>(R.id.no_result_empty_view).visibility = View.VISIBLE
                findViewById<View>(R.id.emptyView_text).visibility = View.INVISIBLE
                message_list.emptyView = no_result_empty_view
                return true  // Return true to expand action view
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
            R.id.developer_delete_all_messages ->{
                deleteAllMessagesEverywhere()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
            R.id.nav_email_settings -> {
                setupMessageList_EmailSettings()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    //MARK: private methods
    private fun setupMessageList(){
        message_list.emptyView = emptyView_for_ListView
        findViewById<View>(R.id.no_result_empty_view).visibility = View.INVISIBLE

        val messageListAdapter = MessageListAdapter(this@MessageListActivity, messageManager!!.allMessages)

        message_list.adapter = messageListAdapter

        //message_list.isTextFilterEnabled = true

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
                        val alertDialog = builder.create()
                        alertDialog.setOnShowListener {
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(applicationContext,R.color.colorPrimary))
                            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(applicationContext,R.color.colorPrimary))
                        }
                        alertDialog.show()
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

    private fun setupMessageList_EmailSettings(){
        toolbar.title = "Email Settings"
        emptyView_text.text = "No settings"
        (message_list.adapter as MessageListAdapter)
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
            Toast.makeText(applicationContext, "Error: failed to receive user-created message", Toast.LENGTH_SHORT).show()
        }
        else {
            messageManager?.addMessage(receivedCompleteData)
            (message_list.adapter as MessageListAdapter).add(receivedCompleteData)
            setUpAlarm(receivedCompleteData)
        }
    }

    private fun receiveEditedMessageAndUpdateListView(data: Intent?){
        val receivedEditedData = data?.getParcelableExtra<MessageWrapper>(Utilities.EDITED_DATA)
        if(receivedEditedData == null){
            Toast.makeText(applicationContext, "Error: failed to receive user-edited message", Toast.LENGTH_SHORT).show()
        }
        else {
            messageManager?.updateMessage(receivedEditedData)
            (message_list.adapter as MessageListAdapter).replace(receivedEditedData)
        }
    }


    private fun setUpAlarm(messageWrapper: MessageWrapper) {
        if (messageWrapper.timeTrigger != null){
            Log.d("setUpAlarm: ", "Alarm is being set.");


            val calendar = messageWrapper.timeTrigger!!.getCalendar()
            val intent = Intent(applicationContext, AlarmReceiver::class.java)
            val bundle = Bundle()
            bundle.putParcelable(Constants.FINAL_DATA, messageWrapper)
            intent.putExtra(com.example.yue.nexttext.Core.Utility.Constants.FINAL_DATA_BUNDLE, bundle)

            val alarmIntent = PendingIntent.getBroadcast(applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            if (calendar.timeInMillis <= System.currentTimeMillis()){
                Log.d("setUpAlarm: ", "The time picked has passed, the alarm won't work properly.")
            }

            if(!messageWrapper.message._to.contains("@")) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                    run {
                        ActivityCompat.requestPermissions(this@MessageListActivity, arrayOf(Manifest.permission.SEND_SMS),
                                1)
                    }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    run {
                        ActivityCompat.requestPermissions(this@MessageListActivity, arrayOf(Manifest.permission.READ_PHONE_STATE), 1);
                    }
            }

            alarmManger!!.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
            Log.d(null, "Alarm is set.")

            //calendar.timeInMillis




        }
    }
}
