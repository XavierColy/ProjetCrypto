package com.example.projetcrypto;



import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.mail.Session;

public class Client extends Application   {

    public static Session sesh;
    static String host;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader logLoader = new FXMLLoader(getClass().getResource("laylog.fxml"));

        Pane llPane = logLoader.load();

        LayLogController llController = logLoader.getController();

        llController.setPrevStage(primaryStage);

        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.setTitle("SendMail \u0020\u0020 | | \u0020\u0020 TDS");
        primaryStage.setResizable(false);

        primaryStage.setX(150);
        primaryStage.setY(200);
        primaryStage.setScene(new Scene(llPane, 480, 210));
        primaryStage.show();
    }

}