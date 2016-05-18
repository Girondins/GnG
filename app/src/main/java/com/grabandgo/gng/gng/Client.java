package com.grabandgo.gng.gng;

import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Client class.
 */
public class Client extends Thread {
    private ExecuteThread exThread;
    private Socket socket;
    private ObjectOutputStream oos;
    private HackedInputStream ois;
    private Receive receiver;
    private String ip;
    private int port;
    private Thread thread = new Thread(this);
    private InetAddress address;
    private Buffer<Object> receiverBuff;
    private Controller cont;
    private MainActivity main;
    private Timer timer = new Timer();

    public Client(String ip, int port, Controller cont) throws UnknownHostException, IOException {
        this.ip = ip;
        this.port = port;
        this.main = main;
        exThread = new ExecuteThread();
        thread.start();
        exThread.start();
        receiverBuff = new Buffer<Object>();
        this.cont = cont;
    }


    public void enableConnect() {
        exThread.execute(new Connect());
    }

    public void disconnect() {
        exThread.execute(new Disconnect());
    }

    public void request(String request) {
        exThread.execute(new SendRequest(request));
    }

    public Object response() throws InterruptedException {
        return receiverBuff.get(); // Modify When Result Is Known aka Ändra sen
    }

    public ObjectOutputStream checkConnection(){
        return oos;
    }

    private class Connect implements Runnable {
        @Override
        public void run() {
            try {
                address = InetAddress.getByName(ip);
                socket = new Socket(address, port);
                Log.d("Connct", address.toString());
                Log.d("reciver.start", "start");
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();
                Log.d("reciver.start", "start");
                ois = new HackedInputStream(socket.getInputStream());
                receiver = new Receive();
                receiver.start();
                Log.d("reciver.start", "start");
            } catch (Exception e) {
                System.err.println(e);
                // cont.checkConnection();
                timer.schedule(new TryConnect(),5000);

            }
        }
    }

    private class Disconnect implements Runnable {
        public void run() {
            try {
                if (ois != null)
                    ois.close();
                if (oos != null)
                    oos.close();
                if (socket != null)
                    socket.close();
                exThread.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendRequest implements Runnable {
        String request;

        public SendRequest(String request) {
            this.request = request;
        }

        @Override
        public void run() {
            try {
                if (request != null) {
                    oos.writeObject(request.toString());
                    Log.d("SENDTASK", request.toString());
                    oos.flush();
                }
//                if(receiver == null){
//                    receiver = new Receive();
//                    receiver.start();
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Receive extends Thread {
        public void run() {
            String information;
            Object obj;
            try {
                while (receiver != null) {
                    obj = ois.readObject();
                    Log.d("Recieving", obj.toString());
                    if (obj instanceof LinkedList) {
                        Log.d("setting", "set");
                        cont.setRestaurants((LinkedList) obj);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                receiver = null;
            }
        }
    }

    private class TryConnect extends TimerTask{

        @Override
        public void run() {
            cont.checkConnection();
        }
    }



}
