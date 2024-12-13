package dataAccess;

import model.Product;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MySQL {
    private Connection connection;
    public MySQL(Connection conn) {
        this.connection = conn;
    }

    public User authenticateUser(String username, String password) {
        String query = "SELECT Roles.Name FROM Users JOIN Roles ON Users.RoleID = Roles.ID WHERE Users.Username = ? AND Users.Password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("Name");
                return new User(username, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Vector<String> getUniqueValues(String columnName) throws SQLException {
        Vector<String> uniqueValues = new Vector<>();
        String query = "SELECT DISTINCT " + columnName + " FROM Products"; // Use PreparedStatement for security if needed
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                uniqueValues.add(rs.getString(columnName));
            }
        }
        return uniqueValues;
    }


    public List<Product> getFilteredProducts(String filterQuery) {
        List<Product> filteredProducts = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(filterQuery)) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("ID"), rs.getString("Name"), rs.getDouble("SellPrice"),
                        rs.getInt("Stock"), rs.getString("Category") // ... other fields
                );
                filteredProducts.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
        }
        return filteredProducts;
    }


    public Product getProductById(int productId) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Products WHERE ID = ?")) {  // Use PreparedStatement
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("ID"), rs.getString("Name"), rs.getDouble("SellPrice"),
                            rs.getInt("Stock"), rs.getString("Category") // ... other fields
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
        }
        return null; // Or throw an exception
    }

}

