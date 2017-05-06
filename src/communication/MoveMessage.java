/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.Serializable;

/**
 *
 * @author WAMPUS
 */
public class MoveMessage extends Message implements Serializable {

    private Integer[] move;
    private boolean valid = false;
    private long timeTaken;

    public MoveMessage(String h, Object b) {
        super(h, b);
    }

    public void setMove(Integer[] i) {
        move = i;
    }

    public Integer[] getMove() {
        return move;
    }

    public void isValid() {
        valid = true;
    }

    public boolean getValid() {
        return valid;
    }

    public void setTime(long t) {
        timeTaken = t;
    }

    public long getTime() {
        return timeTaken;
    }

}
