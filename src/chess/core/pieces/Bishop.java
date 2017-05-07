package chess.core.pieces;

import chess.core.Board;

/**
 * A Bishop piece
 *
 * @author ragilmore0
 * @author Kerwin Yoder
 * @version 2017.05.02
 */
public class Bishop extends Piece {

    /**
     * Creates a new Bishop with the given position, color, and type
     *
     * @param x the column of the starting position
     * @param y the row of the starting position
     * @param color the color of the piece
     * @param type the type of the piece
     */
    public Bishop(int x, int y, String color, String type) {
        super(x, y, color, type);
    }

    @Override
    public boolean isValidMove(Board board, int targetXPos, int targetYPos) {
        if (!isInBounds(targetXPos, targetYPos) || isOccupiedByFriend(board, targetXPos, targetYPos)) {
            return false;
        }
        if (isDiagonal(targetXPos, targetYPos) && !isBlockedDiagonally(board, targetXPos, targetYPos)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasValidMoves(Board board) {
        return isValidMove(board, xPos - 1, yPos - 1)
                || isValidMove(board, xPos - 1, yPos + 1)
                || isValidMove(board, xPos + 1, yPos - 1)
                || isValidMove(board, xPos + 1, yPos + 1);
    }

}
