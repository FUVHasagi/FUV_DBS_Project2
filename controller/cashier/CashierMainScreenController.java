package controller.cashier;

import dataAccess.MongoDB;
import dataAccess.MySQL;
import dataAccess.Redis;
import model.User;
import view.cashier.CashierMainScreen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CashierMainScreenController implements ActionListener {
    private CashierMainScreen view;
    private User cashier;
    private MongoDB mongoDB;
    private MySQL mySQL;
    private Redis redis;

    public CashierMainScreenController(User cashier,  MySQL mySQL, MongoDB mongoDB, Redis redis) {
        this.cashier = cashier;
        this.mongoDB = mongoDB;
        this.mySQL = mySQL;
        this.redis = redis;

        // Initialize the view
        this.view = new CashierMainScreen();
        this.view.setVisible(true);

        // Add action listeners to buttons
        this.view.getProductBrowseButton().addActionListener(this);
        this.view.getNewOrderButton().addActionListener(this);
        this.view.getOrderHistoryButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getProductBrowseButton()) {
            new CashierProductCheckController(cashier, mySQL, mongoDB); // Open product check
        } else if (e.getSource() == view.getNewOrderButton()) {
            new CashierOrderController(cashier, mySQL, mongoDB); // Open new order creation
        } else if (e.getSource() == view.getOrderHistoryButton()) {
            new CashierOrderHistoryController(cashier, mongoDB); // Open order history
        }
    }
}
