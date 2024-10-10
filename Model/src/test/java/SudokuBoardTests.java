import org.example.BacktrackingSudokuSolver;
import org.example.SudokuBoard;
import org.example.SudokuField;
import org.example.SudokuSolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SudokuBoardTests {
    @Test
    public void whenSetValue_thenBoardIsUpdated() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.set(0, 0, 5);
        Assertions.assertEquals(5, board.get(0, 0));
    }

    @Test
    public void testSetAndGet() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.set(0, 0, 5); // Set the top-left cell to 5
        int value = board.get(0, 0);
        Assertions.assertEquals(5, value, "The cell value should be set to 5.");
    }

    @Test
    public void testGetRow() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.getRow(0).setFieldValue(0, 5); // Set a value in the first row
        int value = board.getRow(0).getFieldValue(0);
        Assertions.assertEquals(5, value, "The row should contain the value set previously.");
    }

    @Test
    public void testGetColumn() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.getColumn(1).setFieldValue(1, 7); // Set a value in the second column, second cell
        int value = board.getColumn(1).getFieldValue(1);
        Assertions.assertEquals(7, value, "The column should contain the value set previously.");
    }

    @Test
    public void testGetBox() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.getBox(1, 1).setFieldValue(4, 9); // Set a value in the middle of the top-left box
        int value = board.getBox(1, 1).getFieldValue(4);
        Assertions.assertEquals(9, value, "The box should contain the value set previously.");
    }

    @Test
    public void testSetFieldValueWithWrongValue() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> board.set(0, 0, 25));
    }

    @Test
    public void testBoardUpdate() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);

        board.set(3, 3, 5);

        int boxValue = board.get(3, 3); // Get the value of the cell in the box
        Assertions.assertEquals(5, boxValue, "The box should be updated with the new value.");
    }

    @Test
    public void testSetWithValidFinalMove() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.set(0, 0, 5, true); // Set the top-left cell to 5 as a final move
        int value = board.get(0, 0);
        Assertions.assertEquals(5, value, "The cell value should be set to 5.");
        Assertions.assertTrue(board.checkBoard(), "The board should be valid after the final move.");
    }

    @Test
    public void testSetWithInvalidFinalMove() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.set(0, 0, 5, true); // Set the first cell to 5 as a final move, making it a valid move.
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            board.set(0, 1, 5, true); // Attempt to set the second cell to 5 as well, which should be invalid.
        }, "An IllegalArgumentException should be thrown for an impossible move.");
        Assertions.assertEquals("Impossible move.", exception.getMessage(), "The exception message should indicate an impossible move.");
    }

    @Test
    public void testGetBoxWrongIndex() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> board.getBox(15, 53));
    }

    @Test
    public void testGetRowWrongIndex() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> board.getRow(53));
    }

    @Test
    public void testGetColumnWrongIndex() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> board.getColumn(53));
    }

    @Test
    public void testClone() {
        SudokuBoard originalSudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBoard clonedSudokuBoard = originalSudokuBoard.clone();

        Assertions.assertEquals(originalSudokuBoard, clonedSudokuBoard);
        Assertions.assertNotSame(originalSudokuBoard, clonedSudokuBoard);
    }

    @Test
    public void testCloneChangeValue() {
        SudokuBoard originalSudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBoard clonedSudokuBoard = originalSudokuBoard.clone();
        originalSudokuBoard.set(0, 0, 1);

        Assertions.assertNotEquals(originalSudokuBoard, clonedSudokuBoard);
        Assertions.assertNotSame(originalSudokuBoard, clonedSudokuBoard);
    }
    @Test
    public void testToString() {
        SudokuBoard sudokuBoard1 = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBoard sudokuBoard2 = new SudokuBoard(new BacktrackingSudokuSolver());

        Assertions.assertEquals(sudokuBoard1.toString(), sudokuBoard2.toString());
    }

    @Test
    public void testEquals() {
        SudokuBoard sudokuBoard1 = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBoard sudokuBoard2 = new SudokuBoard(new BacktrackingSudokuSolver());

        Assertions.assertEquals(sudokuBoard1, sudokuBoard2);
    }

    @Test
    public void testHashCode() {
        SudokuBoard sudokuBoard1 = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBoard sudokuBoard2 = new SudokuBoard(new BacktrackingSudokuSolver());

        Assertions.assertEquals(sudokuBoard1.hashCode(), sudokuBoard2.hashCode());
    }

    @Test
    public void testEqualsNull() {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());

        Assertions.assertNotEquals(sudokuBoard, null);
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    public void testEqualsOtherClass() {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuField sudokuField = new SudokuField(0);

        Assertions.assertNotEquals(sudokuBoard, sudokuField);
    }

    @Test
    public void testEqualsDifferentBoards() {
        SudokuBoard sudokuBoard1 = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard1.set(0, 0, 1);
        SudokuBoard sudokuBoard2 = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard2.set(0, 0, 2);

        Assertions.assertNotEquals(sudokuBoard1, sudokuBoard2);
    }
}
