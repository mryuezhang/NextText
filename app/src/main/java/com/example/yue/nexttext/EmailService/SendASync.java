package com.example.yue.nexttext.EmailService;


import android.os.AsyncTask;
import android.util.Log;

import javax.mail.MessagingException;

public class SendASync extends AsyncTask<String, Void, Integer> {
    private static final String TAG = "SendASync";
    private GMailSupport sender;
    private String username,password;

    public SendASync(String username, String password){
        super();
        this.username= username;
        this.password = password;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        sender = new GMailSupport(username,password);
    }

    @Override
    protected Integer doInBackground(String... params) {
        int success = 0;
        try {
            sender.sendMail(username, params[2], params[3], params[4]);
            success = 0;

        } catch (MessagingException e) {
            e.printStackTrace();
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
            Log.i(TAG, "Succeeded sending Email");
        } else {
            Log.e(TAG, "Failed sending Email");
        }
    }
}

