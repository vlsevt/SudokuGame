<h1>Sudoku Game</h1>
<p style="font-size: 16px;">This project is a JavaFX-based Sudoku game that allows players to solve Sudoku puzzles interactively. The game includes features such as saving and loading game states, setting difficulty levels, and validating player moves. The user interface is built using FXML, and the game logic utilizes a backtracking algorithm for puzzle solving.</p>

<h2>Features</h2>
<ul style="font-size: 14px;">
    <li><strong>Interactive Sudoku Board:</strong> Users can input numbers into the board and attempt to solve the puzzle.</li>
    <li><strong>Difficulty Levels:</strong> Puzzles are generated based on different difficulty levels (easy, medium, hard).</li>
    <li><strong>Move Validation:</strong> Player moves are validated to ensure they adhere to Sudoku rules.</li>
    <li><strong>Save and Load:</strong> Players can save their current game state and load it later to resume where they left off.</li>
    <li><strong>Localization:</strong> The game supports multiple languages based on the player’s settings.</li>
</ul>

<h2>How It Works</h2>
<p style="font-size: 14px;">Game Board: The board is a 9x9 grid with editable cells for user input.</p>
<p style="font-size: 14px;">Puzzle Generation: A backtracking algorithm generates a complete Sudoku solution, and then fields are removed based on the selected difficulty level.</p>
<p style="font-size: 14px;">Validation: The system validates user input dynamically, ensuring each number adheres to Sudoku rules without auto-solving the puzzle.</p>
<p style="font-size: 14px;">Saving and Loading: Game state is serialized and stored as .sudoku files. Players can load saved games at any point.</p>

<h2>Technologies Used</h2>
<ul style="font-size: 14px;">
    <li>JavaFX: For the user interface.</li>
    <li>FXML: For layout and UI components.</li>
    <li>Java: For game logic and backend processing.</li>
    <li>Maven: To manage dependencies and build the project.</li>
</ul>

<h2>Installation</h2>
<ol style="font-size: 14px;">
    <li>Clone the repository:</li>
    <pre><code>git clone &lt;repository-url&gt;</code></pre>
    <li>Open the project in your favorite IDE.</li>
    <li>Ensure you have Java 21 installed.</li>
    <li>Build the project using Maven:</li>
    <pre><code>mvn clean install</code></pre>
    <li>Run the application.</li>
</ol>

<h2>How to Play</h2>
<ul style="font-size: 14px;">
    <li>Select a difficulty level and start a new game.</li>
    <li>Input numbers into the grid, ensuring that no duplicate numbers exist in any row, column, or 3x3 subgrid.</li>
    <li>You can save your progress and load it later from the main menu.</li>
</ul>
