package com.BasicLogin.util;

import com.sun.mail.smtp.SMTPTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.security.Security;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

@Service
public class CommonUtil {
    private static String password;
    private static String host;
    private static String protocol;

    @Autowired
    public CommonUtil(@Value("${email.password}") String property, @Value("${mail.smtps.host}") String hostName, @Value("${email.protocol}") String protocolType) {
        password = property;
        host = hostName;
        protocol = protocolType;
    }

    public static void sendEmail(String to, String userMessage, String subject, String from) throws UnknownHostException, MessagingException {
        // Get system properties
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        // Get a Properties object
        Properties props = System.getProperties();
        // Get the default Session object.
        Session session = Session.getDefaultInstance(props);
        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);
        // Set From: header field of the header.
        message.setFrom(new InternetAddress(from));
        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // Set Subject: header field
        message.setSubject(subject);
        // Now set the actual message
        message.setText(userMessage);
        SMTPTransport t = (SMTPTransport) session.getTransport(protocol);
        t.connect(host, from, password);
        t.sendMessage(message, message.getAllRecipients());
        t.close();
        // Send message
    }

    public static String genearteOTP(int len) {
        // Using numeric values
        String numbers = "0123456789";
        // Using random method
        Random randomMethod = new Random();
        char[] otp = new char[len];

        for (int i = 0; i < len; i++) {
            otp[i] = numbers.charAt(randomMethod.nextInt(numbers.length()));
        }
        return new String(otp);
    }

    public static String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

}