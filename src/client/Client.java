/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

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
    private Socket socket;

    public Client() throws IOException {
        kbinput = new Scanner(System.in);
        portSelector = new Random();
        port = (portSelector.nextInt(7000) + 1000);
        socket = new Socket();
        gui = new LobbyGUI();
        gui.setVisible(true);
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

        while (true) {
            in = new ObjectInputStream(socket.getInputStream());
            Object rec = in.readObject();
            LinkedList<String> players = new LinkedList();
            if (!rec.equals(1)) {
                players = (LinkedList) rec;
            }
            if (!players.isEmpty()) {
                gui.updateList(players);
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
