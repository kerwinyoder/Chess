/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import chess.core.Board;
import communication.Message;
import communication.MoveMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frost
 */
public class GameConnection implements Runnable {

    private Server server;
    private Socket player1;
    private String p1Color;
    private ObjectOutputStream p1Out;
    private ObjectInputStream p1In;
    private Socket player2;
    private String p2Color;
    private ObjectOutputStream p2Out;
    private ObjectInputStream p2In;
    private Board g;
    private Random rand;
    private Socket[] players;

    public GameConnection(Socket s1, Socket s2, Server server) {

        this.server = server;

        player1 = s1;
        player2 = s2;

        players = new Socket[]{player1, player2};

        try {
            player1.setSoTimeout(0);
            player2.setSoTimeout(0);
        } catch (SocketException ex) {
            Logger.getLogger(GameConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            p1Out = new ObjectOutputStream(player1.getOutputStream());
            p2Out = new ObjectOutputStream(player2.getOutputStream());

            p1Out.reset();
            p1Out.flush();

            p2Out.reset();
            p2Out.flush();
        } catch (IOException ex) {
            Logger.getLogger(GameConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        rand = new Random();
        g = new Board();

        Message p1M = new Message("board", null);
        Message p2M = new Message("board", null);

        int coinToss = rand.nextInt(2);
        if (coinToss == 1) {
            p1Color = "white";
            p2Color = "black";
        } else {
            p1Color = "black";
            p2Color = "white";
        }

        p1M.setColor(p1Color);
        p2M.setColor(p2Color);

        try {
            p1Out.writeUnshared(p1M);
            p1Out.flush();
            System.out.println("Made it!");
        } catch (IOException ioe) {
            Logger.getLogger(GameConnection.class.getName()).log(Level.SEVERE, null, ioe);
        }

        try {
            p2Out.writeUnshared(p2M);
            p2Out.flush();
            System.out.println("Made it!");
        } catch (IOException ioe) {
            Logger.getLogger(GameConnection.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    @Override
    public void run() {
        System.out.println("Game started!");
        boolean temp1 = !g.isCheckMate();
        boolean temp2 = !g.isMandatoryDraw();

        while (temp1 && temp2) {

            try {
                processMoves();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameConnection.class.getName()).log(Level.SEVERE, null, ex);
            }

//            try {
//                p1In = new ObjectInputStream(player1.getInputStream());
//            } catch (IOException ex) {
//                Logger.getLogger(GameConnection.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            try {
//                p2In = new ObjectInputStream(player2.getInputStream());
//            } catch (IOException ex) {
//                Logger.getLogger(GameConnection.class.getName()).log(Level.SEVERE, null, ex);
//            }
            Message move = null;

            if (g.isWhiteTurn()) {
                boolean received = false;
                if (p1Color.equals("white")) {

                } else {

                }
            } else {
                boolean received = false;
                if (p1Color.equals("black")) {

                } else {

                }
            }
            if (move != null) {
                System.out.println(move.getBody().toString());
            }
        }

        returnSockets();
    }

    private void returnSockets() {
        server.returnSocket(player1);
        server.returnSocket(player2);
    }

    private void processMoves() throws ClassNotFoundException {
        for (Socket s : players) {
            ObjectInputStream in = null;
            MoveMessage m = null;
            try {
                s.setSoTimeout(500);
            } catch (SocketException se) {
                System.err.println("Could not set timeout");
            }
            try {
                in = new ObjectInputStream(s.getInputStream());
                Object o = in.readObject();
                m = (MoveMessage) o;
            } catch (SocketTimeoutException ste) {
                //Socket read times out
            } catch (IOException ioe) {
                //Did not read an object
            }
            if (m != null) {
                System.out.println(Arrays.toString(m.getMove()));
            }
        }
    }
}
