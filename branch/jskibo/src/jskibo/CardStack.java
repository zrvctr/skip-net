package jskibo;

import java.util.Stack;


public class CardStack {
         protected Stack<Card> Cards = new Stack<Card>();
         
      
        public int getCardsLeft () {
            return Cards.size();
        }
}