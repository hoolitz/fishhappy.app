package tz.co.fishhappy.app.endpoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import tz.co.fishhappy.app.model.FishModel;

/**
 * Created by Simon on 06-May-17.
 */

public interface GetPayService {

    @GET(EndPointsUrls.PAYMENT)
    Call<String> getPaymentUrl(@Query("token") String token, @Query("amount") String amount);
}
