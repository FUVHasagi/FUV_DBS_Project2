package view.customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CustomerMainScreen extends JFrame {
    private JButton productViewButton = new JButton("Product View");
    private JButton cartButton = new JButton("Cart");
    private JButton orderHistoryButton = new JButton("Order History");

    public CustomerMainScreen() {
        setTitle("Customer Dashboard");
//        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        initializeComponents();
    }

    private void initializeComponents() {
        JLabel title = new JLabel("Customer Dashboard");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
        this.getContentPane().add(panelTitle);

        JPanel panelButtons = new JPanel();
        panelButtons.add(productViewButton);
        panelButtons.add(cartButton);
        panelButtons.add(orderHistoryButton);
        this.getContentPane().add(panelButtons);
    }

    public JButton getProductViewButton() {
        return productViewButton;
    }

    public JButton getCartButton() {
        return cartButton;
    }

    public JButton getOrderHistoryButton() {
        return orderHistoryButton;
    }

    public void addActionListener(ActionListener actionListener) {
        productViewButton.addActionListener(actionListener);
        cartButton.addActionListener(actionListener);
        orderHistoryButton.addActionListener(actionListener);
    }
}
