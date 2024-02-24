package tz.co.fishhappy.app.endpoint;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import tz.co.fishhappy.app.model.FavoriteRemoveModel;

/**
 * Created by Simon on 06-May-17.
 */

public interface FavoriteRemoveService {

    @DELETE(EndPointsUrls.API_REMOVE_FAVORITE)
    @Headers({"Accept: application/json"})
    Call<FavoriteRemoveModel> removeFavorite(@Path("productId") int productId);
}
