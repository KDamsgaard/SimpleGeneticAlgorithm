package ga;

import problems.Problem;

public class Solution {

    final Problem problem;
    final Individual bestIndividual;
    final long timeElapsed;
    final int bestGeneration;
    final int wastedGenerations;

    public Solution(Problem problem, Individual bestIndividual, long timeElapsed, int bestGeneration, int wastedGenerations) {
        this.problem = problem;
        this.bestIndividual = bestIndividual;
        this.timeElapsed = timeElapsed;
        this.bestGeneration = bestGeneration;
        this.wastedGenerations = wastedGenerations;
    }

    public Individual getBestIndividual() {
        return bestIndividual;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public int getBestGeneration() {
        return bestGeneration;
    }

    public int getWastedGenerations() {
        return wastedGenerations;
    }
}
