package com.example.asm3.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Book implements Serializable {
    private String name;
    private String author;
    private String description;
    private float price;
    private int quantity;
    private String publishedAt;
    private String category;
    private String subCategory;
    private String customer;
    private String _id;

    public static String idKey="_id";
    public static String nameKey="name";
    public static String authorKey="author";
    public static String descriptionKey="description";
    public static String priceKey="price";
    public static String quantityKey="quantity";
    public static String publishedAtKey="publishedAt";
    public static String categoryKey="category";
    public static String subCategoryKey="subCategory";
    public static String customerKey="customer";


    public Book(String name, String author, String description, float price, int quantity, String publishedAt, String category, String subCategory, String customer, String _id) {
        this._id = _id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.publishedAt = publishedAt;
        this.category = category;
        this.subCategory = subCategory;
        this.customer = customer;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public static Book fromJSON(JSONObject jsonObject) {
        Book book = null;
        String name;
        String author;
        String description;
        float price;
        int quantity;
        String publishedAt;
        String category;
        String subCategory;
        String customer;
        String _id;
        if (jsonObject != null) {
            try {
                name = jsonObject.getString(nameKey);
                author = jsonObject.getString(authorKey);
                description = jsonObject.getString(descriptionKey);
                price = (float) jsonObject.getDouble(priceKey);
                quantity = jsonObject.getInt(quantityKey);
                publishedAt = jsonObject.getString(publishedAtKey);
                category = jsonObject.getString(categoryKey);
                subCategory = jsonObject.getString(subCategoryKey);
                customer = jsonObject.getString(customerKey);
                _id = jsonObject.getString(idKey);

                book = new Book(name,author,description,price,quantity,publishedAt,category,subCategory,customer,_id);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return book;
    }

    public static JSONObject toJSON(Book book) {
        JSONObject jsonObject = null;
        if (book != null) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put(nameKey, book.name);
                jsonObject.put(idKey, book._id);
                jsonObject.put(authorKey, book.author);
                jsonObject.put(descriptionKey, book.description);
                jsonObject.put(priceKey, book.price);
                jsonObject.put(quantityKey, book.quantity);
                jsonObject.put(publishedAtKey, book.publishedAt);
                jsonObject.put(categoryKey, book.category);
                jsonObject.put(subCategoryKey, book.subCategory);
                jsonObject.put(customerKey, book.customer);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }

}
