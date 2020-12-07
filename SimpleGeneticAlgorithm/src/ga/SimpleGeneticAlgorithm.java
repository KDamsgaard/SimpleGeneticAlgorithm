package ga;

import problems.Problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimpleGeneticAlgorithm {

	private final Stopwatch sw = new Stopwatch();
	private final Problem problem;
	private final int generations;
	private final double crossoverRate;
	private final double mutationRate;
	private final int tournamentSize;
	private final int elites;
	private final int populationSize;

	public SimpleGeneticAlgorithm(Problem problem, int generations, double crossoverRate, double mutationRate,
								  int tournamentSize, int elites, int populationSize) {
		this.problem = problem;
		this.generations = generations;
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
		this.tournamentSize = tournamentSize;
		this.elites = elites;
		this.populationSize = populationSize;
	}

	public Solution runAlgorithm(int iterations) {
		Population population = new Population(problem, populationSize, true);
		int generationCount = 0;
		int bestGeneration = 0;
		Individual fittest = population.getFittest();

		// Sort population according to evaluated value (method for which is contained in each problem)
		sortIndividuals(population);
		if (iterations == 1) {
			System.out.println("\nStarting point	---------------------------------------------------------------------");
			System.out.println("Generation: " + generationCount + ",\tfitness: " + population.getFittest().getFitness() +
					", genes: " + population.getFittest().getGenes());
			System.out.println("\nEvolution	-------------------------------------------------------------------------");
		}
		// Start the stopwatch
		sw.start();
		while (generationCount < generations) {
			population = evolvePopulation(population);
			// Sort population according to evaluated value (method for which is contained in each problem)
			sortIndividuals(population);
			generationCount++;
			// Print result if better than previous best
			if (population.getFittest().getFitness() > fittest.getFitness()) {
				if (iterations == 1) {
					System.out.println("Generation: " + generationCount + ",\tfittest: " +
							population.getFittest().getFitness() + ", genes: " + population.getFittest().getGenes());
				}
				bestGeneration = generationCount;
				fittest = population.getFittest();
			}
		}
		// Stop the stopwatch
		sw.stop();

		if (iterations == 1) {
			System.out.println("\nBest generation: " + bestGeneration + ", fitness: " + population.getFittest().getFitness() +
					", genes: " + population.getFittest().getGenes());
			System.out.println("Total evaluations: " + problem.getAmountEvaluations());
			System.out.println("Time elapsed: " + sw.getTimeElapsed() + "ms");
			System.out.println("Total generations: " + generationCount);
			System.out.println("Wasted generations: " + (generationCount - bestGeneration));
		}
		return new Solution(problem, fittest, sw.getTimeElapsed(), bestGeneration, (generationCount-bestGeneration));
	}

	public Population evolvePopulation(Population population) {
		Population newPopulation = new Population(problem, population.getIndividuals().size(), false);
		// Copy elites from old population to new one (note an "elite" setting of 0 ignores this loop)
		for (int i = 0; i < elites; i++) {
			newPopulation.getIndividuals().add(i, population.getIndividual(i));
		}
		// Fill new population with new children
		for (int i = elites; i < population.getIndividuals().size(); i++) {
			Individual parent1 = tournamentSelection(population);
			Individual parent2 = tournamentSelection(population);
			Individual newChild = crossover(parent1, parent2);
			newPopulation.getIndividuals().add(i, newChild);
		}
		// Mutate entire population
		for (int i = elites; i < newPopulation.getIndividuals().size(); i++) {
			mutate(newPopulation.getIndividual(i));
		}
		return newPopulation;
	}

	private Individual crossover(Individual parent1, Individual parent2) {
		int geneLength = parent1.getDefaultGeneLength();
		Individual child = new Individual(problem);
		// Decide (by crossover rate) if the new child should be a "mix" of parent DNA
		if (Math.random() <= crossoverRate) {
			for (int i = 0; i < geneLength; i++) {
				double avg = (parent1.getSingleGene(i) + parent2.getSingleGene(i)) / geneLength;
				child.setSingleGene(i, avg);
			}
		} else {
			for (int i = 0; i < geneLength; i++) {
				if (Math.random() <= 0.5) {
					child.setSingleGene(i, parent1.getSingleGene(i));
				} else {
					child.setSingleGene(i, parent2.getSingleGene(i));
				}
			}
		}
		// Check if child values violate problem min/max values and set to min/max if they do
		checkBoundaries(child);
		return child;
	}

	private void mutate(Individual individual) {
		for (int i = 0; i < individual.getDefaultGeneLength(); i++) {
			if (Math.random() <= mutationRate) {
				Random random = new Random();
				individual.setSingleGene(i, 0.1 * random.nextGaussian() + individual.getSingleGene(i));
				// Check if child values violate problem min/max values and set to min/max if they do
				checkBoundaries(individual);
			}
		}
	}

	private Individual tournamentSelection(Population pop) {
		Population tournament = new Population(problem, tournamentSize, false);
		for (int i = 0; i < tournamentSize; i++) {
			int randomId = (int) (Math.random() * pop.getIndividuals().size());
			tournament.getIndividuals().add(i, pop.getIndividual(randomId));
		}
		return tournament.getFittest();
	}

	private void checkBoundaries(Individual individual) {
		for(int i = 0; i < individual.getDefaultGeneLength(); i++){
			if (individual.getSingleGene(i) < problem.getMinValues().get(i)){
				individual.setSingleGene(i, problem.getMinValues().get(i));
			}
			if (individual.getSingleGene(i) > problem.getMaxValues().get(i)){
				individual.setSingleGene(i, problem.getMaxValues().get(i));
			}
		}
	}

	private void sortIndividuals(Population population) {
		Collections.sort(population.getIndividuals());
	}

	protected static double getFitness(Individual individual, Problem problem) {
		return problem.eval(individual.getGenes());
	}
}