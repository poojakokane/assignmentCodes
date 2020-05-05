import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * An intelligent player for the game of TicTacToe.
 *
 */
public class AI extends Player {

    Map<Pair<Board, Integer>, Outcome> history = null;
    int ownPlayerNumber;
    int totalCells;
    private List<Pair<Board, Integer>> listOfAllMovesForBoardPositions;

    /**
     * _ Part 1: Implement this method _
     *
     * This method is called by the TicTacToe class when a new game begins.
     * Any work that is done to prepare for a new game should be done here.
     * However, you should *not* reset any outcome data that you have already
     * obtained.  
     * 
     * That is, outcome data should persists across multiple individual games
     * (otherwise your statistics will be really boring!), but there may
     * still be things you need to do when a new game starts. If so, do them 
     * here.
     *
     * @param dim the width (also height) of the board
     * @param playernum 1 if player 1; 2 if player 2
     * @param q true iff the player should be "quiet" during play (to minimize output)
     */
    public void startGame(int dim, int playernum, boolean q) {
        super.marker = playernum;
        if(history == null)
            history = new HashMap<Pair<Board, Integer>, Outcome>();

        totalCells = dim*dim;
        listOfAllMovesForBoardPositions = new ArrayList<Pair<Board, Integer>>();
        ownPlayerNumber = playernum;
        return;
    }

    /**
     * _ Part 2: Implement this method _
     *
     * This method is called by the TicTacToe class when the
     * player is being asked for a move.  The current Board
     * (state) is passed in, and the player should respond 
     * with an integer value corresponding to the cell it
     * would like to move into. (cell numbers start at 0
     * and increase from left to right and top to bottom).
     *
     * @param board the current board
     *
     * @return the cell the player would like to move into
     */
    public int requestMove(Board board){
        int blankCell = -1;
        int bestCellAsPerHistory = -1;
        int lastScore = Integer.MIN_VALUE;
        for(int i=0; i<totalCells; i++){
            if(board.getCell(i) == 0){
                blankCell = i;
            }
            Outcome o = getOutcomeForMove(board, i);
            if(o != null){
                int currentScore;
                if(ownPlayerNumber == 1)
                    currentScore = (o.p1wins - o.p2wins);// / o.attempts;
                else
                    currentScore = (o.p2wins - o.p1wins);// / o.attempts;

                if(currentScore > lastScore)
                {
                    lastScore = currentScore;
                    bestCellAsPerHistory = i;
                }
            }
        }

        if(bestCellAsPerHistory == -1) {
            listOfAllMovesForBoardPositions.add(new Pair<Board, Integer>(board, blankCell));
            return blankCell;
        }

        listOfAllMovesForBoardPositions.add(new Pair<Board, Integer>(board, bestCellAsPerHistory));
        return bestCellAsPerHistory;
    }


    /**
     *
     * _ Part 3: Implement this method _
     *
     * This method is called by the TicTacToe game when a 
     * game completes.  It passes in the final board state
     * (since this player may not know what it is, if the 
     * opponent moved last) and the winner.  Note that
     * the argument winner, is not necessarily consistent
     * with the winner obtianed via b.getWinner() because
     * it is possible that the winner was declared by
     * disqualification. In that case, the TicTacToe game 
     * declares the winner (passed here as the argument winner)
     * but b.getWinner() will return -1 (since the game appears
     * incomplete).
     * 
     * Any work to compute outcomes should probably happen here
     * once you know how the game ended.
     *
     * @param b
     * @param winner
     */
    public void endGame(Board b, int winner) {
        int lastMove = b.getMostRecentCellOccupied();
        listOfAllMovesForBoardPositions.add(new Pair<Board, Integer>(b, lastMove));

        for(Pair<Board, Integer>x : listOfAllMovesForBoardPositions){
            Outcome o = getOutcomeForMove(x.getKey(), x.getValue());
            if(o == null){
                o = new Outcome();
            }

            o.attempts++;
            if(b.getWinner() == 1){
                o.p1wins++;
            }
            else if(b.getWinner() == 2){
                o.p2wins++;
            }

            history.put(x, o);
        }

        return;
    }

    /**
     * _ Part 4: Implement this method _
     *
     * Retrieve an outcome for a particular move from a particular board
     * state. If that board/move combination was never encountered, return
     * null.

     * @param state
     * @param move
     * @return 
     */
    public Outcome getOutcomeForMove(Board state, int move) {
        if(history.containsKey(new Pair<Board, Integer>(state, move))){
            return history.get(new Pair<Board, Integer>(state, move));
        }
        
       return null;
    }


}
