package com.example.yue.nexttext.Core.SendReceiveService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.yue.nexttext.Core.Database.MessageManager;
import com.example.yue.nexttext.Core.EmailService.GMailSender;
import com.example.yue.nexttext.Core.EmailService.SendASync;
import com.example.yue.nexttext.Core.Utility.Constants;
import com.example.yue.nexttext.DataType.Message;
import com.example.yue.nexttext.DataType.MessageWrapper;
import com.example.yue.nexttext.UI.MessageListActivity;

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
            sendEmail(thisContext);
        } else {
            sendSms(thisContext);
        }
    }

    public void sendEmail(final Context context) {
            final Message message = new Message(wrapperData.getMessage().get_from(), wrapperData.getMessage().get_password(), wrapperData.getMessage().get_to(), wrapperData.getMessage().get_subject(), wrapperData.getMessage().get_content());
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
                        Toast.makeText(context, "Your email to " + wrapperData.getMessage().get_to() + ", and from " + wrapperData.getMessage().get_from() + " has sent.", Toast.LENGTH_LONG).show();
                    } else {
                        //failed
                        Toast.makeText(context, "Your email to " + wrapperData.getMessage().get_to() + " failed to send, please retry making sure your password is correct.", Toast.LENGTH_LONG).show();
                    }
                }
            };
            myAsync.execute(message.get_from(), message.get_password(), message.get_subject(), message.get_content(), message.get_to());
    }

    public void sendSms(Context context){
        Message message = new Message(wrapperData.getMessage().get_to(), wrapperData.getMessage().get_content());
        SmsManager smsManager = SmsManager.getDefault();

        smsManager.sendTextMessage(message.get_to(), null, message.get_content(), null, null);

        Toast.makeText(context, "Your sms to " + wrapperData.getMessage().get_to() + " has sent.", Toast.LENGTH_LONG).show();

    }
}