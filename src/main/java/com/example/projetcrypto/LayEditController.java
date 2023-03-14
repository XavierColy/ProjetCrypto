package com.example.projetcrypto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

import static com.example.projetcrypto.Client.sesh;
import static com.example.projetcrypto.LayLogController.UN;

public class LayEditController extends TransitionController {

    @FXML private ComboBox<String> cmbTYPE;
    @FXML protected TextField tto;
    @FXML protected TextField thead;
    @FXML protected TextField tsub;
    @FXML protected TextArea ttext;

    private String mto, mhead, msub, cTYPE, cTEXT;

    @FXML
    protected void handleSendButton(ActionEvent e) throws IOException {
        cTYPE = cmbTYPE.getValue();
        mto = tto.getText();
        mhead = thead.getText();
        msub = tsub.getText();
        cTEXT = ttext.getText();
        Mail(mto, msub, cTEXT);
        if(!mto.isEmpty() || !cTEXT.isEmpty() || !msub.isEmpty()){
            transitionScene("Sent Email", "laysent.fxml", 500, 260);
        }
    }
    public void Mail(String to, String sub, String cont) {
        try {

            System.out.println("\n \n>> ?" + mto);
            System.out.println("\n \n>> ?" + to);
            Message m = new MimeMessage(sesh);
            m.setFrom(new InternetAddress(UN));
            m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            m.setSubject(sub);
            m.setSentDate(new Date());
            m.setContent(cont, cTYPE);
            m.setHeader("EMAIL HEAD", mhead);
            System.out.println("\n \n \n \t >> ??????? " + m.getContentType());
            System.out.println("\n \n \n \t >> ??????? " + m.getDataHandler());
            System.out.println("\n \n \n \t >> ??????? " + m.getSubject());

            Transport t;
            if(Client.host.equals("smtp.mail.yahoo.com"))
                t = sesh.getTransport("smtps");
            else
                t = sesh.getTransport("smtp");
            //
            System.out.println(">> ? smtp(s) ---> ## " + t.getURLName() + " \n>> ?");

            Transport.send(m);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}