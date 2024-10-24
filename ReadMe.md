Sudoku Game
This project is a JavaFX-based Sudoku game that allows players to solve Sudoku puzzles interactively. The game includes features such as saving and loading game states, setting difficulty levels, and validating player moves. The user interface is built using FXML, and the game logic utilizes a backtracking algorithm for puzzle solving.

Features
Interactive Sudoku Board: Users can input numbers into the board and attempt to solve the puzzle.
Difficulty Levels: Puzzles are generated based on different difficulty levels (easy, medium, hard).
Move Validation: Player moves are validated to ensure they adhere to Sudoku rules.
Save and Load: Players can save their current game state and load it later to resume where they left off.
Localization: The game supports multiple languages based on the player’s settings.
How It Works
Game Board: The board is a 9x9 grid with editable cells for user input.
Puzzle Generation: A backtracking algorithm generates a complete Sudoku solution, and then fields are removed based on the selected difficulty level.
Validation: The system validates user input dynamically, ensuring each number adheres to Sudoku rules without auto-solving the puzzle.
Saving and Loading: Game state is serialized and stored as .sudoku files. Players can load saved games at any point.
Technologies Used
JavaFX: For the user interface.
FXML: For layout and UI components.
Java: For game logic and backend processing.
Maven: To manage dependencies and build the project.
Installation
Clone the repository:
bash
Копировать код
git clone <repository-url>
Open the project in your favorite IDE.
Ensure you have Java 21 installed.
Build the project using Maven:
Копировать код
mvn clean install
Run the application.
How to Play
Select a difficulty level and start a new game.
Input numbers into the grid, ensuring that no duplicate numbers exist in any row, column, or 3x3 subgrid.
