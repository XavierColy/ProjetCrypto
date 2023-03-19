package com.example.projetcrypto.controllers;

import com.example.projetcrypto.TransitionController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javax.mail.Message;
import java.io.IOException;

import static com.example.projetcrypto.utils.HandleEmail.getInboxEmails;

public class MainViewController extends TransitionController {
    @FXML
    public Button newMessageButton;
    public Button answerButton;
    public Button forwardButton;
    public ListView<Message> mailList;
    public Button receptionButton;
    public Button sentMessagesButton;
    public Button draftMessagesButton;
    public Button spamMessagesButton;
    public Button trashButton;


    @FXML
    private void init() {
        mailList = new ListView<>(getInboxEmails());
    }

    @FXML
    public void handleNewMessageButton(ActionEvent event) throws IOException {
        setPrevStage((Stage) newMessageButton.getScene().getWindow());
        displayNextWindow("newMessageWindow.fxml", false);
    }

    public void displayInbox(ActionEvent actionEvent) {
        mailList = new ListView<>(getInboxEmails());
    }
}
