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

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.Nullable;

public class SudokuField implements Cloneable, Serializable, Comparable<SudokuField> {
    private static final Logger logger = LogManager.getLogger(SudokuField.class);
    private int value;
    private PropertyChangeSupport propertyChangeSupport;

    public SudokuField(int value) {
        this.value = value;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < 0 || value > 9) {
            logger.error("Number entered was not between 0 and 9");
            throw new IllegalArgumentException("Number must be between 0 and 9.");
        }
        int oldValue = this.value;
        this.value = value;
        if (value != 0) {
            propertyChangeSupport.firePropertyChange("value", oldValue, value);
        }
    }

    @Override
    public SudokuField clone() {
        try {
            return (SudokuField) super.clone();
        } catch (CloneNotSupportedException e) {
            logger.error(SudokuField.class + " does not support logging", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
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
        final SudokuField other = (SudokuField) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public int compareTo(@Nullable SudokuField field) {
        if (field != null) {
            return Integer.compare(this.value, field.value);
        } else {
            logger.error("Passed SudokuField is null");
            throw new NullPointerException("The passing parameter is null");
        }
    }
}
