package com.zluo.wheretoeat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserSelectionFragment.UserSelectionListener} interface
 * to handle interaction events.
 * Use the {@link UserSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSelectionFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int screenHeight;
    private int screenWidth;
    private boolean buttonLeftClicked;
    private boolean buttonMiddleClicked;
    private boolean buttonRightClicked;
    private boolean radioButtonClicked;
    private Button buttonLeft;
    private Button buttonMiddle;
    private Button buttonRight;
    private RadioButton radioButton;
    private Button buttonGo;

    private String term;
    private String latitude;
    private String longitude;
    private String radius;
    private String categories;
    private String locale;
    private String limit;
    private String offset;
    private String sort_by;
    private String price;
    private String open_now;
    private String open_at;
    private String attributes;

    private String[] listParams;
    private String[] paramNames;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private boolean isRequestingLocationUpdates;

    private HashMap<String, String> userSelection;
    private UserSelectionListener userSelectionListener;

    public UserSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserSelectionFragment newInstance(String param1, String param2) {
        UserSelectionFragment fragment = new UserSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        Log.d("UserSElectionFragment","onAttach");
        super.onAttach(context);
        if (context instanceof UserSelectionListener) {
            userSelectionListener = (UserSelectionListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("UserSElectionFragment","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("UserSElectionFragment","onCreateVIew");
        return inflater.inflate(R.layout.fragment_user_selection, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("UserSElectionFragment","onActivityCreaaated");
        super.onActivityCreated(savedInstanceState);
        screenHeight = getActivity().getResources().getDisplayMetrics().heightPixels;
        screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;

        buttonLeft = (Button) getActivity().findViewById(R.id.button_left);
        buttonMiddle = (Button) getActivity().findViewById(R.id.button_middle);
        buttonRight = (Button) getActivity().findViewById(R.id.button_right);
        buttonLeft.setWidth(screenWidth / 4);
        buttonMiddle.setWidth(screenWidth / 4);
        buttonRight.setWidth(screenWidth / 4);
        buttonLeft.setHeight(screenWidth / 4);
        buttonMiddle.setHeight(screenWidth / 4);
        buttonRight.setHeight(screenWidth / 4);

        ViewGroup.MarginLayoutParams layoutParams_Middle = (ViewGroup.MarginLayoutParams) buttonMiddle.getLayoutParams();
        layoutParams_Middle.leftMargin = screenWidth / 12;
        layoutParams_Middle.rightMargin = screenWidth / 12;
        layoutParams_Middle.bottomMargin = screenWidth / 6;

        Button buttonGo = (Button) getActivity().findViewById(R.id.button_go);
        ViewGroup.MarginLayoutParams layoutParams_Go = (ViewGroup.MarginLayoutParams) buttonGo.getLayoutParams();
        layoutParams_Go.topMargin = screenWidth / 12;
        layoutParams_Go.bottomMargin = screenWidth / 8;
        //buttonGo.setHeight(screenWidth / 8);

        radioButton = (RadioButton) getActivity().findViewById(R.id.radioButton);
        radioButton.setOnClickListener(this);

        buttonLeft.setOnClickListener(this);
        buttonMiddle.setOnClickListener(this);
        buttonRight.setOnClickListener(this);

        buttonGo.setOnClickListener(this);

        //google location api
        googleApiClient = new GoogleApiClient.Builder(getActivity()).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).
                build();


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000L);
        locationRequest.setFastestInterval(1000L);

        //listParams = new String[]{term, latitude, longitude, radius, categories, locale,
        //        limit, offset, sort_by, price, open_now, open_at, attributes};
        paramNames = new String[]{"term", "latitude", "longitude", "radius", "categories", "locale",
                "limit", "offset", "sort_by", "price", "open_now", "open_at", "attributes"};
    }

    @Override
    public void onStart() {
        Log.d("UserSElectionFragment","onStart");
        super.onStart();
        googleApiClient.connect();
        //setting them null so when the fragment is accessed from displayresultfragment through back button
        //the data is new i don know if they get reset
        buttonLeftClicked = false;
        buttonMiddleClicked = false;
        buttonRightClicked = false;
        radioButtonClicked = false;
        radioButton.setChecked(false);
        /*
        term = null;
        latitude = null;
        longitude = null;
        radius = null;
        categories = null;
        locale = null;
        limit = null;
        offset = null;
        sort_by = null;
        price = null;
        open_now = null;
        open_at = null;
        attributes = null;
        */
        //need to set null otherwise maintain its state from previous
        sort_by = null;
        price = null;
        userSelection = new HashMap<>();
    }

    @Override
    public void onResume() {
        Log.d("UserSElectionFragment","onREsume");
        super.onResume();
        if (googleApiClient.isConnected() && !isRequestingLocationUpdates) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            isRequestingLocationUpdates = true;
        }
    }


    @Override
    public void onPause() {
        Log.d("UserSElectionFragment","onPause");
        super.onPause();
        if (googleApiClient.isConnected() && isRequestingLocationUpdates) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            isRequestingLocationUpdates = false;
        }
    }

    @Override
    public void onStop() {
        Log.d("UserSElectionFragment","onStop");
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onDestroyView() {
        Log.d("UserSElectionFragment","onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("UserSElectionFragment","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("UserSElectionFragment","onDetach");
        super.onDetach();
        userSelectionListener = null;
        //setting them null so when the fragment is accessed from displayresultfragment through back button
        //the data is new i don know if they get reset

    }

    public interface UserSelectionListener {
        void setUserSelection(HashMap<String, String> userSelection, boolean random);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        //return;
        //}
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            Log.d("onConnected","setting latitudelongitude");
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            Log.d("LongandLa","" + longitude + " " + latitude);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        isRequestingLocationUpdates = true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.radioButton : {
                radioButtonClicked = !radioButtonClicked;
                if (!radioButtonClicked) {
                    radioButton.setChecked(false);
                }
                break;
            }
            case R.id.button_left : {
                buttonLeftClicked = !buttonLeftClicked;
                if (buttonLeftClicked) {
                    //buttonMiddleClicked = buttonRightClicked = false;
                    buttonLeft.getBackground().setColorFilter(Color.parseColor("#af0606"), PorterDuff.Mode.SRC);
                    //buttonMiddle.getBackground().clearColorFilter();
                    //buttonRight.getBackground().clearColorFilter();
                    //sort_by = null;
                    price = "1";
                } else {
                    buttonLeft.getBackground().clearColorFilter();
                    price = null;
                }
                break;
            }
            case R.id.button_middle : {
                buttonMiddleClicked = !buttonMiddleClicked;
                if (buttonMiddleClicked) {
                    //buttonLeftClicked =
                    buttonRightClicked = false;
                    buttonMiddle.getBackground().setColorFilter(Color.parseColor("#af0606"), PorterDuff.Mode.SRC);
                    //buttonLeft.getBackground().clearColorFilter();
                    buttonRight.getBackground().clearColorFilter();
                    sort_by = "rating";
                    //price = null;
                } else {
                    buttonMiddle.getBackground().clearColorFilter();
                    sort_by = null;
                }
                break;
            }
            case R.id.button_right : {
                buttonRightClicked = !buttonRightClicked;
                if (buttonRightClicked) {
                    //buttonLeftClicked
                    buttonMiddleClicked = false;
                    buttonRight.getBackground().setColorFilter(Color.parseColor("#af0606"), PorterDuff.Mode.SRC);
                    //buttonLeft.getBackground().clearColorFilter();
                    buttonMiddle.getBackground().clearColorFilter();
                    sort_by = "distance";
                    //price = null;
                } else {
                    buttonRight.getBackground().clearColorFilter();
                    sort_by = null;
                }
                break;
            }
            case R.id.button_go : {
                open_now = "true";
                //if term not given will search everything which includeds sports bars?????
                term = (term == null) ? "food" : term;
                if (!buttonLeftClicked && !buttonMiddleClicked && !buttonRightClicked) {
                    radioButtonClicked = true;
                    radioButton.setChecked(true);
                }
                //userSelection = new HashMap<>();

                //here tripped me up, usesd to have this in oncreate, but i think everything was stored by value
                // and not reference ,therefore when the variables are updated,
                listParams = new String[]{term, latitude, longitude, radius, categories, locale,
                        limit, offset, sort_by, price, open_now, open_at, attributes};

                for (int i = 0; i < listParams.length; i++) {
                    if (listParams[i] != null) {
                        Log.d("adding to userselection", listParams[i] +"+"+userSelection.size());
                        userSelection.put(paramNames[i], listParams[i]);
                    }
                }

                //test case
                //userSelection.put("latitude", "37.786882");
                //userSelection.put("longitude", "-122.399972");
                /*for (String string : userSelection.keySet()) {
                    Log.d("mapkeys and value", string + "," + userSelection.get(string));
                }
                */
                userSelectionListener.setUserSelection(userSelection, radioButtonClicked);
            }
        }
    }
}

