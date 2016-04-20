package com.grabandgo.gng.gng;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Main activity, GNG Map and navigation.
 * Brachn kasbdsfouds
 */
public class MainActivity extends Activity implements GoogleMap.OnMarkerClickListener {



    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private double latitude;
    private double longitude;
    private String provider;
    private MapFragment mapFragment;
    private GoogleMap map;
    private Controller cont = new Controller(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMap();
    }
    
    
    public void initMap() {
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
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(new OMRC());



    }
    
    private class OMRC implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;

            Log.d("GnG", "Map Ready");

         //   customMarker();

            latitude = 56.1;
            longitude = 13.2;

            map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))

            );
            cont.getRestaurants();
        }
    }

    public void addMarker(String lat){

       Update up =  new Update(lat);
        runOnUiThread(up);


    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

     //   centerMarkerOnScreen(marker);

        return true;
    }



        private class LocationList implements LocationListener {
        
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            //     addMarker(new LatLng(latitude, longitude),user);

            map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
            );
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


    private void customMarker() {
        SparseArray<BitmapDescriptor> iconCache = new SparseArray<>();
        int iconResId = (R.drawable.start_icon_temp);

        BitmapDescriptor icon = iconCache.get(iconResId);
        if (icon == null) {
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getResources().getDrawable(iconResId, null);
            } else {
                drawable = getResources().getDrawable(iconResId);
            }
            if (drawable instanceof GradientDrawable) {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                drawable.draw(canvas);
                icon = BitmapDescriptorFactory.fromBitmap(bitmap);
                bitmap.recycle();
            } else {
                icon = BitmapDescriptorFactory.fromResource(iconResId);
            }
            iconCache.put(iconResId, icon);

            map.addMarker(new MarkerOptions()
                    .icon(icon)
                    .position(new LatLng(latitude, longitude)));
        }

        Log.d("GnG", latitude+  " " + longitude);
    }

    private void centerMarkerOnScreen(Marker marker){

        //get the map container height
        RelativeLayout mapContainer = (RelativeLayout) findViewById(R.id.mapLayout);
        int container_height = mapContainer.getHeight();

        Projection projection = map.getProjection();

        LatLng markerLatLng = new LatLng(marker.getPosition().latitude,
                marker.getPosition().longitude);
        Point markerScreenPosition = projection.toScreenLocation(markerLatLng);
        Point pointHalfScreenAbove = new Point(markerScreenPosition.x,
                markerScreenPosition.y - (container_height / 2));

        LatLng aboveMarkerLatLng = projection
                .fromScreenLocation(pointHalfScreenAbove);

        marker.showInfoWindow();
        CameraUpdate center = CameraUpdateFactory.newLatLng(aboveMarkerLatLng);
        map.moveCamera(center);

    }


    private class Update implements Runnable{
        private String lat;

        public Update(String lat){
            this.lat = lat;
        }

        @Override
        public void run() {
            latitude = Double.parseDouble(lat);

            Log.d("ADDING MARKER", latitude + "");
            map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
            );
        }
    }


}
