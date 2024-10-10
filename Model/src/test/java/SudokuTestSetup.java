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

import org.example.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class SudokuTestSetup {
    final int[][] defaultBoard = {
            {0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 4, 0, 0},
            {0, 0, 4, 0, 0, 0, 0, 0, 6},
            {0, 0, 0, 0, 2, 0, 3, 0, 7},
            {0, 0, 0, 0, 0, 0, 9, 0, 0},
            {3, 1, 0, 8, 7, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 5, 6, 0, 0},
            {0, 9, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 2, 4, 0, 0, 0, 0}
    };

    void setBoard(int[][] from, SudokuBoard to) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                to.set(r, c, from[r][c]);
            }
        }
    }

    int[][] getBoard(SudokuBoard from) {
        int[][] to = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                to[r][c] = from.get(r, c);
            }
        }
        return to;
    }
}
