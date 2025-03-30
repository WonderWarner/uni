package graphics;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The GrandTichuPanel class represents a panel for managing the Grand Tichu round components,
 * excluding the player cards. It includes a label indicating the current state of the Grand Tichu round,
 * buttons for players to respond with "YES" or "NO," and associated variables to track player responses.
 */
public class GrandTichuPanel extends JPanel {

    /** The panel containing the label for the Grand Tichu round. */
    JPanel grandTichuLabelPanel = new JPanel();

    /** The label displaying information about the Grand Tichu round. */
    JLabel grandTichuLabel = new JLabel();

    /** The panel containing "YES" and "NO" buttons for player responses. */
    JPanel YNPanel = new JPanel();

    /** The button for players to respond with "YES" to the Grand Tichu round. */
    JButton yes = new JButton("YES");

    /** The button for players to respond with "NO" to the Grand Tichu round. */
    JButton no = new JButton("NO");

    /** An array indicating whether each player has said Grand Tichu. */
    boolean[] saidGrandTichu = new boolean[4];

    /** The count of players who have said Grand Tichu. */
    int grandTichuCnt = 0;

    /**
     * Constructs a GrandTichuPanel with default settings.
     * Initializes and adds components for managing the Grand Tichu round.
     */
    public GrandTichuPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        grandTichuLabelPanel.add(grandTichuLabel);
        add(grandTichuLabelPanel);

        YNPanel.setLayout(new BoxLayout(YNPanel, BoxLayout.X_AXIS));
        YNPanel.add(yes);
        YNPanel.add(no);
        add(YNPanel);
    }
}
