package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The Popup class represents a simple pop-up dialog with a label and an OK button.
 * It can be used to display messages to the user.
 */
public class Popup extends JDialog {
	/** The label displaying the message in the pop-up. */
    JLabel label = new JLabel();

    /** The OK button to close the pop-up. */
    JButton button = new JButton("OK");

    /** The panel containing the label in the pop-up. */
    JPanel labelPanel = new JPanel();

    /** The panel containing the OK button in the pop-up. */
    JPanel buttonPanel = new JPanel();

    /**
     * Constructs a Popup with the specified parent frame, name, and message.
     *
     * @param parent  The parent JFrame to which the pop-up is associated.
     * @param name    The name or title of the pop-up.
     * @param message The message to be displayed in the pop-up.
     */
	public Popup(JFrame parent, String name, String message) {
		super(parent, name, true);
		label.setText(message);
		// Set size and other properties based on the pop-up name
		if(name.equals("Shift screen")) {
			setSize(parent.getSize());
			setUndecorated(true);
		} else setSize(250, 100);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// Add label to the center of the pop-up
		labelPanel.add(label);
		add(labelPanel, BorderLayout.CENTER);
		// Add OK button to the bottom of the pop-up
		button.addActionListener(new OkButtonListener());
		buttonPanel.add(button);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
     * ActionListener implementation for the OK button. Closes the pop-up when the button is clicked.
     */
	private class OkButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}