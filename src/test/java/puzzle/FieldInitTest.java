package puzzle;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static sk.tuke.GameStudio.puzzle.FieldInit.*;

class FieldInitTest {

    static final int rows = 4;
    static final int col = 4;
    static final int[][] puzzleField = new int[rows][col];
    static final String[][] puzzleFieldToDisplay = new String[rows + 1][col + 1];
    String[][] displayGrid = {{" *", "A", "B", "C", "D"},
            {" 1|", "null", "null", "null", "null"},
            {" 2|", "null", "null", "null", "null"},
            {" 3|", "null", "null", "null", "null"},
            {" 4|", "null", "null", "null", "null"}};

    static final int sumOfAllElements = 120;

    @Test
    void baseArrayInitTest() {
        int sum = 0;

        assertEquals(4, puzzleField.length);

        baseArrayInit(puzzleField, puzzleFieldToDisplay);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sum = sum + puzzleField[i][j];
            }
        }
        assertEquals(sumOfAllElements, sum);
    }


    @Test
    void fillArraysByRandomNumbersTest() {
        fillArraysByRandomNumbers(puzzleField, displayGrid);
        System.out.println(puzzleFieldToDisplay[0][0]);
        boolean zeroExist = false;

    }

    @Test
    void displayInitTest() {
        assertEquals(5, puzzleFieldToDisplay.length);
    }

    @Test
    void initRandomTest() {
        initRandomTiles(puzzleField);
        int[][] wonGrid = {{0,1, 2, 3}, {4,5, 6, 7}, {8, 9, 10, 11}, {12,13, 14, 15}};
        assertFalse(Arrays.deepEquals(wonGrid,puzzleField));
    }
}