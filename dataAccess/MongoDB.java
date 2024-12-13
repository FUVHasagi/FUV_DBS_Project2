package dataAccess;
// ... import necessary MongoDB libraries ...
import com.mongodb.client.*;
import org.bson.Document;

import model.Customer;
import model.Order;
import model.Product;
import model.Review;
// ... other imports

public class MongoDB implements DataAccess {
    private MongoClient mongoClient;
    private MongoDatabase database;


    public MongoDB() {
        // Initialize MongoDB connection
        String connectionString = "mongodb://localhost:27017"; // or your Atlas URI
        this.mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase("your_database_name"); // Replace your_database_name
    }


    // ...DataAccess Interface methods implementation.


}