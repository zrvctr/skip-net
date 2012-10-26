package jskibo;

public class DropStack extends ViewableStack {
    
     private boolean canBeDropped(Card card) {  
            return (getCardsLeft()==12 || card.getValue() == viewCard().getValue()+1  
            || card.getValue() == 13);
        }
    
   protected boolean dropCard(Card card) {
        if(canBeDropped(card)) {
            Cards.add(card);
            inStack++;
            return true;
        } else {
            return false;
        }
    }
}
