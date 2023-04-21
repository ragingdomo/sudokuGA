package Sudoku;

import java.util.*;

// Algoritm adapted from the paper. all methods are handled the same as the
// paper handled them. I don't know if any of these work because they all
// rely on the Chromosome class which still isn't ready. 
public class SudokuGA {
    public static void main(String args[]) {
        int generationSize = 700;
        int maxIterations = 3000;
        int currentIterations = 0;
        int purgeValue = 200;
        ArrayList<Chromosome> population = generatePopulation(generationSize);
        ArrayList<Chromosome> generation = population;
        
        long startTime = System.currentTimeMillis();

        while (currentIterations < maxIterations) {
            ArrayList<Chromosome> newG = new ArrayList<Chromosome>();
            Chromosome best = getBest(generation);
            if (best.getPenalty() == 0){
                long stopTime = System.currentTimeMillis();
                System.out.println("Perfect board found!!! And it only took "+((int)((stopTime-startTime)/1000))+" seconds.");
                System.out.println(best.toString());
                break;
            }
            //fill the new generation
            while (newG.size() < generation.size()) {
                
                Chromosome parent1 = kTournament(generation);
                generation.remove(parent1);
                Chromosome parent2 = kTournament(generation);
                generation.remove(parent2);
                Chromosome child = crossover(parent1, parent2);
                child.mutate(0.05);
                parent1.mutate(0.5);
                parent2.mutate(0.5);
                Chromosome bestParent = bestOf(parent1, parent2);
                newG.add(bestParent);
                newG.add(child);
            }
            newG.add(best);
            generation = newG;
            currentIterations++;
            if (currentIterations%purgeValue==0 && currentIterations!=0){
                generation = purge(generation, best);
                best.mutate(1);
                System.out.println("Purging...best penalty: "+best.getPenalty());

            }
        }
        long stopTime = System.currentTimeMillis();
        System.out.println("After 3000 generations and "+((int)((stopTime-startTime)/1000))+" seconds, the algorthm has yeilded no perfect boards. :(");
    }

    public static Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        ArrayList<Integer[]> arr = new ArrayList<Integer[]>();
        for (int block = 0; block < 9; block++) {
            int randomNumber = (int) (Math.random() * 2);
            if (randomNumber == 1)
                arr.add(parent1.getBlock(block));
             else
                arr.add(parent2.getBlock(block));
        }

        Integer[] b1 = arr.get(0);
        Integer[] b2 = arr.get(1);
        Integer[] b3 = arr.get(2);
        Integer[] b4 = arr.get(3);
        Integer[] b5 = arr.get(4);
        Integer[] b6 = arr.get(5);
        Integer[] b7 = arr.get(6);
        Integer[] b8 = arr.get(7);
        Integer[] b9 = arr.get(8);
        Chromosome c = new Chromosome(b1, b2, b3, b4, b5, b6, b7, b8, b9);
        return c;
    }

    public static ArrayList<Chromosome> generatePopulation(int size) {
        ArrayList<Chromosome> populationArray = new ArrayList<Chromosome>();
        for (int iteration = 0; iteration < size; iteration++) {
            Chromosome c = new Chromosome();
            populationArray.add(c);
        }
        return populationArray;
    }

    public static Chromosome getBest(ArrayList<Chromosome> dummyGeneration) {
        Iterator<Chromosome> iter = dummyGeneration.iterator();
        int lowestPenalty = 0;
        Chromosome bestChromosome = new Chromosome();

        while (iter.hasNext()) {
            Chromosome c = iter.next();
            int penalty = c.getPenalty();
            if (penalty < lowestPenalty) {
                bestChromosome = c;
                lowestPenalty = penalty;
            }
        }
        return bestChromosome;
    }

    public static Chromosome bestOf(Chromosome parent1, Chromosome parent2) {
        if (parent1.getPenalty() < parent2.getPenalty())
            return parent1;
        else
            return parent2;
    }

    //Grab 7 chromosomes at random and return the best (fitness) of those 7
    public static Chromosome kTournament(ArrayList<Chromosome> dummyGeneration) {
        int k = 7;
        ArrayList<Chromosome> arr = new ArrayList<Chromosome>();
        for (int i = 0; i < k; i++) {
            int ranIndex = (int)(Math.random()*dummyGeneration.size());
            Chromosome copy = dummyGeneration.get(ranIndex);
            arr.add(copy);
        }
        return getBest(arr);
    }

    //every 200 iterations, we perform crossover with the current best and everyone in the generation.
    public static ArrayList<Chromosome> purge(ArrayList<Chromosome> dummyGeneration, Chromosome best){
        ArrayList<Chromosome> postPurgeGeneration = new ArrayList<Chromosome>();
        Iterator<Chromosome> iter = dummyGeneration.iterator();
        while (iter.hasNext()){
            Chromosome c = iter.next();
             postPurgeGeneration.add(crossover(best, c));
        }
        return postPurgeGeneration;
    }
}
