package ga;

import problems.*;

import java.util.ArrayList;

public class MainApp {

	public static void main(String[] args) {
		ArrayList<Solution> solutions = new ArrayList<>();
		// Set iterations to run the algorithm repeatedly, if set higher than 1 the algorithm will not supply individual
		// readouts, but the .
		int iterations = 1; // Must be 1 or higher

		final int generations = 1; //Default = 100
		final int populationSize = 15000; //Default = 100
		final int tournamentSize = 1; //Default = 5, must be 1 or higher
		final int elites = 0; //Default = 5, must not equal or exceed populationSize
		final double crossoverRate = 0.5; //Default = 0.5
		final double mutationRate = 0.025; //Default = 0.025

		Problem problem = new P1();
//		Problem problem = new P2();
//		Problem problem = new RevAckley();
//		Problem problem = new RevSphere();
//		Problem problem = new RevRosenbrock();

		SimpleGeneticAlgorithm ga = new SimpleGeneticAlgorithm(problem, generations, crossoverRate, mutationRate,
				tournamentSize, elites, populationSize);

		// Compute "iteration" solution(s)
		for (int i = 0; i < iterations; i++ ) {
			solutions.add(ga.runAlgorithm(iterations));
		}

		// Only calculate and print averages if more than 1 iteration is run
		if (iterations > 1) {
			long totalEvaluations = 0;
			double averageEvaluation = 0;
			long averageTimeElapsed = 0;
			int averageBestGeneration = 0;
			int averageWastedGenerations = 0;

			for (Solution solution : solutions) {
				System.out.println(solution.bestIndividual.getGenes());
				totalEvaluations += solution.problem.getAmountEvaluations();
				averageEvaluation += solution.getBestIndividual().getFitness();
				averageTimeElapsed += solution.getTimeElapsed();
				averageBestGeneration += solution.getBestGeneration();
				averageWastedGenerations += solution.getWastedGenerations();
			}

			averageEvaluation = averageEvaluation / solutions.size();
			averageTimeElapsed = averageTimeElapsed / solutions.size();
			averageBestGeneration = averageBestGeneration / solutions.size();
			averageWastedGenerations = averageWastedGenerations / solutions.size();

			System.out.println("\nAverage across " + solutions.size() + " iterations: " + averageEvaluation);
			System.out.println("Average time elapsed: " + averageTimeElapsed + "ms");
			System.out.println("Average best generation: " + averageBestGeneration);
			System.out.println("Average wasted generations: " + averageWastedGenerations);
			System.out.println("Total evaluations performed: " + totalEvaluations);
		}
	}
}
