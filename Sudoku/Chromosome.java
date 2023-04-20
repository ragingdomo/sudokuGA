package Sudoku;

import java.util.*;

//each board knows its penalty, the initial state before filling, and the fitness.
// These are calculated in the constructor. calcPenalty is the only thing (so far)
// that doesn't work due to the way it's setup.
public class Chromosome {
    int penalty;
    double fitness;
    // sBoard is formatted as a 2D array, where each item is an array of the BLOCKS.
    Integer[][] sBoard = {
            { 8, 3, 9, 0, 6, 2, 0, 1, 0 }, { 0, 0, 6, 0, 0, 3, 2, 0, 8 }, { 0, 0, 0, 0, 9, 1, 0, 5, 0 },
            { 9, 0, 0, 0, 0, 0, 4, 5, 0 }, { 0, 7, 4, 0, 0, 0, 6, 8, 0 }, { 0, 6, 3, 0, 0, 0, 0, 0, 2 },
            { 0, 4, 0, 6, 9, 0, 0, 0, 0 }, { 8, 0, 2, 4, 0, 0, 9, 0, 0 }, { 0, 7, 0, 1, 2, 0, 4, 3, 5 }, };

    // Constructor. Sets
    public Chromosome() {
        sBoard = fill(sBoard);
        penalty = calcPenalty(sBoard);
        fitness = 1 / penalty;
    }

    public Chromosome(Integer[] b1, Integer[] b2, Integer[] b3, Integer[] b4, Integer[] b5, Integer[] b6, Integer[] b7,
            Integer[] b8, Integer[] b9) {
        Integer[][] board = { b1, b2, b3, b4, b5, b6, b7, b8, b9 };
        sBoard = board;
        penalty = calcPenalty(sBoard);
        fitness = 1 / penalty;
    }

    public Integer[][] makeRowBoard(Integer[][] board) {
        Integer[][] rowBoard = new Integer[9][9];
        //
        // Iterates over the row of the sudoku board, the rows within the sudoku board
        // then the blocks within, then the cells.
        for (int section = 0; section < 3; section++) {
            for (int modifier = 0; modifier < 3; modifier++) {
                ArrayList<Integer> temporaryRow = new ArrayList<Integer>();
                for (int block = 0; block < 3; block++) {
                    for (int cell = 0; cell < 3; cell++) {
                        temporaryRow.add(sBoard[(3 * section)][cell + (3 * modifier)]);
                    }
                }
                Integer[] superTemporaryArray = new Integer[temporaryRow.size()];
                rowBoard[modifier + (3 * section)] = temporaryRow.toArray(superTemporaryArray);
            }
        }
        return rowBoard;
    }

    public Integer[] getBlock(int block) {
        return this.sBoard[block];
    }

    public int getPenalty() {
        return this.penalty;
    }

    public double getFitness() {
        return this.fitness;
    }

    public Integer[][] getBoard() {
        return sBoard;
    }

    // Pick two random cells, call them cell1 and cell2.
    // Make sure they aren't equal, then swap the contents of the cells.
    public void mutate() {
        for (int block = 0; block < 9; block++) {
            int cell1 = (int) (Math.random() * 10);
            int cell2;
            while (true) {
                cell2 = (int) (Math.random() * 10);
                if (cell2 != cell1)
                    break;
            }
            int num1 = sBoard[block][cell1];
            int num2 = sBoard[block][cell2];

            sBoard[block][cell1] = num2;
            sBoard[block][cell2] = num1;
        }
    }

    private int calcPenalty(Integer[][] board) {
        int pen = 0;
        // First, parse the rows
        Integer[][] tempRowBoard = makeRowBoard(board);

        for (int row = 0; row < 9; row++) {
            for (int pass = 0; pass < 9; pass++) {
                for (int cell = 0; cell < 9; cell++) {
                    if (!(cell == pass)) {
                        if (tempRowBoard[row][pass] == tempRowBoard[row][cell]) {
                            pen++;
                        }
                    }
                }
            }
        }

        return pen;

    }

    private Integer[][] fill(Integer[][] board) {

        for (int i = 0; i < 9; i++) {
            ArrayList<Integer> arr = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    int numInThatSpot = board[i][j];
                    arr.set((numInThatSpot - 1), 0);
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

    // TODO: print out the sudoko board all nice like
    public String toString() {
        //
        return "";
    }
}
