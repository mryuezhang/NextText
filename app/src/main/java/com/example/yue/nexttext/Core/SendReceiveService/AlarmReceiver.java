package com.example.yue.nexttext.Core.SendReceiveService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.yue.nexttext.Core.Utility.Constants;
import com.example.yue.nexttext.DataType.MessageWrapper;

/**
 * Created by jamesmulvenna on 2017-10-24.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context thisContext, Intent thisIntent) {
        Log.d("test1", Constants.TIME_TRIGGER_DATA);
        Toast.makeText(thisContext, "Alarm Triggered", Toast.LENGTH_LONG).show();

        Bundle bundle = thisIntent.getBundleExtra(Constants.TIME_TRIGGER_DATA);
        MessageWrapper wrapperData = (MessageWrapper) bundle.getParcelable(Constants.FINAL_DATA);

        Intent intent = new Intent(thisContext, MessageSender.class);
        intent.putExtra(Constants.SENT_DATA, bundle);
        thisContext.startService(intent);
    }
}