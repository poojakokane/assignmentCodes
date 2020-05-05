/**
 * An immutable representation of a TicTacToe Board
 *
 * Creating a new Board with the Constructor
 * creates an "empty" board, ready to start a new game.
 *
 * The .move() method creates new Board instances
 * related to the calling instance, but one move
 * further in play.
 */
public class Board {
    public static final int GRID_DIMENSION = 3;
    private static char[] intToPlayerChar = {' ', 'X', 'O'};

    private int[] cells; // cells 0-8 from left to right top to bottom
    private int winner; // 0 = none
    private int nOccupied;
    private int mostRecentCellOccupied;

    /**
     * Creates a new, empty Board.
     */
    public Board() {
        cells = new int[9];
        winner = -1; // -1 indicates the game is still in play
        nOccupied = 0;
        mostRecentCellOccupied = -1; // -1 here indicates no moves have been made
    }

    /**
     * Tests if two Board instances are the same
     * (have the same markers in the same locations)
     *
     * @param o
     * @return true iff o is a Board with the same makers as this one
     */
    public boolean equals(Object o) {
        if (!(o instanceof Board)) return false;
        Board ob = (Board) o;

        for(int i = 0; i < cells.length; i++) {
            if (ob.cells[i] != cells[i]) return false;
        }
        return true;
    }

    /**
     *
     * @return a numeric value representing this board state
     */
    public int hashCode() {
        int h = 0;
        int col = 1;

        for(int i = 0; i < cells.length; i++) {
            h += col * cells[i];
            col *= 3;
        }
        return h;
    }

    /**
     * Gets the contents of a particular cell
     *
     * @param n the cell to get the contents of
     * @return 0 (empty), 1 (player one: X), 2 (player 2: O)
     */
    public int getCell(int n) {
        return cells[n];
    }

    /**
     * Apply a specific move to the board and produce a
     * new Board instance representing the result.
     *
     * The new Board instance also tracks what that last
     * move was (how it's related to it's parent Board);
     * this can be obtained via a call to getMostRecentCellOccupied()
     *
     * @param player the player requesting a move
     * @param cell the cell the player wants to move into
     * @return a new Board instance representing the game after the move is made
     */
    public Board move(int player, int cell) {
        // check if the cell is empty, otherwise, it's not legal
        if (cells[cell] != 0) {
            return null;
        }
        Board b = new Board();
        for(int i = 0, n = GRID_DIMENSION * GRID_DIMENSION; i < n; i++) {
            b.cells[i] = cells[i];
        }
        b.cells[cell] = player;
        b.nOccupied = nOccupied + 1;
        b.mostRecentCellOccupied = cell;

        if (colWin(b, cell) || rowWin(b, cell) || diagWin(b, cell)) {
            b.winner = player;
        }
        else if (b.nOccupied == GRID_DIMENSION * GRID_DIMENSION) {
            b.winner = 0;
        }
        return b;
    }

    /**
     * The winner is either:
     * -1 -- no winner (yet), game is still in play
     *  0 -- stalemate (no winner)
     *  1 -- player 1
     *  2 -- player 2
     *
     * @return the winner (based on the board's state)
     */
    public int getWinner() {
        return winner;
    }

    /**
     * If the Board instance was produced by a call to move()
     * then this method indicates where the most recent move was
     * made (and thus, also who made the last move).
     *
     * @return the cell most recently moved into
     */
    public int getMostRecentCellOccupied() {
        return mostRecentCellOccupied;
    }

    private boolean colWin(Board b, int cell) {
        int player = b.cells[cell];
        int cellCol = cell % GRID_DIMENSION;
        boolean isWin = true;
        for(int i = 0; i < GRID_DIMENSION; i++) {
            isWin = isWin && (b.cells[i * GRID_DIMENSION + cellCol] == player);
        }
        return isWin;
    }

    private boolean rowWin(Board b, int cell) {
        int player = b.cells[cell];
        int cellRow = cell / GRID_DIMENSION;
        boolean isWin = true;
        for(int i = 0; i < GRID_DIMENSION; i++) {
            isWin = isWin && (b.cells[cellRow * GRID_DIMENSION + i] == player);
        }
        return isWin;
    }

    private boolean diagWin(Board b, int cell) {
        // could do this faster for a 3x3 only board
        // but this allows some future potential expansion.
        int player = b.cells[cell];
        boolean isWin = true;
        for(int i = 0; i < GRID_DIMENSION; i++) {
            isWin = isWin && (b.cells[i * GRID_DIMENSION + i] == player);
        }
        if (!isWin) {
            isWin = true;
            for(int i = 0; i < GRID_DIMENSION; i++) {
                isWin = isWin && (b.cells[i * GRID_DIMENSION + (GRID_DIMENSION - 1 - i)] == player);
            }
        }
        return isWin;
    }

    /**
     *
     * @return A single line representation of the Board state
     */
    public String conciseString() {
        StringBuilder sb = new StringBuilder(20);
        for(int i : cells) {
            sb.append(i);
            sb.append(',');
        }
        sb.setLength(sb.length() - 1);
        sb.append(" -- ");
        sb.append(mostRecentCellOccupied);
        return sb.toString();
    }

    /**
     *
     * @return A human readable string representation of the Board
     */
    public String toString() {

        StringBuilder sb = new StringBuilder(20);
        sb.append(conciseString());
        sb.append('\n');
        for(int i = 0; i < GRID_DIMENSION; i++) {
            for(int j = 0; j < GRID_DIMENSION; j++) {
                sb.append(intToPlayerChar[cells[i * GRID_DIMENSION + j]]);
                sb.append(" | ");
            }
            sb.setLength(sb.length() - 3);
            sb.append("\n---------\n");
        }
        sb.setLength(sb.length() - 11); // remove trailing "\n---------\n"
        return sb.toString();
    }
}