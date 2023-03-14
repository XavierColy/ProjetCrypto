package com.example.projetcrypto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.Properties;

public class LayLogController extends TransitionController {

    private final static String GMAIL_HOST = "smtp.gmail.com";
    private final static String GMAIL_PORT = "587";
    private final static String YAHOO_HOST = "smtp.mail.yahoo.com";
    private final static String YAHOO_PORT = "465";
    private final static String DEFAULT_PORT = "80";

    @FXML private GridPane layLOG;
    @FXML private ComboBox<String> cmbHOST;
    @FXML private TextField tUN, tPW;
    @FXML private static final Text UNfail = new Text("Cannot authenticate");

    public static String UN;
    static String PW;
    static String port;
    static Properties prop = new Properties();

    private Stage prevStage;

    public void setPrevStage(Stage s) { this.prevStage = s; }

    @FXML
    protected void handleVerifyButton(ActionEvent e) throws IOException{
        UN = tUN.getText();
        PW = tPW.getText();
        switch (Client.host = cmbHOST.getValue()) {
            case GMAIL_HOST:
                port = GMAIL_PORT;
                break;
            case YAHOO_HOST:
                port = YAHOO_PORT;
                break;
            default:
                port = DEFAULT_PORT;
        }
        if(layLOG.getChildren().contains(UNfail)) {
            System.out.print("y");
            layLOG.getChildren().remove(UNfail);
        }
        auth();
    }

    private void auth() throws IOException{
        boolean auth = chk(UN, PW);
        if(!auth) {
            System.out.print("Not auth");
            layLOG.add(UNfail, 3, 1);
            tUN.clear();
            tPW.clear();
        } else if (auth) {
            System.out.print("Auth");
            transitionScene("Edit Email", "layedit.fxml", 640, 710);
        } else {
            System.out.print("Not auth");
            layLOG.add(UNfail, 3, 1);
            cmbHOST.setValue(" ");
            tUN.clear();
            tPW.clear();
        }
    }

    private boolean chk(String UN, String PW) {

        prop.put("mail.smtp.auth", "true");
        if(Client.host.equals("smtp.gmail.com") || Client.host.equals("smtp.mail.yahoo.com")){
            prop.put("mail.smtp.starttls.enable", "true");
        }
        prop.put("mail.smtp.host", Client.host);
        prop.put("mail.smtp.port", port);
        if(Client.host.equals("smtp.mail.yahoo.com")) { prop.put("mail.smtp.ssl.enable", "true"); }

        boolean check = true;
        //
        try {
            InternetAddress e = new InternetAddress(UN);
            e.validate();
        } catch (AddressException e) {
            e.getStackTrace();
            check = false;
        }

        if(check) {
            Client.sesh = Session.getInstance(prop,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(UN, PW);
                        }
                    });
        }

        return check;
    }
}