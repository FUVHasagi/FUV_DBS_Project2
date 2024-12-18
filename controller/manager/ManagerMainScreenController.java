package controller.manager;

import dataAccess.MongoDB;
import dataAccess.MySQL;
import dataAccess.Redis;
import model.User;
import view.manager.ManagerMainScreen;
import view.manager.ProductManagerView;
import view.manager.ManagerReportView;
import view.manager.ManagerOrderHistoryView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerMainScreenController implements ActionListener {
    private ManagerMainScreen view;
    private User manager;
    private MongoDB mongoDB;
    private MySQL mySQL;
    private Redis redis;

    public ManagerMainScreenController(User manager, MySQL mySQL, MongoDB mongoDB, Redis redis) {
        this.manager = manager;
        this.mySQL = mySQL;
        this.mongoDB = mongoDB;
        this.redis = redis;

        // Initialize the view
        this.view = new ManagerMainScreen();
        this.view.setVisible(true);

        // Add action listeners to buttons
        this.view.getStockManagementButton().addActionListener(this);
        this.view.getReportButton().addActionListener(this);
        this.view.getOrderManagementButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getStockManagementButton()) {
            openProductManagerView();
        } else if (e.getSource() == view.getReportButton()) {
            openManagerReportView();
        } else if (e.getSource() == view.getOrderManagementButton()) {
            openOrderManagementView();
        }
    }

    private void openProductManagerView() {
        new ProductManagerController(mySQL); // Assuming ProductManagerController exists
    }

    private void openManagerReportView() {
        new ManagerReportController(redis, mongoDB); // Assuming ManagerReportController exists
    }

    private void openOrderManagementView() {
        new ManagerOrderHistoryController(mongoDB, redis); // Assuming ManagerOrderHistoryController exists
    }
}
