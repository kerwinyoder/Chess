/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.core;

import chess.core.pieces.Piece;
import chess.core.pieces.*;

/**
 *
 * @author ragilmore0
 */
public class Game {

    private final Piece[][] board;
    private int whitePieces;
    private int blackPieces;
    private String[][] b = {{"", ""}, {"", ""}};

    public Game() {
        this.board = new Piece[8][8];
        this.whitePieces = 0;
        this.blackPieces = 0;
    }

    public void initialPopulate() {
        // Populate Pawns

        whitePopulate();
        blackPopulate();

    }

    private void whitePopulate() {
        int x = 6;
        int y;
        //Pawns
        for (y = 0; y < 8; y++) {
            this.board[x][y] = new Pawn(x, y, "white", "pawn");
        }
        //Rooks
        this.board[7][0] = new Rook(6, 0, "white", "rook");
        this.board[7][7] = new Rook(6, 7, "white", "rook");

        //Knights
        this.board[7][1] = new Knight(6, 1, "white", "knight");
        this.board[7][6] = new Knight(6, 6, "white", "knight");

        //Bishops
        this.board[7][2] = new Bishop(6, 2, "white", "bishop");
        this.board[7][5] = new Bishop(6, 5, "white", "bishop");

        //King
        this.board[7][4] = new King(6, 4, "white", "king");

        //Queen
        this.board[7][3] = new Queen(6, 5, "white", "queen");

    }

    private void blackPopulate() {
        int x = 1;
        int y;
        //Pawns
        for (y = 0; y < 8; y++) {
            this.board[x][y] = new Pawn(x, y, "black", "pawn");
        }
        //Rooks
        this.board[0][0] = new Rook(0, 0, "black", "rook");
        this.board[0][7] = new Rook(0, 7, "black", "rook");

        //Knights
        this.board[0][1] = new Knight(0, 1, "black", "knight");
        this.board[0][6] = new Knight(0, 6, "black", "knight");

        //Bishops
        this.board[0][2] = new Bishop(0, 2, "black", "bishop");
        this.board[0][5] = new Bishop(0, 5, "black", "bishop");

        //King
        this.board[0][3] = new King(0, 3, "black", "king");

        //Queen
        this.board[0][4] = new Queen(0, 4, "black", "queen");

    }

    public void printBoard() {

        String type;
        //String color;

        for (int x = 0; x < this.board.length; x++) {
            for (int y = 0; y < this.board.length; y++) {
                if (this.board[x][y] != null) {
                    type = this.board[x][y].getType();
                    switch (type) {
                        case "pawn":
                            if ("black".equals(this.board[x][y].getColor())) {
                                System.out.print(" |PB| ");
                            } else {
                                System.out.print(" |PW| ");
                            }
                            break;
                        case "bishop":
                            if ("black".equals(this.board[x][y].getColor())) {
                                System.out.print(" |BB| ");
                            } else {
                                System.out.print(" |BW| ");
                            }
                            break;
                        case "king":
                            if ("black".equals(this.board[x][y].getColor())) {
                                System.out.print(" |KB| ");
                            } else {
                                System.out.print(" |KW| ");
                            }
                            break;
                        case "knight":
                            if ("black".equals(this.board[x][y].getColor())) {
                                System.out.print(" |HB| ");
                            } else {
                                System.out.print(" |HW| ");
                            }
                            break;
                        case "queen":
                            if ("black".equals(this.board[x][y].getColor())) {
                                System.out.print(" |QB| ");
                            } else {
                                System.out.print(" |QW| ");
                            }
                            break;
                        case "rook":
                            if ("black".equals(this.board[x][y].getColor())) {
                                System.out.print(" |RB| ");
                            } else {
                                System.out.print(" |RW| ");
                            }
                            break;

                    }
                } else {
                    System.out.print(" |NN| ");
                }
            }
            System.out.println();
        }

    }

    public Piece[][] getBoard() {
        return board;
    }
}
