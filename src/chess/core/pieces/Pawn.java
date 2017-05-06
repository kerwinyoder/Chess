package chess.core.pieces;

import chess.core.Board;

/**
 * A Pawn piece
 *
 * @author ragilmore0
 * @author Kerwin Yoder
 * @version 2017.05.02
 */
public class Pawn extends Piece {

    /**
     * Creates a new Pawn with the given position, color, and type
     *
     * @param x the columnn of the position
     * @param y the row of the position
     * @param color the color of the piece
     * @param type the type of the piece
     */
    public Pawn(int x, int y, String color, String type) {
        super(x, y, color, type);
    }

    @Override
    public boolean isValidMove(Board board, int targetXPos, int targetYPos) {
        if (!isInBounds(targetXPos, targetYPos) || !isValidDirection(targetYPos)) {
            return false;
        }
        int deltaX = Math.abs(targetXPos - xPos); //change in x
        int deltaY = Math.abs(targetYPos - yPos); //change in y
        switch (deltaX) {
            case 0: //moving straight forward
                switch (deltaY) {
                    case 1: //moving 1 block forward
                        return !isOccupied(board, targetXPos, targetYPos);
                    case 2: //moving 2 blocks forward
                        return !hasMoved() && !isBlockedVertically(board, targetYPos) && !isOccupied(board, targetXPos, targetYPos);
                    default: //invalid move
                        return false;
                }
            case 1: //moving to the side one square to capture a piece
                if (deltaY == 1) {
                    return isOccupiedByFoe(board, targetXPos, targetYPos) || isEnPassant(board, targetXPos, targetYPos) || (!isTurn(board) && isOccupiedByFriend(board, targetXPos, targetYPos));
                } else {
                    return false;
                }
            default: //invalid move
                return false;
        }
    }

    /*
     * Checks if the given target position is a move in a valid direction. It verifies that the pawn is not attempting to move backward.
     * @param targetYPos the column of the target position
     * @return true if the move is in a valid direction (forward)
     */
    private boolean isValidDirection(int targetYPos) {
        if (COLOR.equalsIgnoreCase("white")) {
            return targetYPos < yPos;
        } else {
            return targetYPos > yPos;
        }
    }

    /*
     * Checks if the Pawn has moved since the beginning of the game
     * @return true if the Pawn has moved since the beginning of the game
     */
    private boolean hasMoved() {
        if (COLOR.equalsIgnoreCase("white")) {
            return yPos != 6;
        } else {
            return yPos != 1;
        }
    }

    /*
     * Checks if the move given by the target position is an en passant move
     * @param board the board on which the piece is located
     * @param targetXPos the column of the target position
     * @param targetYPos the row of the target position
     * @return true if the given target position is an en passant move
     */
    private boolean isEnPassant(Board board, int targetXPos, int targetYPos) {
        Piece piece = board.getPiece(targetXPos, yPos);
        return piece != null && piece instanceof Pawn && piece == board.getEnPassantVictim();
    }

    @Override
    public boolean hasValidMoves(Board board) {
        if (COLOR.equals("black")) {
            return isValidMove(board, xPos, yPos + 1)
                    || isValidMove(board, xPos, yPos + 2)
                    || isValidMove(board, xPos - 1, yPos + 1)
                    || isValidMove(board, xPos + 1, yPos + 1);
        } else {
            return isValidMove(board, xPos, yPos - 1)
                    || isValidMove(board, xPos, yPos - 2)
                    || isValidMove(board, xPos - 1, yPos - 1)
                    || isValidMove(board, xPos + 1, yPos - 1);
        }
    }

}
