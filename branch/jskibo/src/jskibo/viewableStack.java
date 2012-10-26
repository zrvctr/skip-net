/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jskibo;

/**
 *
 * @author uplink
 */
public class ViewableStack extends CardStack {
     
	public Card viewCard() {
		return Cards.get(inStack);
	}
        
}
