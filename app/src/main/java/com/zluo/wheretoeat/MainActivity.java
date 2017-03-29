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
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.ActivityCompatApi23;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;

//import com.google.android.gms.drive.metadata.SearchableCollectionMetadataField;

public class MainActivity extends AppCompatActivity implements UserSelectionFragment.UserSelectionListener,
        DisplayResultFragment.DisplayResultListener{

    private static FragmentTransaction fragmentTransaction;

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

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.refresh);
        floatingActionButton.setVisibility(View.INVISIBLE);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        UserSelectionFragment userSelectionFragment = new UserSelectionFragment();
        fragmentTransaction.add(R.id.fragment_container, userSelectionFragment).commit();

    }

    @Override
    public void setUserSelection(HashMap<String, String> userSelection, boolean random) {
        //add animation
        DisplayResultFragment displayResultFragment = DisplayResultFragment.newInstance(userSelection, random);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, displayResultFragment);
        Log.d("addfragmentbackstac","addingfragmenttobackstack");
        fragmentTransaction.addToBackStack(null).commit();
    }

    @Override
    public void displayResult(Uri uri) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
