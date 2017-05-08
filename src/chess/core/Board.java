package chess.core;

import chess.core.pieces.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A chess board
 *
 * @author ragilmore0
 * @author Kerwin Yoder
 * @version 2017.05.02
 */
public class Board {

    private final Piece[][] BOARD;
    //a list of the white pieces that have not been captured
    private final ArrayList<Piece> whitePieces;
    //a list of the black pieces that have not been captured
    private final ArrayList<Piece> blackPieces;
    private King whiteKing;
    private King blackKing;
    //the piece is elegible to be the victim of an en passant attack on the next turn.
    private Piece enPassantVictim;
    //tracks whether the king of the opposing side was put in check by a move
    private boolean inCheck;
    private boolean isWhiteTurn;
    //Tracks if a successful EnPassant was performed
    private boolean successfulEnPassant;
    //Tracks if the server is waiting on a promotion from the user
    private boolean promotionInProgress;
    //used to check for 50 consecutive moves without moving a pawn or capturing a piece
    private int moveCount;
    //used to check for draw by three fold repetition.
    private HashMap<String, Integer> stateCount;

    /**
     * Creates a new Chess Board.
     */
    public Board() {
        BOARD = new Piece[8][8];
        whitePieces = new ArrayList<>(16);
        blackPieces = new ArrayList<>(16);
        enPassantVictim = null;
        inCheck = false;
        isWhiteTurn = true;
        successfulEnPassant = false;
        promotionInProgress = false;
        moveCount = 0;
        stateCount = new HashMap<>(100);
        initialPopulate();
    }

    /**
     * Makes the given move if it is valid
     *
     * @param move the attempted move
     * @return true if the move was valid and the pieces were moved or false
     * otherwise
     */
    public boolean move(Move move) {
        //Check the bounds of the starting position
        if (!isInBounds(move.START_X, move.START_Y)) {
            return false;
        }

        //Check if the player whose turn it is has a piece at the specified starting position
        Piece piece = BOARD[move.START_X][move.START_Y];
        if (piece == null || (isWhiteTurn && piece.getColor().equalsIgnoreCase("black")) || (!isWhiteTurn && piece.getColor().equalsIgnoreCase("white"))) {
            return false;
        }
        //if the king is in check, ensure that he is not in check after the move
        boolean isValid = piece.isValidMove(this, move.TARGET_X, move.TARGET_Y);
        if (inCheck && isValid) {
            King king = piece.getColor().equalsIgnoreCase("white") ? whiteKing : blackKing;
            isValid = !king.isCheckAfterMove(this, move);
        }
        //Make the requested move if it is valid
        if (isValid) {
            //Move the piece
            BOARD[move.START_X][move.START_Y] = null;
            Piece victim = BOARD[move.TARGET_X][move.TARGET_Y];
            //handle en passant moves
            successfulEnPassant = false;
            if (piece instanceof Pawn && victim == null && move.TARGET_X - move.START_X != 0) {
                victim = BOARD[move.TARGET_X][move.START_Y];
                successfulEnPassant = true;
                BOARD[move.TARGET_X][move.START_Y] = null;
            }
            BOARD[move.TARGET_X][move.TARGET_Y] = piece;
            piece.setXPos(move.TARGET_X);
            piece.setYPos(move.TARGET_Y);
            ++moveCount;

            //update the state count
            String state = getState();
            if (stateCount.get(state) == null) {
                stateCount.put(state, 1);
            } else {
                stateCount.put(state, stateCount.get(state) + 1);
            }

            //if a piece was captured, update the move count and list of alive pieces
            if (victim != null) {
                if (victim.getColor().equalsIgnoreCase("white")) {
                    whitePieces.remove(victim);
                } else {
                    blackPieces.remove(victim);
                }
                //reset the move count since a piece was captured
                moveCount = 0;
                //clear the state count since all existing states can never be achieved after a piece has been captured
                stateCount.clear();
            }
            enPassantVictim = null;

            //Check for en passant and promotion and update the move count
            if (piece instanceof Pawn) {
                //if a pawn moved two squares, mark it as a potential en passant victim
                if (Math.abs(move.TARGET_Y - move.START_Y) == 2) {
                    enPassantVictim = piece;
                } //if the pawn reached the far side of the board, promote it
                else if (isPawnPromoted((Pawn) piece, move)) {
                    promotionInProgress = true;
//                    promotePawn((Pawn) piece, move);
                }
                //reset the move count since a pawn was moved
                moveCount = 0;
            }

            //Check for castling
            if (piece instanceof King) {
                //if the king moved two squares horizontally, the move was a castle
                if (Math.abs(move.TARGET_X - move.START_X) == 2) {
                    //move the rook; the king was already moved
                    if (move.TARGET_X == 2) {
                        //queen-side castle
                        BOARD[3][move.TARGET_Y] = BOARD[0][move.TARGET_Y];
                        BOARD[0][move.TARGET_Y] = null;
                    } else {
                        //king-side castle
                        BOARD[5][move.TARGET_Y] = BOARD[7][move.TARGET_Y];
                        BOARD[7][move.TARGET_Y] = null;
                    }
                }
            }
            King king = piece.getColor().equalsIgnoreCase("white") ? blackKing : whiteKing;
            inCheck = king.isInCheck(this);
            if (!promotionInProgress) {
                isWhiteTurn = !isWhiteTurn;
            }
        }
        return isValid;
    }

    /**
     * Gets the piece that is currently eligible to be the victim of an en
     * passant attack if a piece is currently eligible
     *
     * @return the piece that is currently eligible to be the victim of an en
     * passant attack or null if no piece is currently eligible
     */
    public Piece getEnPassantVictim() {
        return enPassantVictim;
    }

    /**
     *
     * @return
     */
    public boolean getSuccessfulEnPassant() {
        return successfulEnPassant;
    }

    /**
     *
     * @return
     */
    public boolean getPromotionInProgress() {
        return promotionInProgress;
    }

    /**
     * Gets the piece at the given column and row in the board. Rows of the
     * board range from 0 - 7 starting from the black side and counting up to
     * the white side. Columns range from 0 - 7 starting on the queen side and
     * counting up to the king side.
     *
     * @param column the column of the piece
     * @param row the row of the piece
     * @return the Piece at the given column and row or null if no piece is at
     * that position.
     */
    public Piece getPiece(int column, int row) {
        return BOARD[column][row];
    }

    /**
     * Moves a piece to a specified location without move validation. This
     * method does not provide move validation. It is intended to use only to
     * temporarily move a king to simplify testing whether he is in check or not
     *
     * @param piece the piece to move to the specified location
     * @param column the column to which the piece should be moved
     * @param row the row to which the piece should be moved
     */
    public void setPiece(Piece piece, int column, int row) {
        BOARD[column][row] = piece;
    }

    /**
     * Adds the piece to the list of alive pieces of its color
     *
     * @param piece the piece to add to the list of alive pieces
     */
    public void addPiece(Piece piece) {
        if (piece == null) {
            return;
        }
        String color = piece.getColor();
        if (color.equalsIgnoreCase("white")) {
            whitePieces.add(piece);
        } else {
            blackPieces.add(piece);
        }
    }

    /**
     * Removes the piece from the list of alive pieces of its color
     *
     * @param piece the piece to remove from the list of alive pieces
     */
    public void removePiece(Piece piece) {
        if (piece == null) {
            return;
        }
        String color = piece.getColor();
        if (color.equalsIgnoreCase("white")) {
            whitePieces.remove(piece);
        } else {
            blackPieces.remove(piece);
        }
    }

    /*
     * Checks if the given pawn is being promoted
     * @param pawn the pawn to check for promotion
     * @param move the move made by the pawnthat may make it eligible for promotion
     * @return true if the pawn is being promoted and false otherwise
     */
    private boolean isPawnPromoted(Pawn pawn, Move move) {
        if (!(pawn instanceof Pawn)) {
            return false;
        }
        switch (pawn.getColor().toLowerCase()) {
            case "white":
                return move.TARGET_Y == 0;
            case "black":
                return move.TARGET_Y == 7;
            default:
                return false;
        }
    }

    /*
     * Promotes the given pawn. The user is asked to select a new piece type via the command line.
     * @param pawn the pawn that is being promoted
     * @param move the move that lead to the pawn's promotion
     */
    public void promotePawn(Piece p, String choice) {
        Pawn pawn = (Pawn) p;
        Move move = new Move(0, 0, pawn.getXPos(), pawn.getYPos());
        Piece newPiece = null;
        switch (choice) {
            case "Q":
                newPiece = new Queen(move.TARGET_X, move.TARGET_Y, pawn.getColor(), "queen");
                break;
            case "R":
                newPiece = new Rook(move.TARGET_X, move.TARGET_Y, pawn.getColor(), "rook");
                break;
            case "B":
                newPiece = new Bishop(move.TARGET_X, move.TARGET_Y, pawn.getColor(), "bishop");
                break;
            case "N":
                newPiece = new Knight(move.TARGET_X, move.TARGET_Y, pawn.getColor(), "knight");
                break;
        }

        //update the list of pieces that are alive
        if (pawn.getColor().equalsIgnoreCase("white")) {
            whitePieces.remove(BOARD[move.TARGET_X][move.TARGET_Y]);
            whitePieces.add(newPiece);
        } else {
            blackPieces.remove(BOARD[move.TARGET_X][move.TARGET_Y]);
            blackPieces.add(newPiece);
        }
        BOARD[move.TARGET_X][move.TARGET_Y] = newPiece;
        promotionInProgress = false;
    }

    /*
     * Checks if the column and row are within the bounds of the board.
     * @param xPos the column
     * @param yPos the row
     * @return true if the given column and row are within the bounds of the board and false otherwise
     */
    private boolean isInBounds(int xPos, int yPos) {
        return xPos >= 0 && xPos < 8 && yPos >= 0 && yPos < 8;
    }

    /**
     * Checks if pieces of the given color can be attacked by at the given
     * position by any enemy piece on the next turn
     *
     * @param xPos the column
     * @param yPos the row
     * @param color the color of pieces that could be the victim of an attack
     * @return true if an enemy piece can attack at the given position and false
     */
    public boolean isThreatened(int xPos, int yPos, String color) {
        ArrayList<Piece> list = color.equals("white") ? blackPieces : whitePieces;
        for (Piece piece : list) {
            if (piece.isValidMove(this, xPos, yPos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the game is a draw because of a stalemate. A stalemate occurs
     * when it is a player's turn and the player cannot make a valid move, but
     * the player's king is not in check.
     *
     * @return true if a stalemate occurred and false otherwise
     */
    public boolean isDrawByStalemate() {
        ArrayList<Piece> list;
        King king;
        if (isWhiteTurn) {
            king = whiteKing;
            list = whitePieces;
        } else {
            king = blackKing;
            list = blackPieces;
        }
        //Check if any pieces (including the king) have valid moves available
        for (Piece piece : list) {
            if (piece.hasValidMoves(this)) {
                return false;
            }
        }
        //If no pieces can move and the king is not in check, the game is a draw by stalemate.
        return !king.isInCheck(this);
    }

    /**
     * Checks if the game is eligible to be declared a draw no pieces have been
     * captured and no pawns have moved for the last 50 moves.
     *
     * @return true if the game is eligible to be declared a draw by move count
     * and false otherwise
     */
    public boolean isDrawByMoveCount() {
        return moveCount >= 50;
    }

    /**
     * Checks if the game is eligible to be declared a drawn by three-fold
     * repetition. A draw by three-fold repetition can be declared if the same
     * board position occurs three times (not necessarily in a row).
     *
     * @return true if the game is eligible to be declared a draw by three-fold
     * repetition and false otherwise
     */
    public boolean isDrawByRepetition() {
        return stateCount.get(getState()) >= 3;
    }

    /**
     * Checks if the game is a drawn by insufficient material. A draw by
     * insufficient material occurs when it is impossible for either player to
     * checkmate the other player with their remaining pieces.<p>
     *
     * Draw by insufficient material occurs in four cases: King vs. King<p>
     * King and Bishop vs. King<p>
     * King and Knight vs. King<p>
     * King and Bishop vs. King and Bishop where the bishops occur different
     * color spaces.
     *
     * @return true if the game is a draw by insufficient material and false
     * otherwise
     */
    public boolean isDrawByInsufficientMaterial() {
        //King vs. King
        if (whitePieces.size() == 1 && blackPieces.size() == 1) {
            return true;
        } //King and Bishop or King and Knight vs. King
        else if (whitePieces.size() == 2 && blackPieces.size() == 1) {
            Piece whitePiece = whitePieces.get(0) == whiteKing ? whitePieces.get(1) : whitePieces.get(0);
            if (whitePiece instanceof Bishop || whitePiece instanceof Knight) {
                return true;
            }
        } //King and Bishop or King and Knight vs. King
        else if (blackPieces.size() == 2 && whitePieces.size() == 1) {
            //find the piece that is not the king
            Piece blackPiece = blackPieces.get(0) == blackKing ? blackPieces.get(1) : blackPieces.get(0);
            if (blackPiece instanceof Bishop || blackPiece instanceof Knight) {
                return true;
            }
        } //King and bishop vs king and bishop where the bishops occupy different color squares
        else if (whitePieces.size() == 2 && blackPieces.size() == 2) {
            Piece whitePiece = whitePieces.get(0) == whiteKing ? whitePieces.get(1) : whitePieces.get(0);
            Piece blackPiece = blackPieces.get(0) == blackKing ? blackPieces.get(1) : blackPieces.get(0);
            if (whitePiece instanceof Bishop && blackPiece instanceof Bishop && !whitePiece.getColor().equalsIgnoreCase(blackPiece.getColor()));
        }
        return false;
    }

    /**
     * Checks if the game is a mandatory draw. The players do not have
     * choice.<p>
     * A mandatory draw occurs in two cases:
     * <p>
     * Draw by stalemate<p>
     * Draw by insufficient material
     *
     * @return true if the game is a mandatory draw
     */
    public boolean isMandatoryDraw() {
        boolean temp1 = isDrawByStalemate();
        boolean temp2 = isDrawByInsufficientMaterial();
        return isDrawByStalemate() || isDrawByInsufficientMaterial();
    }

    /**
     * Checks if the game may optionally be declared a draw.<p>
     * A game may be declared a draw in two cases: Fifty or more moves have been
     * made without capturing a piece or moving a pawn.<p>
     * Draw by three-fold repetition
     *
     * @return true if the game may be declared a draw
     */
    public boolean isOptionalDraw() {
        return isDrawByMoveCount() || isDrawByRepetition();
    }

    /**
     * Checks if it is currently white's turn;
     *
     * @return true if it is white's turn and false otherwise
     */
    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    /**
     * Checks if the king whose turn it is is in check.
     *
     * @return True if the king check
     */
    public boolean isInCheck() {
        return inCheck;
    }

    /**
     * Checks if the game has ended with a checkmate
     *
     * @return true if the game is over
     */
    public boolean isCheckMate() {
        if (isWhiteTurn) {
            return whiteKing.isCheckmated(this);
        } else {
            return blackKing.isCheckmated(this);
        }
    }

    /**
     * Checks which king is in checkmate.
     *
     * @return The color of the king in checkmate
     */
    public String checkmatedKing() {
        if (whiteKing.isCheckmated(this)) {
            return "white";
        } else {
            return "black";
        }
    }

    private String getState() {
        StringBuilder builder = new StringBuilder();
        Piece piece;
        char c;
        for (int i = 0; i < BOARD.length; ++i) {
            for (int j = 0; j < BOARD.length; ++j) {
                piece = BOARD[i][j];
                if (piece != null) {
                    switch (BOARD[i][j].getType().toLowerCase()) {
                        case "pawn":
                            c = piece.getColor().equalsIgnoreCase("white") ? 'P' : 'p';
                            break;
                        case "rook":
                            c = piece.getColor().equalsIgnoreCase("white") ? 'R' : 'r';
                            break;
                        case "knight":
                            c = piece.getColor().equalsIgnoreCase("white") ? 'N' : 'n';
                            break;
                        case "bishop":
                            c = piece.getColor().equalsIgnoreCase("white") ? 'B' : 'b';
                            break;
                        case "queen":
                            c = piece.getColor().equalsIgnoreCase("white") ? 'Q' : 'q';
                            break;
                        default: //king
                            c = piece.getColor().equalsIgnoreCase("white") ? 'K' : 'k';
                            break;
                    }
                } else {
                    c = ' ';
                }
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * Gets the game board array
     *
     * @return the game board array
     */
    public Piece[][] getBoard() {
        return BOARD;
    }

    /*
     * Populate the board with the initial pieces in their initial location.
     */
    private void initialPopulate() {
        // Populate Pawns
        whitePopulate();
        blackPopulate();

    }

    /*
     * Populate the board with the initial white pieces in their initial locations
     */
    private void whitePopulate() {
        int x;
        int y = 6;
        //Pawns
        for (x = 0; x < 8; x++) {
            BOARD[x][y] = new Pawn(x, y, "white", "pawn");
            whitePieces.add(BOARD[x][y]);
        }
        //Rooks
        BOARD[0][7] = new Rook(0, 7, "white", "rook");
        BOARD[7][7] = new Rook(7, 7, "white", "rook");

        //Knights
        BOARD[1][7] = new Knight(1, 7, "white", "knight");
        BOARD[6][7] = new Knight(6, 7, "white", "knight");

        //Bishops
        BOARD[2][7] = new Bishop(2, 7, "white", "bishop");
        BOARD[5][7] = new Bishop(5, 7, "white", "bishop");

        //King
        BOARD[4][7] = whiteKing = new King(4, 7, "white", "king");

        //Queen
        BOARD[3][7] = new Queen(3, 7, "white", "queen");

        //add the rest of the pieces to the list of alive pieces
        for (int i = 0; i < 8; ++i) {
            whitePieces.add(BOARD[i][7]);
        }
    }

    /*
     * Populate the board with the initial white pieces in their initial locations
     */
    private void blackPopulate() {
        int x;
        int y = 1;
        //Pawns
        for (x = 0; x < 8; x++) {
            BOARD[x][y] = new Pawn(x, y, "black", "pawn");
            blackPieces.add(BOARD[x][y]);
        }
        //Rooks
        BOARD[0][0] = new Rook(0, 0, "black", "rook");
        BOARD[7][0] = new Rook(7, 0, "black", "rook");

        //Knights
        BOARD[1][0] = new Knight(1, 0, "black", "knight");
        BOARD[6][0] = new Knight(6, 0, "black", "knight");

        //Bishops
        BOARD[2][0] = new Bishop(2, 0, "black", "bishop");
        BOARD[5][0] = new Bishop(5, 0, "black", "bishop");

        //King
        BOARD[4][0] = blackKing = new King(4, 0, "black", "king");

        //Queen
        BOARD[3][0] = new Queen(3, 0, "black", "queen");

        //add the rest of the pieces to the list of alive pieces
        for (int i = 0; i < 8; ++i) {
            blackPieces.add(BOARD[i][0]);
        }
    }

    /**
     * Prints the board to the terminal.
     */
    public void printBoard() {

        String type;

        for (int y = 0; y < BOARD.length; y++) {
            for (int x = 0; x < BOARD.length; x++) {
                if (BOARD[x][y] != null) {
                    type = BOARD[x][y].getType();
                    switch (type) {
                        case "pawn":
                            if ("black".equals(BOARD[x][y].getColor())) {
                                System.out.print(" |PB| ");
                            } else {
                                System.out.print(" |PW| ");
                            }
                            break;
                        case "bishop":
                            if ("black".equals(BOARD[x][y].getColor())) {
                                System.out.print(" |BB| ");
                            } else {
                                System.out.print(" |BW| ");
                            }
                            break;
                        case "king":
                            if ("black".equals(BOARD[x][y].getColor())) {
                                System.out.print(" |KB| ");
                            } else {
                                System.out.print(" |KW| ");
                            }
                            break;
                        case "knight":
                            if ("black".equals(BOARD[x][y].getColor())) {
                                System.out.print(" |HB| ");
                            } else {
                                System.out.print(" |HW| ");
                            }
                            break;
                        case "queen":
                            if ("black".equals(BOARD[x][y].getColor())) {
                                System.out.print(" |QB| ");
                            } else {
                                System.out.print(" |QW| ");
                            }
                            break;
                        case "rook":
                            if ("black".equals(BOARD[x][y].getColor())) {
                                System.out.print(" |RB| ");
                            } else {
                                System.out.print(" |RW| ");
                            }
                            break;

                    }
                } else {
                    System.out.print(" |  | ");
                }
            }
            System.out.println();
        }
    }
}
