package graphics;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Represents a panel used for handling player wishes in the Tichu game.
 * If a Sparrow card is played, this panel allows the player to choose whether to make a wish
 * and, if yes, to specify the desired card value for their wish.
 */
public class WishPanel extends JPanel {
	/** Label prompting the player to choose if they want to wish for a number. */
	JLabel wishAsk=new JLabel("Choose if you want to wish for a number:");
	/** Array of wish options, including no wish ('-'), numbers 2-10, and face cards (J, Q, K, A). */
	Object[] wishOptions=new Object[14];
	/** ComboBox for selecting the value to wish for. */
	JComboBox<Object> wishComboBox;
	/** Button for confirming and submitting the wish. */
	JButton wishButton=new JButton("Wish");
    /** Array of sub-panels to organize components within the main panel. */
	JPanel[] wishPanelParts=new JPanel[3];
	
	/**
     * Constructs a new WishPanel.
     * Initializes the wish options, sets up the layout, and adds components to the panel.
     */
	public WishPanel() {
		wishOptions[0]="-";
		for(int i=1; i<10; i++) {
			wishOptions[i]=Integer.toString(i+1);
		}
		wishOptions[10]="J";
		wishOptions[11]="Q";
		wishOptions[12]="K";
		wishOptions[13]="A";
		wishComboBox=new JComboBox<>(wishOptions);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for(int i=0; i<wishPanelParts.length; i++) {
			wishPanelParts[i]=new JPanel();
			add(wishPanelParts[i]);
		}
		wishPanelParts[0].add(wishAsk);
		wishPanelParts[1].add(wishComboBox);
		wishPanelParts[2].add(wishButton);
	}
}
