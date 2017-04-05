package com.fetchpups.android.fetch;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DogParkMapsFragment extends Fragment implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMyLocationButtonClickListener {

    //Map & Location Data
    private GoogleMap googleMap;
    private MapView mMapView;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;


    //Set default coordinates to USF classroom
    double lastLatitude = 28.058412;
    double lastLongitude = -82.410019;

    //ID for the permission request
    private static final int READ_LOCATION_PERMISSIONS_REQUEST = 1;

    //JSON Data
    RequestQueue mRequestQueue;

    //Layout variables
    View rootView;
    Button btnParks, btnStores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.location_fragment, container, false);

        mRequestQueue = Volley.newRequestQueue(getActivity());

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnParks = (Button) rootView.findViewById(R.id.btnParks);
        btnStores = (Button) rootView.findViewById(R.id.btnStores);

        initBtnListeners();

        mMapView.getMapAsync(this);
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
        googleMap.setOnMyLocationButtonClickListener(this);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestAndUpdateLastLocation();
    }

    private void initBtnListeners(){
        btnParks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", "btnPark clicked");
                googleMap.clear();
                processPlacesAPI(lastLatitude, lastLongitude, 1);
                Snackbar.make(rootView, "Nearby Dog Parks", Snackbar.LENGTH_LONG).show();
            }
        });

        btnStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", "btnStores clicked");
                googleMap.clear();
                processPlacesAPI(lastLatitude, lastLongitude, 2);
                Snackbar.make(rootView, "Nearby Dog Stores", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void requestAndUpdateLastLocation(){
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation != null){
            updateValuesAndCameraWithLastLocation(mLastLocation);
        }
    }

    public void updateValuesAndCameraWithLastLocation(Location loc){

        lastLatitude = loc.getLatitude();
        lastLongitude = loc.getLongitude();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLatitude, lastLongitude), 11));


    }

    public void processPlacesAPI(double latitude, double longitude, final int sel){
        String jsonURL = getUrl(latitude, longitude, sel);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, jsonURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("results");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject fullLocationInfo = jsonArray.getJSONObject(i);
                                String locationTitle = fullLocationInfo.getString("name");
                                JSONObject locationCoord = fullLocationInfo.getJSONObject("geometry").getJSONObject("location");
                                double mLatitude = locationCoord.getDouble("lat");
                                double mLongitude = locationCoord.getDouble("lng");

                                Log.i("jsonObjectRequest", locationTitle);
                                insertLocationMarkers(mLatitude, mLongitude, locationTitle, sel);
                            }


                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("jsonObjectRequest", "Error while fetching JSON");
                    }
                }
        );
        mRequestQueue.add(jsonObjectRequest);
    }

    private void insertLocationMarkers(double latitude, double longitude, String title, int sel){
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng placeLoc = new LatLng(latitude, longitude);
        markerOptions.position(placeLoc);
        markerOptions.title(title);
        if(sel == 1){
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_paw));
        } else{
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_store));
        }
        googleMap.addMarker(markerOptions);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        requestAndUpdateLastLocation();
        return false;   //Return false is basically calling the super method
    }

    private String getUrl(double latitude, double longitude, int nearbyPlace){
        //1 = dog parks     2 = pet_stores
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        if(nearbyPlace == 1){
            googlePlacesUrl.append("location=" + latitude + "," + longitude);
            googlePlacesUrl.append("&radius=" + "30000");   //30,000 meters = ~20 miles
            googlePlacesUrl.append("&type=" + "park");
            googlePlacesUrl.append("&keyword=" + "dog");
            googlePlacesUrl.append("&key=" + "AIzaSyCRifebkgQdcCRnSNYdkb8G6HsuySjlCpI"); //Personal Google Places Web API
        }

        if(nearbyPlace == 2){
            googlePlacesUrl.append("location=" + latitude + "," + longitude);
            googlePlacesUrl.append("&radius=" + "30000");   //30,000 meters = ~20 miles
            googlePlacesUrl.append("&type=" + "pet_store");
            googlePlacesUrl.append("&key=" + "AIzaSyCRifebkgQdcCRnSNYdkb8G6HsuySjlCpI"); //Personal Google Places Web API
        }

        Log.d("getUrl", googlePlacesUrl.toString());

        return googlePlacesUrl.toString();
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
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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
                    //(Permission granted) Do location related work here
                    if(mGoogleApiClient == null){
                        buildGoogleApiClient();
                    }
                    googleMap.setMyLocationEnabled(true);
                } else {
                    //TODO: (Permission denied). Disable functionality that depends on this permission
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }
}