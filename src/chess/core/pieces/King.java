package chess.core.pieces;

import chess.core.Board;

/**
 * A King piece
 *
 * @author ragilmore0
 * @author Kerwin Yoder
 * @version 2017.05.02
 */
public class King extends Piece {

    boolean hasMoved;

    /**
     * Creates a new King with the given position, color, and type
     *
     * @param x the column of the starting position
     * @param y the row of the starting position
     * @param color the color of the piece
     * @param type the type of the piece
     */
    public King(int x, int y, String color, String type) {
        super(x, y, color, type);
        hasMoved = false;
    }

    @Override
    public boolean isValidMove(Board board, int targetXPos, int targetYPos) {
        int deltaX = Math.abs(targetXPos - xPos);
        int deltaY = Math.abs(targetYPos - yPos);
        if (!isInBounds(targetXPos, targetYPos) || isOccupiedByFriend(board, targetXPos, targetYPos)) {
            return false;
        }
        switch (deltaX) {
            case 0:
            case 1:
                if ((deltaY == 0 || deltaY == 1) && !board.isThreatened(targetXPos, targetYPos, COLOR)) {
                    hasMoved = true;
                    return true;
                } else {
                    return false;
                }
            case 2:
                //check if this is a valid castle move and the king has not moved yet
                if (deltaY != 0 || hasMoved) {
                    return false;
                }
                Piece rook;
                //queen-side castle
                if (targetXPos == 2) {
                    //if the king is in check, will pass through check, or will be in check after moving, he cannot castle
                    for (int i = 4; i >= 2; --i) {
                        if (board.isThreatened(i, yPos, COLOR)) {
                            return false;
                        }
                    }
                    rook = board.getPiece(0, yPos);
                    return rook != null && rook instanceof Rook && !((Rook) rook).hasMoved() && !isBlockedHorizontally(board, 0);
                } //king-side castle 
                else {
                    //if the king is in check, will pass through check, or will be in check after moving, he cannot castle
                    for (int i = 4; i <= 6; ++i) {
                        if (board.isThreatened(i, yPos, COLOR)) {
                            return false;
                        }
                    }
                    rook = board.getPiece(7, yPos);
                    return rook != null && rook instanceof Rook && !((Rook) rook).hasMoved() && !isBlockedHorizontally(board, 7);
                }
            default:
                //invalid isValidMove
                return false;
        }
    }

    /**
     * Checks if the King is in check
     *
     * @param board the board on which the King is loated
     * @return true if the King is in check and false otherwise
     */
    public boolean isInCheck(Board board) {
        return board.isThreatened(xPos, yPos, COLOR);
    }

    /**
     * Checks if the King is checkmated
     *
     * @param board the board on which the King is located
     * @return true if the King is checkmated and false otherwise
     */
    public boolean isCheckmated(Board board) {
        //if the king is not in check, he is not checkmated
        if (!isInCheck(board)) {
            return false;
        }
        //Check if the king has any valid moves
        return hasValidMoves(board);
    }

    //check all possible moves to see if there is a valid move
    public boolean hasValidMoves(Board board) {
        for (int i = xPos - 1; i <= xPos + 1; ++i) {
            for (int j = yPos - 1; j <= yPos + 1; ++j) {
                if (isValidMove(board, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }
}
