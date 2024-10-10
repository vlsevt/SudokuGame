package org.example;

import java.util.ListResourceBundle;

public class MyResources_en extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][] {
                {"s1", "Author name: Vladislav Sevtsenko"},
                {"s2", "Project name: SudokuGame"},
                {"s3", "Group: mkw_thu_1030_02"},
        };
    }
}
