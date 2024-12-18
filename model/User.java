package model;

public class User {
    private int id;
    private String username;
    private String password;
    private String customerId;
    private String displayName;
    private String role;
    private Customer customer;

    // Constructor for Full User Initialization
    public User(int id, String username, String password, String customerId, String displayName, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.customerId = customerId;
        this.displayName = displayName;
        this.role = role;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}
