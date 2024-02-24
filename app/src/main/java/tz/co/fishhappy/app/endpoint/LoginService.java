package tz.co.fishhappy.app.endpoint;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import tz.co.fishhappy.app.model.LoginModel;
import tz.co.fishhappy.app.model.LoginResponseModel;

/**
 * Created by Simon on 01-May-17.
 */

public interface LoginService {

    @POST(EndPointsUrls.API_LOGIN)
    @Headers({"Accept: application/json"})
    Call<LoginResponseModel> login(@Body LoginModel model);

}
