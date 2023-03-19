package com.example.projetcrypto.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.MessageIDTerm;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import static com.example.projetcrypto.utils.Config.getEmailSession;
import static com.example.projetcrypto.utils.Config.getSessionOwner;

/**
 * @author Kaoutar
 */
public class HandleEmail {

    //region send

    /**
     * Send Mail without attachments
     */
    public static void sendMail(String destination, String subject, String text, String cc) {
        try {
            MimeMessage message = new MimeMessage(getEmailSession());
            message.setFrom(getSessionOwner());
            message.setText(text);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
            if (cc != null && !cc.isEmpty()) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
            }
            message.setSubject(subject);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    /**
     * Send mail with attachments
     */
    public static void sendMail(String destination, String subject, String text, String[] attachmentPaths, String cc) {
        try {
            MimeMessage message = new MimeMessage(getEmailSession());
            message.setFrom(getSessionOwner());
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));

            // Add CC recipient if present
            if (cc != null && !cc.isEmpty()) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
            }

            message.setSubject(subject);

            Multipart emailContent = new MimeMultipart();
            MimeBodyPart bodypart = new MimeBodyPart();
            bodypart.setText(text);

            emailContent.addBodyPart(bodypart);

            // Add attachments
            for (String attachmentPath : attachmentPaths) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(new File(attachmentPath));
                emailContent.addBodyPart(attachmentPart);
            }

            message.setContent(emailContent);
            Transport.send(message);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    //endregion

    public static ObservableList<Message> getInboxEmails() {
        try{
            Store store = getEmailSession().getStore("imaps");
            store.connect();
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            ObservableList<Message> obsList = FXCollections.observableList(Arrays.stream(messages).toList());

            inbox.close(false);
            store.close();
            return obsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteMail(String messageID) {
        try {
            Store store = getEmailSession().getStore("imaps");
            store.connect();

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            Message[] messages = folder.search(new MessageIDTerm(messageID));
            if (messages.length == 0) {
                System.out.println("No messages found with the specified ID.");
                return;
            }

            for (Message message : messages) {
                message.setFlag(Flags.Flag.DELETED, true);
                System.out.println("Message deleted successfully.");
            }

            folder.close(true);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void forwardMail(String messageID, String forwardTo) {
        try {
            Store store = getEmailSession().getStore("imaps");
            store.connect();

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                if (message.getHeader("Message-ID")[0].equals(messageID)) {
                    MimeMessage forward = new MimeMessage(getEmailSession());
                    forward.setFrom(new InternetAddress(getSessionOwner()));
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reply(String messageID,String text){
        try {
            Store store = getEmailSession().getStore("imaps");
            store.connect();

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                if (message.getHeader("Message-ID")[0].equals(messageID)) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Do you want to reply to only the sender or everyone? (Enter 1 for Sender, 2 for Everyone)");
                    int replyType = scanner.nextInt();

                    MimeMessage reply = (MimeMessage) message.reply(replyType != 1);
                    reply.setText(text);
                    reply.setSubject("RE: " + message.getSubject());
                    reply.setFrom(new InternetAddress(inbox.getFullName()));
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
