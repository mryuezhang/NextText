package com.example.yue.nexttext.Core.SendReceiveService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.yue.nexttext.Core.EmailService.GMailSender;
import com.example.yue.nexttext.Core.EmailService.SendASync;
import com.example.yue.nexttext.Core.Utility.Constants;
import com.example.yue.nexttext.DataType.Message;
import com.example.yue.nexttext.DataType.MessageWrapper;

/**
 * Created by jamesmulvenna on 2017-10-24.
 */

public class AlarmReceiver extends BroadcastReceiver {
    MessageWrapper wrapperData = null;
    @Override
    public void onReceive(Context thisContext, Intent thisIntent) {
        Log.d("alarmReceiver: ", "Got to the receiver.");

        Bundle bundle = thisIntent.getBundleExtra(Constants.FINAL_DATA_BUNDLE);
        if (bundle != null){
            wrapperData = (MessageWrapper)bundle.getParcelable(Constants.FINAL_DATA);
        }

        if (wrapperData.getMessage().get_to().contains("@")){
            sendEmail();
            Toast.makeText(thisContext, "Event has been triggered, your email to " + wrapperData.getMessage().get_to() + " is sending now.", Toast.LENGTH_LONG).show();
        } else {
            sendSms();
            Toast.makeText(thisContext, "Event has been triggered, your sms to " + wrapperData.getMessage().get_to() + " is sending now.", Toast.LENGTH_LONG).show();
        }

        /*Intent intent = new Intent(thisContext, MessageSender.class);
        intent.putExtra(Constants.SENT_DATA, bundle);
        thisContext.startService(intent);*/

    }

    public void sendEmail() {
        final Message message = new Message("jamespmulvenna@gmail.com", "asklzmV!", wrapperData.getMessage().get_to(), wrapperData.getMessage().get_subject(), wrapperData.getMessage().get_content());
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, Integer> myAsync = new AsyncTask<String, Void, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                GMailSender gMailSender = new GMailSender(message.get_from(), message.get_password());

                int checkError;

                try {
                    gMailSender.sendMail(message.get_subject(), message.get_content(), message.get_from(), message.get_to());
                    checkError = 0;
                } catch (Exception ex) {
                    checkError = 1;
                    ex.printStackTrace();
                }
                return checkError;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if (result == 0) {
                    //succeed

                } else {
                    //failed

                }
            }
        };
        myAsync.execute(message.get_from(), message.get_password(), message.get_subject(), message.get_content(), message.get_to());
    }

    public void sendSms(){
        Message message = new Message(wrapperData.getMessage().get_to(), wrapperData.getMessage().get_content());
        SmsManager smsManager = SmsManager.getDefault();

        /* I don't believe the intents should be null, but not sure what they should be
        https://developer.android.com/reference/android/telephony/SmsManager.html
        */
        smsManager.sendTextMessage(message.get_to(), null, message.get_content(), null, null);
    }
}