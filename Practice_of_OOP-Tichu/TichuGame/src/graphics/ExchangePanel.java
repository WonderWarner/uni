package graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The ExchangePanel class represents a panel for managing the card exchanging process,
 * excluding the player cards. It includes components for players to select cards for exchange,
 * labels indicating the card exchange instructions, and a button to perform the exchange.
 */
public class ExchangePanel extends JPanel {

    /** The subpanels for different parts of the exchange process. */
    JPanel[] exchangePanelParts = new JPanel[3];

    /** The subpanels containing components for each card exchange box. */
    JPanel[] panelForExchangeBox = new JPanel[3];

    /** Labels indicating the instructions for each card exchange. */
    JLabel[] giveCardLabel = new JLabel[3];

    /** Array of integers representing the indices of cards available for exchange. */
    Integer[] exchangeIdxs = new Integer[14];

    /** List of ComboBoxes for each player to select cards for exchange. */
    ArrayList<JComboBox<Integer>> giveCardOptions = new ArrayList<>();

    /** The button to execute the card exchange process. */
    JButton exchangeButton = new JButton("EXCHANGE");

    /** Array to store the indices of cards selected for exchange by each player. */
    int[] exchangesMade = new int[4 * 3];

    /** Counter to track the number of exchanges made. */
    int exchangeCnt = 0;

    /**
     * Constructs an ExchangePanel with default settings.
     * Initializes and adds components for managing the card exchange process.
     */
    public ExchangePanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // Initialize the array of indices for card exchange
        for (int i = 0; i < 14; i++) {
            exchangeIdxs[i] = i + 1;
        }

        // Initialize components for each player's card exchange
        for (int i = 0; i < 3; i++) {
            exchangePanelParts[i] = new JPanel();
            exchangePanelParts[i].setLayout(new BoxLayout(exchangePanelParts[i], BoxLayout.Y_AXIS));

            panelForExchangeBox[i] = new JPanel();
            giveCardLabel[i] = new JLabel();
            exchangePanelParts[i].add(giveCardLabel[i]);

            giveCardOptions.add(new JComboBox<>(exchangeIdxs));
            giveCardOptions.get(i).addActionListener(new ExchangeComboBoxListener());

            panelForExchangeBox[i].add(giveCardOptions.get(i));
            exchangePanelParts[i].add(panelForExchangeBox[i]);

            add(exchangePanelParts[i]);
        }

        add(exchangeButton);
    }

    /**
     * ActionListener implementation for the ComboBoxes in the exchange panel.
     * Checks if the selected cards for exchange are valid and enables the exchange button accordingly.
     */
    private class ExchangeComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIdxs[] = new int[3];
            for (int i = 0; i < selectedIdxs.length; i++) {
                selectedIdxs[i] = (int) giveCardOptions.get(i).getSelectedItem();
            }

            // Check if any selected cards are the same
            if (selectedIdxs[0] == selectedIdxs[1] || selectedIdxs[0] == selectedIdxs[2] || selectedIdxs[1] == selectedIdxs[2]) {
                exchangeButton.setEnabled(false);
            } else {
                exchangeButton.setEnabled(true);
            }
        }
    }
}