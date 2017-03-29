package com.zluo.wheretoeat;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.text.util.LinkifyCompat;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayResultFragment.DisplayResultListener} interface
 * to handle interaction events.
 * Use the {@link DisplayResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayResultFragment extends Fragment implements View.OnClickListener {

    private static final String REQUEST_PARAMS = "REQUEST_PARAMS";
    private static final String RANDOM = "RANDOM";

    private static final String MY_YELP_TOKEN = "Yelp Token";
    private HashMap<String, String> requestParams;

    private RestaurantSearchService restaurantSearchService;
    private List<Business> businessList;
    private Business business;

    private ImageView image_url;
    private TextView name;
    private TextView categories;
    //displaying in the way rating(reviewcount);
    private TextView rating_review_count;
    private TextView price;
    private TextView distance;
    //make the url a hyperlink
    private TextView url;
    private Button eatHere;

    private int currentIndex;



    private DisplayResultListener displayResultListener;



    public DisplayResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayResultFragment newInstance(HashMap<String, String> requestParams, boolean random) {
        DisplayResultFragment fragment = new DisplayResultFragment();
        Bundle args = new Bundle();
        args.putSerializable(REQUEST_PARAMS, requestParams);
        args.putBoolean(RANDOM, random);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        Log.d("displayResultFragment","onattach");
        super.onAttach(context);
        if (context instanceof DisplayResultListener) {
            displayResultListener = (DisplayResultListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("displayResultFragment","oncreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("displayResultFragment","oncreateview");
        return inflater.inflate(R.layout.fragment_display_result, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("displayResultFragment","onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        name = (TextView) getActivity().findViewById(R.id.businessName);
        categories = (TextView) getActivity().findViewById(R.id.categories);
        rating_review_count = (TextView) getActivity().findViewById(R.id.rating);
        price = (TextView) getActivity().findViewById(R.id.price);
        distance = (TextView) getActivity().findViewById(R.id.distance);
        url = (TextView) getActivity().findViewById(R.id.url);
        eatHere = (Button) getActivity().findViewById(R.id.eatHere);
        eatHere.setVisibility(View.INVISIBLE);
        //add in whether to walk or ride or drive to the restaurant
        /*
        q: Sets the end point for navigation searches. This can be a latitude,longitude or a query formatted address. If it is a query string that returns more than one result, the first result will be selected.
        mode sets the method of transportation. Mode is optional, defaults to driving, and can be set to one of:

        d for driving
        w for walking
        b for bicycling
        avoid sets features the route should try to avoid. Avoid is optional and can be set to one or more of:

        t for tolls
        h for highways
        f for ferries
        */


        if (getArguments() != null) {
            requestParams = (HashMap)(getArguments().getSerializable(REQUEST_PARAMS));
        }
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://api.yelp.com/v3/businesses/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        restaurantSearchService = retrofit.create(RestaurantSearchService.class);
        Call<Businesses> call = restaurantSearchService.findRestaurants(MY_YELP_TOKEN, requestParams);
        call.enqueue(new Callback<Businesses>() {
            @Override
            public void onResponse(Call<Businesses> call, Response<Businesses> response) {
                if (response.isSuccessful()) {
                    businessList = response.body().getBusinesses();
                    Random rand = new Random();
                    if (getArguments().getBoolean(RANDOM)) {
                        business = businessList.get(rand.nextInt(businessList.size()));
                    } else if (businessList.size() >= 1) {
                        currentIndex = 0;
                        business = businessList.get(currentIndex);
                    }
                    setResult(business);
                    FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.refresh);
                    floatingActionButton.setOnClickListener(DisplayResultFragment.this);
                    floatingActionButton.bringToFront();
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Businesses> call, Throwable t) {

            }
        });
        //setting views for this fragment
        int screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
        image_url = (ImageView) getActivity().findViewById(R.id.imageView);
        image_url.getLayoutParams().width = screenWidth * 2 / 3;
        //ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) image_url.getLayoutParams();
        ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.scrollView);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) scrollView.getLayoutParams();
        marginLayoutParams.topMargin = getToolBarHeight();//getActivity().getActionBar().getHeight();

    }

    @Override
    public void onStart() {
        Log.d("displayResultFragment","onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("displayResultFragment","onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("displayResultFragment","onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("displayResultFragment","onstop");
        super.onStop();
        //businessList = null;
        business = null;

    }

    @Override
    public void onDestroyView() {
        Log.d("displayResultFragment","ondestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("displayResultFragment","ondestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("displayResultFragment","onDEtach");
        super.onDetach();
        displayResultListener = null;
        getActivity().findViewById(R.id.refresh).setVisibility(View.INVISIBLE);
    }


    private void setResult(Business business) {
        if (business != null) {

            Picasso.with(getActivity())
                    .load(business.getImage_url())
                    //.placeholder(R.drawable.cast_album_art_placeholder_large)
                    //.resize()
                    .into(image_url, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("success","success");
                        }

                        @Override
                        public void onError() {
                            Log.d("error","error");
                        }
                    });
            name.setText(getString(R.string.display_result, "Name", business.getName()));
            categories.setText(getString(R.string.display_result, "Categories", business.getCategories()));
            rating_review_count.setText(getString(R.string.display_rating_result, "Rating", business.getRating(), business.getReview_count()));
            price.setText(getString(R.string.display_result, "Price", business.getPrice()));
            distance.setText(getString(R.string.display_distance_result, "Distance", String.valueOf(Double.valueOf(business.getDistance()).intValue())));
            //when user clicks link opens website
            url.setClickable(true);
            final Uri uri = Uri.parse(business.getUrl());
            url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    //intent.setPackage("com.google.android.apps.chrome");
                    startActivity(intent);
                }
            });
            url.setText(getString(R.string.display_result, "Yelp Page", "Click to View"));//business.getUrl()));
            eatHere.setVisibility(View.VISIBLE);
            final String latitude = business.getLatitude();
            final String longitude = business.getLongitude();
            final String name = business.getName();
            eatHere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("google.navigation:q=" + latitude
                            + "," + longitude + "("
                            + TextUtils.join("+", name.split("\\s")) + ")");
                    Intent googleMapIntent = new Intent(Intent.ACTION_VIEW, uri);
                    googleMapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(googleMapIntent);

                }
            });
        } else {
            name.setText("No Restaurant Found");
        }
    }

    @Override
    public void onClick(View view) {
        Log.d("buttonclicked","buttonCilicked");
        Random rand = new Random();

        if (getArguments().getBoolean(RANDOM)) {
            setResult(businessList.get(rand.nextInt(businessList.size())));
        } else if (currentIndex < businessList.size() - 2) {
            currentIndex += 1;
            setResult(businessList.get(currentIndex));
        } else {
            setResult(businessList.get(rand.nextInt(businessList.size())));
        }
    }

    private int getToolBarHeight() {
        int[] attrs = new int[] {R.attr.actionBarSize};
        TypedArray ta = getContext().obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return toolBarHeight;
    }
    public interface DisplayResultListener {
        // TODO: Update argument type and name
        void displayResult(Uri uri);
    }
}
