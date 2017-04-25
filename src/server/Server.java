/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author frost
 */
public class Server {

    private boolean running = true;
    private int portNum;
    private ObjectOutputStream out;
    private ExecutorService threadPool;
    private LinkedList<Socket> addresses;
    private ServerSocket listener;

    public Server() throws IOException {
        portNum = 5000;
        listener = new ServerSocket(portNum);
        addresses = new LinkedList<>();
        threadPool = Executors.newCachedThreadPool();
    }

    public void run() throws IOException {
        System.out.println("The server is now running at " + InetAddress.getLocalHost() + ":" + listener.getLocalPort());
        while (running) {
            Socket client = listener.accept();
            if (!addresses.contains(client)) {
                addresses.add(client);
                sendList();
            }
        }
    }

    /**
     * Send a list of addresses from clients connected to the server to each
     * connected client
     *
     * @throws IOException
     */
    private void sendList() throws IOException {
        for (Socket s : addresses) {
            out = new ObjectOutputStream(s.getOutputStream());
            LinkedList<String> ips = new LinkedList();
            for (int i = 0; i < addresses.size(); i++) {
                if (!s.getInetAddress().equals(addresses.get(i).getInetAddress())) {
                    ips.add(addresses.get(i).getInetAddress().toString());
                }
            }
            out.writeObject(ips);
            out.flush();
        }
    }

}
