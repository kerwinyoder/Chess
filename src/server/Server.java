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
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
    private volatile LinkedList<Socket> addresses;
    private ServerSocket listener;

    public Server() throws IOException {
        portNum = 5000;
        listener = new ServerSocket(portNum);
        listener.setSoTimeout(10);
        addresses = new LinkedList<>();
        threadPool = Executors.newCachedThreadPool();
    }

    /**
     * Run the server.&nbsp;Accept new sockets, process requests, and distribute
     * the client list.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void run() throws IOException, ClassNotFoundException {
        System.out.println("The server is now running at " + InetAddress.getLocalHost() + ":" + listener.getLocalPort());
        while (running) {
            Socket client = null;

            processRequests();

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
            Message m = new Message("list", null);
            for (int i = 0; i < addresses.size(); i++) {
//                if (!s.getInetAddress().equals(addresses.get(i).getInetAddress())) {
                ips.add(addresses.get(i).getInetAddress().toString());
//                }
            }
            m.setBody(ips);
            out.writeObject(m);
            out.flush();
        }
    }

    /**
     * Iterate through the client list and process any pending requests that
     * have been made.
     *
     * @throws ClassNotFoundException
     */
    private void processRequests() throws ClassNotFoundException {
        ObjectInputStream in = null;
        for (Socket s : addresses) {
            Message m = null;
            try {
                s.setSoTimeout(500);
            } catch (SocketException se) {
                System.err.println("Could not set timeout");
            }
            try {
                in = new ObjectInputStream(s.getInputStream());
                Object o = in.readObject();
                m = (Message) o;
            } catch (SocketTimeoutException ste) {
                //Socket read times out
            } catch (IOException ioe) {
                //Did not read an object
            }
            if (m != null) {
                switch (m.getHeader()) {
                    case "request":
                        if (!m.getRequestSeen()) {
                            Socket sendTo = findSocket(m.getRequestedIP());
                            try {
                                out = new ObjectOutputStream(sendTo.getOutputStream());
                                out.writeObject(m);
                                out.flush();
                            } catch (IOException ioe) {

                            }
                        } else//Both clients have seen the message, request was accepted
                        if (m.requestAccepted()) {
                            Socket sender = findSocket(m.getSendingIP());
                            Socket receiver = findSocket(m.getRequestedIP());

                            Message gameStart = new Message("game", null);

                            //Send messages to both clients to send them into the game GUI
                            try {
                                ObjectOutputStream outSender = new ObjectOutputStream(sender.getOutputStream());
                                ObjectOutputStream outReceiver = new ObjectOutputStream(receiver.getOutputStream());
                                System.out.println(sender);
                                System.out.println(receiver);
                                outSender.writeObject(gameStart);
                                outReceiver.writeObject(gameStart);
                                outSender.flush();
                                outReceiver.flush();
                            } catch (IOException ioe) {
                                //Whoops
                                System.out.println("Could not send to either of the clients");
                            }

                            //Remove the two sockets from the list to keep them from showing up in the list
                            addresses.remove(sender);
                            addresses.remove(receiver);
                            GameConnection gc = new GameConnection(sender, receiver, this);
                            threadPool.execute(gc);
                        } else {
                            Socket sender = findSocket(m.getSendingIP());
                            try {
                                ObjectOutputStream rOut = new ObjectOutputStream(sender.getOutputStream());
                                rOut.writeObject(m);
                                rOut.flush();
                            } catch (IOException ioe) {

                            }
                        }
                        break;
                    default://Catch any unforseen input
                        break;
                }
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
                sendList();
            }
        }
    }

    /**
     * Given a string representation of an IP address, find the socket.
     *
     * @param ip String representation of an IP address
     * @return
     */
    private Socket findSocket(String ip) {
        Socket temp = null;

        for (Socket s : addresses) {
            String curr = s.getInetAddress().toString();
            if (curr.equals(ip)) {
                temp = s;
                return temp;
            }
        }
        //Why U no return temp?
        return temp;
    }

    /**
     * Used to return sockets from a game thread
     *
     * @param s The socket being returned
     */
    public void returnSocket(Socket s) {
        if (!addresses.contains(s)) {
            addresses.add(s);
        }
    }

}
