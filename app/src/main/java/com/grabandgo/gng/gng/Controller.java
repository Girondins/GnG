package com.grabandgo.gng.gng;

import android.util.Log;

import java.io.IOException;

/**
 * Created by alexander on 2016-04-13.
 */
public class Controller {

    private Client client;
    private MainActivity main;


    public Controller(MainActivity main){
        this.main = main;
        try {
            Log.d("Connecting","client");
            client = new Client("10.2.2.10",3000,this);
            client.enableConnect();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void getRestaurants(){
        client.request("getRestaurants");
    }

    public void testRequest(String information){
        Log.d("TEST",information + "J");
        main.addMarker(information);
        Log.d("addmark",information);

    }


}
