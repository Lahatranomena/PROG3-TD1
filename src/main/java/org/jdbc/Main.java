package org.jdbc;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataRetriever dataRetriever = null;
        try {
            dataRetriever = new DataRetriever();
        } catch (SQLException e) {
            System.err.println("Error creating DataRetriever: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.println("=== Test of getAllCategories() ===");
        try {
            List<Category> allCategories = dataRetriever.getAllCategories();
            System.out.println("Number of categories" + allCategories.size());
            for (Category cat : allCategories) {
                System.out.println(cat);
            }
        } catch (SQLException e) {
            System.err.println("Error while getAllCategories: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== Test of getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax, int page, int size) ===");
        // Données exactes du tableau page 5 (d)
        Object[][] paginatedCriteriaTests = {
                {null, null, null, null, 1, 10},
                {"Dell", null, null, null, 1, 5},
                {null, "informatique", null, null, 1, 10}
        };
        String[] paginatedDescriptions = {
                "productName=null, categoryName=null, creationMin=null, creationMax=null, page=1, size=10",
                "productName=\"Dell\", categoryName=null, creationMin=null, creationMax=null, page=1, size=5",
                "productName=null, categoryName=\"informatique\", creationMin=null, creationMax=null, page=1, size=10"
        };
        for (int i = 0; i < paginatedCriteriaTests.length; i++) {
            String productName = (String) paginatedCriteriaTests[i][0];
            String categoryName = (String) paginatedCriteriaTests[i][1];
            Instant creationMin = (Instant) paginatedCriteriaTests[i][2];
            Instant creationMax = (Instant) paginatedCriteriaTests[i][3];
            int page = (Integer) paginatedCriteriaTests[i][4];
            int size = (Integer) paginatedCriteriaTests[i][5];
            System.out.println(paginatedDescriptions[i] + ":");
            try {
                List<Product> products = dataRetriever.getProductsByCriteria(productName, categoryName, creationMin, creationMax, page, size);
                System.out.println("Number of products: " + products.size());
                for (Product p : products) {
                    System.out.println(p);
                }
            } catch (SQLException e) {
                System.err.println("Error while getProductsByCriteria with pagination: " + productName + ", " + categoryName + ", " + creationMin + ", " + creationMax + ", " + page + ", " + size + "): " + e.getMessage());
                e.printStackTrace();
            }
            System.out.println();
        }
    }
}