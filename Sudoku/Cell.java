package Sudoku;

public class Cell {
    int value;
    boolean original;

    public Cell(){
        this.value = 0;
        this.original = false;
    }

    public Cell(int value, boolean original){
        this.value = value;        
        this.original = original;        
    }

    public void setValue(int value){
        this.value = value;
    }
    
    public void setValue(boolean original){
        this.original = original;
    }
    
    public int getValue(){
        return this.value;
    }
    
    public boolean getOriginal(){
        return this.original;
    }
}

