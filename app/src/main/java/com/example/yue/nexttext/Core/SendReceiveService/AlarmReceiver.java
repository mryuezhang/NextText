package com.example.yue.nexttext.Core.SendReceiveService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.yue.nexttext.Core.Utility.Constants;
import com.example.yue.nexttext.DataType.MessageWrapper;

/**
 * Created by jamesmulvenna on 2017-10-24.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context thisContext, Intent thisIntent) {
        Log.d(null,"FUCKKKKCKKKKKKKKKKKKK");
        Bundle bundle = thisIntent.getBundleExtra(Constants.TIME_TRIGGER_ALARM);
        MessageWrapper thisData = bundle.getParcelable(Constants.TIME_TRIGGER_DATA);

        Intent in = new Intent(thisContext, MessageSender.class);
        in.putExtra(Constants.FINAL_DATA, bundle);
        thisContext.startService(in);
    }
}