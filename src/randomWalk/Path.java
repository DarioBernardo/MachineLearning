package randomWalk;

import java.util.ArrayList;

/**
 * Created by dario on 28/12/14.
 */
public class Path {

    final private ArrayList<Direction> path;
    private int horizontalDistance;
    private int verticalDistance;

    public Path() {
        this.path = new ArrayList<>();
        this.horizontalDistance = 0;
        this.verticalDistance = 0;
    }

    public void addStep(Direction step) {

        this.path.add(step);

        if(step.equals(Direction.north)) {
            this.verticalDistance++;
        }
        if(step.equals(Direction.south)) {
            this.verticalDistance--;
        }
        if(step.equals(Direction.east)) {
            this.horizontalDistance++;
        }
        if(step.equals(Direction.west)) {
            this.horizontalDistance--;
        }
    }

    public double calculateDistance() {

        double a = Math.pow(verticalDistance, 2);
        double b = Math.pow(horizontalDistance, 2);
        return Math.sqrt(a + b);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Path walked:\n");
        for (Direction direction : path) {
            b.append(direction).append("\n");
        }
        return b.toString();
    }
}
