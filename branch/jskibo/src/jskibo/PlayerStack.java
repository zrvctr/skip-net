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

        public PlayerStack() {}
    
        public PlayerStack(MainStack Stack) {
	
            for (int i=0; i<10;i++) {
                Cards.push(Stack.takeCard());
            }
        }
    
	public boolean playCard(DropStack dropstack) {

            Card card = Cards.peek();
            if(dropstack.dropCard(card)) {
		Cards.pop();
		return true;
	} 
		return false;
	}    
}