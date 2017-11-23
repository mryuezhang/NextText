package com.example.yue.nexttext.UI;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.example.yue.nexttext.UI.Utilities;

import com.example.yue.nexttext.Core.EmailService.GMailSender;
import com.example.yue.nexttext.DataType.Message;

import java.util.Random;

/**
 * Created by jamesmulvenna on 2017-11-19.
 */

public class EmailSettings {
    private String username;
    private String password;

    public EmailSettings(String user, String pass){
        this.username = user;
        this.password = pass;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void sendEmail() {
        final Message message = new Message("jamespmulvenna@gmail.com", "asklzmV!", "jamespmulvenna@gmail.com", "test sub", "test cont");
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, Integer> myAsync = new AsyncTask<String, Void, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                GMailSender gMailSender = new GMailSender(message.get_from(), message.get_password());

                int checkError;

                try {
                    gMailSender.sendMail(message.get_subject(), message.get_content(), message.get_from(), message.get_to());
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
                    //succeed, delete message from messagelist and database

                } else {
                    //failed, try again?

                }
            }
        };
        myAsync.execute(message.get_from(), message.get_password(), message.get_subject(), message.get_content(), message.get_to());
    }

    public void configureConfirmationEmail() {
        Log.d(null, "Got to sending the email");
        Random r = new Random();
        int code = r.nextInt(9999 - 1000) + 1000;
        final Message message = new Message("jamespmulvenna@gmail.com", "asklzmV!", username, "Confirmation Code", Integer.toString(code));
        Log.d(null, username);
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, Integer> myAsync = new AsyncTask<String, Void, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                GMailSender gMailSender = new GMailSender(message.get_from(), message.get_password());

                int checkError;

                try {
                    gMailSender.sendMail(message.get_subject(), message.get_content(), message.get_from(), message.get_to());
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
                    //succeed, delete message from messagelist and database
                    Log.d(null, "success");

                } else {
                    //failed, try again?
                    Log.d(null, "fail");


                }
            }
        };
        myAsync.execute(message.get_from(), message.get_password(), message.get_subject(), message.get_content(), message.get_to());
        Log.d(null, "Got to end");
    }

    public Boolean validatedEmail(int expectedCode, int actualCode){
        if (expectedCode == actualCode){
            return true;
        } else {
            return false;
        }
    }
}
