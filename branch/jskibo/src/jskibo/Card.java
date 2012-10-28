package jskibo;

public class Card {
    private int cardValue;

    protected Card(int cardval) {
        if ((cardval > 1) || (cardval < 14)) {
            cardValue = cardval;
        } else {
            System.out.println("You cannot stick an illigal Card in here");
        }
    }

    public int getValue() {
        return cardValue;
    }

    @Override
    public String toString() {
        if (this.getValue() == 13) {
            return ("J");
        } else {
            return new Integer(this.getValue()).toString();

            // crates a new Interger with the Value as argument
            // and Converts this into a String witch in turn is returned
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
