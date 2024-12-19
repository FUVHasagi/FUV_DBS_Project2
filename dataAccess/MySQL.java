package dataAccess;

import model.Product;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MySQL {
    private Connection connection;

    public MySQL(Connection conn) {
        this.connection = conn;
    }

    // Authenticate User
    // Authenticate User with Customer ID
    public User authenticateUser(String username, String password) {
        String query = "SELECT Users.ID, Users.Username, Users.Password, Users.CustomerID, " +
                "Users.DisplayName, Roles.Name AS RoleName " +
                "FROM Users " +
                "JOIN Roles ON Users.RoleID = Roles.ID " +
                "WHERE Users.Username = ? AND Users.Password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("ID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("CustomerID"),
                        rs.getString("DisplayName"),
                        rs.getString("RoleName")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Retrieve Product by ID
    public Product getProductById(int productId) {
        String query = "SELECT * FROM Products WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getDouble("SellPrice"),
                        rs.getInt("Stock"),
                        rs.getString("Category"),
                        rs.getString("Brand")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add Product to Stock
    public void addProduct(Product product) {
        String query = "INSERT INTO Products (Name, SellPrice, Stock, Category, Brand) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getSellPrice());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getCategory());
            stmt.setString(5, product.getBrand());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Product by ID
    public void deleteProduct(int productId) {
        String query = "DELETE FROM Products WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Product Stock
    public void updateProductStock(int productId, int quantity) {
        String query = "UPDATE Products SET Stock = Stock + ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modify Product Information
    public void modifyProduct(Product product) {
        String query = "UPDATE Products SET Name = ?, SellPrice = ?, Stock = ?, Category = ?, Brand = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getSellPrice());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setString(4, product.getCategory());
            stmt.setString(5, product.getBrand());
            stmt.setInt(6, product.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Filter Products based on criteria
    public List<Product> getFilteredProducts(String filterQuery) {
        List<Product> products = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(filterQuery)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getDouble("SellPrice"),
                        rs.getInt("Stock"),
                        rs.getString("Category"),
                        rs.getString("Brand")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Fetch Unique Values for Filtering
    public Vector<String> getUniqueValues(String columnName) {
        Vector<String> values = new Vector<>();
        String query = "SELECT DISTINCT " + columnName + " FROM Products";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                values.add(rs.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }

    // Check if stock is sufficient for a given product ID and required quantity
    public boolean isStockSufficient(int productId, int requiredQuantity) {
        String query = "SELECT Stock FROM Products WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int currentStock = rs.getInt("Stock");
                return currentStock >= requiredQuantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Reduce the stock for a given product ID after an order is placed
    public void reduceProductStock(int productId, int quantity) {
        String query = "UPDATE Products SET Stock = Stock - ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
