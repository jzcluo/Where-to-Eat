package com.zluo.wheretoeat;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.QueryMap;

/**
 * Created by Jeff on 3/11/2017.
 */

public interface RestaurantSearchService {
    @GET("search")
    Call<Businesses> findRestaurants(
            @Header("Authorization") String token,
            @QueryMap Map<String, String> filters
    );
}
