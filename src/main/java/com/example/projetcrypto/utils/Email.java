package com.example.projetcrypto.utils;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.PasswordAuthentication;
import javax.mail.Store;

public class Email {
	
	public static void listMessageIDs(String user, String password) {
	    Properties properties = new Properties();
	    properties.put("mail.store.protocol", "imaps");
	    properties.put("mail.imaps.host", "imap.gmail.com");
	    properties.put("mail.imaps.host", "imap.outlook.com");
	    properties.put("mail.imaps.port", "993");
	    properties.put("mail.imaps.starttls.enable", "true");

	    try {
	        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(user, password);
	            }
	        });
	        Store store = session.getStore("imaps");
	        store.connect();
	        Folder inbox = store.getFolder("INBOX");
	        inbox.open(Folder.READ_ONLY);

	        Message[] messages = inbox.getMessages();
	        System.out.println("Total messages: " + messages.length);
	        for (int i = 0; i < messages.length; i++) {
	            String messageId = messages[i].getHeader("Message-ID")[0];
	            String subject = messages[i].getSubject();
	            System.out.println("Message " + (i+1) + " ID: " + messageId);
	            System.out.println("Message " + (i+1) + " Subject: " + subject);
	        }

	        inbox.close(false);
	        store.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	}


