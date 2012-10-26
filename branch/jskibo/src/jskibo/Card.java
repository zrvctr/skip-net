package jskibo;


public class Card {
	private int cardValue;
	
        protected Card(int cardval) {
		if (cardval > 1 || cardval < 14) {
			cardValue = cardval;
		} else {
                    System.out.println("You cannot stick an illigal Card in here");
                }
                    
	}
	
	public int getValue() {
		return cardValue;
	}
      
        
        boolean isJoker() {
            return cardValue==13;
        }
}
