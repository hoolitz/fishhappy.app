package tz.co.fishhappy.app.endpoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tz.co.fishhappy.app.model.OrderCancelResponse;
import tz.co.fishhappy.app.model.Orders;

/**
 * Created by Simon on 06-May-17.
 */

public interface OrderCancelService {

    @POST(EndPointsUrls.API_CANCEL_ORDER)
    @Headers({"Accept: application/json"})
    Call<OrderCancelResponse> cancelOrder(@Header("Authorization") String token,
                                          @Path("orderId") String id);
}
