import org.example.SudokuField;
import org.example.SudokuGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SudokuGroupTests {
    @Test
    public void testClone() {
        SudokuField[] sudokuFields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            sudokuFields[i] = new SudokuField(0);
        }
        SudokuGroup originalSudokuGroup = new SudokuGroup(sudokuFields);
        SudokuGroup clonedSudokuGroup = originalSudokuGroup.clone();

        Assertions.assertEquals(originalSudokuGroup, clonedSudokuGroup);
        Assertions.assertNotSame(originalSudokuGroup, clonedSudokuGroup);
    }
}