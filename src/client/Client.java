/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import chess.core.pieces.Piece;
import client.gui.GameGUI;
import client.gui.LobbyGUI;
import client.gui.RejectedGUI;
import client.gui.RequestGUI;
import communication.Message;
import communication.MoveMessage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author frost
 */
public class Client {

    private int serverPort;
    private LobbyGUI gui;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Random portSelector;
    private Scanner kbinput;
    public Socket socket;

    public Client() throws IOException {
        kbinput = new Scanner(System.in);
        portSelector = new Random();
        serverPort = 5000;
        socket = new Socket();
    }

    public void run() throws IOException, ClassNotFoundException {
        System.out.print("Please enter the IP Address of the server: ");
        String ip = kbinput.nextLine();
        InetSocketAddress sa = new InetSocketAddress(ip, serverPort);
        try {
            socket.connect(sa);
        } catch (SocketException se) {
            close();
        }

        socket.setTcpNoDelay(true);

        //Set the lobby client to be visible to the user
        gui = new LobbyGUI(this);
        gui.setVisible(true);

        GameGUI game = null;

        out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        out.flush();

        while (true) {
            Message m = null;
            Object om = null;
            try {
                in = new ObjectInputStream(socket.getInputStream());
                Object rec = in.readObject();
                om = rec;
                m = (Message) rec;
            } catch (IOException ioe) {
                //Nothing was read from the socket
            }

            if (m != null) {
                if (!m.getHeader().equals("probe")) {
                    System.out.println(m.getHeader());
                }
                switch (m.getHeader()) {
                    case "board":
                        MoveMessage mm = (MoveMessage) om;
                        if (game != null) {
                            if (!m.getColor().equals("") && !game.colorSet()) {
                                game.setColor(m.getColor());
                            } else {
                                game.updateBoard(mm);
                            }
                            game.getTurn();
                        }
                        break;
                    case "game":
                        game = new GameGUI(this);
                        game.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        game.setVisible(true);
                        gui.setVisible(false);
                        break;
                    case "list"://Server is distributing the client list
                        LinkedList<String> players = (LinkedList) m.getBody();
                        if (!players.isEmpty()) {
                            gui.updateList(players);
                        }
                        break;
                    case "request":
                        if (m.getRequestSeen() && !m.requestAccepted()) {
                            RejectedGUI dg = new RejectedGUI();
                            dg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                            dg.setVisible(true);
                        } else {
                            RequestGUI rg = new RequestGUI(this, m);
                            rg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            rg.setVisible(true);
                        }
                        break;
                    default://Utilized for the proof of life messages from the server
                        break;
                }
            }

        }
    }

    public void sendMove(Message m) {
        try {
            out.reset();
            out.writeUnshared(m);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * If there was a connection error, close the application.
     */
    private void close() {
        boolean setToClose = true;
        try {
            System.out.println("There was an error connecting to the server. Please check the IP Address and try again.");
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
