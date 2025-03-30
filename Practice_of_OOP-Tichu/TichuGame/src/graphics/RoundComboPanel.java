package graphics;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The RoundComboPanel class represents a graphical panel displaying information about the current
 * round's combination, including type, value, player, number of passes, and whether a wish is in play.
 */
public class RoundComboPanel extends JPanel{

	/** The label indicating the type of combination in play. */
    JLabel comboType = new JLabel("Type: ");

    /** The label indicating the value of the combination in play. */
    JLabel comboValue = new JLabel("Value: ");

    /** The label indicating the player who played the combination. */
    JLabel comboPlayer = new JLabel("Player: ");

    /** The label indicating the number of passes in the round. */
    JLabel passCount = new JLabel("Passes: ");

    /** The label indicating whether a wish is in play. */
    JLabel isWishInPlay = new JLabel("Wish: ");
	
    /**
     * Constructs a RoundComboPanel to display information about the current round's combination.
     */
	public RoundComboPanel() {
		setLayout(new FlowLayout(FlowLayout.CENTER));
		add(comboType);
		add(comboValue);
		add(comboPlayer);
		add(passCount);
		add(isWishInPlay);
	}
	
	/**
     * Updates the RoundComboPanel based on the game state and round information.
     *
     * @param ints    An array of integers representing various game state information.
     * @param strings An array of strings representing player names and roundCombo data.
     */
	void gameViewForRoundCombo(int[] ints, String[] strings) {
		// Show or hide the comboType label based on the type of combination in play
		if(strings[3].equals("None")) {
			comboType.setVisible(false);
		} else {
			comboType.setVisible(true);
			comboType.setText("Type: "+strings[3]);
		}
		// Show or hide the comboValue label based on the value of the combination in play
		if(ints[11]==-10) {
			comboValue.setVisible(false);
		} else {
			comboValue.setVisible(true);
			comboValue.setText("Value: "+ints[11]);
		}
		// Show or hide the comboPlayer and passCount labels based on the player who played the combination
		if(strings[4]==null) {
			comboPlayer.setVisible(false);
			passCount.setVisible(false);
		} else {
			comboPlayer.setVisible(true);
			comboPlayer.setText("Player: "+strings[4]);
			passCount.setVisible(true);
			passCount.setText("Passes: "+ints[10]);
		}
		// Show or hide the isWishInPlay label based on whether a wish is in play
		if(ints[12]==0) {
			isWishInPlay.setVisible(false);
		}
		else {
			isWishInPlay.setVisible(true);
			isWishInPlay.setText("Wish: "+ints[12]);
		}
	}
}
