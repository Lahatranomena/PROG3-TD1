import org.jdbc.Category;
import org.jdbc.DataRetriever;
import org.jdbc.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataRetrieverTest {

    private static DataRetriever dataRetriever;

    @BeforeAll
    static void setup() throws SQLException {
        dataRetriever = new DataRetriever();
    }
    @Test
    void testGetAllCategories() throws SQLException {
        List<Category> categories = dataRetriever.getAllCategories();
        assertNotNull(categories, "The list of categories can not null");
        System.out.println("All categories");
        categories.forEach(System.out::println);
    }

    @Test
    void testGetProductListPagination() throws SQLException {
        int[][] tests = {{1,10}, {1,5}, {1,3}, {2,2}};
        for (int[] test : tests) {
            int page = test[0];
            int size = test[1];
            List<Product> products = dataRetriever.getProductList(page, size);

            assertNotNull(products, "The list of products can not null");
            System.out.println("Page " + page + ", size " + size + " ===");
            products.forEach(System.out::println);
        }
    }

    @Test
    void testGetProductsByCriteriaNoPagination() throws SQLException {
        Object[][] tests = {
                {"Dell", null, null, null},
                {null, "informatique", null, null},
                {"Iphone", "mobile", null, null},
                {"Samsung", "bureau", null, null},
                {"sony", "informatique", null, null},
                {null, "audio", null, null},
                {null, null, null, null}
        };

        for (Object[] test : tests) {
            String productName = (String) test[0];
            String categoryName = (String) test[1];
            List<Product> results = dataRetriever.getProductsByCriteria(productName, categoryName, null, null);

            assertNotNull(results, "The  list of products can not null");
            System.out.println(": product=" + productName + ", category=" + categoryName );
            results.forEach(System.out::println);
        }
    }

    @Test
    void testGetProductsByCriteriaWithPagination() throws SQLException {
        Object[][] tests = {
                {null, null, null, null, 1, 10},
                {"Dell", null, null, null, 1, 5},
                {null, "informatique", null, null, 1, 5}
        };

        for (Object[] test : tests) {
            String productName = (String) test[0];
            String categoryName = (String) test[1];
            int page = (int) test[4];
            int size = (int) test[5];

            List<Product> results = dataRetriever.getProductsByCriteria(
                    productName, categoryName, null, null, page, size
            );

            assertNotNull(results, "The list of products can not null");
            System.out.println("Test criteria + pagination : product=" + productName + "\nCategory=" + categoryName +
                    "\nPage=" + page + "\nSize=" + size);
            results.forEach(System.out::println);
        }
    }


}
