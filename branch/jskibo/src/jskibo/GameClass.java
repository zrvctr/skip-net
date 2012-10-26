/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jskibo;

import java.util.ArrayList;

/**
 *
 * @author uplink
 */
public class GameClass {

    public GameClass() {
        
        
        
    }
 
    /**
     *
     */
    
    protected MainStack Stack = new MainStack();
    protected ArrayList<Player> Players = new ArrayList<>() ;
    public boolean isFinished = false;
       
    static int CurrentPlayer=0;
    
    public Player CreatePlayer() {
        Players.add(new Player(Stack,Players.size()+1));
        return Players.get(Players.size()-1);
    }

    public int getPlayerInGame() {
        return Players.size();
    }
    
    public void nextTurn() {
        if(CurrentPlayer>=Players.size()) {
            CurrentPlayer = 0;
        }
        Players.get(CurrentPlayer++).makeMove();
    }
    
    
    
}
