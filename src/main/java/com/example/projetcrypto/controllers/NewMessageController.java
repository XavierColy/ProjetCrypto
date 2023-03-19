package com.example.projetcrypto.controllers;

import com.example.projetcrypto.utils.HandleEmail;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.example.projetcrypto.utils.SendEmail;

import java.util.Scanner;

/**
 * Creates a new window to send a message */
public class NewMessageController {
    //region attributes
    public Button sendButton;
    public Button cancelButton;
    public Button SaveDraftButton;
    public TextField recipientTextField;
    public TextField bccTextField;
    public TextField subjectField;
    public Button joinAttachmentsButton;
    public TextField attachmentsField;
    public TextArea messageField;
    //endregion

    //region methods
    public void sendMail(){

        String recipient = recipientTextField.getText();
        String subject = subjectField.getText();
        String message = messageField.getText();
        String attachmentPath = attachmentsField.getText();
        String user = "cryptotest10@outlook.com";
        String password = "Azerty@2023";

        HandleEmail.sendMail(recipient, subject, message) ;
/*
        if (!attachmentPath.isEmpty()) {
            SendEmail.sendmessagewithattachement(user, password, recipient, attachmentPath);
        }*/
    }
}


