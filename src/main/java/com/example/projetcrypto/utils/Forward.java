package com.example.projetcrypto.utils;



import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Forward {

    public static void forwardMessage(String user, String password, String forwardTo) throws IOException{
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp-mail.outlook.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user,password);
            }
        });

        try {
            Store store = session.getStore("imaps");
            store.connect("imap.outlook.com", user, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Scanner myID = new Scanner(System.in);
	        System.out.print("Enter the message ID: ");
	        String messageID = myID.nextLine();
            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                if (message.getHeader("Message-ID")[0].equals(messageID)) {
                    MimeMessage forward = new MimeMessage(session);
                    forward.setFrom(new InternetAddress(user));
                    forward.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(forwardTo));
                    forward.setSubject("FWD: " + message.getSubject());
                    forward.setSentDate(message.getSentDate());
                    forward.setHeader("Content-Type", message.getContentType());

                    forward.setContent(message.getContent(), message.getContentType());

                    Transport.send(forward);
                    break;
                }
            }

            inbox.close(false);
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
