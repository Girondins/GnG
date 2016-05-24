package com.grabandgo.gng.gng;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Controller class.
 */
public class Controller {

    private LinkedList<Restaurant> restaurants;
    private Client client;
    private MainActivity main;
    private Object lock = new Object();
    private LinkedList<Restaurant> filterdRestaurants = new LinkedList<Restaurant>();
    private LinkedList<Restaurant> favouriteRestaurants;
    private LinkedList<String> filtersChecked = new LinkedList<String>();

   // private Filter filter = new Filter();

    public Controller(MainActivity main) {
        this.main = main;
        favouriteRestaurants = new LinkedList<Restaurant>();
        try {
            Log.d("Connecting", "client");
           // filter = new Filter();
            client = new Client("192.168.1.215", 3000, this);
            client.enableConnect();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

   /** public void setSubCategoryTrue(String subCategory){
        filter.setSubCategoryTrue(subCategory);
        filterRestaurants();
    }

    public void setSubCategoryFalse(String subCategory){
        filter.setSubCategoryFalse(subCategory);
        filterRestaurants();
    }

    **/

    public void setFilterCategory(String category){
        filtersChecked.add(category);
        filterRestaurants();
    }

    public void removeFilterCategory(String category){
        Log.d("REmoving Cat", category);
        filtersChecked.remove(category);
        Log.d("Check", filtersChecked.size() + "");
        filterRestaurants();
    }

    public void onlyFilterRemoval(String category){
        filtersChecked.remove(category);
    }

    public void initAllRest(){
        initiateRestaurants(restaurants);
    }

    public void getRestaurants() {
        client.request("getRestaurants");
    }

    public LinkedList<Restaurant> fetchRestaurants(){
        return restaurants;
    }

    public void setRestaurants(LinkedList<Restaurant> restaurants) {
        this.restaurants = restaurants;
        initiateRestaurants(restaurants);
    }

    public void filterRestaurants() {
        Log.d("Restaurant Filteeeerr", filterdRestaurants.size() + "");
        checkFil();
    //    Thread t = new Thread(new CheckFilter());
    //    t.start();
    }

    public void initiateRestaurants(LinkedList<Restaurant> initRest) {
        for (int i = 0; i < initRest.size(); i++) {
            main.addMarker(initRest.get(i));
        }
      //  filterRestaurants();
    }

    public void reconnect(){
        client.enableConnect();
    }

    public void checkConnection(){
        if(client.checkConnection() == null){
            main.requestReconnect();
        }else
            getRestaurants();
    }

   /** public LinkedList<Restaurant> saveFavouriteRestaurant(){
        return this.favouriteRestaurants;
    }
    **/

   public void saveFavouriteRestaurants(){
       SharedPreferences sp = main.getSharedPreferences("Favourites", Activity.MODE_PRIVATE);
       SharedPreferences.Editor editor = sp.edit();
       Gson gson = new Gson();
       String json = gson.toJson(favouriteRestaurants);
       editor.putString("favouritRests",json);
       editor.commit();

   }

    public void getSavedFavouriteRestaurants(){
        SharedPreferences sp = main.getSharedPreferences("Favourites", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("favouritRests","");

        Log.d("GnG", "JSON: " + json);

        JSONArray obj = null;
        try {
            obj = new JSONArray(json);

            Log.d("GnG", "Array: " +obj.toString());


        for (int i = 0; i < obj.length(); i++){
            JSONObject o = obj.getJSONObject(i);
            int ID = (int)o.get("ID");
            byte[] logoSrc = (byte[])o.get("logo");
            double lat = (double)o.get("latitude");
            double lon = (double)o.get("longitude");
            String name = (String)o.get("name");
            int rating = (int)o.get("rating");
            byte[] picSrc = (byte[])o.get("restaurantPic");

            Restaurant r = new Restaurant();
            r.setID(ID);
      //      r.setLogoRaw(logoSrc);
            r.setLongitude(lon);
            r.setLatitude(lat);
            r.setName(name);
            r.setRating(rating);
      //      r.setRestaurantPicRaw(picSrc);

        }

        }catch (Exception e){
            Log.d("GnG", e.getMessage());
        }


        /*
        JSONObject fromJson = gson.fromJson(json,JSON.class);
        int count = fromJson.size() / 7;
        int j = 0;
        for(int i = 0; i < count; i++){

            Restaurant temp = new Restaurant();
            temp.setID((int)(fromJson.get(i * 7)));
            temp.setLatitude((double)(fromJson.get(i * 7)));
            temp.setImageSource(i * 7 + 2);
            temp.setLongitude(i*7 + 3);




            for(j = j*i ; j<= i*7 ; j++) {
                Restaurant restaurant = new Restaurant();
                restaurant.setID(fromJson.get());
            }
        }
        Log.d("GnG", json);
        */
    }

    public LinkedList<Restaurant> getFavourites(){
        return favouriteRestaurants;
    }

    public void addToFavourite(Restaurant restaurant){
        favouriteRestaurants.add(restaurant);
    }

    public void removeFromFavourite(Restaurant restaurant){
        for(int i = 0; i<favouriteRestaurants.size(); i++){
            if(favouriteRestaurants.get(i).getID() == restaurant.getID()){
                favouriteRestaurants.remove(i);
            }
        }
    }


    public void checkFil(){
        int factor, from, to, threadCount = 10;
            filterdRestaurants.clear();

        Log.d("Filterd Resteee", filterdRestaurants.size() + "");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        if (restaurants.size() < threadCount) {
            //  new Thread(new Filtering(0, restaurants.size(), restaurants, filtersChecked)).start();
            threadCount = 1;
        }
        factor = (restaurants.size() / threadCount);
        for (int i = 0; i < threadCount; i++) {
            from = i * factor;
            to = i * factor + factor;
            executorService.execute(new Filtering(from, to, restaurants, filtersChecked));
            Log.d("Factor Threads", threadCount + " " + factor);
        }
        executorService.shutdown();
        main.clearMarkers();
        Log.d("Clear markers", "true");
        initiateRestaurants(filterdRestaurants);
        Log.d("Filterd Rest", filterdRestaurants.size() + "");
    }

    private class CheckFilter implements Runnable {
        int factor, from, to, threadCount = 10;

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void run() {
            ExecutorService executeService = Executors.newFixedThreadPool(10);
            if (restaurants.size() < threadCount) {
              //  new Thread(new Filtering(0, restaurants.size(), restaurants, filtersChecked)).start();
                threadCount = 1;
            }
                factor = (restaurants.size() / threadCount);
                for (int i = 0; i < threadCount; i++) {
                    from = i * factor;
                    to = i * factor + factor;
                    executeService.execute(new Filtering(from, to, restaurants, filtersChecked));
                    Log.d("Factor Threads", threadCount + "");
                }
                executeService.shutdown();
                main.clearMarkers();
                Log.d("Clear markers", "true");
                initiateRestaurants(filterdRestaurants);
                Log.d("Filterd Rest", filterdRestaurants.size() + "");
                try {
                    executeService.awaitTermination(1, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }
    }

    private class Filtering implements Runnable {
        private int from, to;
        private LinkedList<Restaurant> restaurants;
        private LinkedList<String> filter;
        private ArrayList<String> currentRestFilter;

        public Filtering(int from, int to, LinkedList<Restaurant> restaurants, LinkedList<String> filter) {
            this.from = from;
            this.to = to;
            this.restaurants = restaurants;
            this.filter = filter;
        }

        @Override
        public void run() {
            Log.d("Filterd Thread", filterdRestaurants.size() + "");
            Log.d("From", from +" " + to);
            for (int i = from; i < to; i++) {
                currentRestFilter = restaurants.get(i).getFilters();
                for(int j = 0 ; j<filter.size(); j++){
                    Log.d("Checking Filter", filter.get(j));
                    if(currentRestFilter.contains(filter.get(j))){
                            filterdRestaurants.add(restaurants.get(i));
                        Log.d("Adding Rest", restaurants.get(i).getName());
                    }
                }



        /**        if(compareFilters(filter,currentRestFilter)){
                    filterdRestaurants.add(restaurants.get(i));
                    Log.d("AddingRest",restaurants.get(i).getName());
                }

         **/
            }
        }
    }

  /**  public boolean compareFilters(Filter appFilters, Filter restFilters){
        int checked = 0;
        int checkedFilters = appFilters.getCheckedCats();

        for(int i = 0; i<appFilters.length() ; i++){
            Log.d("Comparing: " + checkedFilters , appFilters.getCategoryStatus(i) + "//" + restFilters.getCategoryStatus(i));
            if(appFilters.getCategoryStatus(i) == true && restFilters.getCategoryStatus(i) == true){
                Log.d("Bo","Returning");
                checked++;
                if(checked == checkedFilters) {
                    return true;
                }
            }
        }

        return false;


    }
   **/

}
