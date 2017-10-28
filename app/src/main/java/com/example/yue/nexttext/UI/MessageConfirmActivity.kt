package com.example.yue.nexttext.UI

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import com.example.yue.nexttext.DataType.MessageWrapper
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.activity_message_confirm.*
import kotlinx.android.synthetic.main.fragment_time_trigger_picker.*

/**
 * Created by yue on 2017-10-28.
 */
class MessageConfirmActivity : AppCompatActivity() {
    private var receivedMessageDataObject: MessageWrapper? = null

    companion object {
        fun getStartActivityIntent(context: Context) =
                Intent(context, MessageConfirmActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_confirm)

        receivedMessageDataObject = intent.getParcelableExtra(Utilities.INCOMPLETE_DATA)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val triggerPickerPagerAdapter = TriggerPickerPagerAdapter(supportFragmentManager)
        viewPager_message_confirmation.adapter = triggerPickerPagerAdapter
        tabs.setupWithViewPager(viewPager_message_confirmation)
    }

    //MARK: Action Menu methods
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_message_confirm, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_confirm_new_message -> {
                confirmMessage()
                return true
            }
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    //MARK: Private methods

    // TODO check trigger
    // currently this function just simply forward the received MessageData object to MainActivity
    // later on it should check if any trigger is set to this object, and forward it to MainActicity
    private fun confirmMessage(){
        intent.putExtra(Utilities.COMPLETE_DATA, receivedMessageDataObject)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    //MARK: MessageCollectionPagerAdapter class
    class TriggerPickerPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): android.support.v4.app.Fragment? = when(position){
            0 -> MessageConfirmActivity.TimePickerFragment()
            1 ->  MessageConfirmActivity.WeatherPickerFragment()
            else ->  MessageConfirmActivity.LocationPickerFragment()
        }

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence = when(position){
            0 -> "Time"
            1 -> "Weather"
            else -> "Location"
        }
    }

    //MARK: Time Picker Fragment
    class TimePickerFragment: android.support.v4.app.Fragment(){
        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
                inflater?.inflate(R.layout.fragment_time_trigger_picker, container, false)

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            date_display.setOnClickListener {
                val newFragment = DatePickerFragment()
                newFragment.textView = date_display
                newFragment.show(fragmentManager, "datePicker")
            }
            time_display.setOnClickListener {
                val newFragment = ExactTimePickerFragment()
                newFragment.textView = time_display
                newFragment.show(fragmentManager, "timePicker")
            }
        }

        //MARK: a fragment for a date picker widget
        class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {
            var textView: TextView? = null

            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
                    DatePickerDialog(activity,
                            this,
                            Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

            override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
                val simpleDateFormat = SimpleDateFormat("EEE, MMMM, d, yyyy")
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day)
                textView?.text = simpleDateFormat.format(calendar)
            }
        }

        //MARK: a fragment for a time picker widget
        class ExactTimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
            var textView: TextView? = null

            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
                    TimePickerDialog(activity,
                            this,
                            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                            Calendar.getInstance().get(Calendar.MINUTE),
                            DateFormat.is24HourFormat(activity))

            override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
                val simpleDateFormat = SimpleDateFormat("h:mm a")
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,hourOfDay, minute)
                textView?.text = simpleDateFormat.format(calendar)
            }
        }
    }


    //MARK: Location Picker Fragment
    class LocationPickerFragment: android.support.v4.app.Fragment(){
        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
                inflater?.inflate(R.layout.fragment_location_trigger_picker, container, false)
    }
    //MARK: Weather Picker Fragment
    class WeatherPickerFragment: android.support.v4.app.Fragment(){
        private val _tag = "WeatherPickerFragment"

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
                inflater?.inflate(R.layout.fragment_weather_trigger_picker, container, false)
    }
}