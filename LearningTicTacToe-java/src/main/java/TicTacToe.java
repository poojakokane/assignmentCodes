/**
 * Board/State representation for the game of Shift Tac Toe.
 *
 */
public class TicTacToe {

    // Consider adding some more instance variables to help represent the board.
    // Although you do need to be able to represent the board as a String,
    // it is probably a lot easier to do board operations (drop, shift, etc)
    // if you have a different representation internally (e.g., arrays?)
    // and then generate Strings from those. 

    // provide a short (~1 line) description of the role each variable have.


    /**
     * Play a game of Tic Tac Toe!
     *
     * @param p1 - Player 1
     * @param p2 - Player 2
     */
    public int play(Player p1, Player p2, int maxbadmoves, boolean quietly) {
        Board board = new Board();
        Board nextBoard;
        Player currentPlayer = p1;
        int badmoves = 0;
        int cell;

        // inform the player's about the board size, and what player they are
        p1.startGame(Board.GRID_DIMENSION, 1, quietly);
        p2.startGame(Board.GRID_DIMENSION, 2, quietly);

    	while( board.getWinner() == -1 ) {
            // if we're not in quiet mode, print out the board and the player's name
            if (!quietly) {
                System.out.println(board.toString());
                System.out.println(currentPlayer.getName() + "'s turn...");
            }
            // ask the current player for a turn, until they give a legal move
            badmoves = 0;
            do {
                cell = currentPlayer.requestMove(board);
                nextBoard = board.move(currentPlayer.getMarker(), cell);
            } while (nextBoard == null && ++badmoves <= maxbadmoves);
            board = nextBoard;
            if (currentPlayer == p1) {
                currentPlayer = p2;
            }
            else {
                currentPlayer = p1;
            }

            // if the last player made too many illegal moves, they loose.
            if (badmoves > maxbadmoves) {
                p1.endGame(board, currentPlayer.getMarker());
                p2.endGame(board, currentPlayer.getMarker());
                if (!quietly) System.out.println("* Bad moves exceeded! Winner: " + currentPlayer.getName());
                return currentPlayer.getMarker();
            }

        }
    	if (!quietly) {
            switch (board.getWinner()) {
                case 0:
                    System.out.println("* Winner: no one! (it's a tie!)");
                    break;
                case 1:
                    System.out.println("* Winner: " + p1.getName());
                    break;
                case 2:
                    System.out.println("* Winner: " + p2.getName());
                    break;
            }
        }
        p1.endGame(board, board.getWinner());
        p2.endGame(board, board.getWinner());
    	return board.getWinner();
    }


    public static void main(String[] args) {
        Player a = new AI();
//        Player a = new Player();
        Player b = new Player();
        TicTacToe stt = new TicTacToe();
        Player winningPlayer;

        int rounds = 100000; // to gather useful data for learning, try 1000 or 10000
        int i = 1;
        int reportInterval = 100;
        int aWins = 0;
        int bWins = 0;
        boolean quiet = true;
        boolean beQuietNow;

        while (i <= rounds) {
            beQuietNow = (i % reportInterval != 0) && quiet;

            if (!beQuietNow)
                System.out.println(" ----------------- Game (rounds)" + i + "/" + rounds + "-----------------");

            int winner = stt.play(a, b, 20, quiet);
            if (winner == 1) aWins++;
            else if (winner == 2) bWins++;


            if (!beQuietNow)
                System.out.println(" -------------------------------------------------");

            winner = stt.play(b, a, 20, quiet);
            if (winner == 1) bWins ++;
            else if (winner == 2) aWins++;

            if (!beQuietNow)
                System.out.println(i*2 + " games played: wins for (" + a.getName() + "," + b.getName() + ") = " + aWins + "," + bWins);

            i++;
        }
        System.out.println(i*2 + " games played: wins for (" + a.getName() + "," + b.getName() + ") = " + aWins + "," + bWins);

    }

}
