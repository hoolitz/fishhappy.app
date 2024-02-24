package tz.co.fishhappy.app.endpoint;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import tz.co.fishhappy.app.model.RegisterModel;
import tz.co.fishhappy.app.model.RegisterResponseModel;

/**
 * Created by Simon on 01-May-17.
 */

public interface RegisterService {

    @POST(EndPointsUrls.API_REGISTER)
    @Headers({"Accept: application/json"})
    Call<RegisterResponseModel> register(@Body RegisterModel model);

}
