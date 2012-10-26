package jskibo;

import java.util.*;

public final class MainStack extends CardStack {
	final int main_stack_size=162;
	  
          
	public MainStack() {
		System.out.println("MainStack gets it's only instance");
		
		for (int i=0;i<main_stack_size-18;i++) { // jokersize is 18;
                Cards.add(new Card (i%12 +1));
		}
		for (int i=main_stack_size-18;i < main_stack_size;i++) {
		Cards.add(new Card (13));
                Collections.shuffle(Cards);
		}
		
	}
        
        public Card takeCard() {
     
           Card card;
            try {
              card = Cards.pop();    
            }   catch (Exception e) {
                    System.out.println("you cannot take a card from an empty Stack "+e);
                    return null;
                }
        return card;
        } 
        
                
}


