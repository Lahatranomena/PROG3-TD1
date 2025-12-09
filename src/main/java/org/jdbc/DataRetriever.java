package org.jdbc;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private Connection connection;

    public DataRetriever() throws SQLException {
        connection = new DBConnection().getDBConnection();
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name FROM product_category ORDER BY id")) {
            while (rs.next()) categories.add(new Category(rs.getInt("id"), rs.getString("name")));
        }
        return categories;
    }

    public List<Product> getProductsByCriteria(String pName, String cName, Instant minDt, Instant maxDt) throws SQLException {
        return getProductsByCriteria(pName, cName, minDt, maxDt, 1, Integer.MAX_VALUE);
    }

    public List<Product> getProductsByCriteria(String pName, String cName, Instant minDt, Instant maxDt, int page, int size) throws SQLException {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;

        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT p.id, p.name, p.creation_datetime FROM product p " +
                        "LEFT JOIN product_category pc ON p.id = pc.product_id WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();
        if (pName != null && !pName.isEmpty()) {
            sql.append(" AND p.name ILIKE ?");
            params.add("%" + pName + "%");
        }
        if (cName != null && !cName.isEmpty()) {
            sql.append(" AND pc.name ILIKE ?");
            params.add("%" + cName + "%");
        }
        if (minDt != null) {
            sql.append(" AND p.creation_datetime >= ?");
            params.add(Timestamp.from(minDt));
        }
        if (maxDt != null) {
            sql.append(" AND p.creation_datetime <= ?");
            params.add(Timestamp.from(maxDt));
        }

        sql.append(" ORDER BY p.id LIMIT ? OFFSET ?");
        params.add(size); params.add(offset);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) p);
                }
                else if (p instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) p);
                }
                else stmt.setObject(i + 1, p);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("creation_datetime");
                }
            }
        }

        return products;
    }
}
