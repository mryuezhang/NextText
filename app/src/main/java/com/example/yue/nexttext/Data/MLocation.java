package com.example.yue.nexttext.Data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.yue.nexttext.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Array;
import java.util.Locale;

/**
 * Created by devonplouffe on 2017-10-22.
 */

public class MLocation implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private LocationRequest locationRequest = null;
    private GoogleApiClient client;
    private boolean mRequestingLocationUpdates;
    private Location location = null;
    private FragmentActivity main = null;
    public MLocation(FragmentActivity m) {
//        super.onCreate(savedInstanceState);
        mRequestingLocationUpdates = true;
        main = m;
        // Create a GoogleApiClient instance
        client = new GoogleApiClient.Builder(m)
                .enableAutoManage(m /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
//                    .addScope(Lo.SCOPE_FILE)
                .build();

        locationRequest = new LocationRequest().create();
//    locationRequest.
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
//        super.onConnectionFailed();
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently
        Log.e("Err:",result.toString());
        // ...
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(main, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(main, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                client, locationRequest, this);
    }



    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    public double[] getLocation(){

        return new double[]{this.location.getLongitude(), this.location.getLatitude()};
    }
}
