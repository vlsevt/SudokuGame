CREATE TABLE sudoku_board_field
(
    sudoku_board_name TEXT,
    field_row INT CHECK (field_row BETWEEN 0 AND 9),
    field_column INT CHECK (field_column BETWEEN 0 AND 9),
    field_value INT CHECK (field_value BETWEEN 0 AND 9),
    sudoku_solver_name TEXT,
    UNIQUE (sudoku_board_name, field_row, field_column)
);