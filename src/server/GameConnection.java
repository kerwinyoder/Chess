/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import chess.core.Game;
import java.net.Socket;

/**
 *
 * @author frost
 */
public class GameConnection implements Runnable{
    
    private Socket player1;
    private Socket player2;
    private Game g;
    
    public GameConnection(Socket s1, Socket s2){
        player1 = s1;
        player2 = s2;
        g = new Game();
        g.initialPopulate();
    }

    @Override
    public void run() {
        g.printBoard();
    }
    
}
