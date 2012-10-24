package jskibo;


public class CardStack {
	
	public CardStack() {
	}
	
	public CardStack(int size) {
		this.Cards = new Card[size];
	}
	
	public Card takeCard() {
		Card card = Cards[InStack--];
		return card;
	}
	public void dropCard(Card card) {
		Cards[++InStack] = card;
	}
	//private void transformJoker();
	protected Card[] Cards;
	protected short InStack;
}