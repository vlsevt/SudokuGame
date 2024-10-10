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
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class BacktrackingSudokuSolver implements SudokuSolver, Cloneable, Serializable {
    private static final Logger logger = LogManager.getLogger(BacktrackingSudokuSolver.class);

    public BacktrackingSudokuSolver() {
    }

    public void solve(SudokuBoard board) {
        recursiveSolve(board);
    }

    private boolean recursiveSolve(SudokuBoard board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.get(row, col) == 0) {
                    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
                    Collections.shuffle(numbers);
                    for (int i = 0; i < 9; i++) {
                        board.set(row, col, numbers.get(i));
                        if (board.checkBoard() && recursiveSolve(board)) {
                            return true;
                        }
                        board.set(row, col, 0);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public BacktrackingSudokuSolver clone() {
        BacktrackingSudokuSolver backtrackingSudokuSolver;

        try {
            backtrackingSudokuSolver = (BacktrackingSudokuSolver) super.clone();
        } catch (CloneNotSupportedException e) {
            logger.error(BacktrackingSudokuSolver.class.getName() + " does not support cloning.", e);
            throw new AssertionError(e);
        }

        return backtrackingSudokuSolver;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return getClass() == obj.getClass();
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.getClass());
    }
}
