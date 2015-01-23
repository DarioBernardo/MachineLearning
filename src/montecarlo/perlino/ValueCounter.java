package montecarlo.perlino;

import java.util.*;

/**
 * Created by dario on 27/12/14.
 */
public class ValueCounter {

    private final HashMap<Integer, Integer> counter;

    public ValueCounter() {
        this.counter = new HashMap<>();
    }



    public void addCardsValues(List<Card> cardList) {
        int accumulator = 0;
        for (Card tempCard : cardList) {
            accumulator += tempCard.getNumber();
        }

        Integer counterForKey = counter.get(accumulator);
        if(counterForKey == null) {
            counterForKey = 1;
        }else {
            counterForKey++;
        }
        counter.put(accumulator, counterForKey);
    }

    public HashMap<Integer, Integer> getMapOfValues() {
        return counter;
    }

    @Override
    public String toString() {

        StringBuilder b = new StringBuilder();
        StringBuilder b2 = new StringBuilder();
        final HashMap<Integer, Integer> mapOfValues = getMapOfValues();

        Set<Integer> keys = mapOfValues.keySet();

        ArrayList<Integer> integerArrayList = new ArrayList<>(keys);
        Collections.sort(integerArrayList);
        int stat = integerArrayList.get(0);
        int end = integerArrayList.get(integerArrayList.size() - 1);

        for (int i = stat; i <= end; i++) {
            Integer integer = mapOfValues.get(i);
            if(integer == null) {
                integer = 0;
            }
            b.append(i).append("\t");
            b2.append(integer).append("\t");
        }

        b.append("\n").append(b2).append("\n");

        return b.toString();
    }
}
