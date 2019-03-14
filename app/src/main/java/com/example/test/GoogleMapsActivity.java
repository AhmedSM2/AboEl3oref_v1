package com.example.test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GoogleMapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,//added
        GoogleApiClient.OnConnectionFailedListener,//added
        LocationListener //added
{

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient; //to use in buildGoogleApi Method
    private LocationRequest locationRequest; //to use in onConnected method
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private double latitude ,longitude;
    private int ProximityRaduis = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onClick(View v)
    {
        String hospital = "hospital" , school = "school" , resturant = "resturant";
        Object transferData[] = new Object[2];
        getNearbyPlaces getNearbyPlaces = new getNearbyPlaces();

        switch (v.getId())
        {
            case R.id.search_place_icon:
                EditText address = findViewById(R.id.search_place_bar);
                String address_text = address.getText().toString();

                List<Address> addressList = null;

                MarkerOptions Addrees_marker_options = new MarkerOptions();

                if(!TextUtils.isEmpty(address_text))
                {
                    Geocoder geocoder = new Geocoder(this);

                    try
                    {
                        addressList = geocoder.getFromLocationName(address_text , 6);

                        if(addressList != null)
                        {
                            for(int i =0 ; i<addressList.size() ; i++)
                            {
                                Address single_address = addressList.get(i);

                                LatLng latLng = new LatLng(single_address.getLatitude() , single_address.getLongitude());

                                Addrees_marker_options.position(latLng);
                                Addrees_marker_options.title(address_text);
                                Addrees_marker_options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                mMap.addMarker(Addrees_marker_options);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


                            }
                        }
                        else
                        {
                            Toast.makeText(this, "Loaction not found try again...", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(this, "Please write any place...", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.hospital_category_icon:
                mMap.clear();
                String url1 = getUrl( latitude , longitude , hospital);
                transferData[0] = mMap;
                transferData[1] = url1;

                Toast.makeText(this, "searching nearby hospitals ...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing nearby hospitals ...", Toast.LENGTH_SHORT).show();

                break;


            case R.id.school_category_icon:
                mMap.clear();
                String url2 = getUrl( latitude , longitude , school);
                transferData[0] = mMap;
                transferData[1] = url2;

                Toast.makeText(this, "searching nearby schools ...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing nearby shcools ...", Toast.LENGTH_SHORT).show();

                break;


            case R.id.resturant_category_icon:
                mMap.clear();
                String url3 = getUrl( latitude , longitude , resturant);
                transferData[0] = mMap;
                transferData[1] = url3;

                Toast.makeText(this, "searching nearby resturants ...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing nearby resturants ...", Toast.LENGTH_SHORT).show();

                break;


        }
    }


    private String getUrl(double latitude ,double longitude ,String nearbyPlace)
    {
        StringBuilder googleUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleUrl.append("location=" + latitude + "," + longitude);
        googleUrl.append("&raduis=" + ProximityRaduis);
        googleUrl.append("&type=" + nearbyPlace);
        googleUrl.append("&sensor=true");
        googleUrl.append("&key=" + "AIzaSyD2KNlfADCtWGpLJdWJOS8P66wRSHE51sk");

        Log.d("GoogleMapsActivity" , "url : " + googleUrl.toString());

        return googleUrl.toString();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(ContextCompat.checkSelfPermission(this ,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            //call buid google api client method
            buildGoogeApiClient();

            mMap.setMyLocationEnabled(true);


            return;
        }
    }

    //checkUserLocationPermission method ------------------------------------------
    public boolean checkUserLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this ,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , Request_User_Location_Code);

            }
            return false;

        }
        else
        {
            return true;
        }
    }
    //------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        switch (requestCode)
        {
            case Request_User_Location_Code:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this ,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient == null)
                        {
                            buildGoogeApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }

    }


    //-----------------buid google api client method -------------------------

    protected synchronized void buildGoogeApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    //-----------------------------------------------------------------------

    @Override
    public void onLocationChanged(Location location)
    {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;

        if(currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("your current location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if(googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient ,  this);
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this ,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient , locationRequest , this);

        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
