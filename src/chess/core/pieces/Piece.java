/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.core.pieces;

import java.io.Serializable;

/**
 *
 * @author ragilmore0
 */
public abstract class Piece implements Serializable {

    private int xPos;
    private int yPos;
    private String color;
    private final String pieceType;
    //private boolean alive; Maybe?

    public Piece(int x, int y, String color, String type) {
        this.xPos = x;
        this.yPos = y;
        this.color = color;
        this.pieceType = type;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getXPos() {
        return this.xPos;
    }

    public void setXPos(int x) {
        this.xPos = x;
    }

    public int getYPos() {
        return this.yPos;
    }

    public void setYPos(int y) {
        this.yPos = y;
    }

    public String getType() {
        return this.pieceType;
    }

}
