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
public class MoveMessage implements Serializable {

    private Integer[] move;

    public MoveMessage(Integer[] move) {
        this.move = move;
    }

    public Integer[] getMove() {
        return move;
    }

}
