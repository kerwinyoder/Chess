/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.core;

/**
 *
 * @author ragilmore0
 */
public class Main {
    
    public static void main(String[] args){
        Game game = new Game();
        game.initialPopulate();
        game.printBoard();
    }
    
}
