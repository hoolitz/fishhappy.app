package tz.co.fishhappy.app.endpoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tz.co.fishhappy.app.model.FishModel;

/**
 * Created by Simon on 06-May-17.
 */

public interface PaymentService {

    @POST(EndPointsUrls.API_PAYMENT)
    @Headers({"Accept: application/json"})
    Call<String> getPayment(@Header("Authorization") String token,
                            @Path("orderId") Integer orderId);
}
