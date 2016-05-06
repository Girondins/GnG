package com.grabandgo.gng.gng;

import android.util.Log;
import android.widget.Toast;

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
    private Filter filter = new Filter();

    public Controller(MainActivity main) {
        this.main = main;
        try {
            Log.d("Connecting", "client");
            filter = new Filter();
            client = new Client("192.168.1.215", 3000, this,main);
            client.enableConnect();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void getRestaurants() {
        client.request("getRestaurants");
    }

    public void setRestaurants(LinkedList<Restaurant> restaurants) {
        this.restaurants = restaurants;
        initiateRestaurants();
    }

    public void filterRestaurants() {
        filterdRestaurants = new LinkedList<Restaurant>();
        Thread t = new Thread(new CheckFilter());
        t.start();
    }

    public void initiateRestaurants() {
        for (int i = 0; i < restaurants.size(); i++) {
            main.addMarker(restaurants.get(i));
        }
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
