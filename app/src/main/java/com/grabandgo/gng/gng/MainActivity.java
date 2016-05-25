package com.grabandgo.gng.gng;

import android.animation.Animator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Projection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Main activity, GNG Map and navigation.
 */
public class MainActivity extends AppCompatActivity implements MapboxMap.OnMarkerClickListener, MapboxMap.OnMapClickListener, MapboxMap.OnScrollListener, MapboxMap.OnMapLongClickListener, MoreInfoFragment.OnOkClickedListener {

    private Controller controller;

    //MapBox
    private MapView mapView;
    private MapboxMap mMap;

    //Marker buttons
    private ImageButton markerFavouritBtn;
    private ImageButton markerDirectionsBtn;
    private ImageButton markerRatingBtn;
    private ImageButton markerCoinBtn;

    //MapBox Marker
    private ImageButton markerButton;
    private boolean markerSelected = false;
    private Marker selectedMarker;
    private RelativeLayout restaurantInfo;
    private MoreInfoFragment moreInfoFragment = new MoreInfoFragment();

    //TabBarMenu button.
    private TapBarMenu tapBarMenu;

    private ActionBarDrawerToggle mDrawerToggle;

    private ArrayList<DrawerCategory> categories = new ArrayList<DrawerCategory>();
    private ArrayList<ArrayList<DrawerSubCategory>> subCategories = new ArrayList<ArrayList<DrawerSubCategory>>();
    private ArrayList<Integer> subCategoriesCount = new ArrayList<Integer>();

    private int previousGroup = 0;
    private int checkedBox = 0;
    private Restaurant selectedRestaurant = null;


    //ICONS
    private IconFactory iconFactory;
    private Bitmap bm;
    private Drawable d;
    private Icon icon2;


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
        TextView tvStartUp = (TextView) findViewById(R.id.actionbar_title);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/splash-font.ttf");
        assert tvStartUp != null;
        tvStartUp.setTypeface(face);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populateArrayLists();

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert mDrawerLayout != null;
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        final ExpandableListView mExpandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);

        assert mExpandableListView != null;
        mExpandableListView.setAdapter(new ExpandableListViewAdapter(this, categories, subCategories, subCategoriesCount, mExpandableListView, this));

        tapBarMenu = (TapBarMenu) findViewById(R.id.tapBarMenu);
        assert tapBarMenu != null;
        tapBarMenu.setToolbar(toolbar);
        tapBarMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tapBarMenu.toggle();
            }
        });

        ImageButton btnFavourite = (ImageButton) findViewById(R.id.tap_bar_btn_favourite);
        ImageButton btnOffers = (ImageButton) findViewById(R.id.tap_bar_btn_offer);

        assert btnOffers != null;
        btnOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentOffers mOffersFragment = new FragmentOffers();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.drawer_layout, mOffersFragment).commit();
            }
        });

        assert btnFavourite != null;
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentFavourite mFavouriteFragment = new FragmentFavourite();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.drawer_layout, mFavouriteFragment).commit();
            }
        });
    }

    private boolean cameraOK = false;

    /**
     * Init MapBox components.
     *
     * @param savedInstanceState - SavedInstanceState
     */
    private void initMapBox(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                mapboxMap.getUiSettings().setLogoEnabled(false);
                mapboxMap.getUiSettings().setAttributionEnabled(false);
                mapboxMap.getUiSettings().setRotateGesturesEnabled(false);
                mapboxMap.getUiSettings().setCompassEnabled(false);
                mapboxMap.getUiSettings().setTiltGesturesEnabled(false);


                if (controller != null) {
                    //controller.checkConnection();
                }

                mMap = mapboxMap;
                mMap.setOnMarkerClickListener(MainActivity.this);
                mMap.setOnMapClickListener(MainActivity.this);
                mMap.setOnScrollListener(MainActivity.this);
                mMap.setOnMapLongClickListener(MainActivity.this);

                mMap.setMyLocationEnabled(true);
                //mMap.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);

                mapboxMap.setStyleUrl(Style.DARK);

                if(!cameraOK) {
                    CameraPosition cameraPosition2 = new CameraPosition.Builder()
                            .target(new LatLng(55.59534300000001, 13.008302500000013)) // set the camera's center position
                            .zoom(13.5)  // set the camera's zoom level
                            .tilt(25)  // set the camera's tilt
                            .build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2), 5000, null);
                    cameraOK = true;
                }

                iconFactory = IconFactory.getInstance(MainActivity.this);
                bm = BitmapFactory.decodeResource(getResources(),
                        R.drawable.loggadubbel);
                d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bm, 80, 175, true));
                icon2 = iconFactory.fromDrawable(d);

                controller.checkConnection();
            }
        });

        restaurantInfo = (RelativeLayout) findViewById(R.id.restaurantInfo);
        restaurantInfo.setClickable(false);
        restaurantInfo.setFocusable(false);
        restaurantInfo.setVisibility(View.INVISIBLE);

        initMarkerButtons();
    }

    boolean isFav = false;
    private void initMarkerButtons(){
        markerButton = (ImageButton) findViewById(R.id.markerbutton);
        markerButton.setOnClickListener(new MarkerButtonClickListener());


        markerFavouritBtn = (ImageButton)findViewById(R.id.favoritebutton);
        markerFavouritBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFavorite = false;
                int i = 0;
                int foundI = 0;
                for (Restaurant rest : getFavourites()){
                    if(rest.getID() == selectedRestaurant.getID()){
                        isFavorite = true;
                        foundI = i;
                    }
                    i++;
                }
                if(!isFavorite) {
                    markerFavouritBtn.setImageResource(R.drawable.heartred);
                    getFavourites().add(selectedRestaurant);
                }else{
                    markerFavouritBtn.setImageResource(R.drawable.heartwhite);
                    getFavourites().remove(foundI);
                }
            }
        });

        markerDirectionsBtn = (ImageButton)findViewById(R.id.directionsButton);
        markerDirectionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        markerRatingBtn = (ImageButton)findViewById(R.id.ratingbutton);
    }

    public LinkedList<Restaurant> getRestaurants(){
        return controller.fetchRestaurants();
    }

    public Restaurant getSelectedRestaurant(){
        return this.selectedRestaurant;
    }

    public void setFilterSubCategories(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            Toast.makeText(getApplicationContext(), checkBox.getContentDescription().toString(), Toast.LENGTH_SHORT).show();
            // controller.setSubCategoryTrue(checkBox.getContentDescription().toString());
            controller.setFilterCategory(checkBox.getContentDescription().toString());
            checkedBox++;
        } else {
            checkedBox--;
            if(checkedBox == 0){
                controller.initAllRest();
                controller.onlyFilterRemoval(checkBox.getContentDescription().toString());
            }else
                controller.removeFilterCategory(checkBox.getContentDescription().toString());
            // controller.setSubCategoryFalse(checkBox.getContentDescription().toString());
        }
    }

    public LinkedList<Restaurant> getFavourites() {
        return controller.getFavourites();
    }

    @Override
    public void onResume() {
        super.onResume();
        File f = new File("/data/data/com.grabandgo.gng.gng/shared_prefs/Favourites.xml");
        if(f.exists()){
            controller.getSavedFavouriteRestaurants();
        }
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        controller.saveFavouriteRestaurants();
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
        controller.saveFavouriteRestaurants();
        mapView.onDestroy();
    }

    ArrayList<Marker> m = new ArrayList<Marker>();

    public void clearMarkers(){
        mMap.removeAnnotations();

        for (Marker m1 : m){
            mMap.removeMarker(m1);
            Log.d("Rest", m1.getTitle());
        }

    }

    public void requestReconnect(){
        runOnUiThread(new Reconnect());
    }

    private class Reconnect implements Runnable{

        @Override
        public void run() {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed to connect to server \n \t Trying to reconnect", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
            controller.reconnect();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;

        int count = getFragmentManager().getBackStackEntryCount();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (count > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    public void addMarker(Restaurant restaurant) {
        Marker marker = mMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions().icon(icon2).position(new LatLng(restaurant.getLatitude(), restaurant.getLongitude())).title(String.valueOf(restaurant.getID())));
        m.add(marker);
    }

    private void openMarker(Marker marker){
        restaurantInfo.setVisibility(View.VISIBLE);
        restaurantInfo.setScaleY(0.3f);
        restaurantInfo.setScaleX(0.3f);
        Point p = getCoordinatesForLayout(restaurantInfo, marker);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        float startY = (p.y - restaurantInfo.getHeight()/8);
        float newY = p.y - restaurantInfo.getHeight() / 2;
        restaurantInfo.setX(p.x);
        restaurantInfo.setY(startY);
        restaurantInfo.animate().scaleX(1f).scaleY(1f).x(p.x).setListener(null).y(newY).setInterpolator(bounceInterpolator).setDuration(600).start();
    }

    private void closeMarker(Marker marker){
        final Point p = getCoordinatesForLayout(restaurantInfo, marker);
        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();

        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                markerSelected = false;
                restaurantInfo.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        };
        restaurantInfo.animate().scaleX(0.1f).scaleY(0.1f).x(p.x).y(p.y - restaurantInfo.getHeight()/20).setListener(listener).setInterpolator(null).setDuration(250).start();
    }

    private void toggleMarker(Marker oldmarker, final Marker newmarker){
        final Point p = getCoordinatesForLayout(restaurantInfo, oldmarker);
        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                restaurantInfo.setVisibility(View.INVISIBLE);
                openMarker(newmarker);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        restaurantInfo.animate().scaleX(0.1f).scaleY(0.1f).x(p.x).y(p.y).setListener(listener).setInterpolator(null).setDuration(250).start();
    }

    private void populateArrayLists() {

        String[] categoriesName = {"Filter"};
        String[] subAsian = {"Turkisk", "Orientalisk", "Amerikansk", "Skandinavisk","Steakhouse",
                "Bistro" , "Ekologiskt","Vegetarisk","Alternativ Vegetarisk", "Gryta","Sallad", "Fisk",
                "Kebab", "Falafel", "Hamburgare", "Serveringstillstånd", };

        categories.clear();

        for (int i = 0; i < categoriesName.length; i++) {
            DrawerCategory drawerCategory = new DrawerCategory();
            drawerCategory.setCategory(categoriesName[i]);
            categories.add(drawerCategory);
        }

        subCategories.clear();

        ArrayList<DrawerSubCategory> tempSubCat = new ArrayList<DrawerSubCategory>();
        DrawerSubCategory drawerSubCategory;

        for (int i = 0; i < subAsian.length; i++) {
            drawerSubCategory = new DrawerSubCategory();
            drawerSubCategory.setSubCategory(subAsian[i]);
            tempSubCat.add(drawerSubCategory);

        }

        subCategories.add(tempSubCat);
        subCategoriesCount.add(tempSubCat.size());

        tempSubCat = null;
    }

    boolean moreInfoSet = false;

    private class MarkerButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            final RelativeLayout container = (RelativeLayout) findViewById(R.id.fragment_container);
            FragmentManager fragmentManager = getFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            moreInfoFragment = MoreInfoFragment.newInstance(selectedRestaurant.getName(), selectedRestaurant.getRestImgByte());


            if(!moreInfoSet) {
                fragmentTransaction.add(R.id.fragment_container, moreInfoFragment, "More Info");
            }else{
                fragmentTransaction.replace(R.id.fragment_container, moreInfoFragment, "More Info");
            }


            fragmentTransaction.commit();
            closeMarker(selectedMarker);

            mapView.animate().alpha(0.3f).setDuration(750).start();
            tapBarMenu.setVisibility(View.INVISIBLE);
            tapBarMenu.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        LinkedList<Restaurant> restaurants = getRestaurants();
        for(Restaurant r : restaurants){
            if(r.getID() == Integer.valueOf(marker.getTitle())){
                selectedRestaurant = r;
                Log.d("GnG", "SELECTED: " + r.getName());
                break;
            }
        }

        setUpRestaurantInfoOnClick();

        if (!markerSelected) {
            openMarker(marker);
        }else if (markerSelected){
            toggleMarker(selectedMarker, marker);
        }
        markerSelected = true;
        selectedMarker = marker;
        return true;
    }

    private void setUpRestaurantInfoOnClick(){

        ImageView imageLogo = (ImageView)findViewById(R.id.restaurantLogoIV);
        byte[] b = selectedRestaurant.getRestImgByte();
        if(b != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(selectedRestaurant.getRestImgByte(), 0, selectedRestaurant.getRestImgByte().length);
            imageLogo.setImageBitmap(bm);
        }

        boolean isFavorite = false;
        for (Restaurant rest : getFavourites()){
            if(rest.getID() == selectedRestaurant.getID()){
                isFavorite = true;
            }
        }
        if(isFavorite){
            markerFavouritBtn.setImageResource(R.drawable.heartred);
        }else{
            markerFavouritBtn.setImageResource(R.drawable.heartwhite);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

        if (selectedMarker != null && markerSelected) {
            markerSelected = false;
            closeMarker(selectedMarker);
        }
    }

    @Override
    public void onScroll() {
        if (selectedMarker != null && markerSelected) {
            markerSelected = false;
            closeMarker(selectedMarker);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (selectedMarker != null && markerSelected) {
            markerSelected = false;
            closeMarker(selectedMarker);
        }
    }

    @Override
    public void onOkClickedListener() {
        getFragmentManager().beginTransaction().remove(moreInfoFragment).commit();
        tapBarMenu.setVisibility(View.VISIBLE);
        mapView.animate().alpha(1f).setDuration(700).start();
    }
}
