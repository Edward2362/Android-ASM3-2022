package com.example.asm3.config;

public class Constant {
    public static String baseDomain = "http://10.0.2.2:9000";
    public static String loginCustomer = "/api/auth/login";
    public static String registerCustomer = "/api/auth/register";
    public static String getCustomerData = "/api/auth/getData";
    public static String getAllCategories = "/api/category/getAllCategories";
    public static String getSubCategories = "/api/subCategory/getSubCategories";
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


    public static int loginActivity = 500;
    public static int registerActivity = 200;

    public static String getAllCategoriesTaskType = "getAllCategories";
    public static String getSubCategoriesTaskType = "getSubCategories";

}
