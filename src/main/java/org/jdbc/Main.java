package org.jdbc;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args)
            throws SQLException {
        DataRetriever dataRetriever = new DataRetriever();

        printProducts("  List product page 1,10 ", dataRetriever.getProductList(1,10));
        printProducts(" List product page 1,5 ", dataRetriever.getProductList(1,5));
        printProducts(" List product page 1,3", dataRetriever.getProductList(1,3));
        printProducts("List product page 2,2", dataRetriever.getProductList(2,2));

        printProducts("List product Dell", dataRetriever.getProductsByCriteria("Dell", null, null, null));
        printProducts("List product Informatique", dataRetriever.getProductsByCriteria(null, "informatique", null, null));
        printProducts("List product Iphone Mobile ", dataRetriever.getProductsByCriteria("Iphone", "mobile", null, null));
        printProducts("List product Samsung Bureau", dataRetriever.getProductsByCriteria("Samsung", "bureau", null, null));
        printProducts("List product Sony Informatique ", dataRetriever.getProductsByCriteria("sony", "informatique", null, null));
        printProducts("List product category Audio ", dataRetriever.getProductsByCriteria(null, "audio", null, null));
        printProducts("All List product", dataRetriever.getProductsByCriteria(null, null, null, null));

        printProducts(" List product (page 1, size 10) ",
                dataRetriever.getProductsByCriteria(null, null, null, null, 1, 10));

        printProducts("List product Dell (page 1, size 5) ",
                dataRetriever.getProductsByCriteria("Dell", null, null, null, 1, 5));

        printProducts("List product category Informatique (page 1, size 5)",
                dataRetriever.getProductsByCriteria(null, "informatique", null, null, 1, 5));
    }

    private static void printProducts(String title, List<Product> products) {
        System.out.println(title);
        if (products == null || products.isEmpty()) {
            System.out.println("No product found");
        } else {
            for (Product p : products) {
                System.out.println(p);
            }
        }
        System.out.println();
    }
}
