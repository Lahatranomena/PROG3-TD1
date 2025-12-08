package org.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public String url = "jdbc:postgresql://localhost:5432/product_management_db";
    public String user = "product_manager_user";
    public String password = "123456";


    public Connection getDBConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Postgres JDBC Driver not found", e);
        }
        return DriverManager.getConnection(url, user, password);
    }
}
