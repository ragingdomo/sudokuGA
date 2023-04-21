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
    Integer[][] rowBoard;
    Map<Integer, ArrayList<Integer>> doNotTouchTheseIndexes;

    // Constructor. 
    public Chromosome() {
        doNotTouchTheseIndexes = calcDoNotTouch();
        sBoard = fill(sBoard);
        rowBoard = makeRowBoard(sBoard);
        penalty = calcPenalty();
        fitness = 1 / penalty;
        
    }

    //manual constructor allows you to set each block individually
    public Chromosome(Integer[] b1, Integer[] b2, Integer[] b3, Integer[] b4, Integer[] b5, Integer[] b6, Integer[] b7,
            Integer[] b8, Integer[] b9) {
        Integer[][] board = { b1, b2, b3, b4, b5, b6, b7, b8, b9 };
        sBoard = board;
        rowBoard = makeRowBoard(sBoard);
        penalty = calcPenalty();
        fitness = 1 / penalty;
    }

    
    public Integer[][] getRowBoard() {
        return this.rowBoard;
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
    // Now that your big brain made the no-touchies list, rewrite this
    // to only mutate the items that can be touched. <3
    public void mutate(double mutationChance) {
        if(((int)(Math.random()*100)) < ((int)(mutationChance*100))){
            int pair1_1 = (int)(Math.random()*9);
            int pair1_2 = (int)(Math.random()*9);
            int pair2_1 = (int)(Math.random()*9);
            int pair2_2 = (int)(Math.random()*9);
            while(pair1_1==pair1_2 || pair1_1==pair2_1 || pair1_1==pair2_2 || pair1_2==pair2_1 || pair1_2==pair2_2 || pair2_1==pair2_2){
                pair1_1 = (int)(Math.random()*9);
                pair1_2 = (int)(Math.random()*9);
                pair2_1 = (int)(Math.random()*9);
                pair2_2 = (int)(Math.random()*9);
            }
            for (int block = 0; block < 9; block++) {
                int swapValue1 = sBoard[block][pair1_1];
                int swapValue2 = sBoard[block][pair1_2];
                sBoard[block][pair1_1] = swapValue2;
                sBoard[block][pair1_2] = swapValue1;
    
                swapValue1 = sBoard[block][pair2_1];
                swapValue2 = sBoard[block][pair2_2];
                sBoard[block][pair2_1] = swapValue2;
                sBoard[block][pair2_2] = swapValue1;
            }
            this.rowBoard = makeRowBoard(this.sBoard);
            this.penalty = calcPenalty();
            this.fitness = 1/this.penalty;
        }
    }

    //Turn the block formatted sudoku board into a row formatted board, which the 
    //Chromosome object knows.
    private Integer[][] makeRowBoard(Integer[][] board) {
         Integer[][] rowBoardPlaceholder = new Integer[9][9];
        //
        // Iterates over the row of the sudoku board, the rows within the sudoku board
        // then the blocks within, then the cells.
        for (int section = 0; section < 3; section++) {
            for (int modifier = 0; modifier < 3; modifier++) {
                ArrayList<Integer> temporaryRow = new ArrayList<Integer>();
                for (int block = 0; block < 3; block++) {
                    for (int cell = 0; cell < 3; cell++) {
                        temporaryRow.add(sBoard[block + (3 * section)][cell + (3 * modifier)]);
                    }
                }
                Integer[] superTemporaryArray = new Integer[temporaryRow.size()];
                rowBoardPlaceholder[modifier + (3 * section)] = temporaryRow.toArray(superTemporaryArray);
            }
        }
        return rowBoardPlaceholder;
    }
    
    private int calcPenalty() {
        int pen = 0;
        
        // First, parse the rows
        for (int row = 0; row < 9; row++) {
            for (int pass = 0; pass < 9; pass++) {
                for (int cell = 0; cell < 9; cell++) {
                    if (!(cell == pass)) {
                        if (this.rowBoard[row][pass] == this.rowBoard[row][cell]) {
                            pen++;
                        }
                    }
                }
            }
        }

        //Then parse the columns
        for (int column = 0; column < 9; column++) {
            for (int pass = 0; pass < 9; pass++) {
                for (int cell = 0; cell < 9; cell++) {
                    if (!(cell == pass)) {
                        if (this.rowBoard[pass][column] == this.rowBoard[cell][column]) {
                            //[pass=0][column=0] == [cell=0][column=0] //skip
                            //[pass=0][column=0] == [cell=1][column=0]
                            //[pass=0][column=0] == [cell=2][column=0]
                            //[pass=0][column=0] == [cell=3][column=0]
                            //[pass=0][column=0] == [cell=4][column=0]
                            //[pass=0][column=0] == [cell=5][column=0]
                            //[pass=0][column=0] == [cell=6][column=0]
                            //[pass=0][column=0] == [cell=7][column=0]
                            // ...
                            //[pass=1][column=0] == [cell=0][column=0]
                            //[pass=1][column=0] == [cell=1][column=0] //skip
                            //[pass=1][column=0] == [cell=2][column=0] 
                            //[pass=1][column=0] == [cell=3][column=0] 
                            // ...
                            //[pass=2][column=0] == [cell=0][column=0] 
                            //[pass=2][column=0] == [cell=1][column=0] 
                            //[pass=2][column=0] == [cell=2][column=0] //skip
                            // ...
                            
                            pen++;
                        }
                    }
                }
            }
        }
        return pen;
    }

    private Integer[][] fill(Integer[][] board) {

        for (int block = 0; block < 9; block++) {
            ArrayList<Integer> availableNumbers = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
            for (int cell = 0; cell < 9; cell++) {
                if (board[block][cell] != 0) {
                    int numInThatSpot = board[block][cell];
                    availableNumbers.set((numInThatSpot - 1), 0);
                }
            }
            for (int cell = 0; cell < 9; cell++) {  //for each cell, grab a random number from the arraylist and put it in that cell. unless the number is zero, then we try again.
                if (board[block][cell] == 0) {
                    int indexToBeGot = (int)(Math.random()*9);
                    int numberForFilling;
                    while(true){
                        if(availableNumbers.get(indexToBeGot)!=0) {
                            numberForFilling=availableNumbers.get(indexToBeGot);
                            availableNumbers.set(indexToBeGot, 0);
                            break;
                        } else {
                            indexToBeGot = (int)(Math.random()*9);
                        }
                    }
                   board[block][cell] = numberForFilling;
                }
            }
        }
        return board;
    }

    private Map<Integer, ArrayList<Integer>> calcDoNotTouch() {
        Map<Integer, ArrayList<Integer>> noTouchingMap = new HashMap<>();
        ArrayList<Integer> noTouching = new ArrayList<Integer>();
        for(int block=0;block<9;block++){
            noTouching = new ArrayList<Integer>();
            for(int cell=0;cell<9;cell++){
                if(this.sBoard[block][cell]!=0)
                    noTouching.add(sBoard[block][cell]);
            }
            noTouchingMap.put(block, noTouching);
        }
        
        return noTouchingMap;
    }

    //Print out that there sudoku board
    public String toString() {
        //
        String veryPrettySudokuString = "";
        veryPrettySudokuString = veryPrettySudokuString.concat("Penalty: "+Integer.toString(this.penalty)+"\n");
        for(int row=0;row<9;row++){
            for(int cell=0;cell<9;cell++){
                veryPrettySudokuString = veryPrettySudokuString.concat(Integer.toString(this.rowBoard[row][cell])+" ");
                if ((cell+1)%3 == 0 && cell != 0)  veryPrettySudokuString = veryPrettySudokuString.concat(" ");
            }
            veryPrettySudokuString = veryPrettySudokuString.concat("\n");
            if ((row+1)%3==0 && row != 0) veryPrettySudokuString = veryPrettySudokuString.concat("\n");
        }
        return veryPrettySudokuString;
    }
}
