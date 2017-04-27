/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import communication.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author frost
 */
public class Client {

    private int port;
    private LobbyGUI gui;
    private ObjectInputStream in;
    private Random portSelector;
    private Scanner kbinput;
    protected Socket socket;

    public Client() throws IOException {
        kbinput = new Scanner(System.in);
        portSelector = new Random();
        port = (portSelector.nextInt(7000) + 1000);
        socket = new Socket();
    }

    public void run() throws IOException, ClassNotFoundException {
        System.out.print("Please enter the IP Address of the server: ");
        String ip = kbinput.nextLine();
        System.out.print("Please enter the port number of the server: ");
        int port = kbinput.nextInt();
        InetSocketAddress sa = new InetSocketAddress(ip, port);
        try {
            socket.connect(sa);
        } catch (SocketException se) {
            close();
        }

        //Set the lobby client to be visible to the user
        gui = new LobbyGUI(this);
        gui.setVisible(true);

        while (true) {
            Message m = null;
            try {
                in = new ObjectInputStream(socket.getInputStream());
                Object rec = in.readObject();
                m = (Message) rec;
            } catch (IOException ioe) {

            }

            if (m != null) {
                switch (m.getHeader()) {
                    case "list"://Server is distributing the client list
                        LinkedList<String> players = new LinkedList();
                        players = (LinkedList) m.getBody();
                        if (!players.isEmpty()) {
                            gui.updateList(players);
                        }
                        break;
                    case "request":
                        System.out.println("A request was receieved!!");
                        RequestGUI rg = new RequestGUI(this, m);
                        rg.setVisible(true);
                        break;
                    default://Utilized for the proof of life messages from the server
                        break;
                }
            }

        }
    }

    private void close() {
        boolean setToClose = true;
        try {
            socket.close();
        } catch (IOException ioe) {
            System.err.println("The socket could not be closed.");
            setToClose = false;
        }
        if (setToClose) {
            System.exit(0);
        }
    }

}
