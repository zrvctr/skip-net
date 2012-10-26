package jskibo;

import java.util.ArrayList;
import java.util.Random;

public final class MainStack extends CardStack {
	final int main_stack_size=162;
	  
	// Singelton 
	private static MainStack mainStack;
	  public static synchronized MainStack getInstance()
	  {
	    if ( mainStack == null ) {
                  mainStack = new MainStack();
              }
	    return mainStack;
	  }
	  // Singleton-End
          
	private MainStack() {
		System.out.println("MainStack gets it's only instance");
		
		Cards = new ArrayList<>(main_stack_size);
		for (int i=0;i<main_stack_size-18;i++) { // jokersize is 18;
                Cards.add(new Card (i%12 +1));
		}
		for (int i=main_stack_size-18;i < main_stack_size;i++) {
		Cards.add(new Card (13));
		}
		this.shuffle();
		this.inStack = main_stack_size-1;
	}
        
        public Card takeCard() {
     
           Card card;
            try {
              card = Cards.get(inStack);    
            }   catch (Exception e) {
                    System.out.println("you cannot take a card from an empty Stack "+e);
                    return null;
                }
        inStack--;
        return card;
        } 
        
        
	
	private void shuffle() {
		 Random generator = new Random();
		 int j;
		 for (int i=main_stack_size-1;i > 0 ;i--) {
			 j = generator.nextInt(i);
			 Card t = Cards.get(j);
			 Cards.set(j, Cards.get(i));
			 Cards.set(i,t);
		 }
	}
        
}


