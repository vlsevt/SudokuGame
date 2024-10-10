package org.example;

public enum Difficulty {
    EASY(45), // Assuming 45 cells are pre-filled for easy difficulty
    MEDIUM(30), // 30 cells for medium difficulty
    HARD(17); // 17 cells for hard difficulty

    private final int cellsToFill;

    Difficulty(int cellsToFill) {
        this.cellsToFill = cellsToFill;
    }

    public int getCellsToFill() {
        return cellsToFill;
    }
}
