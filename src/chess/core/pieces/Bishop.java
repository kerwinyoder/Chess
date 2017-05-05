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
        /*isTurn is a hack that is used so isValidMove returns true when it is 
        being used by isThreatened to see if the king is in check. When it is 
        not the piece's turn, isValidMove is being used to see if the piece 
        could attack the target position on its next turn This makes the 
        assumption that any friendly pieces in that location will no longer be 
        there on the next move (i.e. an enemy piece is capturing the piece at 
        the target destination.*/
        if (!isInBounds(targetXPos, targetYPos) || (isOccupiedByFriend(board, targetXPos, targetYPos) && isTurn(board))) {
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
