module com.note.main{
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires javafx.web;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;

    opens com.note.main.app to javafx.fxml;
    exports com.note.main.app;


    opens com.note.main.auth to javafx.fxml;
    exports com.note.main.auth;
}
