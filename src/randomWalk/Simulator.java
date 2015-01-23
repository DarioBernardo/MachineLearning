package randomWalk;

import java.util.*;

/**
 * Created by dario on 29/12/14.
 */
public class Simulator {

    private static final int NUMBER_OF_RUNS = 10_000;
    private static final int NUMBER_OF_STEPS_TO_WALK = 1_000;

    /*public static void main(String[] args) {

        ArrayList<Double> allDistanceWalked = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            double distanceWalked = runSimulation(NUMBER_OF_STEPS_TO_WALK);
            allDistanceWalked.add(distanceWalked);
        }

        double averageAnAllDistanceWalked = computeAverage(allDistanceWalked);

        System.out.println(averageAnAllDistanceWalked);
    }*/

    public static void main(String[] args) {

        Random random = new Random();
        HashMap<Integer, Double> allRatios = new HashMap<>();
        int initialStep = 100;
        for (int tempStepToWalk = initialStep; tempStepToWalk < NUMBER_OF_STEPS_TO_WALK; tempStepToWalk++) {
            ArrayList<Double> allDistanceWalked = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_RUNS; i++) {

                double distanceWalked = runSimulation(tempStepToWalk, random);
                allDistanceWalked.add(distanceWalked);

            }

            double averageAnAllDistanceWalked = computeAverage(allDistanceWalked);
            double ratio = (averageAnAllDistanceWalked*100.0)/(1.0*tempStepToWalk);
            allRatios.put(tempStepToWalk, ratio);

            if (tempStepToWalk % (NUMBER_OF_STEPS_TO_WALK / 10) == 0 /*|| cnt % 1_000_000L == 0*/) {
                double progress = tempStepToWalk * 100.0 / (1.0 * NUMBER_OF_STEPS_TO_WALK);
                System.out.println("Step calculated:  "+tempStepToWalk+"("+Math.round(progress)+"%)");
            }
        }

        StringBuilder builder = new StringBuilder("All ratio between distance walked and distance\n");
        StringBuilder temp = new StringBuilder();
        Set<Integer> keySet = allRatios.keySet();
        ArrayList<Integer> orderedKeys = new ArrayList<>(keySet);
        Collections.sort(orderedKeys);
        for (Integer tempStepToWalk : orderedKeys) {
            builder.append(tempStepToWalk).append("\t");
            temp.append(allRatios.get(tempStepToWalk)).append("\t");
        }
        builder.append("\n");
        builder.append(temp).append("\n");

        System.out.println(builder.toString());
    }

    public static double computeAverage(ArrayList<Double> allDistanceWalked) {
        double accumulator = 0;
        for (Double distanceWalked : allDistanceWalked) {
            accumulator += distanceWalked;
        }

        return accumulator / allDistanceWalked.size();
    }

    private static double runSimulation(int numberOfStepsToWalk, Random random) {

        Walker walker = new Walker(random);
        for (int i = 0; i < numberOfStepsToWalk; i++) {
            walker.makeStep();
        }

        //System.out.println(walker);

        return walker.getDistanceWalked();
    }

}
