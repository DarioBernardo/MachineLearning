package montecarlo.perlino;

import java.util.*;

/**
 * Created by dario on 26/12/14.
 */
public class Deck {

    private final Random rand;
    private final List<Card> deck;

    public Deck(List<Card> deck) {
        this.deck = deck;
        rand = new Random();
    }
    
    public void shuffle() {
        Collections.shuffle(deck, rand);
    }

    public static Deck create() {

        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 1; i <= 7; i++) {
            cards.add(new Card("Spade", i));
        }
        cards.add(new Card("Spade", 10));
        cards.add(new Card("Spade", 10));
        cards.add(new Card("Spade", 10));

        for (int i = 1; i <= 7; i++) {
            cards.add(new Card("Coppe", i));
        }
        cards.add(new Card("Coppe", 10));
        cards.add(new Card("Coppe", 10));
        cards.add(new Card("Coppe", 10));

        for (int i = 1; i <= 7; i++) {
            cards.add(new Card("Bastoni", i));
        }
        cards.add(new Card("Bastoni", 10));
        cards.add(new Card("Bastoni", 10));
        cards.add(new Card("Bastoni", 10));

        for (int i = 1; i <= 7; i++) {
            cards.add(new Card("Denari", i));
        }
        cards.add(new Card("Denari", 10));
        cards.add(new Card("Denari", 10));
        cards.add(new Card("Denari", 10));

        return new Deck(cards);
    }

    public Card getRandomCardAndRemove() {

        if (deck.size() == 0 )
            throw new RuntimeException("The Deck is Empty! cannot pull another card from it!");

        if ( deck.size() == 1) {
            return deck.get(0);
        }
        
        int min = 0;
        int max = deck.size()-1;
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return deck.remove(randomNum);
    }
    
    public List<Card> getNRandomCardsAndRemove(int n) {
        
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cards.add(getRandomCardAndRemove());
        }

        return cards;
    }
}
