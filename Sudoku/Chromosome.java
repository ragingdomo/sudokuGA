package Sudoku;

import java.util.*;

//each board knows its penalty, the initial state before filling, and the fitness.
// These are calculated in the constructor. calcPenalty is the only thing (so far)
// that doesn't work due to the way it's setup.
public class Chromosome {
    int penalty;
    double fitness;
    // sBoard is formatted as a 2D array, where each item is an array of the BLOCKS.
    // This is disgusting, but all that's happening is getting the lists all situated. Cells with values at the start get the "true"
    // parameter passed in so they don't get mutated later, changing the problem. 

    ArrayList<Cell> temp1 = new ArrayList<Cell>( Arrays.asList( new Cell(8, true), new Cell(3, true), new Cell(9, true), new Cell(), new Cell(6, true), new Cell(2, true), new Cell(), new Cell(1, true), new Cell() )); 
    ArrayList<Cell> temp2 = new ArrayList<Cell>( Arrays.asList( new Cell(), new Cell(), new Cell(6, true), new Cell(), new Cell(), new Cell(3, true), new Cell(2, true), new Cell(), new Cell(8, true) )); 
    ArrayList<Cell> temp3 = new ArrayList<Cell>( Arrays.asList( new Cell(), new Cell(), new Cell(), new Cell(), new Cell(9, true), new Cell(1, true), new Cell(), new Cell(5, true), new Cell() ));
    ArrayList<Cell> temp4 = new ArrayList<Cell>( Arrays.asList( new Cell(9, true), new Cell(), new Cell(), new Cell(), new Cell(), new Cell(), new Cell(4, true), new Cell(5, true), new Cell() )); 
    ArrayList<Cell> temp5 = new ArrayList<Cell>( Arrays.asList( new Cell(), new Cell(7, true), new Cell(4, true), new Cell(), new Cell(), new Cell(), new Cell(6, true), new Cell(8, true), new Cell() )); 
    ArrayList<Cell> temp6 = new ArrayList<Cell>( Arrays.asList( new Cell(), new Cell(6, true), new Cell(3, true), new Cell(), new Cell(), new Cell(), new Cell(), new Cell(), new Cell(2, true) ));
    ArrayList<Cell> temp7 = new ArrayList<Cell>( Arrays.asList( new Cell(), new Cell(4, true), new Cell(), new Cell(6, true), new Cell(9, true), new Cell(), new Cell(), new Cell(), new Cell() )); 
    ArrayList<Cell> temp8 = new ArrayList<Cell>( Arrays.asList( new Cell(8, true), new Cell(), new Cell(2, true), new Cell(4, true), new Cell(), new Cell(), new Cell(9, true), new Cell(), new Cell() )); 
    ArrayList<Cell> temp9 = new ArrayList<Cell>( Arrays.asList( new Cell(), new Cell(7, true), new Cell(), new Cell(1, true), new Cell(2, true), new Cell(), new Cell(4, true), new Cell(3, true), new Cell(5, true) ));
    ArrayList<ArrayList<Cell>> sBoard = new ArrayList<ArrayList<Cell>>( Arrays.asList(temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9) );
    
    ArrayList<ArrayList<Cell>> rowBoard;

    // Constructor. 
    public Chromosome() {
        sBoard = fill(sBoard);
        rowBoard = makeRowBoard(sBoard);
        penalty = calcPenalty();
        fitness = 1 / penalty;
        
    }

    //manual constructor allows you to set each block individually
    public Chromosome(ArrayList<Cell> b1, ArrayList<Cell> b2, ArrayList<Cell> b3, ArrayList<Cell> b4, ArrayList<Cell> b5, ArrayList<Cell> b6, ArrayList<Cell> b7,
            ArrayList<Cell> b8, ArrayList<Cell> b9) {
        ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>( Arrays.asList(b1, b2, b3, b4, b5, b6, b7, b8, b9 ));
        sBoard = board;
        rowBoard = makeRowBoard(sBoard);
        penalty = calcPenalty();
        fitness = 1 / penalty;
    }

    
    public ArrayList<ArrayList<Cell>> getRowBoard() {
        return this.rowBoard;
    }
    
    public ArrayList<Cell> getBlock(int block) {
        return this.sBoard.get(block);
    }
    
    public int getPenalty() {
        return this.penalty;
    }
    
    public double getFitness() {
        return this.fitness;
    }
    
    public ArrayList<ArrayList<Cell>> getBoard() {
        return sBoard;
    }
    
    // Pick two random cells, call them cell1 and cell2.
    // Make sure they aren't equal, then swap the contents of the cells.
    // Now that your big brain made the no-touchies list, rewrite this
    // to only mutate the items that can be touched. <3
    public void mutate(double mutationChance) {
        // if the mutate chance is high enough to go, then we go
        if(((int)(Math.random()*100)) < ((int)(mutationChance*100))) {
            for (int block = 0; block < 9; block++) {

                int pair1_1;
                do {
                    pair1_1 = (int)(Math.random()*9);
                } while ((sBoard.get(block).get(pair1_1).getOriginal())); // regenerate number if the cell has the original trait

                int pair1_2;
                do {
                    pair1_2 = (int)(Math.random()*9);
                } while ((sBoard.get(block).get(pair1_2).getOriginal()) || pair1_2==pair1_1); // keep getting more numbers if the cell is original or if the pairs are equal.

                // repeat the above, but with a second pair
                int pair2_1;
                do {
                    pair2_1 = (int)(Math.random()*9);
                } while ((sBoard.get(block).get(pair2_1).getOriginal()));

                int pair2_2;
                do {
                    pair2_2 = (int)(Math.random()*9);
                } while ((sBoard.get(block).get(pair2_2).getOriginal()) || pair2_2==pair2_1);

                // this is where the actual swap happens. First pair is first...
                int swapValue1 = sBoard.get(block).get(pair1_1).getValue();
                int swapValue2 = sBoard.get(block).get(pair1_2).getValue();
                sBoard.get(block).get(pair1_1).setValue(swapValue2);
                sBoard.get(block).get(pair1_2).setValue(swapValue1);
                
                // and then the second pair.
                swapValue1 = sBoard.get(block).get(pair2_1).getValue();
                swapValue2 = sBoard.get(block).get(pair2_2).getValue();
                sBoard.get(block).get(pair2_1).setValue(swapValue2);
                sBoard.get(block).get(pair2_2).setValue(swapValue1);
            }
            // then we must update the board
            this.rowBoard = makeRowBoard(this.sBoard);
            this.penalty = calcPenalty();
            this.fitness = 1/this.penalty;
        }
    }

    //Turn the block formatted sudoku board into a row formatted board, which the 
    //Chromosome object knows.
    private ArrayList<ArrayList<Cell>> makeRowBoard(ArrayList<ArrayList<Cell>> board) {
        ArrayList<ArrayList<Cell>> rowBoardPlaceholder = new ArrayList<ArrayList<Cell>>();
        //
        // Iterates over the row of the sudoku board, the rows within the sudoku board
        // then the blocks within, then the cells.
        for (int section = 0; section < 3; section++) {
            for (int modifier = 0; modifier < 3; modifier++) {
                ArrayList<Cell> temporaryRow = new ArrayList<Cell>();
                for (int block = 0; block < 3; block++) {
                    for (int cell = 0; cell < 3; cell++) {
                        temporaryRow.add(sBoard.get(block + (3 * section)).get(cell + (3 * modifier)));
                    }
                }
                rowBoardPlaceholder.add(temporaryRow);
                //Integer[] superTemporaryArray = new Integer[temporaryRow.size()];
                //rowBoardPlaceholder[modifier + (3 * section)] = temporaryRow.toArray(superTemporaryArray);
            }
        }
        return rowBoardPlaceholder;
    }
    
    private int calcPenalty() {
        int pen = 0;
        
        // First, parse the rows
        for (int row = 0; row < 9; row++) {
            for (int pass = 0; pass < 9; pass++) {
                ArrayList<Integer> badIndexes = new ArrayList<Integer>();
                badIndexes.add(pass);
                for (int cell = 0; cell < 9; cell++) {
                    Iterator<Integer> iter = badIndexes.iterator();
                    ArrayList<Integer> temporaryBadIndexes = new ArrayList<Integer>();
                    while (iter.hasNext()){
                        int index = iter.next();
                        if (!(cell == index)) {
                            if (this.rowBoard.get(row).get(pass).getValue() == this.rowBoard.get(row).get(cell).getValue()) {
                                pen++;
                                temporaryBadIndexes.add(cell);
                            }
                        }
                    }
                    badIndexes.addAll(temporaryBadIndexes);
                }
            }
        }

        //Then parse the columns
        for (int column = 0; column < 9; column++) {
            for (int pass = 0; pass < 9; pass++) {
                ArrayList<Integer> badIndexes = new ArrayList<Integer>();
                badIndexes.add(pass);
                for (int cell = 0; cell < 9; cell++) {
                    Iterator<Integer> iter = badIndexes.iterator();
                    ArrayList<Integer> temporaryBadIndexes = new ArrayList<Integer>();
                    while (iter.hasNext()){
                        int index = iter.next();
                        if (!(cell == index)) {
                            if (this.rowBoard.get(pass).get(column).getValue() == this.rowBoard.get(cell).get(column).getValue()) {
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
                                temporaryBadIndexes.add(cell);
                            }
                        }
                    }
                }
            }
        }
        return pen;
    }

    // I should simplify this with the remove() method. Forgot about that one.
    // Anyway, fill just turns any unfilled cell into a random number to get 1-9
    // in each block. 
    private ArrayList<ArrayList<Cell>> fill(ArrayList<ArrayList<Cell>> board) {

        for (int block = 0; block < 9; block++) {
            ArrayList<Integer> availableNumbers = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
            for (int cell = 0; cell < 9; cell++) {
                if (board.get(block).get(cell).getValue() != 0) {
                    int numInThatSpot = board.get(block).get(cell).getValue();
                    availableNumbers.set((numInThatSpot - 1), 0);
                }
            }
            for (int cell = 0; cell < 9; cell++) {  //for each cell, grab a random number from the arraylist and put it in that cell. unless the number is zero, then we try again.
                if (board.get(block).get(cell).getValue() == 0) {
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
                   board.get(block).get(cell).setValue(numberForFilling);
                }
            }
        }
        return board;
    }


    //Print out that there sudoku board
    public String toString() {
        //
        String veryPrettySudokuString = "";
        veryPrettySudokuString = veryPrettySudokuString.concat("Penalty: "+Integer.toString(this.penalty)+"\n");
        for(int row=0;row<9;row++){
            for(int cell=0;cell<9;cell++){
                veryPrettySudokuString = veryPrettySudokuString.concat(Integer.toString(this.rowBoard.get(row).get(cell).getValue())+" ");
                if ((cell+1)%3 == 0 && cell != 0)  veryPrettySudokuString = veryPrettySudokuString.concat(" ");
            }
            veryPrettySudokuString = veryPrettySudokuString.concat("\n");
            if ((row+1)%3==0 && row != 0) veryPrettySudokuString = veryPrettySudokuString.concat("\n");
        }
        return veryPrettySudokuString;
    }
}
