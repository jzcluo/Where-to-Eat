package com.zluo.wheretoeat;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.ActivityCompatApi23;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

//import com.google.android.gms.drive.metadata.SearchableCollectionMetadataField;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private int screenHeight;
    private int screenWidth;
    private boolean buttonLeftClicked = false;
    private boolean buttonMiddleClicked = false;
    private boolean buttonRightClicked = false;
    private boolean radioButtonClicked = false;
    private Button buttonLeft;
    private Button buttonMiddle;
    private Button buttonRight;
    private RadioButton radioButton;
    private Button buttonGo;
    private double latitude;
    private double longitude;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private boolean isRequestingLocationUpdates;

    static final String BUTTON_CLICKED_IN_MAIN = "BUTTON_CLICKED_IN_MAIN";
    static final String RANDOM_SELECTION = "RANDOMLY_SELECT_A_RESTAURANT";
    static final String CURRENT_LATITUDE = "CURRENT_LATITUDE";
    static final String CURRENT_LONGITUDE = "CURRENT_LONGITUDE";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolBar);


        screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        screenWidth = this.getResources().getDisplayMetrics().widthPixels;

        buttonLeft = (Button) findViewById(R.id.button_left);
        buttonMiddle = (Button) findViewById(R.id.button_middle);
        buttonRight = (Button) findViewById(R.id.button_right);
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

        Button buttonGo = (Button) findViewById(R.id.button_go);
        ViewGroup.MarginLayoutParams layoutParams_Go = (ViewGroup.MarginLayoutParams) buttonGo.getLayoutParams();
        layoutParams_Go.topMargin = screenWidth / 12;
        layoutParams_Go.bottomMargin = screenWidth / 8;
        //buttonGo.setHeight(screenWidth / 8);

        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton.setOnClickListener(this);

        buttonLeft.setOnClickListener(this);
        buttonMiddle.setOnClickListener(this);
        buttonRight.setOnClickListener(this);

        buttonGo.setOnClickListener(this);

        //google location api
        googleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).
                build();


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000L);
        locationRequest.setFastestInterval(1000L);


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
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("LongandLa","" + longitude + " " + u originlatitude);
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
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected() && isRequestingLocationUpdates) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            isRequestingLocationUpdates = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected() && !isRequestingLocationUpdates) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            isRequestingLocationUpdates = true;
        }
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
                    buttonMiddleClicked = buttonRightClicked = false;
                    buttonLeft.getBackground().setColorFilter(Color.parseColor("#af0606"), PorterDuff.Mode.SRC);
                    buttonMiddle.getBackground().clearColorFilter();
                    buttonRight.getBackground().clearColorFilter();
                } else {
                    buttonLeft.getBackground().clearColorFilter();
                }
                break;
            }
            case R.id.button_middle : {
                buttonMiddleClicked = !buttonMiddleClicked;
                if (buttonMiddleClicked) {
                    buttonLeftClicked = buttonRightClicked = false;
                    buttonMiddle.getBackground().setColorFilter(Color.parseColor("#af0606"), PorterDuff.Mode.SRC);
                    buttonLeft.getBackground().clearColorFilter();
                    buttonRight.getBackground().clearColorFilter();
                } else {
                    buttonMiddle.getBackground().clearColorFilter();
                }
                break;
            }
            case R.id.button_right : {
                buttonRightClicked = !buttonRightClicked;
                if (buttonRightClicked) {
                    buttonLeftClicked = buttonMiddleClicked = false;
                    buttonRight.getBackground().setColorFilter(Color.parseColor("#af0606"), PorterDuff.Mode.SRC);
                    buttonLeft.getBackground().clearColorFilter();
                    buttonMiddle.getBackground().clearColorFilter();
                } else {
                    buttonRight.getBackground().clearColorFilter();
                }
                break;
            }
            case R.id.button_go : {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                if (buttonLeftClicked) {
                    intent.putExtra(BUTTON_CLICKED_IN_MAIN, 0);
                } else if (buttonMiddleClicked) {
                    intent.putExtra(BUTTON_CLICKED_IN_MAIN, 1);
                } else if (buttonRightClicked) {
                    intent.putExtra(BUTTON_CLICKED_IN_MAIN, 2);
                } else {
                    radioButtonClicked = true;
                    radioButton.setChecked(true);
                }
                intent.putExtra(RANDOM_SELECTION, radioButtonClicked);
                intent.putExtra(CURRENT_LATITUDE, latitude);
                intent.putExtra(CURRENT_LONGITUDE, longitude);
                startActivity(intent);
            }
        }
    }
}
