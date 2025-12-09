package org.jdbc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private Instant creationDatetime;
    private List<Category> categories;

    public Product(int id, String name, Instant creationDatetime, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.creationDatetime = creationDatetime;
        this.categories = (categories != null) ? categories : new ArrayList<>();
    }

    public Product(int id, String name, Instant creationDatetime, Category category) {
        this(id, name, creationDatetime,
                (category != null) ? List.of(category) : null);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getCategoryNames() {
        List<String> names = new ArrayList<>();
        for (Category c : categories) {
            names.add(c.getName());
        }
        return String.join(", ", names);
    }

    @Override
    public String toString() {
        return "Product{id=" + id + "\nName='" + name +
                "\nCreation=" + creationDatetime +
                "\nCategories=[" + getCategoryNames() + "]}";
    }
}
