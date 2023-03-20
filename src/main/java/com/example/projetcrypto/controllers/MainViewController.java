package com.example.projetcrypto.controllers;

import com.example.projetcrypto.bo.EmailModel;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.search.MessageIDTerm;
import java.io.IOException;

import static com.example.projetcrypto.utils.Config.getEmailSession;
import static com.example.projetcrypto.utils.HandleEmail.*;

public class MainViewController extends TransitionController {
    @FXML
    public Button newMessageButton;
    public Button replyButton;
    public Button forwardButton;
    public Button deleteButton;
    public ListView<EmailModel> mailList;
    public Button receptionButton;
    public Button sentMessagesButton;
    public Button draftMessagesButton;
    public Button spamMessagesButton;
    public Button trashButton;
    public SplitPane emailSplitPane;
    public TextField subjectField;
    public TextField senderField;
    public TextField attachmentsDisplayField;
    public Button downloadAttachmentsButton;
    public TextArea textArea;
    @FXML
    private Label mailContentLabel;
    private EmailModel selectedEmail;


    @FXML
    private void initialize() {
        if (mailList == null) {
            mailList = new ListView<EmailModel>();
        }
        mailList.setItems(getEmailsByFolder("INBOX") );

        // Only allowed to select single row in the ListView.
        mailList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        mailList.getSelectionModel().selectedItemProperty().addListener(this::onSelectionChange);
    }

    @FXML
    public void handleNewMessageButton(ActionEvent event) throws IOException {
        setPrevStage((Stage) newMessageButton.getScene().getWindow());
        displayNextWindow("newMessageWindow.fxml", false);
    }


    @FXML
    public void displayInbox() {
        setButtonsAndMailZoneActivated(false);

        this.selectedEmail = null;

        if (mailList == null) {
            mailList = new ListView<EmailModel>();
        }
        mailList.setItems(getEmailsByFolder("INBOX") );
    }
    @FXML
    public void showSentMails() {
        setButtonsAndMailZoneActivated(false);

        this.selectedEmail = null;

        if (mailList == null) {
            mailList = new ListView<EmailModel>();
        }
        mailList.setItems(getEmailsByFolder("Sent"));
    }


    public void deleteEmail() {
        try {
            Store store = getEmailSession().getStore("imaps");
            store.connect();
            Folder folder = store.getFolder(this.selectedEmail.getFolderName());
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.search(new MessageIDTerm(this.selectedEmail.getId()));
            Message foundMessage = messages[0];
            deleteMail(foundMessage.getHeader("Message-ID")[0], this.selectedEmail.getFolderName());
            folder.close(true);
            store.close();
            displayInbox();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


   /* public void forward(){
        forwardMail(String messageID, String forwardTo);
    }
    public void replyto(){
        reply(String messageID,String text);
    }*/

    private void onSelectionChange(ObservableValue<? extends EmailModel> observable, EmailModel oldValue, EmailModel newValue){
        if (newValue!=null){
            setButtonsAndMailZoneActivated(true);

            try {
                Store store = getEmailSession().getStore("imaps");
                store.connect();
                Folder folder = store.getFolder(newValue.getFolderName());
                folder.open(Folder.READ_WRITE);
                Message[] messages = folder.search(new MessageIDTerm(newValue.getId()));
                Message foundMessage = messages[0];
                this.selectedEmail = newValue;

                //Set subject
                subjectField.setText(foundMessage.getSubject());

                //Set Sender
                senderField.setText(foundMessage.getFrom()[0].toString());

                //todo display attachments

                //Set Content
                textArea.setText(foundMessage.getContent().toString());

                folder.close(true);
                store.close();
            } catch (MessagingException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setButtonsAndMailZoneActivated(boolean state){
        //Set the zone visible
        emailSplitPane.setVisible(state);

        //Set buttons enabled
        forwardButton.setDisable(!state);
        replyButton.setDisable(!state);
        deleteButton.setDisable(!state);
    }
}