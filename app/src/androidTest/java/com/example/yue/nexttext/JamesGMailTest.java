package com.example.yue.nexttext;

/**
 * Created by jamesmulvenna on 2017-11-01.
 */

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.yue.nexttext.Core.EmailService.GMailSender;
import com.example.yue.nexttext.DataType.Message;
import com.example.yue.nexttext.DataType.MessageWrapper;
import com.example.yue.nexttext.UI.EmailSettings;

import org.junit.Assert;
import org.junit.Test;


public class JamesGMailTest {

    /*@Test
    public void testGMail() {
        Context context = InstrumentationRegistry.getTargetContext();
        Message message = new Message("jamespmulvenna@gmail.com", "admld", "jamespmulvenna@gmail.com", "test subject", "test content");
        MessageWrapper messageWrapper = new MessageWrapper(message);

        GMailSender gMailSender = new GMailSender(message.get_from(), message.get_password());

        try {
            gMailSender.sendMail(message.get_subject(), message.get_content(), message.get_from(), message.get_to());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
    @Test
    public void testGmailAuthenticate(){
        EmailSettings newEmail = new EmailSettings("jamespmulvenna@gmail.com", "asklzmV!");

        int actualCode;
        newEmail.sendConfirmationEmail();
        //boolean result = newEmail.validatedEmail(8734, actualCode);

    }

}