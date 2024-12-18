package view.manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManagerReportView extends JFrame {
    private JTable tableBestSellingProducts;
    private JTable tableBestSellingCategories;
    private JTable tableMostIncomeProducts;
    private JTable tableMostIncomeCustomers;
    private JButton backButton;

    public ManagerReportView() {
        setTitle("Manager Report");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize tables
        tableBestSellingProducts = createTable("Best Selling Products");
        tableBestSellingCategories = createTable("Best Selling Categories");
        tableMostIncomeProducts = createTable("Most Income Products");
        tableMostIncomeCustomers = createTable("Most Income Customers");

        // Create panels
        JPanel tablePanel = new JPanel(new GridLayout(4, 1, 10, 10));
        tablePanel.add(new JScrollPane(tableBestSellingProducts));
        tablePanel.add(new JScrollPane(tableBestSellingCategories));
        tablePanel.add(new JScrollPane(tableMostIncomeProducts));
        tablePanel.add(new JScrollPane(tableMostIncomeCustomers));

        // Back button
        backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);

        // Add components to frame
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JTable createTable(String title) {
        JTable table = new JTable(new DefaultTableModel(new Object[]{"Rank", title, "Value"}, 0));
        table.setEnabled(false);
        return table;
    }

    public JTable getTableBestSellingProducts() {
        return tableBestSellingProducts;
    }

    public JTable getTableBestSellingCategories() {
        return tableBestSellingCategories;
    }

    public JTable getTableMostIncomeProducts() {
        return tableMostIncomeProducts;
    }

    public JTable getTableMostIncomeCustomers() {
        return tableMostIncomeCustomers;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
