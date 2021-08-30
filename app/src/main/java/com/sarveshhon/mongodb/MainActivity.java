package com.sarveshhon.mongodb;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sarveshhon.mongodb.databinding.ActivityMainBinding;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.mongo.MongoCollection;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    // MongoDb Database Parameters
    public static String APP_ID = "<your-realm-app-id>";
    public static String CLIENT_NAME = "mongodb-atlas";
    public static String DATABASE_NAME = "MongoDB";
    public static String COLLECTION_NAME = "entry";

    App app;
    MongoCollection mongoCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialise MongoDB Realm
        Realm.init(this);

        // Build app Object Connected to Server
        app = new App(new AppConfiguration.Builder(APP_ID).build());

        // Anonymous Login Implemented here
        app.loginAsync(Credentials.anonymous(), result -> {
            if (result.isSuccess()) {
                Toast.makeText(this, "Login Successful ", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }

            // Path to MongoDB Collection with respect to Current User Authorisations
            mongoCollection = app.currentUser().getMongoClient(CLIENT_NAME).getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);

        });

        // Inserting Document OnClick
        binding.btnInsertOne.setOnClickListener(v -> {
            String name = binding.etName.getText().toString();
            String rollno = binding.etRollNo.getText().toString();

            if (!name.isEmpty() && !rollno.isEmpty()) {

                insertData(name, rollno);

            } else {
                Toast.makeText(this, "Complete the Fields", Toast.LENGTH_SHORT).show();
            }

        });

        // Find Document OnClick
        binding.btnFindOne.setOnClickListener(v -> {
            String rollno = binding.etRollNo.getText().toString();

            if (!rollno.isEmpty()) {

                findOneData(rollno);

            } else {
                binding.etRollNo.setError("Enter Roll No");
            }
        });

        // Delete Document OnClick
        binding.btnDeleteOne.setOnClickListener(v -> {

            String rollno = binding.etRollNo.getText().toString();

            if (!rollno.isEmpty()) {

                deleteData(rollno);

            } else {
                binding.etRollNo.setError("Enter Roll No");
            }
        });


    }

    //Finding Document from Collection using Roll No
    void findOneData(String rollno) {
        try {
            // Document Object with Parameters
            Document filter = new Document().append("rollno", rollno);
            binding.btnFindOne.setClickable(false);

            // MongoDB Method for Deleting Single Document
            mongoCollection.findOne(filter).getAsync(result -> {
                if (result.isSuccess()) {
                    binding.btnFindOne.setClickable(true);
                    try{
                        // Document Object with result Parameters
                        Document data = (Document) result.get();
                        binding.etName.setText(data.getString("name"));
                    } catch (Exception e) {

                        Toast.makeText(this, "Not Found ", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(this, "Fetched Successful ", Toast.LENGTH_SHORT).show();

                } else {
                    binding.btnFindOne.setClickable(true);
                    Toast.makeText(this, "Fetching Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Invalid Request", Toast.LENGTH_SHORT).show();
        }

    }

    // Inserting Document in the Database Collection
    void insertData(String name, String rollno) {

        try {
            // MongoDB Method for Inserting Single Document
            mongoCollection.insertOne(new Document("userid", app.currentUser().getId()).append("name", name).append("_id", rollno).append("rollno", rollno)).getAsync(result -> {
                binding.btnInsertOne.setClickable(false);
                if (result.isSuccess()) {
                    binding.btnInsertOne.setClickable(true);
                    binding.etName.setText("");
                    binding.etRollNo.setText("");
                    Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    binding.btnInsertOne.setClickable(true);
                    Toast.makeText(this, "Data Inserted Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Invalid Request", Toast.LENGTH_SHORT).show();
        }

    }

    // Delete Document from Collection using Roll No
    void deleteData(String rollno) {

        try {
            // Document Object with Parameters
            Document filter = new Document().append("rollno", rollno);

            // MongoDB Method for Deleting Single Document
            mongoCollection.deleteOne(filter).getAsync(result -> {
                binding.btnDeleteOne.setClickable(false);
                if (result.isSuccess()) {
                    binding.btnDeleteOne.setClickable(true);
                    binding.etRollNo.setText("");
                    Toast.makeText(this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();

                } else {
                    binding.btnDeleteOne.setClickable(false);
                    Toast.makeText(this, "Data Deletion Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Invalid Request", Toast.LENGTH_SHORT).show();
        }

    }


}