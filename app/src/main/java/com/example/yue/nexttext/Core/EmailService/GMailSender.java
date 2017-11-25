package com.example.yue.nexttext.Core.EmailService;

/**
 * Created by jamesmulvenna on 2017-11-01.
 */

import android.util.Log;

import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMailSender extends javax.mail.Authenticator {
    static {
        Security.addProvider(new JSSEProvider());
    }

    private String user;
    private String password;
    private Session session;

    public String getUser(){ return user; }
    public String getPassword(){ return password; }

    public void setUser(String newUser){
        user = newUser;
    }

    public void setPassword(String newPass){
        password = newPass;
    }

    public GMailSender(String user, String password) {
        setUser(user);
        setPassword(password);

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        String mailhost = "smtp.gmail.com";
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(getUser(), getPassword());
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {
        /*
        Recipient email must have this link set to on
        https://myaccount.google.com/lesssecureapps?pli=1
         */

        MimeMessage message = new MimeMessage(session);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
        message.setFrom(new InternetAddress(sender));
        message.setSubject(subject);
        message.setDataHandler(handler);

        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));

        Transport transport = session.getTransport();
        //Connect to Host (Mail Server)
        transport.connect(getUser(), getPassword());
        //Send Email
        transport.sendMessage(message, message.getAllRecipients());
        //Close Connection
        transport.close();
    }
}
