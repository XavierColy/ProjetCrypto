module com.example.projetcrypto {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
	requires jpbc.api;
	requires jpbc.plaf;
	requires java.logging;
	requires jdk.httpserver;
	requires java.instrument;
	requires java.mail;

    opens com.example.projetcrypto to javafx.fxml;
    exports com.example.projetcrypto;
}