/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import communication.Message;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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

    public void run() throws IOException, ClassNotFoundException {
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
            processRequests();
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
            Message m = new Message("list", null);
            for (int i = 0; i < addresses.size(); i++) {
                if (!s.getInetAddress().equals(addresses.get(i).getInetAddress())) {
                    ips.add(addresses.get(i).getInetAddress().toString());
                }
            }
            m.setBody(ips);
            out.writeObject(m);
            out.flush();
        }
    }

    private void processRequests() throws ClassNotFoundException {
        ObjectInputStream in = null;
        for (Socket s : addresses) {
            try {
                in = new ObjectInputStream(s.getInputStream());
                Object o = in.readObject();
                Message m = (Message) o;
                switch (m.getHeader()) {
                    case "request":
                        Socket sendTo = null;
                        for (Socket s1 : addresses) {
                            if (s1.getInetAddress().toString().equals(m.getRequestedIP())) {
                                sendTo = s1;
                            }
                        }
                        m.setSendingIP(sendTo.getInetAddress().toString());
                        ObjectOutputStream out = new ObjectOutputStream(sendTo.getOutputStream());
                        out.writeObject(m);
                        break;
                    default://Catch any unforseen input
                        break;
                }
            } catch (IOException ioe) {

            }
        }
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
                Message hb = new Message("probe", 1);
                out.writeObject(hb);
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
