package jskibo;

public class SupportStack extends DropStack {

        public Card takeCard() {
            try {
            return Cards.get(inStack--);    
            } catch (Exception e) {
                System.out.println("you cannot take a card from an empty Stack");
            }
        return null;
        }        
}
