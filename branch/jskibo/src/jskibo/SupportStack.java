package jskibo;

public class SupportStack extends PlayerStack {

        public Card takeCard() {
            try {
            return Cards.pop();    
            } catch (Exception e) {
                System.out.println("you cannot take a card from an empty Stack");
            }
        return null;
        }        
}
