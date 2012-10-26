package jskibo;

/**
 * 
 * 
 * @author uplink
 */

 public class Player {

	public Player(int pnum) {
		PlayerNumber = pnum;
		SupportStacks = new SupportStack[4];
                PlayerStack PlayerStack = new PlayerStack();
		try {
			this.draw();    
		} catch (Exception e) {
			System.out.println("Exception occord "+e);
                }
		        System.out.println("Cards left " + MainStack.getInstance().getCardsLeft());
	}
	
        
	protected final void draw() {
		while (CardsInHand < 5)
		 {
			Hand[CardsInHand] = MainStack.getInstance().takeCard();
			System.out.println("Player " + PlayerNumber + " Drew " + Hand[CardsInHand].getValue());
			CardsInHand++;
		}
	}
        
	public boolean drop(Card card, DropStack dropStack) {
		return dropStack.dropCard(card);
	}
	
	public int getPoints() {
		return this.points;
	}
        
	public void makeMove() {
		// Here everything is done what a player can do in a trun
	}
		
	protected SupportStack[] SupportStacks;
	private  Card[] Hand = new Card[5];
	private byte CardsInHand=0;
	private int points=0;
	private int PlayerNumber;
	private boolean hasWon=false;

}

