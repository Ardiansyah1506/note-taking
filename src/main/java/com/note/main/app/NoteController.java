package com.note.main.app;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteController {
    private MongoCollection<Document> notesCollection;
    private String userId;
    private String idNote;

    @FXML
    private Button btn_save;

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_delete;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private TextField tf_judul;

    @FXML
    private TableView<Document> tableView;
    @FXML
    private TableColumn<Document, String> listColumn;

    public void initialize() {
        DatabaseManager databaseManager = new DatabaseManager();

        // Set notesCollection directly from DatabaseManager
        notesCollection = databaseManager.getNotesCollection();

        // Set up table view
        listColumn.setCellValueFactory(cellData -> {
            String note = cellData.getValue().getString("judul"); // Ambil nilai 'note' dari Document
            return new SimpleStringProperty(note);
        });
        refreshTable();

        // Handle button click events
        btn_save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (idNote == null){
                    newNote(); // Call the newNote method when the button is clicked

                }else{
                    updateNote();
                }
                refreshTable(); // Refresh table after saving
                htmlEditor.setHtmlText("");
                tf_judul.setText(""); // Clear the HTML editor after saving
                htmlEditor.setDisable(false);
            }
        });
        btn_edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tf_judul.setDisable(false);
                htmlEditor.setDisable(false);
            }
        });
        btn_delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteNote();
                htmlEditor.setDisable(false);
                tf_judul.setDisable(false);
            }
        });

        tableView.setOnMouseClicked(event -> {
            // Ambil catatan yang dipilih dari tabel
            Document selectedNote = tableView.getSelectionModel().getSelectedItem();
            if (selectedNote != null) {
                // Mendapatkan nilai 'judul' dari Document yang dipilih
                String judul = selectedNote.getString("judul");
                // Mendapatkan nilai 'note' dari Document yang dipilih
                String noteText = selectedNote.getString("note");

                // Menetapkan judul dan konten catatan ke HTMLEditor
                tf_judul.setText(judul);
                htmlEditor.setHtmlText(noteText);

                // Lakukan pencarian berdasarkan nilai 'judul' yang dipilih
                Document foundNote = notesCollection.find(Filters.eq("judul", judul)).first();
                htmlEditor.setDisable(true);
                tf_judul.setDisable(true);
                // Jika ditemukan catatan dengan nilai yang sesuai, lanjutkan
                if (foundNote != null) {
                    // Mendapatkan nilai _id dari catatan yang ditemukan
                    idNote = foundNote.getObjectId("_id").toString();
                }
            } else {
                System.out.println("No note selected.");
            }
        });
    }



    public void getuserId(String userId) {
        this.userId = userId;
    }

    public void newNote() {
        String htmlContent = htmlEditor.getHtmlText();
        String judul = tf_judul.getText();

        // Memeriksa apakah konten catatan tidak kosong
        if (!htmlContent.isEmpty()) {
            // Create a new note document
            Document newNote = new Document("user_id", this.userId)
                    .append("note", htmlContent)
                    .append("judul", judul);

            // Insert the new note document into the 'notes' collection
            try {
                notesCollection.insertOne(newNote);
                System.out.println("Note saved successfully.");
                htmlEditor.setHtmlText(""); // Clear the HTML editor after saving
                tf_judul.setText(""); // Clear the HTML editor after saving
                refreshTable(); // Refresh table after saving
            } catch (Exception e) {
                System.out.println("Failed to save note. Error: " + e.getMessage());
            }
        } else {
            System.out.println("No content to save."); // Kasus di mana konten catatan kosong
        }
    }

    public void refreshTable() {
        // Clear existing items from table view
        tableView.getItems().clear();

        // Get all notes for the current userId from the database
        List<Document> notesList = new ArrayList<>();
        notesCollection.find(Filters.eq("user_id", this.userId)).forEach(notesList::add);

        // Convert the list to ObservableList
        ObservableList<Document> notes = FXCollections.observableArrayList(notesList);

        // Add notes to table view
        tableView.setItems(notes);
    }

    public void updateNote() {
        try {
            if (idNote != null) {
                // If a note with a matching id is found, proceed with updating the note
                String updatedNoteText = htmlEditor.getHtmlText();
                String updatedJudul = tf_judul.getText();
                notesCollection.updateOne(
                        Filters.eq("_id", new ObjectId(idNote)),
                        Updates.combine(
                                Updates.set("note", updatedNoteText),
                                Updates.set("judul", updatedJudul)
                        )
                );

                System.out.println("Note updated successfully.");
            }
            idNote = null;
        } catch (Exception e) {
            System.out.println("Failed to update note. Error: " + e.getMessage());
        }
    }
    public void deleteNote() {
        try {
            if (idNote != null) {
                // If idNote is not null, proceed with deleting the note
                notesCollection.deleteOne(Filters.eq("_id", new ObjectId(idNote)));
                System.out.println("Note deleted successfully.");
                refreshTable(); // Refresh table after deleting the note
                htmlEditor.setHtmlText(""); // Clear the HTML editor after deleting the note
                tf_judul.setText("");
                idNote = null; // Set idNote to null after deleting the note
            } else {
                System.out.println("No note selected.");
            }
        } catch (Exception e) {
            System.out.println("Failed to delete note. Error: " + e.getMessage());
        }
    }

}
