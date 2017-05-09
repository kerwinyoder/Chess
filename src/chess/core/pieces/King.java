package chess.core.pieces;

import chess.core.Board;
import chess.core.Move;

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
                if (deltaY == 0 || deltaY == 1) {
                    /*!isTurn handles the case where a king is capturing a piece that is protected by the opposing king.
                    Without it, the opposing king's isValidMove() will return false when the attacking king's isMoveIntoCheck()
                    is called if an enemy can move to the contested square.*/
                    boolean isTurn = isTurn(board);
                    Piece victim = board.getPiece(targetXPos, targetYPos);
                    if (!isTurn && victim != null && victim instanceof King) {
                        return true;
                    } else if (!isCheckAfterMove(board, new Move(xPos, yPos, targetXPos, targetYPos))) {
                        hasMoved = true;
                        return true;
                    }
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
                        if (isCheckAfterMove(board, new Move(xPos, yPos, i, yPos))) {
                            return false;
                        }
                    }
                    rook = board.getPiece(0, yPos);
                    return rook != null && rook instanceof Rook && !((Rook) rook).hasMoved() && !isBlockedHorizontally(board, 0);
                } //king-side castle
                else {
                    //if the king is in check, will pass through check, or will be in check after moving, he cannot castle
                    for (int i = 4; i <= 6; ++i) {
                        if (isCheckAfterMove(board, new Move(xPos, yPos, i, yPos))) {
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
     * Checks if moving the king to the given location will put him in check.
     *
     * @param board the game board
     * @param targetXPos the column of the target location
     * @param targetYPos the row of the target location
     * @return true if the move will put the king in check and false otherwise
     */
    public boolean isCheckAfterMove(Board board, Move move) {
        boolean isCheck = false;
        //temporarily move the king to test if the king will be in check at the new location.
        Piece piece = board.getPiece(move.START_X, move.START_Y);
        Piece victim = board.getPiece(move.TARGET_X, move.TARGET_Y);
        if (victim != null) {
            board.removePiece(victim);
        }
        board.setPiece(null, move.START_X, move.START_Y);
        piece.setXPos(move.TARGET_X);
        piece.setYPos(move.TARGET_Y);
        board.setPiece(piece, move.TARGET_X, move.TARGET_Y);
        isCheck = isInCheck(board);

        //move the pieces back to their original locations
        board.setPiece(victim, move.TARGET_X, move.TARGET_Y);
        if (victim != null) {
            board.addPiece(victim);
        }
        board.setPiece(piece, move.START_X, move.START_Y);
        piece.setXPos(move.START_X);
        piece.setYPos(move.START_Y);
        return isCheck;
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
