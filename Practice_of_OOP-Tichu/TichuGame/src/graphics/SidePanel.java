package graphics;

import java.io.File;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The SidePanel class represents a graphical panel displaying information about an individual player
 * on the side of the main game screen. It includes the player's name, Tichu declaration, placement,
 * and a representation of the number of cards they have in hand.
 */
public class SidePanel extends JPanel{
	
	/** The panel containing graphical representations of the cards in the player's hand. */
    JPanel sideCardPanel = new JPanel();

    /** The label displaying the player's name. */
    JLabel sidePlayerName = new JLabel("Player");

    /** The label indicating whether the player declared Tichu. */
    JLabel sidePlayerTichu = new JLabel("Tichu?");

    /** The label displaying the player's placement. */
    JLabel sidePlayerPlacement = new JLabel("Placement: ");

    /** An array list of CardComponents representing the back of cards, indicating the number of cards in hand. */
    ArrayList<CardComponent> backCards = new ArrayList<>();
	
    /**
     * Constructs a SidePanel for a player.
     *
     * @param order The order of the side panel (1 for right, 2 for top, 3 for left).
     */
	public SidePanel(int order) {		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// Set layout based on the order
		if(order==1) {
			sideCardPanel.setLayout(new BoxLayout(sideCardPanel, BoxLayout.X_AXIS));
		} else {
			sideCardPanel.setLayout(new BoxLayout(sideCardPanel, BoxLayout.Y_AXIS));
		}
		// Initialize backCards with CardComponents representing the back of cards
		for(int i=0; i<14; i++) {
			if(order==1) backCards.add(new CardComponent("CardImage"+File.separator+"backlr.png"));
			else backCards.add(new CardComponent("CardImage"+File.separator+"backud.png"));
			sideCardPanel.add(backCards.get(i));
			backCards.get(i).setEnabled(false);
		}
		// Add components to the SidePanel
		add(sidePlayerName);
		add(sidePlayerTichu);
		add(sidePlayerPlacement);
		add(sideCardPanel);
	}
	
	/**
     * Updates the SidePanel based on the game state and player information.
     *
     * @param i       The index representing the player (0 for current player, 1 for right, 2 for top, 3 for left).
     * @param ints    An array of integers representing various game state information.
     * @param strings An array of strings representing player names and roundCombo data.
     */
	void gameViewForSidePanel(int i, int[] ints, String[] strings) {
		// Hide or show the sideCardPanel based on the number of cards in hand
		if(ints[i+1]==0) {
			sideCardPanel.setVisible(false);
		} else {
			for(int j=0; j<backCards.size(); j++) {
				if(j<ints[i+1]) {
					backCards.get(j).setVisible(true);
				} else {
					backCards.get(j).setVisible(false);
				}
			}
			sideCardPanel.setSize(sideCardPanel.getPreferredSize());
			sideCardPanel.setVisible(true);
		}
		// Update player information labels
		sidePlayerName.setText(strings[i]);
		if(ints[i+4]==2) {
			sidePlayerTichu.setVisible(true);
			sidePlayerTichu.setText("said grand Tichu");
		} else if(ints[i+4]==1) {
			sidePlayerTichu.setVisible(true);
			sidePlayerTichu.setText("said Tichu");
		} else {
			sidePlayerTichu.setVisible(false);
		}
		if(ints[i+7]!=0) {
			sidePlayerPlacement.setVisible(true);
			sidePlayerPlacement.setText("Placement: "+ints[i+7]);
		} else {
			sidePlayerPlacement.setVisible(false);
		}
	}
}
