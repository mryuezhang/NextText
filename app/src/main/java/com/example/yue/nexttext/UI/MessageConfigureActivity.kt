package com.example.yue.nexttext.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.SparseArray
import android.view.*
import com.example.yue.nexttext.DataType.Message
import com.example.yue.nexttext.DataType.MessageWrapper
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_message_configure.*
import kotlinx.android.synthetic.main.fragment_email.*
import kotlinx.android.synthetic.main.fragment_sms.*

/**
 * Created by yue on 2017-10-28.
 */
class MessageConfigureActivity : AppCompatActivity() {
    private val tag = "MessageConfiActivity"
    private var messageCollectionPaperAdapter: MessageCollectionPagerAdapter? = null

    companion object {
        fun getStartActivityIntent(context: Context) =
                Intent(context, MessageConfigureActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_configure)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        messageCollectionPaperAdapter = MessageCollectionPagerAdapter(supportFragmentManager)
        viewPager_message_configure.adapter = messageCollectionPaperAdapter
        tabs.setupWithViewPager(viewPager_message_configure)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode){
            Utilities.MESSAGE_CONFIRMATION_ACTIVITY_REQUEST_CODE ->{
                when(resultCode){
                    Activity.RESULT_OK -> receiveCompleteMessage(data)
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

    }

    //MARK: Action menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_message_configure, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
            R.id.action_save_new_message_configure -> {
                val messageData = createNewMessage()
                if(viewPager_message_configure.currentItem == 0 && !isNullSMS(messageData) ||
                        viewPager_message_configure.currentItem == 1 && !isNullEmail(messageData)){
                    forwardMessageData(messageData)
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }


    //MARK: Private methods
    private fun createNewMessage(): MessageWrapper{
        val message: Message?
        val messageWrapper: MessageWrapper?
        if(viewPager_message_configure.currentItem == 0){
            //SMS
            val fragment: SMSObjectFragment = messageCollectionPaperAdapter?.
                    getRegisteredFragment(viewPager_message_configure.currentItem) as SMSObjectFragment
            message = Message(fragment.getPhoneNumber(),fragment.getSMSContent() )
            messageWrapper = MessageWrapper(message)
        }
        else{
            //Email
            val fragment: EmailObjectFragment = messageCollectionPaperAdapter?.
                    getRegisteredFragment(viewPager_message_configure.currentItem) as EmailObjectFragment

            //TODO This is probably not going to be the final implement
            message = Message(null, null, fragment.getEmailAddress(), fragment.getEmailSubject(), fragment.getEmailCompose())

            messageWrapper = MessageWrapper(message)
        }

        return messageWrapper
    }

    private fun receiveCompleteMessage(data: Intent?){
        val receivedCompleteData = data?.getParcelableExtra<MessageWrapper>(Utilities.COMPLETE_DATA)

        if (receivedCompleteData == null) Log.e(tag, "Null received!")

        intent.putExtra(Utilities.COMPLETE_DATA, receivedCompleteData)

        setResult(Activity.RESULT_OK, intent)

        finish()
    }

    private fun forwardMessageData(messageData: MessageWrapper){
        val messageConfirmationActivityIntent = MessageConfirmActivity.getStartActivityIntent(this)
        messageConfirmationActivityIntent.putExtra(Utilities.INCOMPLETE_DATA, messageData)
        startActivityForResult(messageConfirmationActivityIntent,
                Utilities.MESSAGE_CONFIRMATION_ACTIVITY_REQUEST_CODE)
    }

    /**
     * This function is used to check whether user is completely finished input for SMS
     */
    private fun isNullSMS(messageData: MessageWrapper): Boolean =
            messageData.message._to == "" || messageData.message._content == ""

    /**
     * This function is used to check whether user is completely finished input for Email
     * TODO This function is currently not complete yet
     */
    private fun isNullEmail(messageData: MessageWrapper): Boolean =
            messageData.message._to == "" || messageData.message._content == ""

    //MARK: MessageCollectionPagerAdapter class
    class MessageCollectionPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
        private var registeredFragments = SparseArray<Fragment>()

        override fun getItem(position: Int): Fragment = when(position){
            0 -> SMSObjectFragment()
            else -> EmailObjectFragment()
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence = when(position){
            0 -> "SMS"
            else -> "Email"
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position) as Fragment
            registeredFragments.put(position, fragment)
            return fragment
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
            registeredFragments.remove(position)
            super.destroyItem(container, position, `object`)
        }

        fun getRegisteredFragment(position: Int): Fragment = registeredFragments.get(position)
    }

    //MARK: SMSObjectFragment class
    class SMSObjectFragment: Fragment(){
        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
                inflater?.inflate(R.layout.fragment_sms, container, false)

        fun getPhoneNumber(): String = editText_phoneNumber.text.toString()
        fun getSMSContent(): String = editText_SMSContent.text.toString()
    }

    //MARK: EmailObjectFragment class
    class EmailObjectFragment: Fragment(){
        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
                inflater?.inflate(R.layout.fragment_email, container, false)

        fun getEmailAddress(): String = editTextViewWithPrefix_emailAddress.text.toString()
        fun getEmailSubject(): String = editText_emailSubject.text.toString()
        fun getEmailCompose(): String = editText_composeEmail.text.toString()
    }
}