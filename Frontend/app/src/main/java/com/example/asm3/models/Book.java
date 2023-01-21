package com.example.asm3.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    private String name;
    private String author;
    private String description;
    private float price;
    private int quantity;
    private String publishedAt;
    private Category category;
    private ArrayList<SubCategory> subCategory;
    private Customer customer;
    private String _id;
    private boolean isNewProduct;
    private String image;
    private boolean isCategoryPopulated = false;
    private boolean isSubCategoryPopulated = false;
    private boolean isCustomerPopulated = false;


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
    public static String isNewProductKey="isNewProduct";
    public static String imageKey="image";


    public Book(String name, String author, String description, float price, int quantity, String publishedAt, Category category, ArrayList<SubCategory> subCategory, Customer customer, String _id, boolean isNewProduct, String image) {
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
        this.isNewProduct = isNewProduct;
        this.image = image;
    }

    public Book() {
        this._id = "";
        this.name = "";
        this.author = "";
        this.description = "";
        this.price = 0F;
        this.quantity = 0;
        this.publishedAt = "";
        this.category = null;
        this.subCategory = null;
        this.customer = null;
        this.isNewProduct = false;
        this.image = "";
    }

    public boolean isCategoryPopulated() {
        return isCategoryPopulated;
    }

    public void setIsCategoryPopulated(boolean isCategoryPopulated) {
        this.isCategoryPopulated = isCategoryPopulated;
    }

    public boolean isSubCategoryPopulated() {
        return isSubCategoryPopulated;
    }

    public void setIsSubCategoryPopulated(boolean isSubCategoryPopulated) {
        this.isSubCategoryPopulated = isSubCategoryPopulated;
    }

    public boolean isCustomerPopulated() {
        return isCustomerPopulated;
    }

    public void setIsCustomerPopulated(boolean isCustomerPopulated) {
        this.isCustomerPopulated = isCustomerPopulated;
    }

    public boolean isNew() {
        return isNewProduct;
    }

    public void setIsNewProduct(boolean isNewProduct) {
        this.isNewProduct = isNewProduct;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ArrayList<SubCategory> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(ArrayList<SubCategory> subCategory) {
        this.subCategory = subCategory;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static Book fromJSON(JSONObject jsonObject) {
        Book book = new Book();
        if (jsonObject != null) {
            try {


                book.setName(jsonObject.getString(nameKey));
                book.setAuthor(jsonObject.getString(authorKey));
                book.setDescription(jsonObject.getString(descriptionKey));
                book.setPrice((float) jsonObject.getDouble(priceKey));
                book.setQuantity(jsonObject.getInt(quantityKey));
                book.setPublishedAt(jsonObject.getString(publishedAtKey));
                if (jsonObject.get(categoryKey) instanceof JSONObject) {
                    Category category = Category.fromJSON(jsonObject.getJSONObject(categoryKey));
                    book.setCategory(category);
                    book.setIsCategoryPopulated(true);
                } else if (jsonObject.get(categoryKey) instanceof String) {
                    Category category = new Category();
                    category.set_id(jsonObject.getString(categoryKey));
                    book.setCategory(category);
                }

                JSONArray subCategoriesJSONArray = jsonObject.getJSONArray(subCategoryKey);
                if (subCategoriesJSONArray.length() ==0) {
                    book.setSubCategory(new ArrayList<SubCategory>());
                } else {
                    if (subCategoriesJSONArray.get(0) instanceof JSONObject) {
                        ArrayList<SubCategory> subCategories = new ArrayList<SubCategory>();
                        for (int i =0; i< subCategoriesJSONArray.length(); ++i) {
                            subCategories.add(SubCategory.fromJSON(subCategoriesJSONArray.getJSONObject(i)));
                        }
                        book.setSubCategory(subCategories);
                        book.setIsSubCategoryPopulated(true);
                    } else if (subCategoriesJSONArray.get(0) instanceof String) {
                        ArrayList<SubCategory> subCategories = new ArrayList<SubCategory>();
                        for (int i =0; i< subCategoriesJSONArray.length(); ++i) {
                            SubCategory subCategory = new SubCategory();
                            subCategory.set_id(subCategoriesJSONArray.getString(i));
                            subCategories.add(subCategory);
                        }
                        book.setSubCategory(subCategories);
                    }
                }

                if (jsonObject.get(customerKey) instanceof JSONObject) {
                    Customer customer = Customer.fromJSON(jsonObject.getJSONObject(customerKey));
                    book.setCustomer(customer);
                    book.setIsCustomerPopulated(true);
                } else if (jsonObject.get(customerKey) instanceof String) {
                    Customer customer = new Customer();
                    customer.set_id(jsonObject.getString(customerKey));
                    book.setCustomer(customer);
                }

                book.set_id(jsonObject.getString(idKey));
                book.setIsNewProduct(jsonObject.getBoolean(isNewProductKey));
                book.setImage(jsonObject.getString(imageKey));
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

                if (book.isCategoryPopulated()) {
                    jsonObject.put(categoryKey, Category.toJSON(book.category));
                } else {
                    jsonObject.put(categoryKey, book.category.get_id());
                }

                JSONArray subCategoriesJSONArray = new JSONArray();

                if (book.isSubCategoryPopulated()) {
                    for (int i = 0; i < book.subCategory.size(); ++i) {
                        subCategoriesJSONArray.put(SubCategory.toJSON(book.subCategory.get(i)));
                    }
                } else {
                    for (int i = 0; i < book.subCategory.size(); ++i) {
                        subCategoriesJSONArray.put(book.subCategory.get(i).get_id());
                    }
                }

                jsonObject.put(subCategoryKey, subCategoriesJSONArray);

                if (book.isCustomerPopulated()) {
                    jsonObject.put(customerKey, Customer.toJSON(book.customer));
                } else {
                    jsonObject.put(customerKey, book.customer.get_id());
                }

                jsonObject.put(isNewProductKey, book.isNewProduct);
                jsonObject.put(imageKey, book.image);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return jsonObject;
    }

    public static ArrayList<String> getSubCategories(JSONArray jsonArray) {
        ArrayList<String> subCategories = new ArrayList<String>();
        try {
            for(int i=0; i<jsonArray.length();i++){
                subCategories.add(jsonArray.getString(i));
            }
        } catch (JSONException jsonException){
            jsonException.printStackTrace();
        }
        return subCategories;
    }

    public static JSONArray getSubCategoryJSONArray(ArrayList<String> subCategories) {
        JSONArray jsonArray = new JSONArray();
        try {
            for(int i=0; i<subCategories.size();i++){
                jsonArray.put(subCategories.get(i));
            }
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return jsonArray;
    }
}
