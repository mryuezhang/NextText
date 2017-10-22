package com.example.yue.nexttext.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yue.nexttext.Data.Email;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesmulvenna on 2017-09-29.
 */

public class EmailManager extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "emailManager";

    private static final String TABLE_EMAILS = "emails";

    private static final String KEY_ID = "id";
    private static final String KEY_FROM = "from";
    private static final String KEY_TO = "to";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_MESSAGE = "message";

    public EmailManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EMAILS_TABLE = "CREATE TABLE " + TABLE_EMAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FROM + " TEXT," + KEY_TO + " TEXT,"
                + KEY_SUBJECT + " TEXT," + KEY_MESSAGE + " TEXT," + ")";
        db.execSQL(CREATE_EMAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMAILS);

        // Create tables again
        onCreate(db);
    }

    // Adding new email
    public void addEmail(Email email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FROM, email.getFrom());
        values.put(KEY_TO, email.getTo());
        values.put(KEY_SUBJECT, email.getSubject());
        values.put(KEY_MESSAGE, email.getMessage());


        // Inserting Row
        db.insert(TABLE_EMAILS, null, values);
        db.close(); // Closing database connection
    }

    public Email getEmail(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EMAILS, new String[] { KEY_ID,
                        KEY_TO, KEY_SUBJECT, KEY_MESSAGE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Email email = new Email(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(3));

        return email;
    }

    public List<Email> getAllEmails() {
        List<Email> emailList = new ArrayList<Email>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EMAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Email email = new Email();
                email.setId(Integer.parseInt(cursor.getString(0)));
                email.setTo(cursor.getString(1));
                email.setSubject(cursor.getString(2));
                email.setMessage(cursor.getString(3));
                // Adding contact to list
                emailList.add(email);
            } while (cursor.moveToNext());
        }

        // return contact list
        return emailList;
    }

    public int getEmailsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EMAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateEmail(Email email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FROM, email.getFrom());
        values.put(KEY_TO, email.getTo());
        values.put(KEY_SUBJECT, email.getSubject());
        values.put(KEY_MESSAGE, email.getMessage());

        // updating row
        return db.update(TABLE_EMAILS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(email.getId()) });
    }

    public void deleteEmail(Email email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMAILS, KEY_ID + " = ?",
                new String[] { String.valueOf(email.getId()) });
        db.close();
    }
}
