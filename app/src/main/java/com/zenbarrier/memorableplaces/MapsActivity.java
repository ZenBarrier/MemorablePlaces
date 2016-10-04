package com.zenbarrier.memorableplaces;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<String> places, currentPlaces;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setTitle("My Map");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        geocoder = new Geocoder(this, Locale.getDefault());
        ArrayList<String> sentPlaces = getIntent().getStringArrayListExtra("places");
        if(sentPlaces != null) {
            currentPlaces = sentPlaces;
        }else{
            currentPlaces = new ArrayList<>();
        }
        places = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                return backHome();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        backHome();
        super.onBackPressed();
    }

    boolean backHome(){
        Intent intent = new Intent();
        intent.putStringArrayListExtra("places",places);
        if (getParent() == null){
            setResult(RESULT_OK, intent);
        }
        else{
            getParent().setResult(RESULT_OK, intent);
        }
        this.finish();
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                try {
                    List<Address> addressList;
                    addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList.size() > 0) {
                        String name = addressList.get(0).getAddressLine(0);
                        places.add(name);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        for (int i = 0; i < currentPlaces.size(); i++) {
            try {
                List<Address> addressList = geocoder.getFromLocationName(currentPlaces.get(i), 1);
                if (addressList.size() > 0) {
                    double lat = addressList.get(0).getLatitude();
                    double lng = addressList.get(0).getLongitude();
                    LatLng place = new LatLng(lat, lng);
                    String title = addressList.get(0).getAddressLine(0);
                    mMap.addMarker(new MarkerOptions().position(place).title(title));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String selectedPlace = getIntent().getStringExtra("selectedPlace");
        if (selectedPlace != null) {
            try {
                List<Address> addressList = geocoder.getFromLocationName(selectedPlace, 1);
                if (addressList.size() > 0) {
                    double lat = addressList.get(0).getLatitude();
                    double lng = addressList.get(0).getLongitude();
                    LatLng place = new LatLng(lat, lng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 10));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
