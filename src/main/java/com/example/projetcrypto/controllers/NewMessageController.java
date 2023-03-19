package com.example.projetcrypto.controllers;

import com.example.projetcrypto.utils.HandleEmail;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//import com.example.projetcrypto.utils.SendEmail;

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
        String[] tabAttachments = attachmentPath.split(";");


        if (tabAttachments.length != 0) {
            HandleEmail.sendMail(recipient, subject, message, tabAttachments) ;
        }else{
            HandleEmail.sendMail(recipient, subject, message) ;
        }
            Stage stage = (Stage) sendButton.getScene().getWindow();
            stage.close(); // Close the window
    }

    public void cancel() {
        cancelButton.getScene().getWindow().hide();
    }


    public void initialize() {
        cancelButton.setOnAction(event -> cancel());
    }


}


