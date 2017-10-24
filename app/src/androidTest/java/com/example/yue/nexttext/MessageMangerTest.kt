package com.example.yue.nexttext

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.yue.nexttext.Data.Message
import com.example.yue.nexttext.Data.MessageData
import com.example.yue.nexttext.Data.Time
import com.example.yue.nexttext.Database.MessageManager
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by yue on 2017-10-23.
 */

@RunWith(AndroidJUnit4::class)
class MessageMangerTest {

    /*
    @Test
    @Throws(Exception::class)
    fun insertTest() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        val dataBase = MessageManager(appContext)

        //email
        val msg1 = Message("a1", "dummypass", "1", "somesubject", "testmessageemail")
        val time1 = Time(null, null, 1, 2)
        val data1 = MessageData(msg1, time1)

        //sms
        val msg2 = Message("b2", null, "2", null, "testmessagesms")
        val time2 = Time(null, null, 3, 4)
        val data2 = MessageData(msg2, time2)

        val msg3 = Message("c3", null, "3", "somesubject", "testmessageemail")
        val time3 = Time(null, null, 1, 2)
        val data3 = MessageData(msg3, time3)

        //sms
        val msg4 = Message("d4", null, "4", null, "testmessagesms")
        val time4 = Time(null, null, 3, 4)
        val data4 = MessageData(msg4, time4)

        //sms
        val msg5 = Message("e5", null, "5", null, "testmessagesms")
        val time5 = Time(null, null, 3, 4)
        val data5 = MessageData(msg5, time5)

        val msg6 = Message("f6", null, "6", null, "testmessagesms")
        val time6 = Time(null, null, 3, 4)
        val data6 = MessageData(msg6, time6)

        dataBase.addMessage(data1)
        dataBase.addMessage(data2)
        dataBase.addMessage(data3)
        dataBase.addMessage(data4)
        dataBase.addMessage(data5)
        dataBase.addMessage(data6)

        //assertEquals(6, dataBase.getMessagesCount())

        assertEquals(data1.message.to, dataBase.getMessageDataByID(1)!!.message.to)
        assertEquals(data1.message.from, dataBase.getMessageDataByID(1)!!.message.from)
        assertEquals(data1.message.message, dataBase.getMessageDataByID(1)!!.message.message)
        //assertEquals(data2, dataBase.getMessageDataByID(2))
        //assertEquals(data3, dataBase.getMessageDataByID(3))
        //assertEquals(data4, dataBase.getMessageDataByID(4))
        //assertEquals(data5, dataBase.getMessageDataByID(5))
        //assertEquals(data6, dataBase.getMessageDataByID(6))
    }
    */


    @Test
    @Throws(Exception::class)
    fun deleteTest() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        val dataBase = MessageManager(appContext)

        //email
        val msg1 = Message("a1", "dummypass", "1", "somesubject", "testmessageemail")
        val time1 = Time(null, null, 1, 2)
        val data1 = MessageData(msg1, time1)

        //sms
        val msg2 = Message("b2", null, "2", null, "testmessagesms")
        val time2 = Time(null, null, 3, 4)
        val data2 = MessageData(msg2, time2)

        val msg3 = Message("c3", null, "3", "somesubject", "testmessageemail")
        val time3 = Time(null, null, 1, 2)
        val data3 = MessageData(msg3, time3)

        //sms
        val msg4 = Message("d4", null, "4", null, "testmessagesms")
        val time4 = Time(null, null, 3, 4)
        val data4 = MessageData(msg4, time4)

        //sms
        val msg5 = Message("e5", null, "5", null, "testmessagesms")
        val time5 = Time(null, null, 3, 4)
        val data5 = MessageData(msg5, time5)

        val msg6 = Message("f6", null, "6", null, "testmessagesms")
        val time6 = Time(null, null, 3, 4)
        val data6 = MessageData(msg6, time6)

        dataBase.addMessage(data1)
        dataBase.addMessage(data2)
        dataBase.addMessage(data3)
        dataBase.addMessage(data4)
        dataBase.addMessage(data5)
        dataBase.addMessage(data6)

        assertEquals(6, dataBase.messagesCount)

        //dataBase.deleteAllMessages()
        //assertEquals(null, dataBase.getMessageDataByID(1))

        dataBase.deleteMessageById(1)
        assertEquals(null, dataBase.getMessageDataByID(1))
        dataBase.deleteAllMessages()

    }

}
