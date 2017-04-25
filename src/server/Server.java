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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
        listener.setSoTimeout(10);
        addresses = new LinkedList<>();
        threadPool = Executors.newCachedThreadPool();
    }

    public void run() throws IOException {
        System.out.println("The server is now running at " + InetAddress.getLocalHost() + ":" + listener.getLocalPort());
        while (running) {
            Socket client = null;
            try {
                client = listener.accept();
            } catch (SocketTimeoutException ste) {
                checkDisconnects();
            }
            if (client != null) {
                if (!addresses.contains(client)) {
                    addresses.add(client);
                    sendList();
                }
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

    private void sendRequest() {

    }

    /**
     * Sends a heartbeat message to a client to validate if the connection is
     * still active.&nbsp If it is not, the client is removed from the list.
     *
     * @throws IOException
     */
    private void checkDisconnects() throws IOException {
        for (Socket s : addresses) {
            boolean reachable = true;
            try {
                out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(1);
            } catch (IOException ioe) {
                reachable = false;
            }
            if (!reachable) {
                try {
                    s.close();
                } catch (IOException ioe) {
                    System.err.println("Socket could not be closed");
                }
                addresses.remove(s);
            }
        }
    }

}
