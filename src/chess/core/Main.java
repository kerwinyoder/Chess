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

        //White to black
        System.out.println("1");
        board.move(new Move(1, 6, 1, 5));
        board.move(new Move(0, 1, 0, 3));
        board.printBoard();

        System.out.println("2");
        board.move(new Move(2, 7, 0, 5));
        board.move(new Move(4, 1, 4, 2));
        board.printBoard();

        System.out.println("3");
        //Bishop Kill Success
        board.move(new Move(0, 5, 5, 0));
        board.move(new Move(0, 3, 0, 4));
        board.printBoard();

        System.out.println("4");
        // Move Bishop back move pawn up. 
        board.move(new Move(5, 0, 0, 5));
        board.move(new Move(6, 1, 6, 3));
        board.printBoard();

        System.out.println("5");
        // Kill pawn for rook
        board.move(new Move(7, 6, 7, 4));
        board.move(new Move(6, 3, 7, 4));
        board.printBoard();

        System.out.println("6");
        // Rook kills
        board.move(new Move(7, 7, 7, 4));
        board.move(new Move(0, 0, 0, 3));
        board.printBoard();

        System.out.println("7");
        // Moved pawn out of way of queen. Moved pawn. 
        board.move(new Move(2, 6, 2, 4));
        board.move(new Move(1, 1, 1, 2));
        board.printBoard();

        System.out.println("8");
        // Move queen
        board.move(new Move(3, 7, 2, 6));
        board.move(new Move(3, 1, 3, 3));
        board.printBoard();

        System.out.println("9");
        //Move Knight out of way of rook
        board.move(new Move(1, 7, 2, 5));
        board.move(new Move(2, 1, 2, 3));
        board.printBoard();
        //board.move(new Move(1, 1, 1, 3));

        //Test castle
        //board.move(new Move(0, 7, 4, 7));
        board.move(new Move(4, 7, 2, 7));
        board.printBoard();
    }

    private static void processMove(Board board, Move move) {
        boolean moved = board.move(move);
        System.out.println(moved);
        board.printBoard();
        System.out.println();
    }
}
