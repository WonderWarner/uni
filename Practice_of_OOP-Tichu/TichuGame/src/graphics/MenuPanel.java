package graphics;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The MenuPanel class represents a panel containing buttons for the starting menu options.
 * It includes buttons for starting a new game, loading a game, viewing statistics, and exiting the game.
 */
public class MenuPanel extends JPanel {

	/** Sub-panels to make layout in main panel. */
	JPanel[] menuButtonPanels=new JPanel[4];
	
    /** The button to start a new game. */
    JButton newButton = new JButton("NEW GAME");

    /** The button to load a saved game. */
    JButton loadButton = new JButton("LOAD GAME");

    /** The button to view game statistics. */
    JButton statButton = new JButton("STATISTICS");

    /** The button to exit the game. */
    JButton exitButton = new JButton("EXIT GAME");

    /**
     * Constructs a MenuPanel with default settings.
     * Initializes and adds buttons for new game, load game, statistics, and exit.
     */
	public MenuPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for(int i=0; i<menuButtonPanels.length; i++) {
			menuButtonPanels[i]=new JPanel();
			add(menuButtonPanels[i]);
		}
		menuButtonPanels[0].add(newButton);
		menuButtonPanels[1].add(loadButton);
		menuButtonPanels[2].add(statButton);
		menuButtonPanels[3].add(exitButton);
	}
}
