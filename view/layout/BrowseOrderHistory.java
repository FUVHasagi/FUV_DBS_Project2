package view.layout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class BrowseOrderHistory extends JPanel {

    private JTable OrderHistory;
    private JPanel panel1;
    private DefaultTableModel tableModel;
    private String[] columnNames= {"OrderID", "OrderDate", "OrderStatus", "TotalMoney"};

    public BrowseOrderHistory() {
        createTable();
    }

    private void createTable() {
        this.tableModel = (DefaultTableModel) OrderHistory.getModel();

    }

    public void setTableData(List<Object[]> data) {
        // Clear previous data
        tableModel.setRowCount(0);

        // Add new data to the table
        for (Object[] row : data) {
            tableModel.addRow(row);
        }

        this.setVisible(true);
    }
}
