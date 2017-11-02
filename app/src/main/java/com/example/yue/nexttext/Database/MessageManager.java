package com.example.yue.nexttext.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.yue.nexttext.DataType.Message;
import com.example.yue.nexttext.DataType.MessageWrapper;
import com.example.yue.nexttext.DataType.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yue on 2017-10-28.
 */

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
    private static final String KEY_CONTENT = "content";
    private static final String KEY_CREATED_TIME = "created_time";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_WEATHER = "weather";

    public MessageManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_FROM + " TEXT," + KEY_PASSWORD + " TEXT," +
                KEY_TO + " TEXT," + KEY_SUBJECT + " TEXT," + KEY_CONTENT + " TEXT," + KEY_CREATED_TIME + " TEXT," + KEY_DATE + " DATE," + KEY_TIME + " TIME," +
                KEY_LOCATION + " LOCATION," + KEY_WEATHER + " WEATHER" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Creating tables again
        onCreate(sqLiteDatabase);
    }

    private int checkTriggerType(MessageWrapper data){
        //time not null
        if (data.getTimeTrigger() != null && data.getLocationTrigger() == null && data.getWeatherTrigger() == null){
            return 1;
        }
        //location not null
        else if (data.getTimeTrigger() == null && data.getLocationTrigger() != null && data.getWeatherTrigger() == null){
            return 2;
        }
        //weather not null
        else if (data.getTimeTrigger() == null && data.getLocationTrigger() == null && data.getWeatherTrigger() != null){
            return 3;
        }

        return 0;
    }

    public void addMessage(MessageWrapper data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        //add content
        content.put(KEY_FROM, data.getMessage().get_from());
        content.put(KEY_PASSWORD, data.getMessage().get_password());
        content.put(KEY_TO, data.getMessage().get_to());
        content.put(KEY_SUBJECT, data.getMessage().get_subject());
        content.put(KEY_CONTENT, data.getMessage().get_content());
        content.put(KEY_CREATED_TIME, data.getCreatedTime());

        //add time
        content.put(KEY_DATE, data.getTimeTrigger().getDate());
        content.put(KEY_TIME, data.getTimeTrigger().getTime());

        /*
        switch(checkTriggerType(data)) {
            // condition 1 is time, 2 is location, 3 is weather
            case 1:
                //content.put(KEY_DATE, data.getTime().getDate());
                //content.put(KEY_TIME, data.getTime().getTime());
                //content.put(KEY_TYPE, data.getTime().getType());
                //content.put(KEY_STATUS, data.getTime().getStatus());
                // condition is location
            case 2:
                //content.put(KEY_LOCATION, data.getLocation());
            case 3:
                //content.put(KEY_WEATHER, data.getWeather());
            default:
                break;
        }
        */

        // Inserting Row
        db.insert(TABLE_NAME, null, content);
        db.close(); // Closing database connection
    }

    public MessageWrapper getMessageDataByID(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                KEY_ID,//0
                KEY_FROM,//1
                KEY_PASSWORD,//2
                KEY_TO, //3
                KEY_SUBJECT,//4
                KEY_CONTENT,//5
                KEY_CREATED_TIME,//6
                KEY_DATE,//7
                KEY_TIME,//8
                KEY_LOCATION, //9
                KEY_WEATHER//10
        };
        String selection = KEY_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(TABLE_NAME,
                projection,
                selection,
                selectionArgs, null, null, null, null);

        if (cursor == null ) {
            return null;
        }
        else if(!cursor.moveToFirst()){
            return null;
        }
        else{
            Message message = new Message(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            Time time = new Time(cursor.getString(7), cursor.getString(8));
            MessageWrapper messageWrapper = new MessageWrapper(message, id);
            messageWrapper.setCreatedTime(cursor.getString(6));
            messageWrapper.setTimeTrigger(time);
            //if(!cursor.isNull(7) && !cursor.isNull(8)){
                //Time time = new Time(cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10));
                //set the current time to when the MessageObject first time created, not when it's queried from database
                //messageWrapper.setTime(time);
            //}
            /*
            else if(!cursor.isNull(11)){

            }
            else{
            }
            */
            cursor.close();

            return messageWrapper;
        }

    }

    public ArrayList<MessageWrapper> getAllEmails() {
        ArrayList<MessageWrapper> emailList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_TO + " LIKE '%@%'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                Time time = new Time(cursor.getString(7), cursor.getString(8));

                MessageWrapper messageData = new MessageWrapper(message, Integer.parseInt(cursor.getString(0)));
                messageData.setTimeTrigger(time);

                emailList.add(messageData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return message list
        return emailList;
    }

    public ArrayList<MessageWrapper> getAllSMS() {
        ArrayList<MessageWrapper> smsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_TO + " NOT LIKE '%@%'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                Time time = new Time(cursor.getString(7), cursor.getString(8));

                MessageWrapper messageWrapper = new MessageWrapper(message, Integer.parseInt(cursor.getString(0)));
                messageWrapper.setTimeTrigger(time);

                smsList.add(messageWrapper);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return message list
        return smsList;
    }

    // Getting All Messages
    public ArrayList<MessageWrapper> getAllMessages() {
        ArrayList<MessageWrapper> messageList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                //Time time = new Time(cursor.getString(6), cursor.getString(7), cursor.getInt(8), cursor.getInt(9));
                Time time = new Time(cursor.getString(7), cursor.getString(8));

                MessageWrapper messageWrapper = new MessageWrapper(message, Integer.parseInt(cursor.getString(0)));
                messageWrapper.setTimeTrigger(time);
                //use setter to set time instead of using constructor
                //messageData.setTime(time);
                // Adding message to list
                messageList.add(messageWrapper);
            } while (cursor.moveToNext());
        }
        cursor.close();
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

    public int updateMessage(MessageWrapper data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //add content
        values.put(KEY_FROM, data.getMessage().get_from());
        values.put(KEY_PASSWORD, data.getMessage().get_password());
        values.put(KEY_TO, data.getMessage().get_to());
        values.put(KEY_SUBJECT, data.getMessage().get_subject());
        values.put(KEY_CONTENT, data.getMessage().get_content());
        values.put(KEY_CREATED_TIME, data.getCreatedTime());

        //time
        values.put(KEY_DATE, data.getTimeTrigger().getDate());
        values.put(KEY_TIME, data.getTimeTrigger().getTime());

        //add time
        /*
        values.put(KEY_DATE, data.getTime().getDate());
        values.put(KEY_TIME, data.getTime().getTime());
        values.put(KEY_TYPE, data.getTime().getType());
        values.put(KEY_STATUS, data.getTime().getStatus());
        values.put(KEY_LOCATION, "null");
        values.put(KEY_WEATHER, "null");
        */

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
        Log.d("YASSSSS", "Reading all messages...");
        //email
        Message msg1 = new Message("yuezhang5@cmail.carleton.ca", "dummypass", "jamesmulvenna@cmail.carleton.ca", "First Dummy", "This is the first dummy Email");
        //Time time1 = new Time(null, null, 1, 2);
        MessageWrapper data1 = new MessageWrapper(msg1);

        //sms
        Message msg2 = new Message("61325093232","This is the first dummy SMS");
        //Time time2 = new Time(null, null, 3, 4);
        MessageWrapper data2 = new MessageWrapper(msg2);

        Message msg3 = new Message("yuezhang5@cmail.carleton.ca", "123", "jamesmulvenna@cmail.carleton.ca", "Second Dummy", "This is the second dummy Email");
        //Time time3 = new Time(null, null, 1, 2);
        MessageWrapper data3 = new MessageWrapper(msg3);

        //sms
        Message msg4 = new Message("6134347584","This is the second dummy SMS");
        //Time time4 = new Time(null, null, 3, 4);
        MessageWrapper data4 = new MessageWrapper(msg4);

        //sms
        Message msg5 = new Message("6135458687","This is the third dummy SMS");
        //Time time5 = new Time(null, null, 3, 4);
        MessageWrapper data5 = new MessageWrapper(msg5);

        Message msg6 = new Message("6136235968","This is the forth dummy SMS");
        //Time time6 = new Time(null, null, 3, 4);
        MessageWrapper data6 = new MessageWrapper(msg6);

        this.addMessage(data1);
        this.addMessage(data2);
        this.addMessage(data3);
        this.addMessage(data4);
        this.addMessage(data5);
        this.addMessage(data6);

        // Reading all messages
        Log.d("Messages: ", "Reading all messages...");
        List<MessageWrapper> messages = this.getAllMessages();

        for (MessageWrapper data : messages) {
            String log = "Id: " + data.getId() + " ,From: " + data.getMessage().get_from() + ", password: " + data.getMessage().get_password()
                    + ", to: " + data.getMessage().get_to() + ", subject: " + data.getMessage().get_subject() + ", content: " + data.getMessage().get_content() +
                    ", currtime: " + data.getCreatedTime();
            //", date: " + data.getTimeTrigger().getDate() + ", time: " + data.getTime().getTime() + ", type: " + data.getTime().getType() +
              //      ", status: " + data.getTime().getStatus() + ", location: " + data.getLocation() + ", weather: " + data.getWeather();
            Log.d("Message : :", log);
        }

    }
}


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
                content.put("message", message.getMessageDataByID());
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
                content.put("message", message.getMessageDataByID());
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
                content.put("message", message.getMessageDataByID());
                content.put("currtime", data.getCurrentTime());

                //add weather
                //content.put("weather", null);
            default:

        }
*/

