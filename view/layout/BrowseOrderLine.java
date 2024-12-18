package view.layout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BrowseOrderLine extends JPanel {
    private JTable orderLineTable;

    public BrowseOrderLine() {
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        orderLineTable = new JTable(new DefaultTableModel(
                new Object[]{"Product ID", "Product Name", "Quantity", "Price", "Total Cost"}, 0
        ));
        orderLineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(orderLineTable);

        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getOrderLineTable() {
        return orderLineTable;
    }

    public void populateTable(Object[][] data) {
        DefaultTableModel tableModel = (DefaultTableModel) orderLineTable.getModel();
        tableModel.setRowCount(0); // Clear existing rows
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
}
