import org.example.BacktrackingSudokuSolver;
import org.example.SudokuBoard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SudokuBacktrackingTests extends SudokuTestSetup {
    @Test
    public void solutionIsCorrect() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        setBoard(defaultBoard, board);

        board.solveGame();

        Assertions.assertTrue(board.checkBoard());
    }

    @Test
    public void subsequentSolvesAreDifferent() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        setBoard(defaultBoard, board);

        board.solveGame();
        int[][] firstSolution = getBoard(board);

        setBoard(defaultBoard, board);
        board.solveGame();
        int[][] secondSolution = getBoard(board);

        Assertions.assertNotEquals(firstSolution, secondSolution);
    }

    @Test
    public void testClone() {
        BacktrackingSudokuSolver originalSudokuSolver = new BacktrackingSudokuSolver();
        BacktrackingSudokuSolver clonedSudokuSolver = originalSudokuSolver.clone();

        Assertions.assertEquals(originalSudokuSolver, clonedSudokuSolver);
        Assertions.assertNotSame(originalSudokuSolver, clonedSudokuSolver);
    }

    @Test
    public void testToString() {
        BacktrackingSudokuSolver sudokuSolver1 = new BacktrackingSudokuSolver();
        BacktrackingSudokuSolver sudokuSolver2 = new BacktrackingSudokuSolver();

        Assertions.assertEquals(sudokuSolver1.toString(), sudokuSolver2.toString());
    }

    @Test
    public void testEquals() {
        BacktrackingSudokuSolver sudokuSolver1 = new BacktrackingSudokuSolver();
        BacktrackingSudokuSolver sudokuSolver2 = new BacktrackingSudokuSolver();

        Assertions.assertEquals(sudokuSolver1, sudokuSolver2);
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    public void testEqualsIsNotTheSameObject() {
        BacktrackingSudokuSolver sudokuSolver = new BacktrackingSudokuSolver();
        SudokuBoard sudokuGroup = new SudokuBoard(sudokuSolver);

        Assertions.assertNotEquals(sudokuSolver, sudokuGroup);
    }

    @Test
    public void testHashCode() {
        BacktrackingSudokuSolver sudokuSolver1 = new BacktrackingSudokuSolver();
        BacktrackingSudokuSolver sudokuSolver2 = new BacktrackingSudokuSolver();

        Assertions.assertEquals(sudokuSolver1.hashCode(), sudokuSolver2.hashCode());
    }
}
