package com.example.yue.nexttext;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.yue.nexttext.Data.Message;
import com.example.yue.nexttext.Data.MessageData;
import com.example.yue.nexttext.Data.Time;
import com.example.yue.nexttext.Database.MessageManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by yue on 2017-10-23.
 */

@RunWith(AndroidJUnit4.class)
public class MessageMangerTest {

    @Test
    public void insertTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        MessageManager dataBase = new MessageManager(appContext);

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

        dataBase.addMessage(data1);
        dataBase.addMessage(data2);
        dataBase.addMessage(data3);
        dataBase.addMessage(data4);
        dataBase.addMessage(data5);
        dataBase.addMessage(data6);

        assertEquals(data1, dataBase.getMessage(1));
        assertEquals(data2, dataBase.getMessage(2));
        assertEquals(data3, dataBase.getMessage(3));
        assertEquals(data4, dataBase.getMessage(4));
        assertEquals(data5, dataBase.getMessage(5));
        assertEquals(data6, dataBase.getMessage(6));
    }

    @Test
    public void deleteTest() throws Exception{
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        MessageManager dataBase = new MessageManager(appContext);

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

        dataBase.addMessage(data1);
        dataBase.addMessage(data2);
        dataBase.addMessage(data3);
        dataBase.addMessage(data4);
        dataBase.addMessage(data5);
        dataBase.addMessage(data6);

        dataBase.deleteMessageById(1);
        assertEquals(null, dataBase.getMessage(1));
        dataBase.deleteMessageById(6);
        assertEquals(null, dataBase.getMessage(6));
    }
}
