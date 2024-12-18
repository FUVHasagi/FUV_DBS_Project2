package controller.manager;

import dataAccess.MongoDB;
import dataAccess.MySQL;
import dataAccess.Redis;
import model.User;

public class ManagerMainScreenController {
    private User user;
    private MySQL mySQL;
    private Redis redis;
    private MongoDB mongoDB;

    public ManagerMainScreenController(User user, MySQL mySQL, MongoDB mongoDB, Redis redis) {
        this.user = user;
        this.mySQL = mySQL;
        this.redis = redis;
        this.mongoDB = mongoDB;
    }
}
