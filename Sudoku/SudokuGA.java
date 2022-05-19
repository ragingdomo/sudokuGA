package Sudoku;

import java.util.*;

// Algoritm adapted from the paper. all methods are handled the same as the
// paper handled them. I don't know if any of these work because they all
// rely on the Chromosome class which still isn't ready. 
public class SudokuGA {
    public static void main(String args[]) {
        ArrayList<Chromosome> population = generatePopulation(50);
        ArrayList<Chromosome> generation = population;
        int maxIterations = 500;
        int currentIterations = 0;

        while (currentIterations < maxIterations) {
            ArrayList<Chromosome> newG = new ArrayList<Chromosome>();
            Chromosome best = getBest(generation);
            if (best.getPenalty() == 0)
                break;
            while (newG.size() < generation.size()) {
                Chromosome parent1 = kTournament(generation);
                generation.remove(parent1);
                Chromosome parent2 = kTournament(generation);
                Chromosome child = crossover(parent1, parent2);
                child.mutate();
                parent1.mutate();
                parent2.mutate();
                Chromosome bestParent = bestOf(parent1, parent2);
                newG.add(child);
                newG.add(bestParent);
            }
            newG.add(best);
            currentIterations++;
            generation = newG;
        }
        System.out.println(currentIterations);
    }

    public static Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        ArrayList<int[]> arr = new ArrayList<int[]>();
        for (int i = 0; i < 9; i++) {
            int num = (int) (Math.random() * 2);
            if (num == 1)
                arr.add(parent1.getBlock(i + 1));
            else
                arr.add(parent2.getBlock(i + 1));
        }
        int[] b1 = arr.get(0);
        int[] b2 = arr.get(1);
        int[] b3 = arr.get(2);
        int[] b4 = arr.get(3);
        int[] b5 = arr.get(4);
        int[] b6 = arr.get(5);
        int[] b7 = arr.get(6);
        int[] b8 = arr.get(7);
        int[] b9 = arr.get(8);
        Chromosome c = new Chromosome(b1, b2, b3, b4, b5, b6, b7, b8, b9);
        return c;
    }

    public static ArrayList<Chromosome> generatePopulation(int size) {
        ArrayList<Chromosome> arr = new ArrayList<Chromosome>();
        for (int i = 0; i < size; i++) {
            Chromosome c = new Chromosome();
            arr.add(c);
        }
        return arr;
    }

    public static Chromosome getBest(ArrayList<Chromosome> generation) {
        Iterator<Chromosome> iter = generation.iterator();
        double bestFitness = 0;
        Chromosome best = new Chromosome();

        while (iter.hasNext()) {
            Chromosome c = iter.next();
            double fitness = c.getFitness();
            if (fitness > bestFitness) {
                best = c;
                bestFitness = fitness;
            }
        }
        return best;
    }

    public static Chromosome bestOf(Chromosome parent1, Chromosome parent2) {
        if (parent1.getFitness() > parent2.getFitness())
            return parent1;
        else
            return parent2;
    }

    public static Chromosome kTournament(ArrayList<Chromosome> generation) {
        int k = 7;
        ArrayList<Chromosome> arr = new ArrayList<Chromosome>();
        Random r = new Random();
        for (int i = 0; i < k; i++) {
            int ranIndex = r.nextInt();
            Chromosome copy = generation.get(ranIndex);
            arr.add(copy);
        }
        return getBest(arr);
    }
}
