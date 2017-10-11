package com.example.yue.nexttext.Database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.*;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

import com.example.yue.nexttext.Data.Email;
import com.example.yue.nexttext.Data.Sms;

/**
 * Created by jamesmulvenna on 2017-09-29.
 */

public class SmsManager extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "smsManager";

    private static final String TABLE_SMS = "emails";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_MESSAGE = "message";

    public SmsManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SMS_TABLE = "CREATE TABLE " + TABLE_SMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT," + KEY_MESSAGE + " TEXT," + ")";
        db.execSQL(CREATE_SMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);

        // Create tables again
        onCreate(db);
    }

    // Adding new email
    public void addSms(Sms sms) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, sms.getName());
        values.put(KEY_PHONE, sms.getPhone());
        values.put(KEY_MESSAGE, sms.getMessage());


        // Inserting Row
        db.insert(TABLE_SMS, null, values);
        db.close(); // Closing database connection
    }

    public Sms getSms(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SMS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PHONE, KEY_MESSAGE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Sms sms = new Sms(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

        return sms;
    }

    public List<Sms> getAllSms() {
        List<Sms> smsList = new ArrayList<Sms>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sms sms = new Sms();
                sms.setID(Integer.parseInt(cursor.getString(0)));
                sms.setName(cursor.getString(1));
                sms.setPhone(cursor.getString(2));
                sms.setMessage(cursor.getString(3));
                // Adding contact to list
                smsList.add(sms);
            } while (cursor.moveToNext());
        }

        // return contact list
        return smsList;
    }

    public int getSmsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateSms(Sms sms) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, sms.getName());
        values.put(KEY_PHONE, sms.getPhone());
        values.put(KEY_MESSAGE, sms.getMessage());

        // updating row
        return db.update(TABLE_SMS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sms.getID()) });
    }

    public void deleteSms(Sms sms) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SMS, KEY_ID + " = ?",
                new String[] { String.valueOf(sms.getID()) });
        db.close();
    }
}
