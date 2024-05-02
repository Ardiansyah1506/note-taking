package com.note.main.auth;

import com.note.main.app.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.note.main.app.DatabaseManager; // Import DatabaseManager

import java.security.NoSuchAlgorithmException;

public class RegisterController {
    private MongoCollection<Document> usersCollection;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private Button btn_register;
    @FXML
    private Button btn_loginPage;

    public void initialize() {
        // Buat instance DatabaseManager
        DatabaseManager databaseManager = new DatabaseManager();
        // Dapatkan koleksi pengguna dari DatabaseManager
        usersCollection = databaseManager.getUsersCollection();

        btn_register.setOnAction(event -> {
            try {
                registerUser(event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        btn_loginPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.changeScene(event,"/auth/login.fxml",null,"Register Page");
            }
        });

    }



    // Tidak perlu konstruktor jika koleksi pengguna diperoleh dari DatabaseManager

    @FXML
    public void registerUser(ActionEvent event) throws Exception {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        String key = Encryptor.generateKey();
        String encryptedPassword = Encryptor.encrypt(password,key);
        // Mendapatkan jumlah pengguna yang sudah terdaftar
        long userCount = usersCollection.countDocuments();

        // Membuat ID pengguna baru berdasarkan jumlah pengguna yang sudah terdaftar
        String id = String.valueOf(userCount + 1);

        // Menambahkan penanganan kesalahan jika terjadi kesalahan saat menghasilkan ID pengguna
        if (id == null) {
            System.out.println("Failed to generate user ID. Please try again later.");
            return;
        }

        // Check if the username already exists
        if (usersCollection.countDocuments(Filters.eq("username", username)) > 0) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }
        // Create a new user document
        Document newUser = new Document("user_id", id)
                .append("email", email)
                .append("username", username)
                .append("password", encryptedPassword)
                .append("key", key);

        // Insert the new user document into the 'users' collection
        try {
            usersCollection.insertOne(newUser);
            System.out.println("User registered successfully.");
            Controller.changeScene(event,"/auth/login.fxml",null,"Login Page");
        } catch (Exception e) {
            System.out.println("Failed to register user. Error: " + e.getMessage());
        }
    }


}
