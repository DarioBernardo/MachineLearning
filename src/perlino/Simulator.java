package perlino;


import java.util.List;

/**
 * Created by dario on 27/12/14.
 */
public class Simulator {

    private static final int INITIAL_NUMBER_OF_CARDS_TO_REMOVE = 10;
    private static final int NUMBER_OF_RUNS = 100000;
    private static final int NUMBER_OF_CARDS_TO_SUM = 2;

    public static void main(String[] args) {

        ValueCounter counter = new ValueCounter();

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            runSimulation(counter);
        }

        System.out.println(counter);

    }

    private static void runSimulation(ValueCounter counter) {
        Deck deck = Deck.create();
        deck.shuffle();

        deck.getNRandomCardsAndRemove(INITIAL_NUMBER_OF_CARDS_TO_REMOVE);

        List<Card> cards = deck.getNRandomCardsAndRemove(NUMBER_OF_CARDS_TO_SUM);
        counter.addCardsValues(cards);
    }
}
