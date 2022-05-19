package Sudoku;

import java.util.*;

//each board knows its penalty, the initial state before filling, and the fitness.
// These are calculated in the constructor. calcPenalty is the only thing (so far)
// that doesn't work due to the way it's setup.
public class Chromosome {
    int penalty;
    double fitness;
    int[][] sBoard = {
            { 8, 3, 9, 0, 6, 2, 0, 1, 0 }, { 0, 0, 6, 0, 0, 3, 2, 0, 8 }, { 0, 0, 0, 0, 9, 1, 0, 5, 0 },
            { 9, 0, 0, 0, 0, 0, 4, 5, 0 }, { 0, 7, 4, 0, 0, 0, 6, 8, 0 }, { 0, 6, 3, 0, 0, 0, 0, 0, 2 },
            { 0, 4, 0, 6, 9, 0, 0, 0, 0 }, { 8, 0, 2, 4, 0, 0, 9, 0, 0 }, { 0, 7, 0, 1, 2, 0, 4, 3, 5 }, };
    int[][] rowBoard;

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

    public int[][] makeRowBoard(int[][] board) {
        ArrayList<Integer> row1 = new ArrayList<Integer>();
        row1.add(board[1][1]);
        row1.add(board[1][2]);
        row1.add(board[1][3]);
        row1.add(board[2][1]);
        row1.add(board[2][2]);
        row1.add(board[2][3]);
        row1.add(board[3][1]);
        row1.add(board[3][2]);
        row1.add(board[3][3]);

        ArrayList<Integer> row2 = new ArrayList<Integer>();
        row2.add(board[1][4]);
        row2.add(board[1][5]);
        row2.add(board[1][6]);
        row2.add(board[2][4]);
        row2.add(board[2][5]);
        row2.add(board[2][6]);
        row2.add(board[3][4]);
        row2.add(board[3][5]);
        row2.add(board[3][6]);

        ArrayList<Integer> row3 = new ArrayList<Integer>();
        row3.add(board[1][7]);
        row3.add(board[1][8]);
        row3.add(board[1][9]);
        row3.add(board[2][7]);
        row3.add(board[2][8]);
        row3.add(board[2][9]);
        row3.add(board[3][7]);
        row3.add(board[3][8]);
        row3.add(board[3][9]);

        ArrayList<Integer> row4 = new ArrayList<Integer>();
        row4.add(board[4][1]);
        row4.add(board[4][2]);
        row4.add(board[4][3]);
        row4.add(board[5][1]);
        row4.add(board[5][2]);
        row4.add(board[5][3]);
        row4.add(board[6][1]);
        row4.add(board[6][2]);
        row4.add(board[6][3]);

        ArrayList<Integer> row5 = new ArrayList<Integer>();
        row5.add(board[4][4]);
        row5.add(board[4][5]);
        row5.add(board[4][6]);
        row5.add(board[5][4]);
        row5.add(board[5][5]);
        row5.add(board[5][6]);
        row5.add(board[6][4]);
        row5.add(board[6][5]);
        row5.add(board[6][6]);

        ArrayList<Integer> row6 = new ArrayList<Integer>();
        row6.add(board[4][7]);
        row6.add(board[4][8]);
        row6.add(board[4][9]);
        row6.add(board[5][7]);
        row6.add(board[5][8]);
        row6.add(board[5][9]);
        row6.add(board[6][7]);
        row6.add(board[6][8]);
        row6.add(board[6][9]);

        ArrayList<Integer> row7 = new ArrayList<Integer>();
        row7.add(board[7][1]);
        row7.add(board[7][2]);
        row7.add(board[7][3]);
        row7.add(board[8][1]);
        row7.add(board[8][2]);
        row7.add(board[8][3]);
        row7.add(board[9][1]);
        row7.add(board[9][2]);
        row7.add(board[9][3]);

        ArrayList<Integer> row8 = new ArrayList<Integer>();
        row8.add(board[7][4]);
        row8.add(board[7][5]);
        row8.add(board[7][6]);
        row8.add(board[8][4]);
        row8.add(board[8][5]);
        row8.add(board[8][6]);
        row8.add(board[9][4]);
        row8.add(board[9][5]);
        row8.add(board[9][6]);

        ArrayList<Integer> row9 = new ArrayList<Integer>();
        row9.add(board[7][7]);
        row9.add(board[7][8]);
        row9.add(board[7][9]);
        row9.add(board[8][7]);
        row9.add(board[8][8]);
        row9.add(board[8][9]);
        row9.add(board[9][7]);
        row9.add(board[9][8]);
        row9.add(board[9][9]);

        int[] r1 = row.toArray();
        int[] r2 = row.toArray();
        int[] r3 = row.toArray();
        int[] r4 = row.toArray();
        int[] r5 = row.toArray();
        int[] r6 = row.toArray();
        int[] r7 = row.toArray();
        int[] r8 = row.toArray();
        int[] r9 = row.toArray();

        return rowBoard;
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
                    if (board[((i+2)][j] == board[i][L])
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
        return ""
    }
}
