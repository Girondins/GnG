package com.grabandgo.gng.gng;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private double latitude;
    private double longitude;
    private String provider;
    
    private MapFragment mapFragment;
    private GoogleMap map;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  initateMap();
    }
    
    
    public void initateMap() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationList();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,
                                               10, locationListener);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OMRC());
        
    }
    
    private class OMRC implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
        }
    }
    
    /** private void addMarker() {
     MarkerOptions mo = new MarkerOptions().position(latLng).title(member);
     map.addMarker(mo);
     
     }
     
     public void clearMarker(){
     map.clear();
     }
     **/
    
    private class LocationList implements LocationListener {
        
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            //     addMarker(new LatLng(latitude, longitude),user);
            
        }
        
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            
        }
        
        @Override
        public void onProviderEnabled(String provider) {
            
        }
        
        @Override
        public void onProviderDisabled(String provider) {
            
        }
    }
}
