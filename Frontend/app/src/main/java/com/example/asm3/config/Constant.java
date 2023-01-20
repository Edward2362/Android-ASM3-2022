package com.example.asm3.config;

import java.util.regex.Pattern;

public class Constant {
    public static String baseDomain = "http://10.0.2.2:9000"; //
    public static String loginCustomer = "/api/auth/login";
    public static String registerCustomer = "/api/auth/register";
    public static String getCustomerData = "/api/auth/getData";
    public static String setCustomerData = "/api/auth/setData";
    public static String changePassword = "/api/auth/changePassword";
    public static String changeAvatar = "/api/auth/changeAvatar";
    public static String increaseCartQuantity = "/api/auth/increaseCartQuantity";
    public static String decreaseCartQuantity = "/api/auth/decreaseCartQuantity";
    public static String getAllCategories = "/api/category/getAllCategories";
    public static String getSubCategories = "/api/subCategory/getSubCategories";
    public static String uploadBook = "/api/book/uploadBook";
    public static String updateBook = "/api/book/updateBook";
    public static String deleteBook = "/api/book/deleteBook";
    public static String getProduct = "/api/book/getProduct";
    public static String getUploadedProducts = "/api/book/getUploadedProducts";
    public static String getNotifications = "/api/notification/getNotifications";
    public static String suggestProduct = "/api/book/suggestProduct";
    public static String searchProduct = "/api/book/searchProduct";
    public static String saveProduct = "/api/book/saveProduct";
    public static String getCart = "/api/book/getCart";
    public static String removeCart = "/api/book/removeCart";
    public static String orderProducts = "/api/order/orderProducts";
    public static String generateOrders = "/api/order/generateOrders";
    public static String getCustomerOrders = "/api/order/getCustomerOrders";
    public static String getSellingOrders = "/api/order/getSellingOrders";
    public static String tokenFile = "token.txt";


    public static final Pattern emailPattern =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern pwPattern =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{8,}$", Pattern.CASE_INSENSITIVE);
    public static String emailError = "Invalid email!";
    public static String pwError = "Require at least 8 characters with uppercase, lowercase, number and symbol";

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
    public static String publicProfileIdKey = "publicProfileId";
    public static String isUploadKey = "isUpload";
    public static String isUpdateKey = "isUpdate";
    public static String isRemoveKey = "isRemove";
    public static String productIdKey = "productId";
    public static String productKey = "product";
    public static String quantityKey = "quantity";
    public static String booksArrPositionKey = "booksArrPosition";

    public static final int mainActivityCode = 000;
    public static final int authActivityCode = 100;
    public static final int accSettingActivityCode = 200;
    public static final int manageBookActivityCode = 300;
    public static final int searchResultActivityCode = 400;
    public static final int productDetailActivityCode = 500;
    public static final int cartActivityCode = 600;
    public static final int checkoutActivityCode = 70;
    public static final int salesProgressActivityCode = 800;

    public static final int uploadCode = 700;
    public static final int isUploadCode = 305;
    public static final int isUpdatedCode = 310;
    public static final int isRemovedCode = 315;

    public static String getAllCategoriesTaskType = "getAllCategories";
    public static String getSubCategoriesTaskType = "getSubCategories";
    public static String uploadBookTaskType = "uploadBook";
    public static String updateBookTaskType = "updateBook";
    public static String deleteBookTaskType = "deleteBook";
    public static String saveProductTaskType = "saveProduct";
    public static String getProductTaskType = "getProduct";
    public static String getUploadedProductsTaskType = "getUploadedProducts";
    public static String setCustomerDataTaskType = "setCustomerData";
    public static String changePasswordTaskType = "changePassword";
    public static String getNotificationsTaskType = "getNotifications";
    public static String suggestProductTaskType = "suggestProduct";
    public static String searchProductTaskType = "searchProduct";
    public static String getCartTaskType = "getCart";
    public static String removeCartTaskType = "removeCart";
    public static String orderProductsTaskType = "orderProducts";
    public static String generateOrdersTaskType = "generateOrders";
    public static String getCustomerOrdersTaskType = "getCustomerOrders";
    public static String getSellingOrdersTaskType = "getSellingOrders";
    public static String changeAvatarTaskType = "changeAvatar";
    public static String increaseCartQuantityTaskType = "increaseCartQuantity";
    public static String decreaseCartQuantityTaskType = "decreaseCartQuantity";


    public static final int cameraPermissionCode = 105;
    public static final int cameraRequest = 1005;
    public static final int galleryPermissionCode = 205;
    public static final int galleryRequest = 2005;
}
