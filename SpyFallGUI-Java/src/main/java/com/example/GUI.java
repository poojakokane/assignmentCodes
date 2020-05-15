package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GUI extends JFrame {

    private Spyfall gameInstance;
    private ArrayList<Player> playerList;

    JPanel playerArea;


    /**
     * the constructor that creates the size of the initial JFrame. Remember since GUI is extending JFrame, to call JFrame methods
     * you call them without any .
     * This constructor also initializes the Spyfall gameInstance and the playerList ArrayList.
     * the Last calls of the constructor are createContents() to fill the JFrame, and after thats all done,
     * to set it to visible.
     */
    public GUI() {
        super("SpyFall!");
        gameInstance = new Spyfall();
        playerList = new ArrayList<Player>();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        createContents();
        setVisible(true);
    }


    /**
     * This method is where the bulk of the work is done.
     * This GUI is divided into the 3 main Panels:
     * a playerEntry panel at the top,
     * a playerArea panel in the middle,
     * and an outputArea in the bottom.
     */
    public void createContents() {
        //TODO declare and instantiate your new button.
        // add an appropriate action listener
        // set the initial visibility of the button
        // add it to the outputArea
        // update the other listeners' actions to hide/show your new button as appropriate

        // creating the playerEntry panel. it has 2 JComponents, nameField, and addPlayerButton.
        JPanel playerEntry = new JPanel();
        playerEntry.setPreferredSize(new Dimension(800, 50));

        //input for a player name
        final JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(100, 30));


        // button to add a player
        JButton addPlayerButton = new JButton("add player");
        addPlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                //should check to see if the nameField is empty. If it isn't create a player, and attempt to add it to
                //the playerList and the playerArea panel by calling addPlayerPanel. It will not add a player if there are already 6 players.
                if (nameField.getText().equals(""))
                    return;
                Player playerToAdd = new Player(nameField.getText());
                if(playerList.contains(playerToAdd))
                    return;

                if(playerList.size()<6) {
                    playerList.add(playerToAdd);
                    addplayerPanel(playerToAdd);
                }

            }
        });

        //adding both at the end.
        playerEntry.add(nameField);
        playerEntry.add(addPlayerButton);



        // creating the playerArea panel. This area will be added to during runtime, whenever addPlayerButton is clicked.
        //This is also why it needs to be a global variable.
        playerArea = new JPanel();
        playerArea.setSize(300, 600);
        playerArea.setLayout(new BoxLayout(playerArea, BoxLayout.PAGE_AXIS));


        // creating the outputArea panel. This panel has 4 Components. A startGameButton, a guessField a guessButton and a outputLabel.
        //guessButton and guessField start out "invisible" and only reappear after the game has started.
        JPanel outputArea = new JPanel();
        outputArea.setPreferredSize(new Dimension(800, 100));

        final JButton startGameButton = new JButton("start game");
        final JButton reassignRoles = new JButton("reassign roles");


        final JLabel outputLabel = new JLabel();

        final JTextField guessField = new JTextField();
        guessField.setPreferredSize(new Dimension(100, 30));


        //button to guess who the spy is.
        final JButton guessButton = new JButton("guess the spy");
        guessButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                outputLabel.setText(gameInstance.checkIfSpy(new Player(guessField.getText())));
                guessButton.setVisible(false);
                guessField.setVisible(false);
                startGameButton.setVisible(true);
                gameInstance.resetGame();
                reassignRoles.setVisible(false);
                updatePlayers();
            }
        });



        // button to start the game
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                //add the functionality to the click event of the startGameButton. Remember that it should start the game,
                //as well as show the guessButton and guessField. You should no longer be able to see the startGameButton.
                //it should also clear the outputLabel of text
                gameInstance.startGame(playerList);
                outputLabel.setText("");
                guessButton.setVisible(true);
                guessField.setVisible(true);
                reassignRoles.setVisible(true);
                startGameButton.setVisible(false);
                updatePlayers();
            }
        });

        //button to guess who the spy is.
        reassignRoles.setVisible(false);
        reassignRoles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                gameInstance.clearPlayerMap();
                gameInstance.startGame(playerList);
                outputLabel.setText("");
                guessButton.setVisible(true);
                guessField.setVisible(true);
                reassignRoles.setVisible(true);
                startGameButton.setVisible(false);
                updatePlayers();
            }
        });


        guessField.setVisible(false);
        guessButton.setVisible(false);

        outputArea.add(reassignRoles);
        outputArea.add(startGameButton);
        outputArea.add(guessButton);
        outputArea.add(guessField);
        outputArea.add(outputLabel);

        //at the very end, add these three panels to the GUI
        add(playerEntry, BorderLayout.NORTH);
        add(playerArea, BorderLayout.CENTER);
        add(outputArea, BorderLayout.SOUTH);
    }


    /**
     * This method creates a new player JPanel inside the player area. This panel should contain a JLabel using the Player's toString method,
     * a Button called roleButton, and a JLabel called roleLabel that displays this players role (if they have one).
     * @param player to be added
     */
    public void addplayerPanel(final Player player) {
        final JPanel output = new JPanel();
        final JLabel playerLabel = new JLabel(player.toString());

        final JLabel roleLabel = new JLabel("no role yet");
        roleLabel.setVisible(false);

        final JButton roleButton = new JButton("click to reveal role");


        roleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if (roleLabel.isVisible()) {
                    roleLabel.setVisible(false);
                    roleButton.setText("click to reveal role");
                } else {
                    roleLabel.setVisible(true);
                    roleButton.setText("click to hide role");
                }
                roleLabel.setText(gameInstance.getPlayersRole(player));
                playerLabel.setText(player.toString());
                output.revalidate();
            }
        });

        output.add(playerLabel);
        output.add(roleButton);
        output.add(roleLabel);

        output.setBorder(BorderFactory.createLineBorder(Color.green));

        playerArea.add(output);
        playerArea.revalidate();
    }




    /**
     * The main method that simply creates a new GUI
     * @param args
     */
    public static void main(String args[]) {
        new GUI();
    }

    /**
     * method used to update labels when the players or their roles may have changed
     */
    private void updatePlayers() {
        for(int i=0; i<playerList.size(); i++) {
            JPanel panel = (JPanel) (playerArea.getComponents()[i]);
            JLabel playerLabel = (JLabel) (panel.getComponents()[0]);
            playerLabel.setText(playerList.get(i).toString());
            JLabel roleLabel = (JLabel) (panel.getComponents()[2]);
            roleLabel.setText(gameInstance.getPlayersRole(playerList.get(i)));
        }
    }

}
