package com.example.projetcrypto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Permet de naviguer d'une fenêtre à une autre*/
public class TransitionController {

    /**Etape précédente*/
    private Stage prevStage;

    public void setPrevStage(Stage s) { this.prevStage = s; }

    public void displayNextWindow(String fxmlFileName, boolean closePrev) throws IOException {
        Stage s = new Stage();
        Pane p = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../fxml/"+fxmlFileName)));
        s.setScene(new Scene(p));
        if (closePrev) prevStage.close();
        s.show();
    }
}