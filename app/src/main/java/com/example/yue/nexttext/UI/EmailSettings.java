package com.example.yue.nexttext.UI;

/**
 * Created by jamesmulvenna on 2017-11-19.
 */


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.yue.nexttext.Core.EmailService.GMailSender;
import com.example.yue.nexttext.DataType.Message;

import javax.mail.PasswordAuthentication;

public class EmailSettings {
    private String username;
    private String password;

    public EmailSettings(){
    }

    public void authenticateEmail(final String newUsername, final String newPassword) {
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, Integer> myAsync = new AsyncTask<String, Void, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                GMailSender gMailSender = new GMailSender(newUsername, newPassword);

                int checkError;

                try {
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
                if(result == 0){
                    //succeed
                    Toast.makeText(null, "You can now send Gmail messages.", Toast.LENGTH_LONG).show();
                    username = newUsername;
                    password = newPassword;
                }
                else{
                    //failed
                    Toast.makeText(null,"Account is invalid, please try again.",Toast.LENGTH_LONG).show();
                }
            }
        };
        myAsync.execute(newUsername,newPassword,"Sign up Message Sequence","",
                newUsername);
    }
}
