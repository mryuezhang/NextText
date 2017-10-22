package com.example.yue.nexttext.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yue.nexttext.Data.MessageData;
import com.example.yue.nexttext.Data.Message;
import com.example.yue.nexttext.Data.Time;
import com.example.yue.nexttext.Data.Location;
import com.example.yue.nexttext.Data.Weather;

import java.util.ArrayList;

public class MessageManager {
    SQLiteDatabase dataManager =null;
    Context context;

    public MessageManager(Context context){
        this.context = context;
    }


    public SQLiteDatabase getDatabase(){
        dataManager = context.openOrCreateDatabase("messageData.db", Context.MODE_PRIVATE, null);

        //if the database exists
        if (dataManager != null) {
            //if the table exists
            if (doesTableExist(dataManager, "tbl_message_data")) {
                return dataManager;
            } else {
                //create message data table
                createMessageTable();
            }

        }
        return dataManager;
    }

    public boolean doesTableExist(SQLiteDatabase thisDatabase, String thisTableName){
        Cursor cursor = thisDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + thisTableName
                + "'", null);
        if (cursor != null){
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public void createMessageTable(){
        String sqlData = "CREATE TABLE [tbl_message_data] (\n" +
                "[id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                //could also be used as display name for sms for instance if Phone number X
                "[fromobj] TEXT  NULL,\n" +
                "[password] TEXT  NULL,\n" +
                "[toobj] TEXT  NULL,\n" +
                "[subject] TEXT  NULL,\n" +
                "[message] TEXT  NULL,\n" +
                "[currtime] TEXT  NULL,\n" +
                "[date] DATE  NULL,\n" +
                "[time] TIME  NULL,\n" +
                "[type] INTEGER  NULL,\n" +
                "[status] INTEGER  NULL\n" +
                //might need more columns later once known
                "[location] LOCATION  NULL,\n" +
                "[weather] WEATHER  NULL,\n" +
                ")";
        dataManager.execSQL(sqlData);
    }

    public int checkConditionType(MessageData data){
        if (data.getTime() != null){
            return 1;
        }
        else if (data.getLocation() != null){
            return 2;
        }
        else if (data.getWeather() != null){
            return 3;
        }

        return 0;
    }

    public boolean insertMessage(MessageData data){
        ContentValues content = new ContentValues();
        //get message content
        Message message = data.getMessage();
        //get condition content

        switch(checkConditionType(data)){
            // condition is time
            case 1:
                Time time = data.getTime();
                //add content
                content.put("fromobj", message.getFrom());
                content.put("password", message.getPassword());
                content.put("toobj", message.getTo());
                if(message.getSubject() != null){
                    content.put("subject", message.getSubject());
                }
                content.put("message", message.getMessage());
                content.put("currtime", data.getCurrentTime());

                //add time
                content.put("date", time.getDate());
                content.put("time", time.getTime());
                content.put("type", time.getType());
                content.put("status", time.getStatus());

            case 2:
                Location location = data.getLocation();
                //add content
                content.put("fromobj", message.getFrom());
                content.put("password", message.getPassword());
                content.put("toobj", message.getTo());
                if(message.getSubject() != null){
                    content.put("subject", message.getSubject());
                }
                content.put("message", message.getMessage());
                content.put("currtime", data.getCurrentTime());

                //add location
                //content.put("location", null);

            case 3:
                Weather weather = data.getWeather();
                //add content
                content.put("fromobj", message.getFrom());
                content.put("password", message.getPassword());
                content.put("toobj", message.getTo());
                if(message.getSubject() != null){
                    content.put("subject", message.getSubject());
                }
                content.put("message", message.getMessage());
                content.put("currtime", data.getCurrentTime());

                //add weather
                //content.put("weather", null);
            default:

        }

        if(dataManager.insert("tbl_message_data", null, content) == -1){
            return false;
        }
        else{
            return true;
        }
    }


    public ArrayList<MessageData> loadDataTbl_Message_Data(){
        ArrayList<MessageData> messageData =new ArrayList<MessageData>();
        //this should get all MessageData in ascending order of time the messagedata object was made
        Cursor cursor = dataManager.query("tbl_message_data",null,null,null,null,null,"currtime ASC");
        int count = cursor.getCount();
        if (count != 0) {
            messageData = new ArrayList();
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false){
                //time
                if(cursor.getString(6) != null && cursor.getString(10) == null && cursor.getString(11) == null) {
                    //    MessageData messageData = new MessageData(cursor.getString(1), cursor.getString(2),cursor.getString(3));
                    Message thisMessage = new Message(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                            cursor.getString(4), cursor.getString(5));

                    Time thisTime = new Time(cursor.getString(6), cursor.getString(7), cursor.getInt(8), cursor.getInt(9));
                    MessageData thisData = new MessageData(thisMessage, thisTime, Integer.parseInt(cursor.getString(0)));
                    messageData.add(thisData);
                    cursor.moveToNext();
                }
                //location
                else if(cursor.getString(10) != null && cursor.getString(6) == null && cursor.getString(11) == null){
                    cursor.moveToNext();
                }
                //weather
                else if(cursor.getString(11) != null && cursor.getString(6) == null && cursor.getString(10) == null){
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();
        return messageData;
    }

    public void deleteAllTable_MessageData(){
        dataManager.execSQL("delete from tbl_message_data");
        dataManager.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='tbl_message_data'");
    }

    public void deleteMessageById(int id){
        String sql = "delete from tbl_message_data where id = '" + id + "'";
        dataManager.execSQL(sql);
    }

    public boolean updateMessageByMessageData(MessageData data){
        ContentValues values = new ContentValues();
        values.put("fromobj", data.getMessage().getFrom());
        values.put("password", data.getMessage().getPassword());
        values.put("toobj",data.getMessage().getTo());
        values.put("message",data.getMessage().getMessage());
        if(data.getMessage().getSubject() != null){
            values.put("subject", data.getMessage().getSubject());
        }
        values.put("date",data.getTime().getDate());
        values.put("time",data.getTime().getTime());
        values.put("type",data.getTime().getType());
        values.put("status",data.getTime().getStatus());
        //values.put("location",null);
        //values.put("weather",null);

        int returnValue = dataManager.update("tbl_message_data",values ,"id =" + data.getId(), null);
        if (returnValue == 0){
            //failed
            return false;
        } else {
            return true;
        }
    }
}
