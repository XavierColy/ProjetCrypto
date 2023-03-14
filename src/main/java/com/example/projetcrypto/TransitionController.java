package com.example.projetcrypto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class TransitionController {

    private Stage prevStage;

    public void setPrevStage(Stage s) { this.prevStage = s; }

    protected void transitionScene(String sceneTitle, String fxmlFileName, double width, double height) throws IOException {
        Stage s = new Stage();
        s.setTitle(sceneTitle);
        Pane p = FXMLLoader.load(getClass().getResource("layedit.fxml"));
        s.setScene(new Scene(p, width, height));
        prevStage.close();
        s.show();
    }
}