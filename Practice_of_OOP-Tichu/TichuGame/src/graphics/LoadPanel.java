package graphics;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * The LoadPanel class represents a panel for loading a game, where players enter their names.
 * It allows players to enter their names, choose their teams, and load a game when ready.
 * The panel includes text fields, combo box, and a load button.
 */
public class LoadPanel extends JPanel {

    /** The panels for organizing components. */
    JPanel[] namePanels = new JPanel[4];

    /** The label indicating the purpose of the panel. */
    JLabel loadTitle = new JLabel("Give your names that you played with, in the good order:");

    /** The text representing player names. */
    Object[] playerComboBoxText = new Object[4];

    /** The combo box for selecting players. */
    JComboBox<Object> cardLayoutOptions;

    /** The panel for managing player names. */
    JPanel playerNameCardLayout = new JPanel();

    /** The panels containing text fields for player names. */
    JPanel[] panelsForTextField = new JPanel[4];

    /** The text fields for entering player names. */
    JTextField[] playerNames = new JTextField[4];

    /** The array storing player names. */
    String[] playerNamesArray = new String[4];

    /** The array indicating whether player names are valid. */
    boolean[] playerNamesRight = new boolean[4];

    /** The button to load the game. */
    JButton loadButton = new JButton("Load Game");

    /**
     * Constructs a LoadPanel with default settings.
     * Initializes and adds components for entering player names, selecting teams, and loading the game.
     */
    public LoadPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        playerNameCardLayout.setLayout(new CardLayout());

        for (int i = 0; i < namePanels.length; i++) {
            namePanels[i] = new JPanel();
            add(namePanels[i]);
        }

        namePanels[0].add(loadTitle);

        for (int i = 0; i < 4; i++) {
            playerNamesArray[i] = "";
            playerNamesRight[i] = false;
            playerComboBoxText[i] = "Player" + (i + 1) + " (Team" + (i % 2 + 1) + ")";
            playerNames[i] = new JTextField(20);
            playerNames[i].getDocument().addDocumentListener(new PlayerNameTextFieldListener());
            panelsForTextField[i] = new JPanel();
            panelsForTextField[i].add(playerNames[i]);
            playerNameCardLayout.add("Player" + (i + 1) + " (Team" + (i % 2 + 1) + ")", panelsForTextField[i]);
        }

        cardLayoutOptions = new JComboBox<>(playerComboBoxText);
        cardLayoutOptions.addActionListener(new PlayerComboBoxListener());
        namePanels[1].add(cardLayoutOptions);
        namePanels[2].add(playerNameCardLayout);
        loadButton.setEnabled(false);
        namePanels[3].add(loadButton);
    }

    /**
     * ActionListener for handling player combo box selection changes.
     * Updates the displayed player name text field based on the selected player.
     */
    private class PlayerComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedPlayer = (String) cardLayoutOptions.getSelectedItem();
            int idx = selectedPlayer.charAt(6) - '0' - 1;
            CardLayout cardLayout = (CardLayout) playerNameCardLayout.getLayout();
            cardLayout.show(playerNameCardLayout, selectedPlayer);
            if (!playerNamesArray[idx].isEmpty()) {
                playerNames[idx].setText(playerNamesArray[idx]);
            } else playerNames[idx].setText("");
        }
    }

    /**
     * DocumentListener to update player names every time a JTextField changes.
     */
    private class PlayerNameTextFieldListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updatePlayerName();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updatePlayerName();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updatePlayerName();
        }
    }

    /**
     * Updates player names after every change and checks if the name is correct.
     * If every name is correct, players can load a new game.
     * A valid name consists of 1-10 characters AND is different from every name AND
     * every character must be one from the following: A-Z, a-z, 0-9, '_', '-'.
     */
    private void updatePlayerName() {
        String selectedPlayer = (String) cardLayoutOptions.getSelectedItem();
        int idx = selectedPlayer.charAt(6) - '0' - 1;
        String actName = playerNames[idx].getText();
        playerNamesArray[idx] = actName;
        boolean rightName = true;

        if (actName.length() == 0 || actName.length() > 20) rightName = false;
        for (int i = 0; i < actName.length(); i++) {
            char actChar = actName.charAt(i);
            if ((actChar > 'Z' || actChar < 'A') && (actChar > 'z' || actChar < 'a') &&
                    (actChar > '9' || actChar < '0') && actChar != '_' && actChar != '-') {
                rightName = false;
                break;
            }
        }

        playerNamesRight[idx] = rightName;
        int cnt = 0;
        for (int i = 0; i < playerNamesRight.length; i++) {
            if (playerNamesRight[i]) cnt++;
        }

        boolean diffNames = true;
        for (int i = 0; i < playerNamesArray.length; i++) {
            if (!diffNames) break;
            for (int j = 0; j < playerNamesArray.length; j++) {
                if (j != i && playerNamesArray[i].equals(playerNamesArray[j])) {
                    diffNames = false;
                    break;
                }
            }
        }

        if (cnt == playerNamesRight.length && diffNames) loadButton.setEnabled(true);
        else loadButton.setEnabled(false);
    }
}
