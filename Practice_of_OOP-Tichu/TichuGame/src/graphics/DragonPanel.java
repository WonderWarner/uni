package graphics;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The DragonPanel class represents a panel used for the dragon round,
 * allowing the user to select an opponent to give the round's cards.
 */
public class DragonPanel extends JPanel {

    /** Array of subpanels for organizing components. */
    JPanel[] dragonPanelParts = new JPanel[3];

    /** Label indicating the purpose of the panel. */
    JLabel whichOpponent = new JLabel("Which opponent do you want to give this round's cards?");

    /** Array of opponent names for the JComboBox. */
    String[] opponentNames = new String[2];

    /** JComboBox for selecting the opponent. */
    JComboBox<String> opponent;

    /** Button to initiate giving the dragon cards. */
    JButton giveDragon = new JButton("Give");

    /**
     * Constructs a DragonPanel with default settings.
     * Initializes and adds components for selecting an opponent to give dragon cards.
     */
    public DragonPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (int i = 0; i < dragonPanelParts.length; i++) {
            dragonPanelParts[i] = new JPanel();
            add(dragonPanelParts[i]);
        }

        dragonPanelParts[0].add(whichOpponent);

        // Initialize JComboBox with opponent names
        opponent = new JComboBox<>(opponentNames);
        dragonPanelParts[1].add(opponent);

        dragonPanelParts[2].add(giveDragon);
    }
}
