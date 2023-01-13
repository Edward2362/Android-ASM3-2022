package com.example.asm3.config;

public class Constant {
    public static String baseDomain = "http://192.168.1.3:9000"; //
    public static String loginCustomer = "/api/auth/login";
    public static String registerCustomer = "/api/auth/register";
    public static String getCustomerData = "/api/auth/getData";
    public static String setCustomerData = "/api/auth/setData";
    public static String changePassword = "/api/auth/changePassword";
    public static String getAllCategories = "/api/category/getAllCategories";
    public static String getSubCategories = "/api/subCategory/getSubCategories";
    public static String uploadBook = "/api/book/uploadBook";
    public static String updateBook = "/api/book/updateBook";
    public static String deleteBook = "/api/book/deleteBook";
    public static String getProduct = "/api/book/getProduct";
    public static String getNotifications = "/api/notification/getNotifications";
    public static String tokenFile = "token.txt";


    public static String tokenHeader = "x-access-token";
    public static String contentType = "Content-type";
    public static String accept = "Accept";

    public static String applicationJson = "application/json";
    public static String charsetUTF8 = "charset=UTF-8";
    public static String email = "email";
    public static String password = "password";
    public static String login = "login";
    public static String register = "register";
    public static String mainFragment = "MainFragment";
    public static String getCustomer = "getCustomer";

    public static String categoryKey = "category";
    public static String customerKey = "customerData";

    public static final int loginActivity = 500;
    public static final int registerActivity = 200;

    public static String getAllCategoriesTaskType = "getAllCategories";
    public static String getSubCategoriesTaskType = "getSubCategories";
    public static String uploadBookTaskType = "uploadBook";
    public static String updateBookTaskType = "updateBook";
    public static String deleteBookTaskType = "deleteBook";
    public static String getProductTaskType = "getProduct";
    public static String setCustomerDataTaskType = "setCustomerData";
    public static String changePasswordTaskType = "changePassword";
    public static String getNotificationsTaskType = "getNotifications";





    public static final int cameraPermissionCode = 105;
    public static final int cameraRequest = 1005;
    public static final int galleryPermissionCode = 205;
    public static final int galleryRequest = 2005;
}
