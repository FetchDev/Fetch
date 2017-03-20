package com.fetchpups.android.fetch;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.LOCATION_SERVICE;
import static com.fetchpups.android.fetch.R.id.map;


public class DogParkMapsFragment extends Fragment implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //Map & Location Data
    private GoogleMap googleMap;
    private MapView mMapView;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    //Set default permissions to USF classroom
    double latitude     = 28.058412;
    double longitude    = -82.410019;

    //ID for the permission request
    private static final int READ_LOCATION_PERMISSIONS_REQUEST = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.location_fragment, container, false);

//        getLocationPermission();

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        //Old getMapAsync code
        //This code contains a map fragment - meant to show on the bottom of the current view
//        mMapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap mMap) {
//                googleMap = mMap;
//
//
//                // For showing a move to my location button
////                googleMap.setMyLocationEnabled(true);
//
//
//
//                // For dropping a marker at a point on the Map
//                LatLng currentLocation = new LatLng(latitude, longitude);
//                googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location").snippet("Marker Description"));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            }
//
//
//        });



        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;


        //Initialize Google Play Services
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                //Location permission already granted
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            } else{
                //Request location permission since it hasn't been granted (yet)
                getLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }

//        // For dropping a marker at a point on the Map
//        LatLng currentLocation = new LatLng(latitude, longitude);
//        googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location").snippet("Marker Description"));
//
//        // For zooming automatically to the location of the marker
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation != null){
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            LatLng currLoc = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(currLoc).title("Current Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 11));
        }


//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }



    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
//            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        //Stop location updates when Fragment no longer active
        if(mGoogleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    //Called when the user performs an action that requires the user's location
    public void getLocationPermission(){
        // Always check for permission (even after granted) since a user can revoke perms at any time
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //Permission not already granted
            //Check if user has been asked about this permission already and denied it. If so, give explanation for permission usage
            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                //Show some UI to explain reasoning. Probably a Toast.
                new AlertDialog.Builder(getActivity())
                        .setTitle("location Permission Required")
                        .setMessage("This app requires the Location permission for proper functionality. Please accept to enable this functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Prompt the user once explanation has been shown
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, READ_LOCATION_PERMISSIONS_REQUEST);
                            }
                        })
                        .create().show();
            } else {
                //No explanation necessary, we can just request the permission
                //Displays standard permission request UI
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, READ_LOCATION_PERMISSIONS_REQUEST);
            }
        }
    }

    //Callback with the request from calling requestPermission

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode)
        {
            case READ_LOCATION_PERMISSIONS_REQUEST: {
                //If request cancelled, results[] is empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //TODO: (Permission granted) Do location related work here
                    if(mGoogleApiClient == null){
                        buildGoogleApiClient();
                    }
                    googleMap.setMyLocationEnabled(true);
                } else {
                    //(Permission denied). Disable functionality that depends on this permission
                    //TODO: Set longitude/latitude to some default values before this?
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

        //Make sure it's our original ACCESS_FINE_LOCATION request
//        if(requestCode == READ_LOCATION_PERMISSIONS_REQUEST) {
//            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //TODO: Do our location related tasks here?
//                Toast.makeText(getActivity(), "Access fine location permission granted", Toast.LENGTH_SHORT).show();
//            } else{
//                //showRationale = false is user clicks Never Ask Again, otherwise true
//                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
//
//                if(showRationale) {
//                    // do something to handle degraded mode?
//                } else{
//                    Toast.makeText(getActivity(), "Access fine location permission denied", Toast.LENGTH_SHORT).show();
//                }
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
    }
}