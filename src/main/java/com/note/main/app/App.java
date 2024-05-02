package com.note.main.app;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Load Login.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/auth/login.fxml"));
        Parent root = loader.load();

        // Set the scene
        primaryStage.setScene(new Scene(root,766,598));
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
