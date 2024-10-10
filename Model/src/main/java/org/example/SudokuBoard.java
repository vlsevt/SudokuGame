/*
 * Copyright (c) 2024 Vladislav Sevtsenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example;

import com.google.common.base.MoreObjects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class SudokuBoard implements Cloneable, Serializable {
    private static final Logger logger = LogManager.getLogger(SudokuBoard.class);
    public SudokuField[][] board;
    private final SudokuRow[] rows;
    private final SudokuColumn[] columns;
    private final SudokuBox[] boxes;
    private final SudokuSolver sudokuSolver;

    public SudokuBoard(SudokuSolver sudokuSolver) {
        this.board = initializeFields();
        this.rows = initializeRows(this.board);
        this.columns = initializeColumns(this.board);
        this.boxes = initializeBoxes(this.board);
        this.sudokuSolver = sudokuSolver;
    }

    SudokuField[][] initializeFields() {
        SudokuField[][] board = new SudokuField[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                board[r][c] = new SudokuField(0);
            }
        }
        return board;
    }

    public void removeFields(Difficulty difficulty) {
        Random random = new Random();
        int cellsToRemove = 81 - difficulty.getCellsToFill();
        for (int i = 0; i < cellsToRemove; i++) {
            int r;
            int c;
            do {
                r = random.nextInt(9);
                c = random.nextInt(9);
            } while (get(r, c) == 0);
            set(r, c, 0);
        }
    }

    private SudokuRow[] initializeRows(SudokuField[][] board) {
        SudokuRow[] rows = new SudokuRow[9];
        for (int r = 0; r < 9; r++) {
            rows[r] = new SudokuRow(board[r]);
        }
        return rows;
    }

    private SudokuColumn[] initializeColumns(SudokuField[][] board) {
        SudokuColumn[] columns = new SudokuColumn[9];
        for (int c = 0; c < 9; c++) {
            SudokuField[] column = new SudokuField[9];
            for (int r = 0; r < 9; r++) {
                column[r] = board[r][c];
            }
            columns[c] = new SudokuColumn(column);
        }
        return columns;
    }

    private SudokuBox[] initializeBoxes(SudokuField[][] board) {
        SudokuBox[] boxes = new SudokuBox[9];
        SudokuField[][] boxesFields = new SudokuField[9][9];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int boxNumber = r / 3 * 3 + c / 3;
                int inBoxPos = r % 3 * 3 + c % 3;
                boxesFields[boxNumber][inBoxPos] = board[r][c];
            }
        }

        for (int i = 0; i < 9; i++) {
            boxes[i] = new SudokuBox(boxesFields[i]);
        }

        return boxes;
    }

    public void solveGame() {
        sudokuSolver.solve(this);
    }

    public int get(int row, int col) {
        return board[row][col].getValue();
    }

    public void set(int row, int col, int val) {
        board[row][col].setValue(val);
    }

    public void set(int row, int col, int val, boolean isFinalMove) throws IllegalArgumentException {
        int previousValue = board[row][col].getValue();

        set(row, col, val);

        if (isFinalMove && !checkBoard()) {
            set(row, col, previousValue);
            logger.error("Impossible move");
            throw new IllegalArgumentException("Impossible move.");
        }
    }

    public SudokuField getField(int row, int col) {
        return board[row][col];
    }

    public SudokuRow getRow(int r) {
        if (r >= 0 && r < 9) {
            return rows[r];
        } else {
            logger.error("Failed to get row");
            throw new IllegalArgumentException("Row index out of bounds");
        }
    }

    public SudokuColumn getColumn(int c) {
        if (c >= 0 && c < 9) {
            return columns[c];
        } else {
            logger.error("Failed to get column");
            throw new IllegalArgumentException("Column index out of bounds");
        }
    }

    public SudokuBox getBox(int row, int col) {
        int boxIndex = row / 3 * 3 + col / 3;
        if (boxIndex >= 0 && boxIndex < 9) {
            return boxes[boxIndex];
        } else {
            logger.error("Failed to get box");
            throw new IllegalArgumentException("Box index out of bounds");
        }
    }

    public SudokuSolver getSudokuSolver() {
        return sudokuSolver;
    }

    public boolean isValidMove(int row, int col, int value) {
        if (value != 0) {
            set(row, col, value);
            boolean valid = checkBoard();
            set(row, col, 0);
            return valid;
        }
        return true;
    }

    public boolean checkBoard() {
        for (int i = 0; i < 9; i++) {
            if (!rows[i].verify() || !columns[i].verify() || !boxes[i].verify()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public SudokuBoard clone() {
        SudokuBoard sudokuBoard1 = new SudokuBoard(sudokuSolver);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                sudokuBoard1.set(r, c, get(r, c));
            }
        }
        return sudokuBoard1;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("board", board)
                .add("rows", rows)
                .add("columns", columns)
                .add("boxes", boxes)
                .add("sudokuSolver", sudokuSolver)
                .toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SudokuBoard other = (SudokuBoard) obj;
        return Arrays.deepEquals(this.board, other.board);
    }


    @Override
    public int hashCode() {
        return Objects.hash(
                Arrays.deepHashCode(this.board)
        );
    }
}
