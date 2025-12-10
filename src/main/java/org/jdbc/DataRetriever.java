package org.jdbc;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private Connection connection;
    DBConnection dbc = new DBConnection();

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

    public List<Product> getProductList(int page, int size) throws SQLException {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * size;
        String query = """
                SELECT p.id as id_product, p.name as product_name, p.price as Price, p.creation_datetime as Creation_Datetime, c.id AS category_id,
                c.name AS category_name FROM Product as p
                left join product_category as c
                on p.id=c.id
                LIMIT ? OFFSET ?
                """;

        try (Connection connection = dbc.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, size);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Category category = null;
                if (resultSet.getInt("category_id") != 0) {
                    category = new Category(
                            resultSet.getInt("category_id"),
                            resultSet.getString("category_name")
                    );
                }
                products.add(new Product(resultSet.getInt("id_product"),
                        resultSet.getString("product_name"),
                        resultSet.getTimestamp("creation_datetime").toInstant(),
                        category));
            }
        }
        return products;
    }

    public List<Product> getProductsByCriteria(
            String productName,
            String categoryName,
            Instant creationMin,
            Instant creationMax
    ) throws SQLException {

        List<Product> products = new ArrayList<>();
        DBConnection dbc = new DBConnection();

        StringBuilder query = new StringBuilder("""
        SELECT 
            p.id AS product_id,
            p.name AS product_name,
            p.price AS price,
            p.creation_datetime AS creation_datetime,
            c.id AS category_id,
            c.name AS category_name
        FROM product p
        LEFT JOIN product_category c ON c.product_id = p.id
        WHERE 1=1
    """);

        List<Object> parameters = new ArrayList<>();


        if (productName != null && !productName.isEmpty()) {
            query.append(" AND p.name ILIKE ? ");
            parameters.add("%" + productName + "%");
        }

        if (categoryName != null && !categoryName.isEmpty()) {
            query.append(" AND c.name ILIKE ? ");
            parameters.add("%" + categoryName + "%");
        }

        if (creationMin != null) {
            query.append(" AND p.creation_datetime >= ? ");
            parameters.add(Timestamp.from(creationMin));
        }

        if (creationMax != null) {
            query.append(" AND p.creation_datetime <= ? ");
            parameters.add(Timestamp.from(creationMax));
        }

        query.append(" ORDER BY p.id ASC ");

        try (Connection connection = dbc.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Category category = null;
                if (resultSet.getInt("category_id") != 0) {
                    category = new Category(
                            resultSet.getInt("category_id"),
                            resultSet.getString("category_name")
                    );
                }
                products.add(new Product(resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getTimestamp("creation_datetime").toInstant(),
                        category));
            }
        }
        return products;
    }

    public List<Product> getProductsByCriteria(String pName, String cName, Instant minDt, Instant maxDt, int page, int size)
            throws SQLException {
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
