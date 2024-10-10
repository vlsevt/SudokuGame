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

public class SudokuGroup implements Cloneable, Serializable {
    private static final Logger logger = LogManager.getLogger(SudokuGroup.class);

    private SudokuField[] fields;

    public SudokuGroup(SudokuField[] fields) {
        this.fields = fields.clone();
    }

    public int getFieldValue(int index) {
        return fields[index].getValue();
    }

    public void setFieldValue(int index, int val) {
        fields[index].setValue(val);
    }

    public boolean verify() {
        int[] numberCount = new int[10];
        for (int i = 0; i < 9; i++) {
            numberCount[getFieldValue(i)]++;
        }
        for (int i = 1; i <= 9; i++) {
            if (numberCount[i] > 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public SudokuGroup clone() {
        SudokuGroup sudokuGroup;

        try {
            sudokuGroup = (SudokuGroup) super.clone();
        } catch (CloneNotSupportedException e) {
            logger.error(SudokuGroup.class.getName() + " does not support cloning.", e);
            throw new AssertionError(e);
        }

        sudokuGroup.fields = this.fields;

        return sudokuGroup;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fields", fields)
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
        final SudokuGroup other = (SudokuGroup) obj;
        return Arrays.equals(this.fields, other.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object[]) this.fields);
    }
}
