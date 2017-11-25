package com.example.yue.nexttext;

/**
 * Created by jamesmulvenna on 2017-11-01.
 */

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.example.yue.nexttext.Core.EmailService.EmailSettings;
import com.example.yue.nexttext.Core.EmailService.GMailSender;
import com.example.yue.nexttext.DataType.Message;
import com.example.yue.nexttext.DataType.MessageWrapper;

import org.junit.Test;


public class JamesGMailTest {

    @Test
    public void testGMail() {
        EmailSettings emailSettings = new EmailSettings("jamespmulvenna@gmail.com", "asklzmV!");
        emailSettings.sendEmail();
    }
}