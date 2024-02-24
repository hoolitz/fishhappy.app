package tz.co.fishhappy.app.endpoint;

/**
 * Created by Simon on 29-Apr-17.
 */

public class EndPointsUrls {

    public static final String BASE_URL = "http://174.138.74.195/";
    public static final String API_LOGIN = "api/cstmr/login";
    public static final String API_REGISTER = "api/cstmr/register";
    public static final String API_LIST_CATEGORY = "api/cstmr/categories";
    public static final String API_LIST_FAVORITE = "api/cstmr/favourites";
    public static final String API_ADD_FAVORITE = "api/cstmr/products/{productId}/favourite";
    public static final String API_REMOVE_FAVORITE = "api/cstmr/products/{productId}/favourite";
    public static final String API_LIST_ORDER = "api/cstmr/orders";
    public static final String API_CANCEL_ORDER = "api/cstmr/orders/{orderId}/cancel";
    public static final String API_CONFIRM_ORDER = "api/cstmr/orders/{orderId}/confirm";
    public static final String API_SEND_ORDER = "api/cstmr/orders";
    public static final String API_LIST_PRODUCT = "api/cstmr/products";
    public static final String API_PAYMENT = "api/cstmr/orders/{orderId}/payments";
    public static final String API_CHANGE_PASSWORD = "api/cstmr/change_password";
    public static final String API_GET_USER_ACCOUNT = "api/cstmr/";
    public static final String API_POST_USER_ACCOUNT = "";

    public static final String PAYMENT = "api/v1/enduser/pay"; //?id={subscriptionId}&token={token}";

}
