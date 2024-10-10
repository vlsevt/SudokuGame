import org.example.BacktrackingSudokuSolver;
import org.example.Dao;
import org.example.SudokuBoard;
import org.example.SudokuBoardDaoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileSudokuBoardDaoTest {

    @TempDir
    Path tempDir;

    SudokuTestSetup setup = new SudokuTestSetup();
    String testFileName = "testSudokuBoard.dat";

    @Test
    public void testWriteAndReadSudokuBoard() throws Exception {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        setup.setBoard(setup.defaultBoard, sudokuBoard);

        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileSudokuBoardDao(tempDir.toString());) {

            assertDoesNotThrow(() -> dao.write(testFileName, sudokuBoard));

            SudokuBoard loadedBoard = assertDoesNotThrow(() -> dao.read(testFileName));

            assertNotNull(loadedBoard);
            assertArrayEquals(setup.getBoard(sudokuBoard), setup.getBoard(loadedBoard));
        }
    }

    @Test
    public void testNamesAfterWritingMultipleFiles() throws Exception {
        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileSudokuBoardDao(tempDir.toString());) {
            for (int i = 0; i < 3; i++) {
                String fileName = "sudoku" + i + ".dat";
                SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
                setup.setBoard(setup.defaultBoard, board);
                dao.write(fileName, board);
            }

            List<String> fileNames = assertDoesNotThrow(dao::names);

            assertEquals(3, fileNames.size(), "There should be three files listed");
            assertTrue(fileNames.contains("sudoku0.dat"));
            assertTrue(fileNames.contains("sudoku1.dat"));
            assertTrue(fileNames.contains("sudoku2.dat"));
        }
    }

//    @Test
//    public void testReadWithRuntimeException() throws Exception {
//        Path filePath = tempDir.resolve("corruptedData.dat");
//        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getSudokuBoardDao(tempDir.toString());) {
//            Files.write(filePath, new byte[]{0, 1, 2, 3});
//            assertThrows(RuntimeException.class, () -> dao.read("corruptedData.dat"), "Expected a RuntimeException due to stream corruption during deserialization.");
//        } catch (IOException e) {
//            fail("Setup of corrupted data should not fail.", e);
//        }
//    }


//    @Test
//    public void testWriteWithRuntimeException() throws Exception {
//        String invalidFilePath = "unwritableFile.dat";
//        Path invalidFile = tempDir.resolve(invalidFilePath);
//        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getSudokuBoardDao(tempDir.toString());) {
//            Files.createDirectory(invalidFile);
//
//            SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
//
//            assertThrows(RuntimeException.class, () -> dao.write(invalidFilePath, sudokuBoard), "Expected a RuntimeException due to I/O error while writing to the file.");
//        } catch (IOException e) {
//            fail("Setup of unwritable file should not fail.", e);
//        }
//    }
}