package tz.co.fishhappy.app.endpoint;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import tz.co.fishhappy.app.model.UserAccountModel;

/**
 * Created by Simon on 04-May-17.
 */

public interface UserAccountService {

    @POST(EndPointsUrls.API_POST_USER_ACCOUNT)
    @Headers({"Accept: application/json"})
    Call<UserAccountModel> updateUserAccount(@Header("Authorization") String token,
                                       @Body UserAccountModel model);

    @GET(EndPointsUrls.API_GET_USER_ACCOUNT)
    @Headers({"Accept: application/json"})
    Call<UserAccountModel> getUserAccount(@Header("Authorization") String token);

}
