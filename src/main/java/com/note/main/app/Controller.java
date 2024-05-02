package com.note.main.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;

public class Controller {


    public static void changeScene(ActionEvent event, String FxmlFile, String Userid, String title){
        Parent root = null;
        if(Userid != null){
            try {
                FXMLLoader loader= new FXMLLoader(Controller.class.getResource(FxmlFile));
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try{
                FXMLLoader loader= new FXMLLoader(Controller.class.getResource(FxmlFile));
                root = loader.load();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,766,598));
    }

    public static void NoteScene(ActionEvent event, String FxmlFile, String userId, String title){
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(Controller.class.getResource(FxmlFile));
            root = loader.load();

            NoteController note = loader.getController(); // Ambil controller dari loader
            note.getuserId(userId); // Set userId pada NoteController
            note.refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 838, 606));
    }

}
