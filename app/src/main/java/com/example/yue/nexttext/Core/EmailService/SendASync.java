package com.example.yue.nexttext.Core.EmailService;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import javax.mail.MessagingException;

public class SendASync extends AsyncTask<String, Void, Integer> {
    private Context context;
    private GMailSender sender;
    private String username,password;

    public SendASync(Context context, String username, String password){
        super();
        this.context = context;
        this.username= username;
        this.password = password;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        sender = new GMailSender(username,password);
    }

    @Override
    protected Integer doInBackground(String... params) {
        int success = 0;
        try {
            sender.sendMail(params[3], params[4], params[0], params[2]);
            success = 0;

        } catch (Exception e) {
            e.printStackTrace();
            success = 1;
        }

        return success;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (result == 0) {
            Log.i(null, "Succeeded sending Email");
        } else {
            Log.e(null, "Failed sending Email");
        }
    }
}

