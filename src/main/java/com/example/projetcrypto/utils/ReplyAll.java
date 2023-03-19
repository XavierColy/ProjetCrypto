package com.example.projetcrypto.utils;

import java.util.Properties;
import java.util.Scanner;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class ReplyAll {
    public static void replyToMessage(String user, String password,String replyMessage) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp-mail.outlook.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(user, password);
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
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Do you want to reply to only the sender or everyone? (Enter 1 for Sender, 2 for Everyone)");
                    int replyType = scanner.nextInt();
                    
                    MimeMessage reply = (MimeMessage) message.reply(replyType == 1 ? false : true);
                    reply.setText(replyMessage);
                    reply.setSubject("RE: " + message.getSubject());
                    reply.setFrom(new InternetAddress(user));
                    Transport.send(reply);
                    break;
                }
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}