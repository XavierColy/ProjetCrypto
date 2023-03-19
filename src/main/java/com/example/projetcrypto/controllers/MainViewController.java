package com.example.projetcrypto.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.mail.Message;

public class MainViewController {
    @FXML
    public Button newMessageButton;
    public Button answerButton;
    public Button forwardButton;
    public Label receivedMessagesLabel;
    public Label sentMessagesLabel;
    public Label draftMessagesLabel;
    public Label spamMessagesLabel;
    public Label trashLabel;
    public ListView mailList;


//    @FXML
//    private void init(){
//        mailList= new ListView<Message>(Email.listMessageIDs("cryptotest10@outlook.com","Azerty@2023"));
//    }
//
//    public void displayNewMessageWindow(ActionEvent actionEvent) {
//        mailList= new ListView<Message>(Email.listMessageIDs("cryptotest10@outlook.com","Azerty@2023"));
//    }
}
