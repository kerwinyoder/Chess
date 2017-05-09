package chess.core.pieces;

import chess.core.Board;
import chess.core.Move;
import java.io.Serializable;

/**
 * A piece for the Chess game
 *
 * @author ragilmore0
 * @author Kerwin Yoder
 * @version 2017.05.02
 */
public abstract class Piece implements Serializable {

    /**
     * The column of the piece's current position
     */
    protected int xPos;

    /**
     * The row of the piece's current position
     */
    protected int yPos;

    /**
     * The color of the piece
     */
    protected final String COLOR;

    /**
     * The type of the piece
     */
    protected final String TYPE;

    /**
     *
     * @param x the column of the piece
     * @param y the row of the piece
     * @param color the color of the piece
     * @param type the type of the piece
     */
    public Piece(int x, int y, String color, String type) {
        this.COLOR = color;
        this.TYPE = type;
        this.xPos = x;
        this.yPos = y;
    }

    /**
     * Gets the color of the piece
     *
     * @return the color of the piece
     */
    public String getColor() {
        return this.COLOR;
    }

    /**
     * Gets the column of the piece's current position
     *
     * @return the column of the piece's current position
     */
    public int getXPos() {
        return this.xPos;
    }

    /**
     * Sets the column of the piece's position
     *
     * @param x the column of the piece's new position
     */
    public void setXPos(int x) {
        this.xPos = x;
    }

    /**
     * Gets the row of the piece's current position
     *
     * @return the row of the piece's current position
     */
    public int getYPos() {
        return this.yPos;
    }

    /**
     * Sets the row of the piece's position
     *
     * @param y the row of piece's new position
     */
    public void setYPos(int y) {
        this.yPos = y;
    }

    /**
     * Gets the type of the piece
     *
     * @return the type of the piece
     */
    public String getType() {
        return this.TYPE;
    }

    /**
     * Checks if the given move is valid
     *
     * @param board The board on which the move is being checked
     * @param targetXPos the column of the new position
     * @param targetYPos the row of the new position
     * @return true if the move is valid and false otherwise
     */
    public abstract boolean isValidMove(Board board, int targetXPos, int targetYPos);

    /**
     * Checks if the piece has any available valid moves
     *
     * @param board the board on which the piece is stored
     * @return true if the piece has a valid move available and false otherwise
     */
    public abstract boolean hasValidMoves(Board board);

    /**
     * Checks if the given position is in the bounds of the board.
     *
     * @param targetXPos the column of the position that is being tested
     * @param targetYPos the row of the position that is being tested
     * @return true if the given position is within the bounds of the board and
     * false otherwise
     */
    protected final boolean isInBounds(int targetXPos, int targetYPos) {
        return targetXPos >= 0 && targetXPos < 8 && targetYPos >= 0 && targetYPos < 8;
    }

    /**
     * Checks if the given position can be reached by making a horizontal move
     * from the piece's current position
     *
     * @param targetXPos the column of the target position
     * @param targetYPos the row of the target position
     * @return true if the target position can be reached by making a horizontal
     * move from the piece's current position
     */
    public final boolean isHorizontal(int targetXPos, int targetYPos) {
        return yPos == targetYPos && targetXPos != xPos;
    }

    /**
     * Checks if the given position can be reached by making a vertical move
     * from the piece's current position
     *
     * @param targetXPos the column of the target position
     * @param targetYPos the row of the target position
     * @return true if the target position can be reached by making a vertical
     * move from the piece's current position
     */
    public final boolean isVertical(int targetXPos, int targetYPos) {
        return xPos == targetXPos && targetYPos != yPos;
    }

    /**
     * Checks if the given position can be reached by making a diagonal move
     * from the piece's current position
     *
     * @param targetXPos the column of the target position
     * @param targetYPos the row of the target position
     * @return true if the target position can be reached by making a diagonal
     * move from the piece's current position
     */
    protected final boolean isDiagonal(int targetXPos, int targetYPos) {
        return Math.abs(targetXPos - xPos) == Math.abs(targetYPos - yPos) && targetXPos != xPos;
    }

    /**
     * Checks if the path between the piece's current position and the target
     * position is blocked
     *
     * @param board the board on which the piece is located
     * @param targetXPos the column of the target position
     * @return true if the path is blocked horizontally and false otherwise
     */
    protected final boolean isBlockedHorizontally(Board board, int targetXPos) {
        //if the move is less than two blocks, the path cannot be blocked
        if (Math.abs(targetXPos - xPos) < 2) {
            return false;
        }

        //check if any pieces block the path
        int max = Math.max(xPos, targetXPos);
        int min = Math.min(xPos, targetXPos);
        for (int i = min + 1; i < max; ++i) {
            if (board.getPiece(i, yPos) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the piece can block an enemy piece vertically
     *
     * @param board the board on which the piece is located
     * @param move the move that the piece is attempting to block from the enemy
     * piece's position to the king's current position
     * @return
     */
    public final boolean canBlockHorizontally(Board board, Move move) {
        int enemyXPos = move.START_X;
        int enemyYPos = move.START_Y;
        int kingXPos = move.TARGET_X;
        //if the move is less than two blocks, the path cannot be blocked
        if (Math.abs(kingXPos - enemyXPos) < 2) {
            return false;
        }

        //check if any pieces block the path
        int max = Math.max(enemyXPos, kingXPos);
        int min = Math.min(enemyXPos, kingXPos);
        for (int i = min + 1; i < max; ++i) {
            if (board.getPiece(i, enemyYPos) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the path between the piece's current position and the target
     * position is blocked
     *
     * @param board the board on which the piece is located
     * @param targetYPos the row of the target position
     * @return true if the path is blocked vertically and false otherwise
     */
    protected final boolean isBlockedVertically(Board board, int targetYPos) {
        //if the move is less than two blocks, the path cannot be blocked
        if (Math.abs(targetYPos - yPos) < 2) {
            return false;
        }

        //check if any pieces block the path
        int max = Math.max(yPos, targetYPos);
        int min = Math.min(yPos, targetYPos);
        for (int i = min + 1; i < max; ++i) {
            if (board.getPiece(xPos, i) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the piece can block an enemy piece horizontally
     *
     * @param board the board on which the piece is located
     * @param move the move that the piece is attempting to block from the enemy
     * piece's position to the king's current position
     * @return
     */
    public final boolean canBlockVertically(Board board, Move move) {
        int enemyXPos = move.START_X;
        int enemyYPos = move.START_Y;
        int kingYPos = move.TARGET_Y;
        //if the move is less than two blocks, the path cannot be blocked
        if (Math.abs(kingYPos - enemyYPos) < 2) {
            return false;
        }

        //check if any pieces block the path
        int max = Math.max(enemyYPos, kingYPos);
        int min = Math.min(enemyYPos, kingYPos);
        for (int i = min + 1; i < max; ++i) {
            if (board.getPiece(enemyXPos, i) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the path between the piece's current position and the target
     * position is blocked
     *
     * @param board the board on which the piece is located
     * @param targetXPos the column of the target position
     * @param targetYPos the row of the target position
     * @return true if the path is blocked diagonally and false otherwise
     */
    protected final boolean isBlockedDiagonally(Board board, int targetXPos, int targetYPos) {
        int x;
        int y;
        if (xPos <= targetXPos && yPos <= targetYPos) {
            x = xPos + 1;
            y = yPos + 1;
            while (x < targetXPos && yPos < targetYPos) {
                if (board.getPiece(x, y) != null) {
                    return true;
                }
                ++x;
                ++y;
            }
        } else if (xPos <= targetXPos && yPos > targetYPos) {
            x = xPos + 1;
            y = yPos - 1;
            while (x < targetXPos && yPos > targetYPos) {
                if (board.getPiece(x, y) != null) {
                    return true;
                }
                ++x;
                --y;
            }
        } else if (xPos > targetXPos && yPos <= targetYPos) {
            x = xPos - 1;
            y = yPos + 1;
            while (x > targetXPos && yPos < targetYPos) {
                if (board.getPiece(x, y) != null) {
                    return true;
                }
                --x;
                ++y;
            }
        } else if (xPos > targetXPos && yPos > targetYPos) {
            x = xPos - 1;
            y = yPos - 1;
            while (x > targetXPos && yPos > targetYPos) {
                if (board.getPiece(x, y) != null) {
                    return true;
                }
                --x;
                --y;
            }
        }
        return false;
    }

    /**
     * Checks if the piece can block an enemy piece diagonally
     *
     * @param board the board on which the piece is located
     * @param move the move that the piece is attempting to block from the enemy
     * piece's position to the king's current position
     * @return
     */
    public final boolean canBlockDiagonally(Board board, Move move) {
        int enemyXPos = move.START_X;
        int enemyYPos = move.START_Y;
        int kingXPos = move.TARGET_X;
        int kingYPos = move.TARGET_Y;
        int x;
        int y;
        if (kingXPos <= enemyXPos && kingYPos <= enemyYPos) {
            x = kingXPos + 1;
            y = kingYPos + 1;
            while (x < enemyXPos && kingYPos < enemyYPos) {
                if (isValidMove(board, x, y)) {
                    return true;
                }
                ++x;
                ++y;
            }
        } else if (kingXPos <= enemyXPos && kingYPos > enemyYPos) {
            x = kingXPos + 1;
            y = kingYPos - 1;
            while (x < enemyXPos && kingYPos > enemyYPos) {
                if (isValidMove(board, x, y)) {
                    return true;
                }
                ++x;
                --y;
            }
        } else if (kingXPos > enemyXPos && kingYPos <= enemyYPos) {
            x = kingXPos - 1;
            y = kingYPos + 1;
            while (x > enemyXPos && kingYPos < enemyYPos) {
                if (isValidMove(board, x, y)) {
                    return true;
                }
                --x;
                ++y;
            }
        } else if (kingXPos > enemyXPos && kingYPos > enemyYPos) {
            x = kingXPos - 1;
            y = kingYPos - 1;
            while (x > enemyXPos && kingYPos > enemyYPos) {
                if (isValidMove(board, x, y)) {
                    return true;
                }
                --x;
                --y;
            }
        }
        return false;
    }

    /**
     * Checks if the given position is occupied by any piece
     *
     * @param board the board on which the piece is located
     * @param targetXPos the column of the target position
     * @param targetYPos the row of the target position
     * @return true if the given position is occupied by a piece and false
     * otherwise
     */
    protected final boolean isOccupied(Board board, int targetXPos, int targetYPos) {
        return board.getPiece(targetXPos, targetYPos) != null;
    }

    /**
     * Checks if the given position is occupied by a piece of the same color as
     * this piece
     *
     * @param board the board on which the piece is located
     * @param targetXPos the column of the target position
     * @param targetYPos the row of the target position
     * @return true if the given position is occupied by a piece of the same
     * color as this piece and false otherwise
     */
    protected final boolean isOccupiedByFriend(Board board, int targetXPos, int targetYPos) {
        Piece destPiece = board.getPiece(targetXPos, targetYPos);
        return destPiece != null && destPiece.getColor().equalsIgnoreCase(COLOR);
    }

    /**
     * Checks if the given position is occupied by an enemy piece
     *
     * @param board the board on which the piece is located
     * @param targetXPos the column of the target position
     * @param targetYPos the row of the target position
     * @return true if the given position is occupied by an enemy piece
     */
    protected final boolean isOccupiedByFoe(Board board, int targetXPos, int targetYPos) {
        Piece destPiece = board.getPiece(targetXPos, targetYPos);
        return destPiece != null && !destPiece.getColor().equalsIgnoreCase(COLOR);
    }

    /**
     * Checks if it is this piece's turn
     *
     * @param board the board on which the piece is located
     * @return true if it is this piece's turn
     */
    protected boolean isTurn(Board board) {
        return (board.isWhiteTurn() && COLOR.equalsIgnoreCase("white")) || !board.isWhiteTurn() && COLOR.equalsIgnoreCase("black");
    }
}
