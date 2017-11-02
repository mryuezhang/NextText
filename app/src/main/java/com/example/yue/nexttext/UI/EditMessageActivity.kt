package com.example.yue.nexttext.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.yue.nexttext.DataType.MessageWrapper
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_edit_message.*

/**
 * Created by yue on 2017-10-28.
 */
class EditMessageActivity: AppCompatActivity(), MessageDataPasser{
    private var receivedMessage: MessageWrapper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_message)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        receivedMessage = intent.getParcelableExtra<MessageWrapper>(Utilities.EDIT_DATA)

        if (receivedMessage!!.message._to.contains("@")){
            val contentFragment = EditEmailFragment()
            contentFragment.arguments = intent.extras

            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, contentFragment, "Email").commit()
        }
        else{
            val contentFragment = EditSMSFragment()
            contentFragment.arguments = intent.extras

            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, contentFragment, "SMS").commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_message, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when(item.itemId) {
            R.id.action_done_edit -> {
                val fragment = if (fragmentManager.findFragmentByTag("SMS") != null){
                    fragmentManager.findFragmentByTag("SMS")
                } else {
                    fragmentManager.findFragmentByTag("Email")
                }
                fragmentManager.beginTransaction().remove(fragment).commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
    }

    override fun onDataPass(data: MessageWrapper) {
        intent.putExtra(Utilities.EDITED_DATA, data)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        fun getStartActivityIntent(context: Context) =
                Intent(context, EditMessageActivity::class.java)
    }
}