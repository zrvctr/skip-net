package jskibo;


public class Card {
	private byte cardvalue;
	
	public Card(int cardval) {
		if (cardval > 1 || cardval < 14) {
			this.cardvalue = (byte)cardval;	
		} else  {
                    System.out.println("You cannot stuck an illigal Card in here");
                }
                    
	}
	
	public byte getValue() {
		return this.cardvalue;
	}
	
}
