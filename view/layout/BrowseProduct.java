package view.layout;

import dataAccess.MySQL;
import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class BrowseProduct extends JPanel {
    private JTable productTable;
    private JComboBox<String> comboBoxCategory;
    private JComboBox<String> comboBoxBrand;
    private JComboBox<String> comboBoxSortedBy;
    private JLabel brandField;
    private JLabel categoryField;
    private JLabel upperPriceField;
    private JLabel lowerPriceField;
    private JTextField textFieldMinPrice;
    private JTextField textFieldMaxPrice;
    private JButton applyFilterButton;
    private JButton clearFilterButton;
    private DefaultTableModel tableModel;
    private Product selectedProduct;
    private MySQL mySQL; // Add MySQL data access object

    public BrowseProduct(MySQL mySQL){
        this.mySQL = mySQL;
        initUI();
    }


    private void populateComboBoxes() {
        try {
            Vector<String> categories = mySQL.getUniqueValues("Category");
            for (String category : categories) {
                comboBoxCategory.addItem(category);
            }

            // Similar for Brand and SortedBy (if needed)
            Vector<String> brands = mySQL.getUniqueValues("Brand");  // Assuming you have a "Brand" column
            for (String brand : brands) {
                comboBoxBrand.addItem(brand);
            }

            comboBoxSortedBy.addItem("ID");
            comboBoxSortedBy.addItem("Name");
            comboBoxSortedBy.addItem("SellPrice");
            // ... Add other sorting options


        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (e.g., display error message)
        }

    }




    private void updateProductTable() {
        String filterQuery = "SELECT * FROM Products";  // Basic query

        String category = (String) comboBoxCategory.getSelectedItem();
        String brand = (String) comboBoxBrand.getSelectedItem();
        String minPrice = textFieldMinPrice.getText();
        String maxPrice = textFieldMaxPrice.getText();


        String whereClause = "";



        if (category != null && !category.isEmpty()) {
            whereClause += " Category = '" + category + "'";
        }

        if (brand != null && !brand.isEmpty()) {
            if(!whereClause.isEmpty()) {
                whereClause += " AND ";
            }
            whereClause += " Brand = '" + brand + "'";
        }

        if (!minPrice.isEmpty()) {
            if(!whereClause.isEmpty()) {
                whereClause += " AND ";
            }
            whereClause += " SellPrice >= " + minPrice;
        }
        if (!maxPrice.isEmpty()) {
            if(!whereClause.isEmpty()) {
                whereClause += " AND ";
            }
            whereClause += " SellPrice <= " + maxPrice;
        }




        if (!whereClause.isEmpty()){
            filterQuery += " WHERE " + whereClause;
        }


        String sortBy = (String) comboBoxSortedBy.getSelectedItem();
        if (sortBy != null && !sortBy.isEmpty()){
            filterQuery += " ORDER BY " + sortBy;
        }




        List<Product> products = mySQL.getFilteredProducts(filterQuery);



        tableModel.setRowCount(0); // Clear existing data
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                    product.getId(), product.getName(), product.getStockQuantity(), product.getSellPrice(), product.getCategory()
                    // ... other product attributes
            });
        }
    }


    public void initUI() {
        this.setLayout(new BorderLayout()); // Example layout

        // Create components
        productTable = new JTable();
        comboBoxCategory = new JComboBox<>();
        comboBoxBrand = new JComboBox<>();
        comboBoxSortedBy = new JComboBox<>();
        brandField = new JLabel("Brand:");
        categoryField = new JLabel("Category:");
        upperPriceField = new JLabel("Max Price:");
        lowerPriceField = new JLabel("Min Price:");
        textFieldMinPrice = new JTextField();
        textFieldMaxPrice = new JTextField();
        applyFilterButton = new JButton("Apply Filter");
        clearFilterButton = new JButton("Clear Filter");


        // Set up table model
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Stock", "Sell Price", "Category"}, 0);
        productTable.setModel(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Populate JComboBox models (example)
        populateComboBoxes();


        // Add action listeners
        applyFilterButton.addActionListener(e -> updateProductTable());
        clearFilterButton.addActionListener(e -> {
            comboBoxCategory.setSelectedItem(null);
            comboBoxBrand.setSelectedItem(null);
            comboBoxSortedBy.setSelectedItem(null);
            textFieldMinPrice.setText("");
            textFieldMaxPrice.setText("");
            updateProductTable();
        });

        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    int productId = (int) productTable.getValueAt(selectedRow, 0);
                    selectedProduct = mySQL.getProductById(productId); // Use mySQL to fetch product
                    // ... (Do something with selectedProduct) ...
                    System.out.println("Selected Product: " + selectedProduct);
                }
            }
        });

        // Add components to panel (example using GridBagLayout - adjust as needed)
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding


        gbc.gridx = 0; gbc.gridy = 0; filterPanel.add(categoryField, gbc);
        gbc.gridx = 1; gbc.gridy = 0; filterPanel.add(comboBoxCategory, gbc);

        gbc.gridx = 0; gbc.gridy = 1; filterPanel.add(brandField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; filterPanel.add(comboBoxBrand, gbc);

        gbc.gridx = 0; gbc.gridy = 2; filterPanel.add(lowerPriceField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; filterPanel.add(textFieldMinPrice, gbc);

        gbc.gridx = 0; gbc.gridy = 3; filterPanel.add(upperPriceField, gbc);
        gbc.gridx = 1; gbc.gridy = 3; filterPanel.add(textFieldMaxPrice, gbc);

        gbc.gridx = 2; gbc.gridy = 0; filterPanel.add(new JLabel("Sort By:"), gbc); // Sort by label
        gbc.gridx = 3; gbc.gridy = 0; filterPanel.add(comboBoxSortedBy, gbc);


        // ... (Add other filter components to the filterPanel) ...


        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(applyFilterButton);
        buttonPanel.add(clearFilterButton);

        this.add(filterPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(productTable), BorderLayout.CENTER); // Add table in a scroll pane
        this.add(buttonPanel, BorderLayout.SOUTH);



        updateProductTable(); // Initial table population
    }

    public Product getSelectedProduct() {
        return this.selectedProduct;
    }



}