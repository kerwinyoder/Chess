package chess.core.pieces;

import chess.core.Board;

/**
 * A Rook piece
 *
 * @author ragilmore0
 * @author Kerwin Yoder
 * @version 2017.05.02
 */
public class Rook extends Piece {

    private boolean hasMoved;

    /**
     * Creates a new rook with the given position, color, and type
     *
     * @param x the column of the starting position
     * @param y the row of the starting position
     * @param color the color of the piece
     * @param type the type of the piece
     */
    public Rook(int x, int y, String color, String type) {
        super(x, y, color, type);
        hasMoved = false;
    }

    @Override
    public boolean isValidMove(Board board, int targetXPos, int targetYPos) {
        if (!isInBounds(targetXPos, targetYPos) || isOccupiedByFriend(board, targetXPos, targetYPos)) {
            return false;
        }
        if ((isHorizontal(targetXPos, targetYPos) && !isBlockedHorizontally(board, targetXPos)) || isVertical(targetXPos, targetYPos) && !isBlockedVertically(board, targetYPos)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the rook has moved since the start of the game
     *
     * @return true if the rook has moved since the start of the game
     */
    public boolean getHasMoved() {
        return hasMoved;
    }

    /**
     * Sets hasMoved to the given value
     *
     * @param hasMoved the value to use for hasMoved
     */
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public boolean hasValidMoves(Board board) {
        return isValidMove(board, xPos - 1, yPos)
                || isValidMove(board, xPos + 1, yPos)
                || isValidMove(board, xPos, yPos - 1)
                || isValidMove(board, xPos, yPos + 1);
    }
}
