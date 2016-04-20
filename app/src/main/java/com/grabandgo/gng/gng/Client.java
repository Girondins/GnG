package com.grabandgo.gng.gng;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by alexander on 2016-04-13.
 */
public class Client extends Thread{
    private ExecuteThread exThread;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Receive receiver;
    private String ip;
    private int port;
    private Thread thread = new Thread(this);
    private InetAddress address;
    private Buffer<Object> receiverBuff;
    private Controller cont;

    public Client(String ip, int port,Controller cont) throws UnknownHostException, IOException {
        this.ip = ip;
        this.port = port;
        exThread = new ExecuteThread();
        thread.start();
        exThread.start();
        receiverBuff = new Buffer<Object>();
        this.cont = cont;
    }


    public void enableConnect(){
        exThread.execute(new Connect());
    }

    public void disconnect(){
        exThread.execute(new Disconnect());
    }

    public void request(String request){
        exThread.execute(new SendRequest(request));
    }

    public Object response() throws InterruptedException {
        return receiverBuff.get(); // Modify When Result Is Known aka Ändra sen
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
                ois = new ObjectInputStream(socket.getInputStream());
                receiver = new Receive();
                receiver.start();
                Log.d("reciver.start", "start");
            } catch (Exception e) {
                System.err.println(e);
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
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendRequest implements Runnable{
        String request;

        public SendRequest(String request){
            this.request = request;
        }

        @Override
        public void run() {
            try{
                if(request!=null){
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
                    cont.testRequest((String)obj);
                    if(obj instanceof String){
                        information = (String) obj;
                        cont.testRequest(information);
                        Log.d("Readeee", information);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                receiver = null;
            }
        }
    }


}
