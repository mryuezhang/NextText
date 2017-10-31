package com.example.yue.nexttext.Utility;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jamesmulvenna on 2017-09-28.
 */

public class PhoneFilter {
    private Collection<String> contacts;
    private ContentResolver contentResolver;

    public PhoneFilter(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.contacts = getContacts();
    }

    private Collection<String> getContacts() {
        ArrayList<String> currPhone = new ArrayList<String>();

        ContentResolver currCR = contentResolver;
        Cursor currCursor = currCR.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (currCursor != null && currCursor.getCount() > 0) {
            while (currCursor.moveToNext()) {
                String id = currCursor.getString(currCursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = currCursor.getString(currCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(currCursor.getString(currCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor newCursor = currCR.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    if (newCursor != null) {
                        while (newCursor.moveToNext()) {
                            String phoneNumber = newCursor.getString(newCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            currPhone.add(name + " " + phoneNumber);
                        }
                        newCursor.close();
                    }
                }
            }
            currCursor.close();
        }
        return currPhone;
    }

    public Collection<String> filterContacts(String compare) {
        ArrayList<String> currCompareList = new ArrayList<String>();
        char[] compareArray = compare.toLowerCase().toCharArray();

        for (String contact : contacts) {
            char[] contactArray = contact.toLowerCase().toCharArray();
            boolean match = true;

            for (int i = 0; i < compareArray.length; i++) {
                if (!Character.toString(compareArray[i]).equals(Character.toString(contactArray[i]))) {
                    match = false;
                    break;
                }
            }

            if (match) currCompareList.add(contact);
        }

        return currCompareList;
    }
}

