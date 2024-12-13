import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;



// Import Jedis
// Import MongoDB
public class Application {
    private static Application instance;

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
        // ####### GET CONNECTION FROM MYSQL #######
//        try {            // Establish connection with MySQL database
//            this.connection = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/project1", "myuser", "mypassword"
//            );
//            this.dataAccess = new DataAccess(connection);
//
//            // Initialize controllers
//            this.loginController = new LoginController(loginView, dataAccess);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException("Failed to connect to the database");
//        }

        // #######
    }
}
