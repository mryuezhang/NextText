package com.example.yue.nexttext.Core.EmailService;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.example.yue.nexttext.DataType.Message;

/**
 * Created by jamesmulvenna on 2017-11-23.
 */

public class EmailSettings {
    private String user;
    private String pass;

    public EmailSettings(String nUser, String nPass) {
        user = nUser;
        pass = nPass;
    }

    public void sendEmail() {
        final String from = "jamespmulvenna@gmail.com";
        final String password = "asklzmV!";
        final String to = "jamespmulvenna@gmail.com";
        final String subject = "subject";
        final String content = "content";
        final Message message = new Message(from, password, to, subject, content);
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, Integer> myAsync = new AsyncTask<String, Void, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                GMailSender gMailSender = new GMailSender(from, password);

                int checkError = 0;

                try {
                    gMailSender.sendMail(subject, content, from, to);
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
                    Log.d(null, "success");
                } else {
                    //failed
                    Log.d(null, "fail");
                }
            }
        };
        myAsync.execute(from, password, subject, content, to);
    }
}
