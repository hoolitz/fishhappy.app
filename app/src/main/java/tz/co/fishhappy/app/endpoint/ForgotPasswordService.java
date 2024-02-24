package tz.co.fishhappy.app.endpoint;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import tz.co.fishhappy.app.model.ForgotPasswordModel;
import tz.co.fishhappy.app.model.LoginModel;

/**
 * Created by Simon on 02-May-17.
 */

public interface ForgotPasswordService {

    //TODO change url to forgot password
    @POST(EndPointsUrls.API_LOGIN)
    @Headers({"Accept: application/json"})
    Call<ForgotPasswordModel> forgotPassword(@Body ForgotPasswordModel model);

}
