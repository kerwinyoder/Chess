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
        /*isTurn is a hack that is used so isValidMove returns true when it is 
        being used by isThreatened to see if the king is in check. When it is 
        not the piece's turn, isValidMove is being used to see if the piece 
        could attack the target position on its next turn This makes the 
        assumption that any friendly pieces in that location will no longer be 
        there on the next move (i.e. an enemy piece is capturing the piece at 
        the target destination.*/
        return isInBounds(targetXPos, targetYPos) && (!isOccupiedByFriend(board, targetXPos, targetYPos)  && isTurn(board)) && isLShaped(targetXPos, targetYPos);
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
