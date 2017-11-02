package com.example.yue.nexttext.Core.SendReceiveService;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;

import com.example.yue.nexttext.Core.Database.MessageManager;
import com.example.yue.nexttext.Core.EmailService.GMailSender;
import com.example.yue.nexttext.Core.Utility.Constants;
import com.example.yue.nexttext.DataType.Message;
import com.example.yue.nexttext.DataType.MessageWrapper;

/**
 * Created by jamesmulvenna on 2017-10-24.
 */

public class MessageSender extends Service {
    MessageManager thisManager;
    MessageWrapper thisData;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent thisIntent, int thisFlag, int thisStartId) {
        thisManager = new MessageManager(getApplicationContext());

        Bundle bundle = thisIntent.getBundleExtra(Constants.FINAL_DATA);
        thisData  = bundle.getParcelable(Constants.TIME_TRIGGER_DATA);

        if (thisData.getMessage().get_to().contains("@")){
            //email
            sendEmail();
            //probably good practice below
            //thisManager.deleteMessageById(thisData.getId());
        } else {
            //sms
            sendSms();
            //probably good practice below
            //thisManager.deleteMessageById(thisData.getId());
        }
        return super.onStartCommand(thisIntent, thisFlag, thisStartId);
    }

    public void sendEmail(){
        Message message = new Message(thisData.getMessage().get_from(), thisData.getMessage().get_password(), thisData.getMessage().get_to(), thisData.getMessage().get_subject(), thisData.getMessage().get_content());

        GMailSender gMailSender = new GMailSender(message.get_from(), message.get_password());

        try {
            gMailSender.sendMail(message.get_subject(), message.get_content(), message.get_from(), message.get_to());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSms(){
        Message message = new Message(thisData.getMessage().get_to(), thisData.getMessage().get_content());
        SmsManager smsManager = SmsManager.getDefault();

        /* I don't believe the intents should be null, but not sure what they should be
        https://developer.android.com/reference/android/telephony/SmsManager.html
        */
        smsManager.sendTextMessage(message.get_to(), null, message.get_content(), null, null);
    }
}

