/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import chess.core.Game;
import communication.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    private Game g;
    private Random rand;

    public GameConnection(Socket s1, Socket s2, Server server) {
        this.server = server;
        
        player1 = s1;
        try{
        p1Out = new ObjectOutputStream(player1.getOutputStream());
        p1Out.flush();
        p1In = new ObjectInputStream(player1.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(GameConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        player2 = s2;
        try{
        p2Out = new ObjectOutputStream(player1.getOutputStream());
        p2Out.flush();
        p2In = new ObjectInputStream(player1.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(GameConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        rand = new Random();
        g = new Game();
        g.initialPopulate();
        
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
        
//        p1M.setBody(g.getBoard());
//        p1M.setBody(g.getBoard());
//        p1Out.writeObject(p1M);
//        p2Out.writeObject(p2M);
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
