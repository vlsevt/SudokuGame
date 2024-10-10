import org.example.SudokuField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SudokuFieldTests {
    @Test
    public void testClone() throws CloneNotSupportedException {
        SudokuField originalSudokuField = new SudokuField(0);
        SudokuField clonedSudokuField = originalSudokuField.clone();

        Assertions.assertEquals(originalSudokuField, clonedSudokuField);
        Assertions.assertNotSame(originalSudokuField, clonedSudokuField);
    }

    @Test
    public void testEquals() {
        SudokuField sudokuField1 = new SudokuField(0);
        SudokuField sudokuField2 = null;

        Assertions.assertNotEquals(sudokuField1, sudokuField2);
    }

    @Test
    public void testHashCode() {
        SudokuField sudokuField1 = new SudokuField(0);
        SudokuField sudokuField2 = new SudokuField(0);

        Assertions.assertEquals(sudokuField1.hashCode(), sudokuField2.hashCode());
    }
}
