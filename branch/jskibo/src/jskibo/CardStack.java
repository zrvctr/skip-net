package jskibo;

import java.util.ArrayList;


public class CardStack {
         protected ArrayList<Card> Cards = new ArrayList<Card>();
         protected int inStack;

      
        public int getCardsLeft () {
            return this.inStack+1;
        }
}