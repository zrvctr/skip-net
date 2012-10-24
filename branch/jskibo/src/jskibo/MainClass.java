
package jskibo;

public class MainClass {
	public static void main (String[] args) {
	System.out.println 		("MainClass started");
	CardStack Stack =  MainStack.getInstance();
	System.out.println		("MainStack"+MainStack.getInstance()+"was created");
	Player Player1 = new Player();
	System.out.println 		("Player 2 draws");
	Player Player2 = new Player();
	
	//Player1.SupportStacks[1].viewCard();
	
//	for (int i=0; i<162;i++) {
//		System.out.println		("drawing "+i+" form stack");
//		Card newcard = Stack.takeCard(); 
//		System.out.println 		("the Card is " + newcard.getValue() );	
//	}
	
	System.out.println 		("MainClass has finished");
	
	}
}

