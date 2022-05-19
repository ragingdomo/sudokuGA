package Sudoku;

// comment
import java.util.Random;
import java.lang.Math;

public class random {
    public static void main(String args[]) {
        for (int i = 0; i < 3; i++) {
            for (int j = 1; j <= 9; j++) {
                for (int k = 1; k <= 3; k++) {
                    int index = (i + j);
                    System.out.println("[" + index + "][" + k + "]");
                }

            }

        }
    }
}
