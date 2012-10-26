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
    
    protected MainStack Stack = MainStack.getInstance();
    protected ArrayList<Player> Players = new ArrayList<>() ;
    public boolean isFinished = false;
       
    static int PlayerInGame=0;
    static int CurrentPlayer=0;
    
    public Player CreatePlayer() {        
        Players.add(new Player(++PlayerInGame));
        return Players.get(PlayerInGame-1);
    }

    public static int getPlayerInGame() {
        return PlayerInGame;
    }
    
    public void nextTurn() {
        if(CurrentPlayer>=PlayerInGame) {
            CurrentPlayer = 0;
        }
        Players.get(CurrentPlayer++).makeMove();
    }
    
    
    
}
