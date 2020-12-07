package ga;

import problems.Problem;

import java.util.ArrayList;
import java.util.List;

public class Population {

	private Problem problem;
	private List<Individual> individuals;

	public Population(Problem problem, int size, boolean createNew) {
		this.problem = problem;
		individuals = new ArrayList<>();
		if (createNew) {
			createNewPopulation(size);
		}
	}

	protected Individual getIndividual(int index) {
		return individuals.get(index);
	}

	protected Individual getFittest() {
		Individual fittest = individuals.get(0);
		for (int i = 0; i < individuals.size(); i++) {
			if (fittest.getFitness() < getIndividual(i).getFitness()) {
				fittest = getIndividual(i);
			}
		}
		return fittest;
	}

	private void createNewPopulation(int size) {
		for (int i = 0; i < size; i++) {
			Individual newIndividual = new Individual(this.problem);
			individuals.add(i, newIndividual);
		}
	}

	public List<Individual> getIndividuals() {
		return individuals;
	}

	public void printIndividuals(){
		for (Individual individual : individuals) {
			System.out.println(individual.getFitness());
		}
		System.out.println("----------------------------------------------------");
	}
}
