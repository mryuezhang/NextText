package com.example.yue.nexttext.EmailService;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import javax.mail.MessagingException;

public class SendASync extends AsyncTask<String, Void, Integer> {
    Context context;
    GMailSupport sender;
    String username,password;

    public SendASync(Context context, String username, String password){
        super();
        this.context = context;
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
            Toast.makeText(context, "Succeeded sending Email", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Failed sending Email", Toast.LENGTH_LONG).show();
        }
    }
}

