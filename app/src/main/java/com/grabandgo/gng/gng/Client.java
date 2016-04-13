package com.grabandgo.gng.gng;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by alexander on 2016-04-13.
 */
public class Client extends Service implements Runnable{

    private Controller controller;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String ip;
    private int port;
    private final IBinder mbinder = new LocalBinder();
    private Thread thread = new Thread(this);

    public Client(String ip, int port) throws UnknownHostException, IOException {
        this.ip = ip;
        this.port = port;
        thread.start();
    }

    @Override
    public void run() {

        try {
            socket = new Socket(ip,port);
            ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        new ClientStarter().start();

    }

    public class LocalBinder extends Binder {

        public Client getClient(){
            return Client.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

    public void setClientController(Controller controller) {
        this.controller = controller;

    }


    private class ClientStarter extends Thread {
        public void run() {
            Object object;
            while (true) {


            }
        }
    }

}
