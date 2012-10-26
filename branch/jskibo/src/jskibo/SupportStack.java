package jskibo;

public class SupportStack extends PlayerStack {

        
      protected boolean dropCard(Card card) {
            Cards.push(card);
            return true;
        }
            
    
}
