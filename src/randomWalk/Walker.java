package randomWalk;

import java.util.Random;

/**
 * Created by dario on 28/12/14.
 */
public class Walker {

    private final Random rand;
    private final Path path;

    public Walker(Random random) {

        rand = random;
        path = new Path();
    }

    public void makeStep() {

        Direction direction = takeRandomDirection();
        addDirectionToPath(direction);
    }

    public Direction takeRandomDirection() {

        Direction[] directions = Direction.values();
        int min = 0;
        int max = directions.length-1;
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return directions[randomNum];
    }

    public void addDirectionToPath(Direction direction) {
        this.path.addStep(direction);
    }

    public double getDistanceWalked() {
        return this.path.calculateDistance();
    }

    @Override
    public String toString() {
        return "Walker{" +
                 path +
                '}';
    }
}
