package tz.co.fishhappy.app.endpoint;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import tz.co.fishhappy.app.model.ForgotPasswordResetModel;

/**
 * Created by Simon on 02-May-17.
 */

public interface ForgotPasswordResetService {

    //TODO change url to forgot password
    @POST(EndPointsUrls.API_LOGIN)
    @Headers({"Accept: application/json"})
    Call<ForgotPasswordResetModel> forgotResetPassword(@Body ForgotPasswordResetModel model);

}
