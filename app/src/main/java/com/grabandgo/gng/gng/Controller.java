package com.grabandgo.gng.gng;

import java.io.IOException;

/**
 * Created by alexander on 2016-04-13.
 */
public class Controller {

    private Client client;

    public Controller(){
        try {
            client = new Client("10.101.2.48",9000);
            client.enableConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
