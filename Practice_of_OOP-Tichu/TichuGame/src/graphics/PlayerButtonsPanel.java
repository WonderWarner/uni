package graphics;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The PlayerButtonsPanel class represents a panel containing player action buttons,
 * such as the "I say Tichu!" and "OK" buttons during the game.
 */
public class PlayerButtonsPanel extends JPanel {
	/** The button for a player to declare "I say Tichu!" during the game. */
    JButton tichu = new JButton("I say Tichu!");

    /** The OK button for a player to confirm their action during the game. */
    JButton ok = new JButton("OK");

    /**
     * Constructs a PlayerButtonsPanel with default settings.
     * Initializes and adds the "I say Tichu!" and "OK" buttons to the panel.
     */public PlayerButtonsPanel() {
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		add(tichu);
		add(ok);
	}
}
