package view.customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderHistory extends JFrame {
    private JPanel panel;
    private JTable table;
    private JScrollPane jScrollPane1;
    private JButton inspectOrderButton;
    private JButton button2;

    public OrderHistory() {
        setTitle("Order Table View");
        setSize(800, 600);
        setLocationRelativeTo(null);
        initTable();
    }

    private void initTable() {

    }

}
