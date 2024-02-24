package tz.co.fishhappy.app.endpoint;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import tz.co.fishhappy.app.model.ChangePasswordModel;

/**
 * Created by Simon on 03-May-17.
 */

public interface ChangePasswordService {

    //TODO change url to forgot password
    @POST(EndPointsUrls.API_CHANGE_PASSWORD)
    @Headers({"Accept: application/json"})
    Call<ChangePasswordModel> changePassword(@Header("Authorization") String token,
                                             @Body ChangePasswordModel model);

}
