package com.pravin.dblog4j.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectioManager {

    private Connection connection;

    public DBConnectioManager(String url, String name, String passw) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection(url, name, passw);
    }

    public Connection getConnection() {
        return connection;
    }

}
