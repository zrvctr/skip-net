package jskibo;

public class OpenCardStack extends CardStack {
	public Card viewCard() {
		Card card = Cards[InStack];
		return card; 
	}
}
