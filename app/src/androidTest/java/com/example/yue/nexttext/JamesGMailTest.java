package com.example.yue.nexttext;

/**
 * Created by jamesmulvenna on 2017-11-01.
 */
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.example.yue.nexttext.DataType.Message;
import com.example.yue.nexttext.DataType.MessageWrapper;
import com.example.yue.nexttext.EmailService.GMailSender;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class JamesGMailTest {

    @Test
    public void testGMail() {
        Context context = InstrumentationRegistry.getTargetContext();
        Message message = new Message("EMAIL FROM HERE", "PASSWORD HERE", "EMAIL TO HERE", "test subject", "test content");
        MessageWrapper messageWrapper = new MessageWrapper(message);

        GMailSender gMailSender = new GMailSender(message.get_from(), message.get_password());

        try {
            gMailSender.sendMail(message.get_subject(), message.get_content(), message.get_from(), message.get_to());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}