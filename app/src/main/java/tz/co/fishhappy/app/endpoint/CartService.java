package tz.co.fishhappy.app.endpoint;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import tz.co.fishhappy.app.model.CartModel;
import tz.co.fishhappy.app.model.CartProductRequestModel;
import tz.co.fishhappy.app.model.CartRequestModel;
import tz.co.fishhappy.app.model.CartResponseModel;
import tz.co.fishhappy.app.model.RegisterModel;

/**
 * Created by Simon on 01-May-17.
 */

public interface CartService {

    @POST(EndPointsUrls.API_SEND_ORDER)
    @Headers({"Accept: application/json"})
    Call<CartResponseModel> sendOrderItems(@Header("Authorization") String token,
                                           @Body CartRequestModel model);

}
