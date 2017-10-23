package com.example.yue.nexttext.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.yue.nexttext.Data.Location;
import com.example.yue.nexttext.Data.Message;
import com.example.yue.nexttext.Data.MessageData;
import com.example.yue.nexttext.Data.Time;
import com.example.yue.nexttext.Data.Weather;

import java.util.ArrayList;
import java.util.List;

public class MessageManager extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "messageManager";
    // Contacts table name
    private static final String TABLE_NAME = "messageTable";
    // Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FROM = "fromobj";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TO = "toobj";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_CURR_TIME = "currtime";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_TYPE = "type";
    private static final String KEY_STATUS = "status";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_WEATHER = "weather";

    public MessageManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_FROM + " TEXT," + KEY_PASSWORD + " TEXT," +
                KEY_TO + " TEXT," + KEY_SUBJECT + " TEXT," + KEY_MESSAGE + " TEXT," + KEY_CURR_TIME + " TEXT," + KEY_DATE + " DATE," + KEY_TIME + " TIME," +
                KEY_TYPE + " INTEGER," + KEY_STATUS + " INTEGER," + KEY_LOCATION + " LOCATION," + KEY_WEATHER + " WEATHER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Creating tables again
        onCreate(db);
    }

    public boolean isEmpty(){
        return this.getMessagesCount() <= 0;
    }

    public int checkConditionType(MessageData data){
        //time not null
        if (data.getTime() != null && data.getLocation() == null && data.getWeather() == null){
            return 1;
        }
        //location not null
        else if (data.getTime() == null && data.getLocation() != null && data.getWeather() == null){
            return 2;
        }
        //weather not null
        else if (data.getTime() == null && data.getLocation() == null && data.getWeather() != null){
            return 3;
        }

        return 0;
    }

    public void addMessage(MessageData data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        //add content
        content.put(KEY_FROM, data.getMessage().getFrom());
        content.put(KEY_PASSWORD, data.getMessage().getPassword());
        content.put(KEY_TO, data.getMessage().getTo());
        if(data.getMessage().getSubject() != null){
            content.put(KEY_SUBJECT, data.getMessage().getSubject());
        }
        content.put(KEY_MESSAGE, data.getMessage().getMessage());
        content.put(KEY_CURR_TIME, data.getCurrentTime());

        //add time

        switch(checkConditionType(data)) {
            // condition 1 is time, 2 is location, 3 is weather
            case 1:
                content.put(KEY_DATE, data.getTime().getDate());
                content.put(KEY_TIME, data.getTime().getTime());
                content.put(KEY_TYPE, data.getTime().getType());
                content.put(KEY_STATUS, data.getTime().getStatus());
                // condition is location
            case 2:
                //content.put(KEY_LOCATION, data.getLocation());
            case 3:
                //content.put(KEY_WEATHER, data.getWeather());
            default:
                break;
        }


        // Inserting Row
        db.insert(TABLE_NAME, null, content);
        db.close(); // Closing database connection
    }

    public MessageData getMessage(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                KEY_FROM, KEY_PASSWORD, KEY_TO, KEY_SUBJECT, KEY_MESSAGE, KEY_CURR_TIME, KEY_DATE, KEY_TIME, KEY_TYPE, KEY_STATUS, KEY_LOCATION, KEY_WEATHER}, KEY_ID + "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Message message = new Message(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        Time time = new Time(cursor.getString(6), cursor.getString(7), cursor.getInt(8), cursor.getInt(9));

        return new MessageData(message, time, Integer.parseInt(cursor.getString(0)));
    }

    // Getting All Messages
    public List<MessageData> getAllMessages() {
        List<MessageData> messageList = new ArrayList<MessageData>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                Time time = new Time(cursor.getString(6), cursor.getString(7), cursor.getInt(8), cursor.getInt(9));
                MessageData messageData = new MessageData(message, time, Integer.parseInt(cursor.getString(0)));
                // Adding message to list
                messageList.add(messageData);
            } while (cursor.moveToNext());
        }

        // return message list
        return messageList;
    }

    // Getting messages Count
    public int getMessagesCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }

        // return count
        return count;
    }

    public int updateMessage(MessageData data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //add content
        values.put(KEY_FROM, data.getMessage().getFrom());
        values.put(KEY_PASSWORD, data.getMessage().getPassword());
        values.put(KEY_TO, data.getMessage().getTo());
        if(data.getMessage().getSubject() != null){
            values.put(KEY_SUBJECT, data.getMessage().getSubject());
        }
        values.put(KEY_MESSAGE, data.getMessage().getMessage());
        values.put(KEY_CURR_TIME, data.getCurrentTime());

        //add time
        values.put(KEY_DATE, data.getTime().getDate());
        values.put(KEY_TIME, data.getTime().getTime());
        values.put(KEY_TYPE, data.getTime().getType());
        values.put(KEY_STATUS, data.getTime().getStatus());
        values.put(KEY_LOCATION, "null");
        values.put(KEY_WEATHER, "null");

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
    }

    public void deleteMessageById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID+"="+id, null);
    }

    public void deleteAllMessages(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }



    public void prepareData() throws Exception {

        //email
        Message msg1 = new Message("a1", "dummypass", "1", "somesubject", "testmessageemail");
        Time time1 = new Time(null, null, 1, 2);
        MessageData data1 = new MessageData(msg1, time1);

        //sms
        Message msg2 = new Message("b2", null, "2", null, "testmessagesms");
        Time time2 = new Time(null, null, 3, 4);
        MessageData data2 = new MessageData(msg2, time2);

        Message msg3 = new Message("c3", null, "3", "somesubject", "testmessageemail");
        Time time3 = new Time(null, null, 1, 2);
        MessageData data3 = new MessageData(msg3, time3);

        //sms
        Message msg4 = new Message("d4", null, "4", null, "testmessagesms");
        Time time4 = new Time(null, null, 3, 4);
        MessageData data4 = new MessageData(msg4, time4);

        //sms
        Message msg5 = new Message("e5", null, "5", null, "testmessagesms");
        Time time5 = new Time(null, null, 3, 4);
        MessageData data5 = new MessageData(msg5, time5);

        Message msg6 = new Message("f6", null, "6", null, "testmessagesms");
        Time time6 = new Time(null, null, 3, 4);
        MessageData data6 = new MessageData(msg6, time6);

        this.addMessage(data1);
        this.addMessage(data2);
        this.addMessage(data3);
        this.addMessage(data4);
        this.addMessage(data5);
        this.addMessage(data6);

        // Reading all messages
        Log.d("Messages: ", "Reading all messages...");
        List<MessageData> messages = this.getAllMessages();

        for (MessageData data : messages) {
            String log = "Id: " + data.getId() + " ,From: " + data.getMessage().getFrom() + ", password: " + data.getMessage().getPassword()
                    + ", to: " + data.getMessage().getTo() + ", subject: " + data.getMessage().getSubject() + ", message: " + data.getMessage().getMessage() +
                    ", currtime: " + data.getCurrentTime() + ", date: " + data.getTime().getDate() + ", time: " + data.getTime().getTime() + ", type: " + data.getTime().getType() +
                    ", status: " + data.getTime().getStatus() + ", location: " + data.getLocation() + ", weather: " + data.getWeather();
            Log.d("Message : :", log);
        }

    }
}


/*

    /*
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
    */

/*
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
        */


