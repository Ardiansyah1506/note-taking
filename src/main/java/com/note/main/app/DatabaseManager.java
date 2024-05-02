package com.note.main.app;

import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.Document;


public class DatabaseManager {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> usersCollection;
    private MongoCollection<Document> notesCollection;
    public DatabaseManager() {
        String connectionString = "mongodb+srv://fauziardiansyah302:XmX6i4WbvkYIPcTf@ziidb.euhwxvb.mongodb.net/?retryWrites=true&w=majority&appName=ziidb";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        mongoClient = MongoClients.create(settings);
        // Get the database
        database = mongoClient.getDatabase("note-taking");
        // Initialize collections
        usersCollection = database.getCollection("users");
        notesCollection = database.getCollection("notes");
    }

    public MongoCollection<Document> getUsersCollection() {
        return usersCollection;
    }

    public MongoCollection<Document> getNotesCollection() {
        return notesCollection;
    }
}