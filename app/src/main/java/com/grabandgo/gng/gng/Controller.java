package com.grabandgo.gng.gng;

import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by alexander on 2016-04-13.
 */
public class Controller {

    private LinkedList<Restaurant> restaurants;
    private Client client;
    private MainActivity main;


    public Controller(MainActivity main){
        this.main = main;
        try {
            Log.d("Connecting","client");
            client = new Client("192.168.0.103",3000,this);
            client.enableConnect();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void getRestaurants(){
        client.request("getRestaurants");
    }

    public void setRestaurants(LinkedList<Restaurant> restaurants){
        this.restaurants = restaurants;
        initiateRestaurants();
    }

    public void initiateRestaurants(){
        for(int i=0; i<restaurants.size();i++){
            main.addMarker(restaurants.get(i));
        }
    }



}
