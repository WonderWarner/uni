package graphics;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The EndGamePanel class represents a panel to display information at the end of a game,
 * including the results of each team, options for continuing or exiting the game, and a menu option.
 */
public class EndGamePanel extends JPanel {

    /** The subpanel for labels displaying game results. */
    JPanel labelPanel = new JPanel();

    /** The subpanel for buttons providing options at the end of the game. */
    JPanel buttonPanel = new JPanel();

    /** Button to continue the game (visible if the goal points are not reached). */
    JButton continueGame = new JButton("Continue");

    /** Button to save and exit the game (visible if the goal points are not reached). */
    JButton saveAndExit = new JButton("Save and Exit");

    /** Button to exit the game. */
    JButton exit = new JButton("Exit");

    /** Button to go back to the main menu (visible if the goal points are reached). */
    JButton menu = new JButton("Back to Menu");

    /** Label displaying information about the game ending and options. */
    JLabel endLabel = new JLabel("This game ended, choose what's next!");

    /** Label displaying the result for Team 1. */
    JLabel team1 = new JLabel("T1 Result");

    /** Label displaying the result for Team 2. */
    JLabel team2 = new JLabel("T2 Result");

    /**
     * Constructs an EndGamePanel with default settings.
     * Initializes and adds components for displaying game results and options.
     */
    public EndGamePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        labelPanel.add(endLabel);
        labelPanel.add(team1);
        labelPanel.add(team2);

        buttonPanel.add(continueGame);
        buttonPanel.add(saveAndExit);
        buttonPanel.add(exit);
        buttonPanel.add(menu);

        add(labelPanel);
        add(buttonPanel);
    }

    /**
     * Sets the view of the EndGamePanel based on the game results and player names.
     *
     * @param points An array containing the points of each team.
     * @param names  An array containing the names of the players.
     */
    void setView(int[] points, String[] names) {
        team1.setText("Team one (" + names[0] + ", " + names[2] + "): " + points[0] + " points");
        team2.setText("Team two (" + names[1] + ", " + names[3] + "): " + points[1] + " points");

        // If either team reached the goal points, adjust button visibility
        if (points[0] >= 1000 || points[1] >= 1000) {
            continueGame.setVisible(false);
            saveAndExit.setVisible(false);
            menu.setVisible(true);
        } else {
            continueGame.setVisible(true);
            saveAndExit.setVisible(true);
            menu.setVisible(false);
        }
    }
}

