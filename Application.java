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
    private MySQL mySQL;
    private MongoDB mongoDB;
    private Redis redis;
    private Connection connection;

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
            // Initialize MySQL
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/project2", "myuser", "mypassword"
            );
            this.mySQL = new MySQL(connection);

            // Initialize MongoDB
            this.mongoDB = new MongoDB();

            // Initialize Redis
            this.redis = new Redis(this.mongoDB);

            // Initialize LoginView and Controller
            LoginView loginView = new LoginView();
            new LoginController(loginView, mySQL, mongoDB, redis);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to MySQL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Initialization failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> Application.getInstance());
    }
}
