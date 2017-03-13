package com.zluo.wheretoeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultsActivity extends AppCompatActivity {
    //private static final boolean BUTTON_LEFT_SELECTED;
    //private static final boolean BUTTON_MIDDLE_SELECTED;
    //private static final boolean BUTTON_RIGHT_SELECTED;
    private final String MY_YELP_TOKEN = "MY_YELP_TOKEN";
    private Map<String, String> requestParams;
    private Double longitude;
    private Double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_results);
        setSupportActionBar(toolBar);

        Bundle bundle = getIntent().getExtras();
        longitude = bundle.getDouble("CURRENT_LONGITUDE");
        latitude = bundle.getDouble("CURRENT_LATITUDE");

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://api.yelp.com/v3/businesses/").
                addConverterFactory(GsonConverterFactory.create()).
                build();

        requestParams = new HashMap<>();
        requestParams.put("latitude", String.valueOf(latitude));
        requestParams.put("longitude", String.valueOf(longitude));




        RestaurantSearchService restaurantSearchService = retrofit.create(RestaurantSearchService.class);
        Call<Businesses> call = restaurantSearchService.findRestaurants(MY_YELP_TOKEN, requestParams);

        call.enqueue(new Callback<Businesses>() {
            @Override
            public void onResponse(Call<Businesses> call, Response<Businesses> response) {
                Businesses businesses = response.body();

            }

            @Override
            public void onFailure(Call<Businesses> call, Throwable t) {

            }
        });



    }
}
