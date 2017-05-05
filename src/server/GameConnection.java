/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import chess.core.Board;
import communication.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
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

    public GameConnection(Socket s1, ObjectInputStream s1In, Socket s2, ObjectInputStream s2In, Server server) {
        this.server = server;

        player1 = s1;
        player2 = s2;
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

            p1In = s1In;
            p2In = s2In;
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

        p1M.setBody(g.getBoard());
        p2M.setBody(g.getBoard());
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
//        while(gameConiditions){
//            if(p1Color.equals("white")){
//                //wait for move
//                //process move
//                //Send board
//            } else {
//                //Wait for p2 move
//                //process move
//                //Send board
//            }
//        }
        returnSockets();
    }

    private void returnSockets() {
        server.returnSocket(player1);
        server.returnSocket(player2);
    }

}
