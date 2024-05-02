package com.note.main.auth;

import com.note.main.app.DatabaseManager;
import com.note.main.app.NoteController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.note.main.app.Controller;

public class LoginController {
    private MongoCollection<Document> usersCollection;
    private MongoCollection<Document> notesCollection;
    private NoteController noteController;
    private String userId;
    private boolean checkuser;

    @FXML
    private Button btn_login;

    @FXML
    private Button btn_registerPage;

    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField tf_password;

    public LoginController() {
        DatabaseManager databaseManager = new DatabaseManager();
        usersCollection = databaseManager.getUsersCollection();
        notesCollection = databaseManager.getNotesCollection();
    }

    public void initialize() {
        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    loginUser();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if(checkuser){
                    Controller.NoteScene(event,"WebView.fxml",userId,"Note App");
                }
            }
        });
        btn_registerPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.changeScene(event,"/auth/Register.fxml",null,"Register Page");
            }
        });
    }

    public void loginUser() throws Exception {
        // Check if username and password fields are empty
        String username = tf_username.getText();
        String password = tf_password.getText();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please enter username and password.");
            return;
        }

        Document user = usersCollection.find(Filters.eq("username", username)).first();
        String key = user.getString("key");
        String EncryptedPassword = user.getString("password");
        String DecryptPassword= Encryptor.decrypt(EncryptedPassword,key);
        if (password.equals(DecryptPassword)) {
            System.out.println("Login successful.");
            this.userId = user.getString("user_id");
            this.checkuser = true;
        } else {
            checkuser = false;
            System.out.println("Invalid username or password.");
        }
    }
}
