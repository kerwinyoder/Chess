package chess.core;

import java.io.Serializable;

/**
 * A Move in the game
 *
 * @author Kerwin Yoder
 * @version 2017.05.02
 */
public class Move implements Serializable {

    /**
     * The column of the starting position
     */
    public final int START_X;

    /**
     * The row of the starting position
     */
    public final int START_Y;

    /**
     * The column of the target position
     */
    public final int TARGET_X;

    /**
     * The row of the target position
     */
    public final int TARGET_Y;

    /**
     * Creates a move with the given starting position and target position
     *
     * @param startX the column of the starting position
     * @param startY the row of the starting position
     * @param targetX the column of the target position
     * @param targetY the row of the target position
     */
    public Move(int startX, int startY, int targetX, int targetY) {
        this.START_X = startX;
        this.START_Y = startY;
        this.TARGET_X = targetX;
        this.TARGET_Y = targetY;
    }
}
