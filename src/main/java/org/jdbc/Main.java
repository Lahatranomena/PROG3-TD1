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
        System.out.println();

        System.out.println("=== Test of getProductList(int page, int size) ===");
        int[][] paginationTests = {{1, 10}, {1, 5}, {1, 3}, {2, 2}};
        for (int[] test : paginationTests) {
            int page = test[0];
            int size = test[1];
            System.out.println("page=" + page + ", size=" + size + ":");
            try {
                List<Product> products = dataRetriever.getProductList(page, size);
                System.out.println("Number of products: " + products.size());
                for (Product p : products) {
                    System.out.println(p);
                }
            } catch (SQLException e) {
                System.err.println("Error while getProduct(" + page + ", " + size + "): " + e.getMessage());
                e.printStackTrace();
            }
            System.out.println();
        }

        System.out.println("=== Test of getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax) ===");
        // Données exactes du tableau page 4-5 (c)
        Object[][] criteriaTests = {
                {"Dell", null, null, null},
                {null, "info", null, null},
                {"iPhone", "mobile", null, null},
                {null, null, LocalDate.parse("2024-02-01"), LocalDate.parse("2024-03-01")},
                {"Samsung", "bureau", null, null},
                {"Sony", "informatique", null, null},
                {null, "audio", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-01")},
                {null, null, null, null}
        };
        String[] descriptions = {
                "productName=\"Dell\", categoryName=null, creationMin=null, creationMax=null",
                "productName=null, categoryName=\"info\", creationMin=null, creationMax=null",
                "productName=\"iPhone\", categoryName=\"mobile\", creationMin=null, creationMax=null",
                "productName=null, categoryName=null, creationMin=2024-02-01, creationMax=2024-03-01",
                "productName=\"Samsung\", categoryName=\"bureau\", creationMin=null, creationMax=null",
                "productName=\"Sony\", categoryName=\"informatique\", creationMin=null, creationMax=null",
                "productName=null, categoryName=\"audio\", creationMin=2024-01-01, creationMax=2024-12-01",
                "productName=null, categoryName=null, creationMin=null, creationMax=null"
        };
        for (int i = 0; i < criteriaTests.length; i++) {
            String productName = (String) criteriaTests[i][0];
            String categoryName = (String) criteriaTests[i][1];
            Instant creationMin = null;
            Instant creationMax = null;
            if (criteriaTests[i][2] != null) {
                LocalDate minDate = (LocalDate) criteriaTests[i][2];
                creationMin = minDate.atStartOfDay(ZoneOffset.UTC).toInstant();
            }
            if (criteriaTests[i][3] != null) {
                LocalDate maxDate = (LocalDate) criteriaTests[i][3];
                creationMax = maxDate.atStartOfDay(ZoneOffset.UTC).toInstant();
            }
            System.out.println(descriptions[i] + ":");
            try {
                List<Product> products = dataRetriever.getProductsByCriteria(productName, categoryName, creationMin, creationMax);
                System.out.println("Nombre de produits: " + products.size());
                for (Product p : products) {
                    System.out.println(p);
                }
            } catch (SQLException e) {
                System.err.println("Error while getProductsByCriteria(" + productName + ", " + categoryName + ", " + creationMin + ", " + creationMax + "): " + e.getMessage());
                e.printStackTrace();
            }
            System.out.println();
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