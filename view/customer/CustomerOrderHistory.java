package view.customer;

import javax.swing.*;

public class CustomerOrderHistory extends JFrame {
    private JPanel panel;
    private JTable table;
    private JScrollPane jScrollPane1;
    private JButton inspectOrderButton;
    private JButton button2;

    public CustomerOrderHistory() {
        setTitle("Order Table View");
        setSize(800, 600);
        setLocationRelativeTo(null);
        initTable();
    }

    private void initTable() {

    }

}
