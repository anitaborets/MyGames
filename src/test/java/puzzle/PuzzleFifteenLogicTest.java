package puzzle;

import org.example.puzzle.PuzzleFifteenLogic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PuzzleFifteenLogicTest {
    PuzzleFifteenLogic logic = new PuzzleFifteenLogic();

    @Test
    void isCanMoveLeftTest() {
        assertFalse(logic.isCanMoveLeft(0));
        assertTrue(logic.isCanMoveLeft(1));
    }

    @Test
    void isCanMoveRightTest() {
        assertTrue(logic.isCanMoveRight(0));
        assertTrue(logic.isCanMoveRight(4));
        assertFalse(logic.isCanMoveRight(3));
    }

    @Test
    void isCanMoveDown() {
        assertTrue(logic.isCanMoveDown(0));
        assertFalse(logic.isCanMoveDown(12));
        assertFalse(logic.isCanMoveDown(13));
    }

    @Test
    void isCanMoveUp() {
        assertFalse(logic.isCanMoveUp(0));
        assertFalse(logic.isCanMoveUp(1));
        assertTrue(logic.isCanMoveUp(12));
        assertTrue(logic.isCanMoveUp(13));
    }
}