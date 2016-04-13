package com.grabandgo.gng.gng;

import java.io.IOException;

/**
 * Created by alexander on 2016-04-13.
 */
public class Controller {
    private Client client;

    public Controller(){
        try {
            client = new Client("192.168.0.1",9000);
            client.setClientController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
