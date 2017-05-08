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
    private Integer[] target;
    private boolean enPassant = false;
    private boolean promotion = false;
    private boolean valid = false;
    private long timeTaken;
    private String reason;
    private String choice;

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

    public void setReason(String r) {
        reason = r;
    }

    public String getReason() {
        return reason;
    }


    /*Start promotion logic*/
    public void isPromotion() {
        promotion = true;
    }

    public boolean getPromotion() {
        return promotion;
    }

    public void setChoice(String c) {
        choice = c;
    }

    public String getChoice() {
        return choice;
    }

    /*Promotion and En Passant Target*/
    public void setEnPassant() {
        enPassant = true;
    }

    public boolean getEnPassant() {
        return enPassant;
    }

    public void setTarget(Integer[] i) {
        target = i;
    }

    public Integer[] getTarget() {
        return target;
    }

}
