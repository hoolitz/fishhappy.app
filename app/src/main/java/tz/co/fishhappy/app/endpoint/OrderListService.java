package tz.co.fishhappy.app.endpoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import tz.co.fishhappy.app.model.FishModel;
import tz.co.fishhappy.app.model.OrderModel;
import tz.co.fishhappy.app.model.Orders;

/**
 * Created by Simon on 06-May-17.
 */

public interface OrderListService {

    @GET(EndPointsUrls.API_LIST_ORDER)
    @Headers({"Accept: application/json"})
    Call<List<Orders>> getOrderList(@Header("Authorization") String token);
}
