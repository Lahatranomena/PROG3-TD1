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

    private Map<Integer, List<Category>> getMapCategory(List<Integer> IdProduct) throws SQLException {
        Map<Integer, List<Category>> map = new HashMap<>();
        if (IdProduct.isEmpty()) return map;
        String clause = "(" + String.join(",", Collections.nCopies(IdProduct.size(), "?")) + ")";
        String sql = "SELECT id, name, product_id FROM product_category WHERE product_id IN " + clause;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < IdProduct.size(); i++) {
                ps.setInt(i + 1, IdProduct.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int pid = rs.getInt("product_id");
                    Category c = new Category(rs.getInt("id"), rs.getString("name"));
                    map.computeIfAbsent(pid, k -> new ArrayList<>()).add(c);
                }
            }
        }
        return map;
    }

    private List<Product> fetchProds(String sql, List<?> params) throws SQLException {
        List<Product> prods = new ArrayList<>();
        List<Integer> Idproduct = new ArrayList<>();
        Map<Integer, Product> temp = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof String) ps.setString(i + 1, (String) p);
                else if (p instanceof Timestamp) ps.setTimestamp(i + 1, (Timestamp) p);
                else if (p instanceof Integer) ps.setInt(i + 1, (Integer) p);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Instant dt = rs.getTimestamp("creation_datetime") != null ?
                            rs.getTimestamp("creation_datetime").toInstant() : null;
                    temp.put(id, new Product(id, name, dt, new ArrayList<>()));
                    Idproduct.add(id);
                }
            }
        }
        if (!Idproduct.isEmpty()) {
            Map<Integer, List<Category>> categories = getMapCategory(Idproduct);
            for (Map.Entry<Integer, Product> e : temp.entrySet()) {
                int id = e.getKey();
                Product p = e.getValue();
                List<Category> cs = categories.getOrDefault(id, new ArrayList<>());
                prods.add(new Product(p.getId(), p.getName(), p.getCreationDatetime(), cs));
            }
        }
        return prods;
    }

    public List<Product> getProductList(int page, int size) throws SQLException {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        String sql = "SELECT id, name, creation_datetime FROM product ORDER BY id LIMIT ? OFFSET ?";
        return fetchProds(sql, Arrays.asList(size, offset));
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
        sql.append(" ORDER BY p.id");
        return fetchProds(sql.toString(), params);
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
        params.add(size);
        params.add(offset);
        return fetchProds(sql.toString(), params);
    }
}