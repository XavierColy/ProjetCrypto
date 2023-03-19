package com.example.projetcrypto.controllers;

import com.example.projetcrypto.utils.HandleEmail;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.stage.FileChooser;


import java.io.File;


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



        HandleEmail.sendMail(recipient, subject, message) ;
        Stage stage = (Stage) sendButton.getScene().getWindow();
        stage.close(); // Close the window

    }

    public void cancel() {
        cancelButton.getScene().getWindow().hide();
    }
    public void addAttachments() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(joinAttachmentsButton.getScene().getWindow());
        if (selectedFile != null) {
            attachmentsField.setText(selectedFile.getAbsolutePath());
        }
    }

    public void sendEMail(){
        String recipient = recipientTextField.getText();
        String subject = subjectField.getText();
        String message = messageField.getText();
        String attachmentPath = attachmentsField.getText();

        HandleEmail.sendMail(recipient, subject, message, new String[]{attachmentPath});
        Stage stage = (Stage) sendButton.getScene().getWindow();
        stage.close();
    }


    public void initialize() {
        cancelButton.setOnAction(event -> cancel());
        joinAttachmentsButton.setOnAction(event -> addAttachments());
        sendButton.setOnAction(event -> sendEMail());
    }


}


