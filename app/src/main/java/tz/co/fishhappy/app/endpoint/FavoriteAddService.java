package tz.co.fishhappy.app.endpoint;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tz.co.fishhappy.app.model.FavoriteAddModel;

/**
 * Created by Simon on 06-May-17.
 */

public interface FavoriteAddService {

    @POST(EndPointsUrls.API_ADD_FAVORITE)
    @Headers({"Accept: application/json"})
    Call<FavoriteAddModel> addFavorite(@Header("Authorization") String token,
                                       @Path("productId") int productId);
}
