/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jskibo;

/**
 * This class represents the Player Stack form witch the cards has to be
 * discarded.
 * @author uplink
 */
public class PlayerStack extends ViewableStack {

	public PlayerStack() {
	
        for (int i=0; i<10;i++) {
	Cards.add(MainStack.getInstance().takeCard());
        }
            System.out.println("PlayerStack of " + GameClass.PlayerInGame + 
                    " gets created" );
        }
    
	public boolean discard(DropStack dropstack) {

	Card card = Cards.get(inStack);
	if(dropstack.dropCard(card)) {
		inStack--;
		return true;
	} 
		return false;
	}    
}