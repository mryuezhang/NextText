package com.example.yue.nexttext.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_message_configure.*

/**
 * Created by yue on 2017-09-30.
 * This class is used to display a collection of SMS and Message objects
 */
class MessageConfigureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_configure)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val messageCollectionPaperAdapter = MessageCollectionPagerAdapter(supportFragmentManager)
        viewPager_message_configure.adapter = messageCollectionPaperAdapter
        tabs.setupWithViewPager(viewPager_message_configure)

    }

    companion object {
        fun getStartActivityIntent(context: Context) =
                Intent(context, MessageConfigureActivity::class.java)
    }


    //MARK: Action menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_message_configure, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
            R.id.action_save_new_message_configure -> createNewMesage()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    //MARK: Private methods
    private fun createNewMesage(){
        if(viewPager_message_configure.currentItem == 0){
            //SMS
        }
        else{
            //Email
        }
        startActivityForResult(MessageConfirmationActivity.getStartActivityIntent(this), Utilities.MESSAGECONFIRMATIONACITIVY_REQUEST_CODE)
    }

    //MARK: MessageCollectionPagerAdapter class
    class MessageCollectionPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): Fragment = when(position){
            0 -> SMSObjectFragment()
            else -> EmailObjectFragment()
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence = when(position){
            0 -> "SMS"
            else -> "Email"
        }
    }

    //MARK: SMSObjectFragment class
    class SMSObjectFragment: Fragment(){
        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
                inflater?.inflate(R.layout.fragment_sms_object, container, false)
    }

    //MARK: EmailObjectFragment class
    class EmailObjectFragment: Fragment(){
        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
                inflater?.inflate(R.layout.fragment_email_layout, container, false)
    }
}