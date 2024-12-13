import controller.LoginController;
import dataAccess.MySQL;
import dataAccess.MongoDB;
import dataAccess.Redis;
import view.LoginView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    private static Application instance;
    private Connection connection;
    private MySQL mySQL;
    private Redis redis;
    private MongoDB mongoDB;
    private LoginController loginController;
    private LoginView loginView;


    public static Application getInstance() {
        if (instance == null) {
            try {
                instance = new Application();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return instance;
    }

    private Application() throws SQLException {
        try {
            // Establish connection with MySQL database
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/project1", "myuser", "mypassword"
            ); // Replace with your actual credentials
            this.mySQL = new MySQL(connection);

            this.redis = new Redis();       // Placeholder initialization
            this.mongoDB = new MongoDB();   // Placeholder initialization

            this.loginView = new LoginView();
            this.loginController = new LoginController(loginView, mySQL);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            throw new SQLException("Failed to connect to the database");
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Application.getInstance();  // Start the application
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
