package org.jdbc;

import java.sql.*;
import java.time.Instant;
import java.util.*;

public class DataRetriever {
    private Connection connection;

    public DataRetriever() throws SQLException {
        connection = new DBConnection().getDBConnection();
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM product_category ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        }
        return categories;
    }


    public List<Product> getProductsByCriteria(String pName, String cName, Instant minDt, Instant maxDt) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT p.id, p.name, p.creation_datetime FROM product p " +
                        "LEFT JOIN product_category pc ON p.id = pc.product_id WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        if (pName != null && !pName.isEmpty()) {
            sql.append(" AND p.name ILIKE ?");
            params.add("%" + pName + "%");
        }
        sql.append(" ORDER BY p.id");
        return List.of();
    }

    public List<Product> getProductsByCriteria(String pName, String cName, Instant minDt, Instant maxDt, int page, int size) throws SQLException {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT p.id, p.name, p.creation_datetime FROM product p " +
                        "LEFT JOIN product_category pc ON p.id = pc.product_id WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        if (pName != null && !pName.isEmpty()) {
            sql.append(" AND p.name ILIKE ?");
            params.add("%" + pName + "%");
        }
        sql.append(" ORDER BY p.id LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);
        return List.of();
    }
}