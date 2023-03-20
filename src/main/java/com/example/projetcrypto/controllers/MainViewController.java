package com.example.projetcrypto.controllers;

import com.example.projetcrypto.bo.EmailModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.sql.SQLException;

import static com.example.projetcrypto.utils.Config.getEmailSession;
import static com.example.projetcrypto.utils.HandleEmail.getInboxEmails;
import static com.example.projetcrypto.utils.HandleEmail.getSentEmails;

public class MainViewController extends TransitionController {
    @FXML
    public Button newMessageButton;
    public Button answerButton;
    public Button forwardButton;
    public ListView<EmailModel> mailList;
    public Button receptionButton;
    public Button sentMessagesButton;
    public Button draftMessagesButton;
    public Button spamMessagesButton;
    public Button trashButton;
    @FXML
    private Label mailContentLabel;


    @FXML
    private void init() {
        mailList = new ListView<>(getInboxEmails());
    }

    @FXML
    public void handleNewMessageButton(ActionEvent event) throws IOException {
        setPrevStage((Stage) newMessageButton.getScene().getWindow());
        displayNextWindow("newMessageWindow.fxml", false);
    }


    @FXML
    public void displayInbox() {
        if (mailList == null) {
            mailList = new ListView<EmailModel>();
        }
        mailList.setItems(getInboxEmails() );

    }
    @FXML
    public void showSentMails() {
        if (mailList == null) {
            mailList = new ListView<EmailModel>();
        }
        mailList.setItems(getSentEmails());

    }





   /* public void forward(){
        forwardMail(String messageID, String forwardTo);
    }
    public void delete(){
        deleteMail(String messageID);
    }
    public void replyto(){
        reply(String messageID,String text);
    }*/
}