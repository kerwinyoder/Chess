/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package communication;

/**
 *
 * @author WAMPUS
 */
public class MoveMessage {
    
    private int[] move;
    
    public MoveMessage(int[] move){
        this.move = move;
    }
    
    public int[] getMove(){
        return move;
    }
    
}
