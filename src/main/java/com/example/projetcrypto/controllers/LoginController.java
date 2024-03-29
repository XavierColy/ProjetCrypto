package com.example.projetcrypto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.Transport;

import com.example.projetcrypto.mail.Client;

import java.io.IOException;

import static com.example.projetcrypto.utils.Config.*;

public class LoginController extends TransitionController {
	
	public static Client clientHttps;
	public static String url = "http://127.0.1.1:8080/service";
	
    public ProgressIndicator progressIndicator;
    public Button loginButton;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private void handleLogin() {
        // Disable the window
        loginButton.setDisable(true);
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
            loginButton.setDisable(false);
            return;
        }

        try {
            // Try to connect to the SMTP server using the user's credentials
            setEmailSession(email,password);
            Transport transport = getEmailSession().getTransport("smtp");
            transport.connect();
            transport.close();

            Store store = getEmailSession().getStore("imaps");
            store.connect();

            setStore(store);
           
            // If the connection is successful
            // client et config
    		clientHttps = new Client(email,Client.receptionConfig(email,url));
    		// navigate to next window
            this.setPrevStage((Stage) anchorPane.getScene().getWindow());
            displayNextWindow("MainView.fxml", true);

        } catch (AuthenticationFailedException e) {
            // If authentication fails, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Login Failed");
            errorAlert.setContentText("The email address or password you entered is incorrect. Please try again.");
            errorAlert.showAndWait();
            progressIndicator.setVisible(false);
            loginButton.setDisable(false);
        } catch (MessagingException e) {
            // If there is any other error, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Login Error");
            errorAlert.setContentText("An error occurred while trying to log in. Please try again later.");
            errorAlert.showAndWait();
            progressIndicator.setVisible(false);
            loginButton.setDisable(false);
        } catch (IOException e) {
            progressIndicator.setVisible(false);
            loginButton.setDisable(false);
            throw new RuntimeException(e);
        }
    }
}
