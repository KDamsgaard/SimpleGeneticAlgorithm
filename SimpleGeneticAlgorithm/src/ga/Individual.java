package ga;

import problems.Problem;

import java.util.ArrayList;

public class Individual implements Comparable<Individual>{

	protected int defaultGeneLength;
	private ArrayList<Double> genes = new ArrayList<>();
	private Problem problem;
	private double fitness = Double.MIN_VALUE;

	public Individual(Problem problem) {
		this.problem = problem;
		this.defaultGeneLength = problem.getDimensions();

		//Sets the initial genes pseudo-randomly within the bounds of the problem's number of dimensions and min/max values
		for(int dim = 0; dim < problem.getDimensions(); dim++){
			genes.add(problem.getMinValues().get(dim) + Math.random() *
					(problem.getMaxValues().get(dim) - problem.getMinValues().get(dim)));
		}
	}

	public ArrayList<Double> getGenes() {
		return genes;
	}

	protected double getSingleGene(int index) {
		return genes.get(index);
	}

	protected void setSingleGene(int index, double value) {
		genes.set(index, value);
		fitness = Double.MIN_VALUE;
	}

	public double getFitness() {
		if (fitness == Double.MIN_VALUE) {
			fitness = SimpleGeneticAlgorithm.getFitness(this, this.problem);
			problem.evaluatedOnce();
		}
		return fitness;
	}

	@Override
	public int compareTo(Individual o) {
		return Double.compare(o.getFitness(), this.getFitness());
	}

	@Override
	public String toString() {
		String geneString = "Fitness: " + this.getFitness() + "[";

		for (double gene : this.genes) {
			if (geneString == "[") {
				geneString += gene;
			} else {
				geneString += ", " + gene;
			}
		}
		geneString += "]";
		return geneString;
	}

	public int getDefaultGeneLength() {
		return defaultGeneLength;
	}
}