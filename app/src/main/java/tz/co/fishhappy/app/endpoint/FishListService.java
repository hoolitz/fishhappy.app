package tz.co.fishhappy.app.endpoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import tz.co.fishhappy.app.model.FishModel;

/**
 * Created by Simon on 06-May-17.
 */

public interface FishListService {

    @GET(EndPointsUrls.API_LIST_PRODUCT)
    @Headers({"Accept: application/json"})
    Call<List<FishModel>> getFishList(@Header("Authorization") String token);
}
