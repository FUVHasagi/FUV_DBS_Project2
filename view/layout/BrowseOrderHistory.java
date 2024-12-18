package view.layout;

import model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BrowseOrderHistory extends JPanel {

    private JTable orderHistoryTable;
    private DefaultTableModel tableModel;
    private String[] columnNames = {"Order ID", "Date", "Total Cost", "Source Type"};

    public BrowseOrderHistory() {
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        // Initialize table model and table
        tableModel = new DefaultTableModel(columnNames, 0);
        orderHistoryTable = new JTable(tableModel);
        orderHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(orderHistoryTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Method to populate the table with order data
    public void setTableData(List<Order> orders) {
        tableModel.setRowCount(0); // Clear existing rows

        for (Order order : orders) {
            tableModel.addRow(new Object[]{
                    order.getOrderID(),
                    order.getOrderDate(),
                    order.getTotalCost(),
                    order.getSourceType()
            });
        }
    }

    // Get the selected order ID
    public String getSelectedOrderID() {
        int selectedRow = orderHistoryTable.getSelectedRow();
        if (selectedRow != -1) {
            return (String) tableModel.getValueAt(selectedRow, 0);
        }
        return null;
    }
}