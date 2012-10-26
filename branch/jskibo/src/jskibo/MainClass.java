
package jskibo;

public class MainClass {
	public static void main (String[] args) {
	System.out.println 		("MainClass started");
	
	GameClass Game = new GameClass();
        for (int i=0;i<4;i++) {
                Game.CreatePlayer();
            }
       // while (Game.isFinished==false) {
       //    Game.nextTurn();
       // }
            
        
	//Player1.SupportStacks[1].viewCard();
	
//	for (int i=0; i<162;i++) {
//		System.out.println		("drawing "+i+" form stack");
//		Card newcard = Stack.takeCard(); 
//		System.out.println 		("the Card is " + newcard.getValue() );	
//	}
	
	System.out.println 		("MainClass has finished");
	
	}
}

