package com.example.projetcrypto.controllers;

import com.example.projetcrypto.bo.EmailModel;
import com.example.projetcrypto.utils.DataTypeEnum;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.MessageIDTerm;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
    public Button trashButton;
    public SplitPane emailSplitPane;
    public TextField subjectField;
    public TextField senderField;
    @FXML
    public ListView<String> attachmentListView;

    public TextArea textArea;
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
    @FXML
    public void Corbeille() {
        setButtonsAndMailZoneActivated(false);

        this.selectedEmail = null;

        if (mailList == null) {
            mailList = new ListView<EmailModel>();
        }
        mailList.setItems(getEmailsByFolder("Deleted Items"));
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


    private void replyAndForward(DataTypeEnum type)  {
        try {
            Stage s = new Stage();
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../fxml/newMessageWindow.fxml")));
            Pane p = loader.load();
            NewMessageController newMessageController = loader.getController();
            newMessageController.setData(this.selectedEmail, type);
            s.setScene(new Scene(p));
            s.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void replyto()  {
        replyAndForward(DataTypeEnum.REPLY);
    }

    public  void forwardto() {
        replyAndForward(DataTypeEnum.FORWARD);
    }

    private Path chooseSaveFilePath(String fileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Attachment");
        fileChooser.setInitialFileName(fileName);

        // Set file extension filter to restrict the file types that can be saved
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "Allowed file types", "*." + FilenameUtils.getExtension(fileName));
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // Ensure that the file extension matches the original attachment
            String extension = FilenameUtils.getExtension(file.getName());
            if (!extension.equalsIgnoreCase(FilenameUtils.getExtension(fileName))) {
                String newFileName = FilenameUtils.getBaseName(file.getName()) + "." +
                        FilenameUtils.getExtension(fileName);
                file = new File(file.getParent(), newFileName);
            }
            return file.toPath();
        } else {
            return null;
        }
    }




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isAttachmentAccessible(BodyPart bodyPart) {
        try {
            InputStream is = bodyPart.getInputStream();
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) {
                // read and do nothing
            }
            return true;
        } catch (IOException | MessagingException ex) {
            return false;
        }
    }

    @FXML
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

                // Display attachments
                Object content = foundMessage.getContent();
                if (content instanceof Multipart) {
                    Multipart multipart = (Multipart) content;

                    // Clear the attachmentListView to remove any previous attachments
                    attachmentListView.getItems().clear();

                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);

                        // Check if the part is an attachment
                        if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {

                            // Get the attachment file name
                            String fileName = bodyPart.getFileName();

                            // Add the attachment file name to the attachmentListView
                            attachmentListView.getItems().add(fileName);

                            // Set a listener to download the selected attachment when it's clicked
                            attachmentListView.setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2) {
                                    try {
                                        Object selectedItem = attachmentListView.getSelectionModel().getSelectedItem();
                                        if (selectedItem instanceof String) {
                                            String selectedFileName = (String) selectedItem;

                                            // Find the BodyPart that corresponds to the selected attachment
                                            BodyPart selectedBodyPart = null;
                                            for (int j = 0; j < multipart.getCount(); j++) {
                                                BodyPart bp = multipart.getBodyPart(j);
                                                if (Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition()) &&
                                                        selectedFileName.equalsIgnoreCase(bp.getFileName())) {
                                                    selectedBodyPart = bp;
                                                    break;
                                                }
                                            }

                                            // Download the selected attachment
                                            if (selectedBodyPart != null) {
                                                InputStream is = selectedBodyPart.getInputStream();
                                                Path saveFilePath = chooseSaveFilePath(selectedFileName);
                                                if (saveFilePath != null) {
                                                    Files.copy(is, saveFilePath, StandardCopyOption.REPLACE_EXISTING);
                                                    showAlert("Download Complete", "Attachment downloaded successfully");
                                                }
                                            }
                                        }
                                    } catch (IOException | MessagingException ex) {
                                        String errorMsg = "Failed to download attachment: " + ex.getMessage();
                                        if (ex instanceof FileNotFoundException) {
                                            errorMsg += "\nAttachment file not found.";
                                        } else if (ex instanceof IOException) {
                                            errorMsg += "\nError accessing attachment file.";
                                        } else {
                                            errorMsg += "\nAttachment is corrupted or inaccessible.";
                                        }
                                        showAlert("Error", errorMsg);
                                    }
                                }
                            });

                        }
                    }
                }





            } catch (Exception ex) {
                showAlert("Error", "Failed to retrieve email content: " + ex.getMessage());
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