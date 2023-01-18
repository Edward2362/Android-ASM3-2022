package com.example.asm3.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiList<T extends Object> {
    private ArrayList<T> list;

    public ApiList(ArrayList<T> list) {
        this.list = list;
    }

    public ApiList() {}

    public ArrayList<T> getList() {
        return this.list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }

    public static <T> ApiList<T> fromJSON(JSONArray jsonArray, Class<T> t) {
        if (t.isAssignableFrom(Customer.class)) {
            ArrayList<Customer> customers = new ArrayList<Customer>();

            try {
                for (int i = 0; i < jsonArray.length(); ++i) {
                    customers.add(Customer.fromJSON(jsonArray.getJSONObject(i)));
                }
            } catch(Exception exception) {
                exception.printStackTrace();
            }

            return new ApiList<T>((ArrayList<T>) customers);
        } else if (t.isAssignableFrom(Category.class)) {
            ArrayList<Category> categories = new ArrayList<Category>();

            try {
                for (int i = 0; i < jsonArray.length(); ++i) {
                    categories.add(Category.fromJSON(jsonArray.getJSONObject(i)));
                }
            } catch(Exception exception) {
                exception.printStackTrace();
            }

            return new ApiList<T>((ArrayList<T>) categories);
        } else if (t.isAssignableFrom(SubCategory.class)) {
            ArrayList<SubCategory> subCategories = new ArrayList<SubCategory>();

            try {
                for (int i = 0; i < jsonArray.length(); ++i) {
                    subCategories.add(SubCategory.fromJSON(jsonArray.getJSONObject(i)));
                }
            } catch(Exception exception) {
                exception.printStackTrace();
            }

            return new ApiList<T>((ArrayList<T>) subCategories);
        } else if (t.isAssignableFrom(Book.class)) {
            ArrayList<Book> books = new ArrayList<Book>();

            try {
                for (int i = 0; i < jsonArray.length(); ++i) {
                    books.add(Book.fromJSON(jsonArray.getJSONObject(i)));
                }
            } catch(Exception exception) {
                exception.printStackTrace();
            }

            return new ApiList<T>((ArrayList<T>) books);
        } else if (t.isAssignableFrom(Notification.class)) {
            ArrayList<Notification> notifications = new ArrayList<Notification>();

            try {
                for (int i = 0; i < jsonArray.length(); ++i) {
                    notifications.add(Notification.fromJSON(jsonArray.getJSONObject(i)));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return new ApiList<T>((ArrayList<T>) notifications);
        } else if (t.isAssignableFrom(Order.class)) {
            ArrayList<Order> orders = new ArrayList<Order>();

            try {
                for (int i = 0; i < jsonArray.length(); ++i) {
                    orders.add(Order.fromJSON(jsonArray.getJSONObject(i)));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return new ApiList<T>((ArrayList<T>) orders);
        } else if (t.isAssignableFrom(OrderDetail.class)) {
            ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

            try {
                for (int i = 0; i < jsonArray.length(); ++i) {
                    orderDetails.add(OrderDetail.fromJSON(jsonArray.getJSONObject(i)));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return new ApiList<T>((ArrayList<T>) orderDetails);
        } else if (t.isAssignableFrom(Review.class)) {
            ArrayList<Review> reviews = new ArrayList<Review>();

            try {
                for (int i = 0; i < jsonArray.length(); ++i) {
                    reviews.add(Review.fromJSON(jsonArray.getJSONObject(i)));
                }
            } catch(Exception exception) {
                exception.printStackTrace();
            }

            return new ApiList<T>((ArrayList<T>) reviews);
        } else {
            return null;
        }
    }

    public static JSONArray getData(String message) {
        JSONArray jsonArray = null;
        JSONObject jsonMessage = null;
        try {
            jsonMessage = new JSONObject(message);
            jsonArray = jsonMessage.getJSONArray("data");
        } catch(JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return jsonArray;
    }
}
