package com.example.yue.nexttext.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class MyAsyncClass extends AsyncTask<String, Void, Integer> {
    Context context;
    GMailSupport sender;
    String username,password;
    boolean sent;

    public MyAsyncClass(Context context, String username, String password){
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
    protected Integer doInBackground(String... mApi) {
        int checkError;

        try {

            //sender.sendMail();
            checkError = 0;
            sent = true;
        }

        catch (Exception ex) {
            sent = false;
            checkError = 1;
            ex.printStackTrace();
        }
        return checkError;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if(result == 0){
            Toast.makeText(context, "Succeeded sending Email", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Failed to send Email", Toast.LENGTH_LONG).show();
        }
    }


}
