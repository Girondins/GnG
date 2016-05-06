package com.grabandgo.gng.gng;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Projection;

/**
 * Main activity, GNG Map and navigation.
 * För att appen ska kunna köras måste servern vara igång.
 */
public class MainActivity extends AppCompatActivity {

    private Controller controller;

    //MapBox
    private MapView mapView;
    private MapboxMap mMap;

    //Animations
    private RelativeLayout restaurantInfo;
    private ScaleAnimation scaleAnim;
    private static final TranslateAnimation transAnim = new TranslateAnimation(0, 0, 0, 20);
    private static AnimationSet animSet = new AnimationSet(true);
    private static final BounceInterpolator bounceInterpolator = new BounceInterpolator();

    //TabBarMenu button.
    private TapBarMenu tapBarMenu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new Controller(this);

        initGUI();
        initMapBox(savedInstanceState);
    }

    /**
     * Init GUI components.
     */
    private void initGUI() {
        //Change FontFace in actionbar.
        TextView tv = (TextView) findViewById(R.id.actionbar_title);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/splash-font.ttf");
        tv.setTypeface(face);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.setDrawerShadow(R.drawable.drawershadow, GravityCompat.START);

        tapBarMenu = (TapBarMenu) findViewById(R.id.tapBarMenu);
        tapBarMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tapBarMenu.toggle();
            }
        });
    }

    /**
     * Init MapBox components.
     * @param savedInstanceState - SavedInstanceState
     */
    private void initMapBox(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                if (controller != null) {
                    controller.checkConnection();
                }

                mMap = mapboxMap;

                mMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        restaurantSelected(marker);
                        return true;
                    }
                });

                mMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {

                    }
                });
                mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);

                IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                Drawable iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.markerlarge);
                Icon icon = iconFactory.fromDrawable(iconDrawable);

                mMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions().position(new LatLng(10, 10)).title("TEST"));

                // Set the camera's starting position
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(41.885, -87.679)) // set the camera's center position
                        .zoom(1)  // set the camera's zoom level
                        .tilt(20)  // set the camera's tilt
                        .build();

                // Move the camera to that position
                mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        restaurantInfo = (RelativeLayout) findViewById(R.id.restaurantInfo);
        restaurantInfo.setVisibility(View.INVISIBLE);
    }

    /**
     * Init animations.
     *
     * @param marker - Marker
     */
    private void initAnimations(Marker marker) {

        Point p = getCoordinatesForLayout(restaurantInfo, marker);
        float pivotX = p.x + restaurantInfo.getWidth() / 2;
        float pivotY = p.y + restaurantInfo.getHeight() / 2;

        restaurantInfo.setX(p.x);
        restaurantInfo.setY(p.y);

        scaleAnim = new ScaleAnimation(0.1f, 1f, 0.1f, 1f, ScaleAnimation.ABSOLUTE, pivotX, ScaleAnimation.ABSOLUTE, pivotY);

        animSet.reset();
        animSet.addAnimation(scaleAnim);
        animSet.addAnimation(transAnim);
        animSet.setFillAfter(true);
        animSet.setDuration(700);

        animSet.setInterpolator(bounceInterpolator);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private Point getCoordinatesForLayout(RelativeLayout relativeLayout, Marker marker) {
        Projection p = mMap.getProjection();
        LatLng ll = marker.getPosition();
        PointF pf = p.toScreenLocation(ll);

        int x = Math.round(pf.x) - relativeLayout.getWidth() / 2;
        int y = Math.round(pf.y) - relativeLayout.getHeight() / 2;

        Point point = new Point(x, y);

        return point;
    }

    private void restaurantSelected(Marker marker) {
        initAnimations(marker);

        restaurantInfo.setVisibility(View.VISIBLE);

        scaleAnim.setDuration(400);
        scaleAnim.setFillAfter(true);

        restaurantInfo.startAnimation(animSet);

        /**
         //TODO: TEMP ID IGEN
         if(controller != null && controller.getRestaurantList() != null) {
         for (Restaurant r : controller.getRestaurantList()) {
         if (Integer.toString(r.getID()).equals(marker.getTitle())) {
         restaurantSelected(r);
         }
         }
         }
         **/
    }

    public void addMarker(Restaurant restaurant) {
        mMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions().position(new LatLng(restaurant.getLatitude(), restaurant.getLongitude())));
    }

    public void requestReconnect(){
        runOnUiThread(new Reconnect(controller));
    }

    private class Reconnect implements Runnable {
        private Controller cont;

        public Reconnect(Controller cont){
            this.cont = cont;
        }

        @Override
        public void run() {
            Toast toast = Toast.makeText(getApplicationContext(),"Failed to connect to server \n  \t Trying to reconnect",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            cont.reconnect();
        }

    }
}
