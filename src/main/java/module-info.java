module com.example.projetcrypto {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.projetcrypto to javafx.fxml;
    exports com.example.projetcrypto;
}