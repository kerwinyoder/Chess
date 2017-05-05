package chess.core.pieces;

import chess.core.Board;

/**
 * A Queen piece
 *
 * @author ragilmore0
 * @author Kerwin Yoder
 * @version 2017.05.02
 */
public class Queen extends Piece {

    /**
     * Creates a new Queen with the given position, color, and type
     *
     * @param x the column of the starting position
     * @param y the row of the starting position
     * @param color the color of the piece
     * @param type the type of the piece
     */
    public Queen(int x, int y, String color, String type) {
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
        if ((isHorizontal(targetXPos, targetYPos) && !isBlockedHorizontally(board, targetXPos))
                || (isVertical(targetXPos, targetYPos) && !isBlockedVertically(board, targetYPos))
                || (isDiagonal(targetXPos, targetYPos) && !isBlockedDiagonally(board, targetXPos, targetYPos))) {
            return true;
        }
        return false;
    }

    @Override
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
