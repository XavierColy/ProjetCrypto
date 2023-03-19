package com.example.projetcrypto.controllers;

import com.example.projetcrypto.utils.HandleEmail;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.stage.FileChooser;


import java.io.File;
import java.util.List;


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

    public void addAttachments() {
        FileChooser fileChooser = new FileChooser();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(joinAttachmentsButton.getScene().getWindow());
        if (selectedFiles != null) {
            String[] attachmentPaths = new String[selectedFiles.size()];
            for (int i = 0; i < selectedFiles.size(); i++) {
                attachmentPaths[i] = selectedFiles.get(i).getAbsolutePath();
            }
            attachmentsField.setText(String.join(";", attachmentPaths));
        }
    }
    public void sendMail() {
        String recipient = recipientTextField.getText();
        String subject = subjectField.getText();
        String message = messageField.getText();
        String cc = bccTextField.getText();
        String attachmentPath = attachmentsField.getText();

        String[] attachmentPaths = attachmentPath.split(";");
        if (attachmentPaths.length == 1 && attachmentPaths[0].isEmpty()) {
            HandleEmail.sendMail(recipient, subject, message, cc);
        } else {
            HandleEmail.sendMail(recipient, subject, message, attachmentPaths, cc);
        }

        Stage stage = (Stage) sendButton.getScene().getWindow();
        stage.close(); // Close the window
    }


    public void cancel() {
        cancelButton.getScene().getWindow().hide();
    }



    public void initialize() {
        cancelButton.setOnAction(event -> cancel());
        joinAttachmentsButton.setOnAction(event -> addAttachments());
        sendButton.setOnAction(event -> sendMail());
    }


}


