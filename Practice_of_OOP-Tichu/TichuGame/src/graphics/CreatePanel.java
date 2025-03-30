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
 * The CreatePanel class represents a panel used for creating a new game.
 * It allows users to set their names and teams for the game.
 */
public class CreatePanel extends JPanel {

    /** Array of subpanels for organizing components. */
    JPanel[] createPanels = new JPanel[4];

    /** Label indicating the purpose of the panel. */
    JLabel createTitle = new JLabel("Set your names:");

    /** Array of player names for the JComboBox. */
    Object[] playerComboBoxText = new Object[4];

    /** JComboBox for selecting the player's name and team. */
    JComboBox<Object> cardLayoutOptions;

    /** Panel for displaying the player names using CardLayout. */
    JPanel playerNameCardLayout = new JPanel();

    /** Array of subpanels for organizing text fields. */
    JPanel[] panelsForTextField = new JPanel[4];

    /** Array of text fields for entering player names. */
    JTextField[] playerNames = new JTextField[4];

    /** Array to store player names. */
    String[] playerNamesArray = new String[4];

    /** Array indicating whether player names are valid. */
    boolean[] playerNamesRight = new boolean[4];

    /** Button to initiate the creation of a new game. */
    JButton createButton = new JButton("Create!");

    /**
     * Constructs a CreatePanel with default settings.
     * Initializes and adds components for setting player names and teams.
     */
    public CreatePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        playerNameCardLayout.setLayout(new CardLayout());

        for (int i = 0; i < createPanels.length; i++) {
            createPanels[i] = new JPanel();
            add(createPanels[i]);
        }

        createPanels[0].add(createTitle);

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
        createPanels[1].add(cardLayoutOptions);
        createPanels[2].add(playerNameCardLayout);
        createButton.setEnabled(false);
        createPanels[3].add(createButton);
    }

    /**
     * ActionListener for the player selection JComboBox.
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
            } else {
                playerNames[idx].setText("");
            }
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
     * Updates player names and checks if the names are correct.
     * A valid name consists of 1-10 characters and is different from every other name.
     * Each character must be one of the following: A-Z, a-z, 0-9, '_', '-'.
     */
    private void updatePlayerName() {
        String selectedPlayer = (String) cardLayoutOptions.getSelectedItem();
        int idx = selectedPlayer.charAt(6) - '0' - 1;
        String actName = playerNames[idx].getText();
        playerNamesArray[idx] = actName;
        boolean rightName = true;

        if (actName.length() == 0 || actName.length() > 20) {
            rightName = false;
        }

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
            if (playerNamesRight[i]) {
                cnt++;
            }
        }

        boolean diffNames = true;

        for (int i = 0; i < playerNamesArray.length; i++) {
            if (!diffNames) {
                break;
            }

            for (int j = 0; j < playerNamesArray.length; j++) {
                if (j != i && playerNamesArray[i].equals(playerNamesArray[j])) {
                    diffNames = false;
                    break;
                }
            }
        }

        if (cnt == playerNamesRight.length && diffNames) {
            createButton.setEnabled(true);
        } else {
            createButton.setEnabled(false);
        }
    }
}

