package view.layout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class BrowseProduct extends JPanel {
    private JTable table1;
    private JComboBox comboBoxCategory;
    private JComboBox comboBoxBrand;
    private JComboBox comboBoxSortedBy;
    private JLabel brandField;
    private JLabel categoryField;
    private JLabel upperPriceField;
    private JPanel lowerPriceField;
    private JTextField textField1;
    private JTextField textField2;
    private JButton applyFilterButton;
    private JButton clearFilterButton;
    private DefaultTableModel tableModel;
    private void createTable(){}

    private void updateProductTable(){}

    public BrowseProduct(){
        initUI();
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
                    selectedProduct = getProductById(productId);
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

        // ... (Add other filter components to the filterPanel) ...


        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(applyFilterButton);
        buttonPanel.add(clearFilterButton);

        this.add(filterPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(productTable), BorderLayout.CENTER); // Add table in a scroll pane
        this.add(buttonPanel, BorderLayout.SOUTH);



        updateProductTable(); // Initial table population
    }



}
