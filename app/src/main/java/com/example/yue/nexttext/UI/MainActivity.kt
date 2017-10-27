/*
 * Copyright (C) <2017>  <Yue Zhang>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.example.yue.nexttext.UI

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import com.example.yue.nexttext.Data.MessageData
import com.example.yue.nexttext.Database.MessageManager
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


/**
 * Created by yue on 2017-09-27.
 * Main screen
 */
class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private var messageManager: MessageManager? = null
    private var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageManager = MessageManager(this)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        setupNavigationDrawer()

        fab.setOnClickListener { startActivityForResult(MessageConfigureActivity.getStartActivityIntent(this),
                Utilities.MESSAGE_CONFIGURE_ACTIVITY_REQUEST_CODE) }

        //messageManager?.prepareData()
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

    //MARK: Action menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> return true
            R.id.action_delete_all_messages -> {
                deleteAllMessagesEverywhere()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        if (drawerToggle!!.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    //MARK: Navigation drawer
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle?.onConfigurationChanged(newConfig)
    }

    fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    //MARK: private methods
    private fun setupMessageList(){
        //set up a additional view for listview when it's empty
        messageList.emptyView = emptyView_for_ListView

        //inflate the list with data from database when database is not empty
        if(!messageManager!!.isEmpty){
            val messageListAdapter = MessageListAdapter(baseContext, messageManager!!.allMessages as ArrayList<MessageData>)
            messageList.adapter = messageListAdapter
            //messageList.setOnItemClickListener { adapterView, view, i, l -> editMessage(i) }
            messageList.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
            setupMultiChoiceListener(messageListAdapter)
        }
    }

    private fun setupNavigationDrawer(){
        drawerToggle = object : ActionBarDrawerToggle(this@MainActivity, drawer_layout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state.  */
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state.  */
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }
        }

        drawerToggle?.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle!!)

    }

    private fun setupMultiChoiceListener(messageListAdapter: MessageListAdapter) {
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

    private fun deleteAllMessagesEverywhere(){
        messageManager?.deleteAllMessages()
        if (messageList.adapter == null ) setupMessageList()
        else (messageList.adapter as MessageListAdapter).deleteAll()
    }

    private fun deleteMessageEverywhere(messageData: MessageData){
        messageManager?.deleteMessageById(messageData.id)
        if (messageList.adapter == null ) setupMessageList()
        (messageList.adapter as MessageListAdapter).delete(messageData)
    }

    private fun receiveMessageAndUpdateListView(data: Intent?){
        val receivedCompleteData = data?.getParcelableExtra<MessageData>(Utilities.COMPLETE_DATA)
        if(receivedCompleteData == null){
            Log.e(tag, "Received MessageData object is Null!")
        }
        else {
            messageManager?.addMessage(receivedCompleteData)
            if(messageList.adapter == null) setupMessageList()
            else (messageList.adapter as MessageListAdapter).add(receivedCompleteData)
        }
    }
}


