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

    public static void main(String[] args) {
        boolean moved;
        Board board = new Board();
        board.printBoard();
        System.out.println();
        
        processMove(board, new Move(4, 6, 4, 4)); //e4
        processMove(board, new Move(4, 1, 4, 3)); //e5

        processMove(board, new Move(4, 7, 4, 6)); //Ke2
        processMove(board, new Move(3, 1, 3, 3)); //d5

        processMove(board, new Move(4, 6, 4, 5)); //Ke3
        processMove(board, new Move(3, 3, 4, 4)); //d3xe4

        //capture moving straight forward
        processMove(board, new Move(4, 5, 4, 4)); //Kxe4
        processMove(board, new Move(5, 1, 5, 3)); //f5

        //capture moving diagonally up to the right
        processMove(board, new Move(4, 4, 5, 3)); //
        processMove(board, new Move(6, 1, 6, 3));

        //capture moving to the right
        processMove(board, new Move(5, 3, 6, 3));
        processMove(board, new Move(7, 1, 7, 3));
        
        //capture moving diagonally down to the right
        processMove(board, new Move(6, 3, 6, 2));
        processMove(board, new Move(2, 1, 2, 3));
        processMove(board, new Move(6, 2, 7, 3));
    }

    private static void processMove(Board board, Move move) {
        boolean moved = board.move(move);
        System.out.println(moved);
        board.printBoard();
        System.out.println();
    }
}
