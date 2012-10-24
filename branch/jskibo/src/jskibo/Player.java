package jskibo;

public class Player {

	public Player() {
                PlayerNumber = CurrentPlayers++;
		Hand = new Card[5];
		SupportStacks = new OpenCardStack[4];
		for (int i=0; i<5; i++) {
			this.draw();
		}
                
	}
	
	private void draw() {
		if (CardsInHand < 5) {
			Hand[CardsInHand] = MainStack.getInstance().takeCard();
			System.out.println("Player " + PlayerNumber + " Drew " + Hand[CardsInHand].getValue());
			CardsInHand++;
		}
		
	}
	public void drop(int CardNumber) {
	//0-4 are Cards in the Hand
	// 5-8 are the SupportStacks
        // 9 is the SkipoStack
		if(CardNumber>=0 & CardNumber<=4 & CardsInHand >= CardNumber)
		{
			
		}
		else if(CardNumber>=5 & CardNumber<=8 & SupportStacks[CardNumber] != null) {
		
		}
		else {
		System.out.println("You are a Silly bugga");
		}
	}
	public int getPoints() {
		return this.points;
	}
	
	
	protected OpenCardStack[] SupportStacks;
	private  Card[] Hand;
	private byte CardsInHand=0;
	private int points=0;
        static int CurrentPlayers=0;
        private int PlayerNumber;
}
