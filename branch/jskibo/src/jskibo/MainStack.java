package jskibo;

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
		
		this.Cards = new Card[main_stack_size];
		for (int i=0;i<main_stack_size-18;i++) { // jokersize is 18;
			this.Cards[i] = new Card (i%12 +1);
		}
		for (int i=main_stack_size-18;i < main_stack_size;i++) {
		this.Cards[i]=new Card(13);
		}
		this.shuffle();
		this.InStack = main_stack_size-1;
	}
	
	public void shuffle() {
		 Random generator = new Random();
		 int j;
		 for (int i=main_stack_size-1;i > 0 ;i--) {
			 j = generator.nextInt(i);
			 Card t = Cards[j];
			 Cards[j] = Cards[i];
			 Cards[i] = t;
		 }
	}
	@Override 
	public void dropCard(Card card) {
	System.out.println("You cannot drop something on the MainStack");	
	}
}


