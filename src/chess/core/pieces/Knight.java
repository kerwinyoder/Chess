package chess.core.pieces;

import chess.core.Board;

/**
 * A Knight piece
 *
 * @author ragilmore0
 * @author Kerwin Yoder
 */
public class Knight extends Piece {

    /**
     * Creates a new Knight with the given starting position, color, and type.
     *
     * @param x the column of the position
     * @param y the row of the position
     * @param color the color of the piece
     * @param type the type of the piece
     */
    public Knight(int x, int y, String color, String type) {
        super(x, y, color, type);
    }

    @Override
    public boolean isValidMove(Board board, int targetXPos, int targetYPos) {
        return isInBounds(targetXPos, targetYPos) && !isOccupiedByFriend(board, targetXPos, targetYPos) && isLShaped(targetXPos, targetYPos);
    }

    private boolean isLShaped(int targetXPos, int targetYPos) {
        int deltaX = Math.abs(targetXPos - xPos);
        int deltaY = Math.abs(targetYPos - yPos);
        return (deltaX == 1 && deltaY == 2) || deltaX == 2 && deltaY == 1;
    }

    @Override
    public boolean hasValidMoves(Board board) {
        return isValidMove(board, xPos - 2, yPos - 3)
                || isValidMove(board, xPos - 2, yPos + 3)
                || isValidMove(board, xPos + 2, yPos - 3)
                || isValidMove(board, xPos + 2, yPos + 3)
                || isValidMove(board, xPos - 3, yPos - 2)
                || isValidMove(board, xPos - 3, yPos + 2)
                || isValidMove(board, xPos + 3, yPos - 2)
                || isValidMove(board, xPos + 3, yPos + 2);
    }

}
