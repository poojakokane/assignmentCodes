/**
 * Outcome: A helper class to maintain statistics about how individual moves
 * from a given board state lead to wins for either player 1 or player 2.
 */
public class Outcome {

        public int p1wins; // number of times p1 won
        public int p2wins; // number of times p2 won
        public int attempts; // number of games total (including ties)

        public String toString() { return "( " + p1wins + "," + p2wins + "," + attempts + " )"; }
}

