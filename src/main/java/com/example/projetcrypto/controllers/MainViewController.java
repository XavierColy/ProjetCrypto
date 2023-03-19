package com.example.projetcrypto.controllers;

import com.example.projetcrypto.TransitionController;
import com.example.projetcrypto.utils.Config;
import com.example.projetcrypto.utils.HandleEmail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class MainViewController extends TransitionController {
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
        @FXML
        public void handleNewMessageButton(ActionEvent event) throws IOException {

            setPrevStage((Stage) newMessageButton.getScene().getWindow());
            displayNextWindow("newMessageWindow.fxml", false);
        }@FXML
    public static void replyToMessage(String user, String password,String replyMessage) {

        try {
            Store store = Config.getEmailSession().getStore("imaps");
            store.connect("imap.outlook.com", user, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Scanner myID = new Scanner(System.in);
            System.out.print("Enter the message ID: ");
            String messageID = myID.nextLine();

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                if (message.getHeader("Message-ID")[0].equals(messageID)) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Do you want to reply to only the sender or everyone? (Enter 1 for Sender, 2 for Everyone)");
                    int replyType = scanner.nextInt();

                    MimeMessage reply = (MimeMessage) message.reply(replyType == 1 ? false : true);
                    reply.setText(replyMessage);
                    reply.setSubject("RE: " + message.getSubject());
                    reply.setFrom(new InternetAddress(user));
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
