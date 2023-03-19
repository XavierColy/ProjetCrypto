package com.example.projetcrypto.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Arrays;

import static com.example.projetcrypto.utils.Config.getEmailSession;

/**
 * @author Kaoutar*/
public class HandleEmail {

    //region send
    /** Send Mail without attachments*/
    public static void sendMail(String destination, String subject, String text){
        try {
            MimeMessage message = new MimeMessage(getEmailSession());
            Store store = getEmailSession().getStore();
            store.connect();
            var user= store.getFolder("INBOX").getFullName();
            message.setFrom(user);
            message.setText(text);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
            message.setSubject(subject);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    /**
     * Send mail with attachments*/
    public static void sendMail(String destination, String subject, String text, String[] attachmentPaths){
        try {
            MimeMessage message = new MimeMessage(getEmailSession());
            Store store = getEmailSession().getStore();
            store.connect();
            var user= store.getFolder("INBOX").getFullName();
            message.setFrom(user);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
            message.setSubject(subject);

            Multipart emailContent = new MimeMultipart();
            MimeBodyPart bodypart = new MimeBodyPart();
            bodypart.setText(text);


            MimeBodyPart attachementfile = new MimeBodyPart();
            Arrays.stream(attachmentPaths).forEach(path-> {
                try {
                    attachementfile.attachFile(path);
                } catch (IOException | MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
            emailContent.addBodyPart(bodypart);
            emailContent.addBodyPart(attachementfile);
            message.setContent(emailContent);
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
    //endregion


}
