package com.example.yue.nexttext.SendReceiveService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.yue.nexttext.DataType.MessageWrapper;

/**
 * Created by jamesmulvenna on 2017-10-24.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context thisContext, Intent thisIntent) {
        Bundle bundle = thisIntent.getBundleExtra("SOMEBUNDLE");
        MessageWrapper thisData = (MessageWrapper) bundle.getParcelable("SOMEMESSAGEPARCABLE");

        Intent in = new Intent(thisContext, MessageSender.class);
        in.putExtra("data", bundle);
        thisContext.startService(in);
    }
}