import java.util.Random;

/**
 * The basic Player class
 *
 * Performs a sad, sad strategy for playing ShiftTacToe.
 * In the standard 3x3x1 game, the player will drop
 * dropping in each column and left shifting each row.
 * These moves order ordered from (0...5) and tried in
 * sequence on successive moves (on the player's first turn
 * they drop in row 0, on the second, they drop in row 1, etc)
 * Once the moves are exhausted, they are repeated until the
 * game eventually ends.
 *
 * It is possible for this player to make an infinite number of
 * illegal moves (if the board is full and all rows are fully left
 * shifted)
 */
public class Player {
    int dimension;
    int marker;
    boolean quiet;
    Random rand;
    int strategy; // unused by this class, but perhaps used by child types

    public Player() {
        this(0);
    }

    public Player(int strategy) {
        this.strategy = strategy;
    }

    /**
     * Indicates that the game is about to start (so the Player can reset it self).
     *
     * @param dim the width (also height) of the board
     * @param playernum 1 if player 1; 2 if player 2
     * @param q true iff the player should be "quiet" during play (to minimize output)
     */
    public void startGame(int dim, int playernum, boolean q) {
        dimension = dim;
        marker = playernum;
        quiet = q;

        rand = new Random();

    }

    public void endGame(Board b, int winner) {
        // game over
        if (!quiet) {
            if (b.getWinner() == 0) {
                System.out.println("a tie!");
            }
        }
    }

    /***
     * Request a Move from the played
     *
     * @param board the current board
     *
     * @return A move to be made by this player
     */
    public int requestMove(Board board) {
        // pick a random cell.

        // count empty cells:
        int empty = 0;
        for(int i = 0, n = Board.GRID_DIMENSION * Board.GRID_DIMENSION; i < n; i++) {
            if (board.getCell(i) == 0) empty++;
        }
        // pick a random number in range 0...(empty-1) inclusive
        int mymove = rand.nextInt(empty);
        // now, find the (mymove)th empty cell
        int i = 0;
        while(mymove >= 0) {
            if (board.getCell(i) == 0) mymove--;
            i++;
        }
        return i-1;
    }

    public int getMarker() {
        return marker;
    }
    public String getName() {
        return "Player " + marker + " [" + this.getClass() + "]";
    }
}

