package com.example.projetcrypto.controllers;

import com.example.projetcrypto.TransitionController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.io.IOException;
import java.util.Properties;

public class LoginController extends TransitionController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Session emailSession;

    @FXML
    private void initialize() {
        // Initialize the email session with default properties
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp-mail.outlook.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        emailSession = Session.getDefaultInstance(properties);
    }

    @FXML
    private void handleLogin() {
        // Get the email and password entered by the user
        String email = usernameField != null ? usernameField.getText() : null;
        String password = passwordField != null ? passwordField.getText() : null;

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            // If either email or password field is null or empty, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Login");
            errorAlert.setContentText("Please enter your email and password.");
            errorAlert.showAndWait();
            return;
        }

        try {
            // Try to connect to the SMTP server using the user's credentials
            Transport transport = emailSession.getTransport("smtp");
            transport.connect(emailSession.getProperty("mail.smtp.host"), email, password);
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
        } catch (MessagingException e) {
            // If there is any other error, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Login Error");
            errorAlert.setContentText("An error occurred while trying to log in. Please try again later.");
            errorAlert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
