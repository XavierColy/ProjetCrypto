package com.example.projetcrypto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LaySentController extends TransitionController {
    @FXML protected TextField tto;
    @FXML protected TextArea ttext;
    @FXML protected TextField tsub;
    @FXML protected TextField thead;
    @FXML
    public void setValues(String to, String text, String sub, String head) {
        tto.setText(to);
        ttext.setText(text);
        tsub.setText(sub);
        thead.setText(head);
    }
}