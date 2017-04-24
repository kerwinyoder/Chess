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
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author frost
 */
public class Client {

    private int port;
    private ObjectInputStream in;
    private Random portSelector;
    private Scanner kbinput;
    private Socket socket;

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
        socket.connect(sa);

        while (true) {
            in = new ObjectInputStream(socket.getInputStream());
            LinkedList<Socket> players = (LinkedList) in.readObject();
            if (!players.isEmpty()) {
                System.out.println(players.toString());
            }
        }
    }

}
