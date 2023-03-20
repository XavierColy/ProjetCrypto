package com.example.projetcrypto.bo;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;

public class EmailModel {
    private final String id;
    private final String sender;
    private final String subject;
    private final String text;
    private final String folderName;

    public EmailModel(Message message) throws MessagingException, IOException {
        this.id = message.getHeader("Message-ID")[0];
        this.subject = message.getSubject();
        this.sender = message.getFrom()[0].toString();
        this.text = message.getContent().toString();
        this.folderName = message.getFolder().getFullName();
    }

    public String getId() {
        return id;
    }

    public String getFolderName() {
        return folderName;
    }

    @Override
    public String toString() {
        return this.sender + "\n" +
                this.subject + " - " + this.text;
    }
}
