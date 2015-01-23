package montecarlo.perlino;

/**
 * Created by dario on 26/12/14.
 */
public class Card {

    private final String palo;
    private final int number;

    public Card(String palo, int number) {
        this.palo = palo;
        this.number = number;
    }

    public String getPalo() {
        return palo;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "{" + number +
                " di '" + palo + '\'' +
                '}';
    }
}
