package Sudoku;

import java.util.*;

//each board knows its penalty, the initial state before filling, and the fitness.
// These are calculated in the constructor. calcPenalty is the only thing (so far)
// that doesn't work due to the way it's setup.
public class Chromosome {
    int penalty;
    double fitness;
    //sBoard is formatted as a 2D array, where each item is an array of the rows.
    int[][] sBoard = {
            { 8, 3, 9, 0, 0, 6, 0, 0, 0 }, 
            { 0, 6, 2, 0, 0, 3, 0, 9, 1 }, 
            { 0, 1, 0, 2, 0, 8, 0, 5, 0 },
            { 9, 0, 0, 0, 7, 4, 0, 6, 3 }, 
            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
            { 4, 5, 0, 6, 8, 0, 0, 0, 2 },
            { 0, 4, 0, 8, 0, 2, 0, 7, 0 }, 
            { 6, 9, 0, 4, 0, 0, 1, 2, 0 }, 
            { 0, 0, 0, 9, 0, 0, 4, 3, 5 }, };
    int[][] rowBoard;

    //Constructor. Sets
    public Chromosome() {
        sBoard = fill(sBoard);
        penalty = calcPenalty(sBoard);
        fitness = 1 / penalty;
    }

    public Chromosome(int[] b1, int[] b2, int[] b3, int[] b4, int[] b5, int[] b6, int[] b7, int[] b8, int[] b9) {
        int[][] board = { b1, b2, b3, b4, b5, b6, b7, b8, b9 };
        sBoard = board;
        penalty = calcPenalty(sBoard);
        fitness = 1 / penalty;
    }

   

    public int[] getBlock(int block) {
        return this.sBoard[block];
    }

    public int getPenalty() {
        return this.penalty;
    }

    public double getFitness() {
        return this.fitness;
    }

    public int[][] getBoard() {
        return sBoard;
    }

    public void mutate() {
        for (int i = 0; i < 9; i++) {
            int index1 = (int) (Math.random() * 10);
            int index2;
            while (true) {
                index2 = (int) (Math.random() * 10);
                if (index2 != index1)
                    break;
            }
            int num1 = sBoard[i][index1];
            int num2 = sBoard[i][index2];

            sBoard[i][index1] = num2;
            sBoard[i][index2] = num1;
        }
    }

    private int calcPenalty(int[][] board) {
        int pen = 0;
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 8; j++) {
                for (int L = j + 1; L <= 9; L++)
                    if (board[(i+2)][j] == board[i][L])
                        pen++;
            }

        }

        return pen;
    }

    private int[][] fill(int[][] board) {

        for (int i = 0; i < 9; i++) {
            ArrayList<Integer> arr = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    int numInThatSpot = board[i][j];
                    arr.set((numInThatSpot - 1), 0);
                    System.out.println(numInThatSpot);
                }
            }
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    int index = (int) (Math.random() * arr.size());
                    while (index == 0)
                        index = (int) (Math.random() * arr.size());
                    board[i][j] = arr.get(index);
                    arr.set(index, 0);
                }
            }
        }
        return board;
    }

    public String toString(){
        return "";
    }
}
