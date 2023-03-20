package com.example.projetcrypto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Transport;
import java.io.IOException;

import static com.example.projetcrypto.utils.Config.getEmailSession;
import static com.example.projetcrypto.utils.Config.setEmailSession;

public class LoginController extends TransitionController {

    public ProgressIndicator progressIndicator;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private void handleLogin() {
        // Disable the window
        anchorPane.setDisable(true);
        progressIndicator.setVisible(true);
        // Get the email and password entered by the user
        String email = usernameField != null ? usernameField.getText() : null;
        String password = passwordField != null ? passwordField.getText() : null;

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            // If either email or password field is null or empty, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Login");
            errorAlert.setContentText("Please enter your email and password.");
            errorAlert.showAndWait();
            anchorPane.setDisable(false);
            return;
        }

        try {
            // Try to connect to the SMTP server using the user's credentials
            setEmailSession(email,password);
            Transport transport = getEmailSession().getTransport("smtp");
            transport.connect();
            transport.close();

            // If the connection is successful, navigate to next window
            this.setPrevStage((Stage) anchorPane.getScene().getWindow());
            displayNextWindow("MainView.fxml", true);

        } catch (AuthenticationFailedException e) {
            // If authentication fails, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Login Failed");
            errorAlert.setContentText("The email address or password you entered is incorrect. Please try again.");
            errorAlert.showAndWait();
            progressIndicator.setVisible(false);
            anchorPane.setDisable(false);
        } catch (MessagingException e) {
            // If there is any other error, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Login Error");
            errorAlert.setContentText("An error occurred while trying to log in. Please try again later.");
            errorAlert.showAndWait();
            progressIndicator.setVisible(false);
            anchorPane.setDisable(false);
        } catch (IOException e) {
            progressIndicator.setVisible(false);
            anchorPane.setDisable(false);
            throw new RuntimeException(e);
        }
    }
}
