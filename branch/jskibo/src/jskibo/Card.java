package jskibo;


public class Card {
	private byte CardValue;
	public boolean isJoker = false;  
	protected Card(int cardval) {
		if (cardval > 1 || cardval < 14) {
			CardValue = (byte)cardval;
                        if (cardval==13) {
                        isJoker=true;   
                        }
		} else  {
                    System.out.println("You cannot stick an illigal Card in here");
                }
                    
	}
	
	public byte getValue() {
		return this.CardValue;
	}
        void transformJoker(int cardval) {
            if(isJoker) {
                CardValue = (byte)cardval;
            }
        }
        
        boolean isJoker() {
            return isJoker;
        }
}
