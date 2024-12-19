package view.layout;

import dataAccess.MySQL;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private Product selectedProduct;
    private DefaultTableModel tableModel;
    private MySQL mySQL;

    public BrowseProduct(MySQL mySQL) {
        this.mySQL = mySQL;
        initUI();
    }

    private void populateComboBoxes() {
        try {
            Vector<String> categories = mySQL.getUniqueValues("Category");
            comboBoxCategory.addItem(""); // Add empty value for no filter
            for (String category : categories) {
                comboBoxCategory.addItem(category);
            }

            Vector<String> brands = mySQL.getUniqueValues("Brand");
            comboBoxBrand.addItem(""); // Add empty value for no filter
            for (String brand : brands) {
                comboBoxBrand.addItem(brand);
            }

            comboBoxSortedBy.addItem(""); // Add empty value for no sorting
            comboBoxSortedBy.addItem("ID");
            comboBoxSortedBy.addItem("Name");
            comboBoxSortedBy.addItem("SellPrice ascending");
            comboBoxSortedBy.addItem("SellPrice descending");

            // No item selected by default
            comboBoxCategory.setSelectedIndex(-1);
            comboBoxBrand.setSelectedIndex(-1);
            comboBoxSortedBy.setSelectedIndex(-1);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading combo box values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateProductTable() {
        String filterQuery = "SELECT * FROM Products";

        String category = (String) comboBoxCategory.getSelectedItem();
        String brand = (String) comboBoxBrand.getSelectedItem();
        String minPrice = textFieldMinPrice.getText();
        String maxPrice = textFieldMaxPrice.getText();

        StringBuilder whereClause = new StringBuilder();

        if (category != null && !category.isEmpty()) {
            whereClause.append("Category = '").append(category).append("'");
        }

        if (brand != null && !brand.isEmpty()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append("Brand = '").append(brand).append("'");
        }

        if (!minPrice.isEmpty()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append("SellPrice >= ").append(minPrice);
        }

        if (!maxPrice.isEmpty()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append("SellPrice <= ").append(maxPrice);
        }

        if (whereClause.length() > 0) {
            filterQuery += " WHERE " + whereClause;
        }

        String sortBy = (String) comboBoxSortedBy.getSelectedItem();
        if (sortBy != null && !sortBy.isEmpty()) {
            filterQuery += " ORDER BY " + sortBy;
        }

        try {
            List<Product> products = mySQL.getFilteredProducts(filterQuery);
            tableModel.setRowCount(0);

            for (Product product : products) {
                tableModel.addRow(new Object[]{
                        product.getId(),
                        product.getName(),
                        product.getStockQuantity(),
                        product.getSellPrice(),
                        product.getCategory()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating product table.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initUI() {
        this.setLayout(new BorderLayout());

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

        // Set fixed size for text fields
        Dimension textFieldSize = new Dimension(100, 20);
        textFieldMinPrice.setPreferredSize(textFieldSize);
        textFieldMaxPrice.setPreferredSize(textFieldSize);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Stock", "Sell Price", "Category"}, 0);
        productTable.setModel(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        populateComboBoxes();

        applyFilterButton.addActionListener(e -> updateProductTable());
        clearFilterButton.addActionListener(e -> {
            comboBoxCategory.setSelectedIndex(-1);
            comboBoxBrand.setSelectedIndex(-1);
            comboBoxSortedBy.setSelectedIndex(-1);
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
                    selectedProduct = mySQL.getProductById(productId);
                    System.out.println("Selected Product: " + selectedProduct);
                }
            }
        });

        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(categoryField, gbc);
        gbc.gridx = 1;
        filterPanel.add(comboBoxCategory, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        filterPanel.add(brandField, gbc);
        gbc.gridx = 1;
        filterPanel.add(comboBoxBrand, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        filterPanel.add(lowerPriceField, gbc);
        gbc.gridx = 1;
        filterPanel.add(textFieldMinPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        filterPanel.add(upperPriceField, gbc);
        gbc.gridx = 1;
        filterPanel.add(textFieldMaxPrice, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Sort By:"), gbc);
        gbc.gridx = 3;
        filterPanel.add(comboBoxSortedBy, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(applyFilterButton);
        buttonPanel.add(clearFilterButton);

        this.add(filterPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(productTable), BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        updateProductTable();
    }

    public Product getSelectedProduct() {
        return this.selectedProduct;
    }
}
