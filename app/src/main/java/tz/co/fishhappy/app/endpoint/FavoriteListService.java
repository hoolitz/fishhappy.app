package tz.co.fishhappy.app.endpoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import tz.co.fishhappy.app.model.FavoriteModel;
import tz.co.fishhappy.app.model.FishModel;

/**
 * Created by Simon on 06-May-17.
 */

public interface FavoriteListService {

    @GET(EndPointsUrls.API_LIST_FAVORITE)
    @Headers({"Accept: application/json"})
    Call<List<FavoriteModel>> getFavoriteList(@Header("Authorization") String token);
}
