package com.example.yue.nexttext.SendReceiveService;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.yue.nexttext.DataType.MessageWrapper;
import com.example.yue.nexttext.Database.MessageManager;
import com.example.yue.nexttext.EmailService.GMailSupport;

import javax.mail.MessagingException;

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

        Bundle bundle = thisIntent.getBundleExtra("SOMEBUNDLEEXTRA");
        thisData  = (MessageWrapper) bundle.getParcelable("SOMEMESSAGEPARCABLE");

        if (thisData.getMessage().get_to().contains("@")){
            //email
            sendEmail();
            thisManager.deleteMessageById(thisData.getId());
        } else {
            //sms
            sendSms();
            thisManager.deleteMessageById(thisData.getId());
        }
        return super.onStartCommand(thisIntent, thisFlag, thisStartId);
    }

    public void sendEmail(){}
    public void sendSms(){}
}

