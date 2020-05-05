import java.util.Scanner;

/**
 * Takes commands from the keyboard.
 *
 * The Move class is responsible for turning
 * a string command into a Move object instance.
 */
public class HumanPlayer extends Player {


    public int requestMove(Board board) {
        Scanner userInput = new Scanner(System.in);
        String response;

        // userInput.hasNext() will return true until
        // you type 'Control-D' on a mac, or Control-Z on
        // a windows machine.  if that happens, we need
        // to break out of the loop and stop.
        System.out.print("Move? (0 is upper left corner)");
        while(userInput.hasNextLine()) {
            response = userInput.nextLine();
            try {
                int i = Integer.parseInt(response);
                return i;
            } catch (NumberFormatException nfe) {}
            System.out.print("Try again...Move?");
        }
        // we should never get here...
        return 0;
    }

}
