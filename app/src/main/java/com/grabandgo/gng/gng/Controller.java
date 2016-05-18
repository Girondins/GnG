package com.grabandgo.gng.gng;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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
    private LinkedList<Restaurant> filterdRestaurants;
    private LinkedList<Restaurant> favouriteRestaurants;
    private Filter filter = new Filter();

    public Controller(MainActivity main) {
        this.main = main;
        favouriteRestaurants = new LinkedList<Restaurant>();
        addToFavourite(new Restaurant("Hej"));
        try {
            Log.d("Connecting", "client");
            filter = new Filter();
            client = new Client("192.168.1.215", 3000, this);
            client.enableConnect();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void setSubCategoryTrue(String subCategory){
        filter.setSubCategoryTrue(subCategory);
        filterRestaurants();
    }

    public void setSubCategoryFalse(String subCategory){
        filter.setSubCategoryFalse(subCategory);
        filterRestaurants();
    }

    public void getRestaurants() {
        client.request("getRestaurants");
    }

    public void setRestaurants(LinkedList<Restaurant> restaurants) {
        this.restaurants = restaurants;
        initiateRestaurants(restaurants);
    }

    public void filterRestaurants() {
        filterdRestaurants = new LinkedList<Restaurant>();
        Thread t = new Thread(new CheckFilter());
        t.start();
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
            r.setLogoRaw(logoSrc);
            r.setLongitude(lon);
            r.setLatitude(lat);
            r.setName(name);
            r.setRating(rating);
            r.setRestaurantPicRaw(picSrc);

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

    private class CheckFilter implements Runnable {
        int from, to, threadCount = 10;

        @Override
        public void run() {
            ExecutorService es = Executors.newFixedThreadPool(10);
            int factor = restaurants.size() / threadCount;
            for (int i = 0; i < threadCount; i++) {
                from = i * factor;
                to = i * factor + factor;
                es.execute(new Filtering(from, to, restaurants, filter));
            }
            es.shutdown();
            main.clearMarkers();
            initiateRestaurants(filterdRestaurants);
            try {
                es.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Filtering implements Runnable {
        private int from, to;
        private LinkedList<Restaurant> restaurants;
        private Filter filter;
        private Filter currentRestFilter;

        public Filtering(int from, int to, LinkedList<Restaurant> restaurants, Filter filter) {
            this.from = from;
            this.to = to;
            this.restaurants = restaurants;
            this.filter = filter;
        }

        @Override
        public void run() {
            for (int i = from; i < to; i++) {
                currentRestFilter = restaurants.get(i).getRestaurantFilter();
                for (int j = 0; j < currentRestFilter.length(); j++) {
                    if (currentRestFilter.getCategoryStatus(j) != filter.getCategoryStatus(j)) {
                        return;
                    } else
                        filterdRestaurants.add(restaurants.get(j));
                }
            }
        }

    }
}
