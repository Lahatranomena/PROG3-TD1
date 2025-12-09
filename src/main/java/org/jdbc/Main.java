package org.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataRetriever dataRetriever = new DataRetriever();
        System.out.println(dataRetriever.getAllCategories());


        System.out.println(dataRetriever.getProductsByCriteria("Dell",null,null,null));
        System.out.println(dataRetriever.getProductsByCriteria(null,"informatique",null,null));
        System.out.println(dataRetriever.getProductsByCriteria("Iphone","mobile",null,null));
        System.out.println(dataRetriever.getProductsByCriteria(null,null,Instant.parse("2024-02-01T00:00:00Z"),null));
        System.out.println(dataRetriever.getProductsByCriteria(null,"informatique",null,null));
        System.out.println(dataRetriever.getProductsByCriteria(null,"informatique",null,null));
        System.out.println(dataRetriever.getProductsByCriteria(null,"informatique",null,null));
        System.out.println(dataRetriever.getProductsByCriteria(null,"informatique",null,null));


        try {
            System.out.println("Product contains phone");
            List<Product> listOne = dataRetriever.getProductsByCriteria(
                    "phone", null, null, null, 1, 10
            );
            for (Product p : listOne) {
                System.out.println(p);
            }
            System.out.println("\nProduct of categories 'Informatique'");
            List<Product> listTwo = dataRetriever.getProductsByCriteria(
                    null, "Informatique", null, null, 1, 10
            );
            for (Product p : listTwo) {
                System.out.println(p);
            }
            System.out.println("\nProduct created between '2024-03-01 et 2024-05-31'");
            Instant min = Instant.parse("2024-03-01T00:00:00Z");
            Instant max = Instant.parse("2024-05-31T23:59:59Z");

            List<Product> listThree = dataRetriever.getProductsByCriteria(
                    null, null, min, max, 1, 10
            );
            for (Product p : listThree) {
                System.out.println(p);
            }

            System.out.println("\nPage 2: size = 5, catégorie 'Informatique'");
            List<Product> listFour = dataRetriever.getProductsByCriteria(
                    null, "Informatique", null, null, 2, 5
            );
            for (Product p : listFour) {
                System.out.println(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}