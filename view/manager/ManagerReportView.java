package view.manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManagerReportView extends JFrame {
    private JTable tableBestSellingProducts;
    private JTable tableBestSellingCategories;
    private JTable tableMostIncomeProducts;
    private JTable tableMostIncomeCustomers;
    private JButton refreshButton;

    public ManagerReportView() {
        setTitle("Manager Report");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Panels for each report category
        tabbedPane.addTab("Best Selling Products", createTablePanel("Product", "Total Quantity"));
        tabbedPane.addTab("Best Selling Categories", createTablePanel("Category", "Total Quantity"));
        tabbedPane.addTab("Most Income Products", createTablePanel("Product", "Total Income"));
        tabbedPane.addTab("Most Income Customers", createTablePanel("Customer", "Total Spent"));

        // Refresh button
        refreshButton = new JButton("Refresh");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);

        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createTablePanel(String labelHeader, String valueHeader) {
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable(new DefaultTableModel(new String[]{"Rank", labelHeader, valueHeader}, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Assign table to appropriate field
        if (labelHeader.equals("Product") && valueHeader.equals("Total Quantity")) {
            tableBestSellingProducts = table;
        } else if (labelHeader.equals("Category") && valueHeader.equals("Total Quantity")) {
            tableBestSellingCategories = table;
        } else if (labelHeader.equals("Product") && valueHeader.equals("Total Income")) {
            tableMostIncomeProducts = table;
        } else if (labelHeader.equals("Customer") && valueHeader.equals("Total Spent")) {
            tableMostIncomeCustomers = table;
        }

        return panel;
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

    public JButton getRefreshButton() {
        return refreshButton;
    }
}
