import org.example.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcSudokuBoardDaoTest {
    SudokuTestSetup setup = new SudokuTestSetup();
    String testName = "testBoard";
    @Test
    public void testWriteAndReadSudokuBoardJdbc() throws Exception {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        setup.setBoard(setup.defaultBoard, sudokuBoard);
        SudokuBoard loadedBoard;
        try (JdbcSudokuBoardDao dao = SudokuBoardDaoFactory.getJdbcSudokuBoardDao()) {
            assertDoesNotThrow(() -> dao.write(testName, sudokuBoard));

            loadedBoard = assertDoesNotThrow(() -> dao.read(testName));
            dao.delete(testName);
        }
        assertNotNull(loadedBoard);
        assertNotSame(sudokuBoard, loadedBoard);
        assertEquals(sudokuBoard, loadedBoard);
    }
}
