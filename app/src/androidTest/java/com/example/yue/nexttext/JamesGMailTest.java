package com.example.yue.nexttext;

/**
 * Created by jamesmulvenna on 2017-11-01.
 */

import org.junit.Test;


public class JamesGMailTest {

    @Test
    public void testGMail() {
        EmailSettings emailSettings = new EmailSettings("jamespmulvenna@gmail.com", "asklzmV!");
        emailSettings.sendEmail();
    }
}