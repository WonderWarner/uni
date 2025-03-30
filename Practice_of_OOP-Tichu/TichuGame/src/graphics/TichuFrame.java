package graphics;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * Represents the main frame for the Tichu game's graphical user interface.
 * It contains various panels for different stages of the game, including menus,
 * player actions, and game information display.
 * Head of the View part
 */
public class TichuFrame extends JFrame {
	//Main panels
	private JPanel center=new JPanel();
	private JPanel playerPanel=new JPanel();
	private JPanel[] sidePanelCentered=new JPanel[3];
	//MenuBar
	private TichuMenuBar menuBar=new TichuMenuBar();
	//Starting menu Components
	private MenuPanel menu=new MenuPanel();
	//Panel to Load a game
	private LoadPanel load=new LoadPanel();
	//Create Components
	private CreatePanel create=new CreatePanel();
	//Grand Tichu round Components (without playerCards)
	private GrandTichuPanel grandTichuPanel=new GrandTichuPanel();
	//Exchange cards Components (without playerCards)
	private ExchangePanel exchangePanel=new ExchangePanel();
	//During play Components
	private PlayerButtonsPanel playerButtons=new PlayerButtonsPanel();
	//The highest combination's data is always displayed
	private RoundComboPanel roundCombo=new RoundComboPanel();
	private WishPanel wishPanel=new WishPanel();
	//If round is won with Dragon an opponent gets it
	private DragonPanel dragonPanel=new DragonPanel();
	//Last combination
	private JPanel comboCards=new JPanel();
	private ArrayList<CardComponent> combo=new ArrayList<>();
	//Player's hand
	private JPanel playerCards=new JPanel();
	private ArrayList<CardComponent> hand=new ArrayList<>();
	//Not actual players' data
	private SidePanel[] sidePanel=new SidePanel[3];
	//Game over screen
	private EndGamePanel endGame=new EndGamePanel();
	
	/**
     * Initializes the TichuFrame with default settings.
     * At the start, only the Menu is visible.
     */
	public TichuFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Tichu");
		setSize(1000, 700);
		setResizable(true);
		initView();
	}
	/**
	 * Getter methods for buttons
	 * making it possible to have ActionListeners in the Control (Main)
	 */
	//Starting menu
	public JButton getExitButton() { return menu.exitButton; }
	public JButton getNewButton() { return menu.newButton; }
	public JButton getWantLoadButton() { return menu.loadButton; }
	public JButton getStatButton() { return menu.statButton; }
	public JButton getMakeLoadButton() { return load.loadButton; }
	//Creating players
	public JButton getCreateButton() { return create.createButton; }
	public String[] getPlayerNamesCreate() { return create.playerNamesArray; }
	public String[] getPlayerNamesLoad() { return load.playerNamesArray; }
	public JButton getYesButton() { return grandTichuPanel.yes; }
	public JButton getNoButton() { return grandTichuPanel.no; }
	//Grand Tichu
	public void setSaidGrandTichu(int idx, boolean bool) { grandTichuPanel.saidGrandTichu[idx]=bool; }
	public int getGrandTichuCnt() { return grandTichuPanel.grandTichuCnt; }
	public boolean[] getSaidGrandTichu() { return grandTichuPanel.saidGrandTichu; }
	//Exchange
	public JButton getExchangeButton() { return exchangePanel.exchangeButton; }
	public int getExchangeCnt() { return exchangePanel.exchangeCnt; }
	public int[] getExchangesMade() { return exchangePanel.exchangesMade; }
	//In-Game buttons
	public JButton getTichuButton() { return playerButtons.tichu; }
	public JButton getOkButton() { return playerButtons.ok; }
	public JButton getDragonButton() { return dragonPanel.giveDragon; }
	public JButton getWishButton() { return wishPanel.wishButton; }
	//Ending Screen
	public JButton getEndContinue() { return endGame.continueGame; }
	public JButton getEndSaveAndExit() { return endGame.saveAndExit; }
	public JButton getEndExit() { return endGame.exit; }
	public JButton getEndMenu() { return endGame.menu; }
	//JMenuItems
	public JMenuItem getMenuItem() { return menuBar.menuItem; }
	public JMenuItem getSaveItem() { return menuBar.saveItem; }
	public JMenuItem getLoadItem() { return menuBar.loadItem; }
	public JMenuItem getExitItem() { return menuBar.exitItem; }
	public JMenuItem getStatItem() { return menuBar.statItem; }
	public JMenuItem getRuleItem() { return menuBar.ruleItem; }
	public JMenuItem getHowtoItem() { return menuBar.howtoItem; }
	
	 /**
     * Hides all panels
     * Used for creating new view, only need to set what should be visible
     */
	public void hideEverything() {
		menu.setVisible(false);
		load.setVisible(false);
		create.setVisible(false);
		grandTichuPanel.setVisible(false);
		exchangePanel.setVisible(false);
		wishPanel.setVisible(false);
		dragonPanel.setVisible(false);
		roundCombo.setVisible(false);
		comboCards.setVisible(false);
		playerCards.setVisible(false);
		playerButtons.setVisible(false);
		for(int i=0; i<sidePanelCentered.length; i++) {
			sidePanelCentered[i].setVisible(false);
		}
		endGame.setVisible(false);
	}
	/**
     * Sets the view to display the starting menu.
     */
	public void newView() {
		hideEverything();
		menu.setVisible(true);
		playerCards.setVisible(true);
	}
	/**
     * Sets the view to display the load game panel.
     */
	public void loadView() {
		hideEverything();
		load.setVisible(true);
	}
	/**
     * Sets the view to display the create game panel.
     */
	public void createView() {
		hideEverything();
		create.setVisible(true);
	}
	/**
     * Sets the view to display the Grand Tichu panel with a specific player's name.
     *
     * @param name The name of the player for whom the Grand Tichu panel is displayed.
     */
	public void grandTichuView(String name) {
		hideEverything();
		playerCards.setVisible(true);
		grandTichuPanel.setVisible(true);
		grandTichuPanel.grandTichuLabel.setText(name+", do you want to say grand Tichu?");
	}
	/**
     * Sets the view to display the exchange panel with specific players' names.
     *
     * @param name1 The name of the first player after the actual one.
     * @param name2 The name of the second player after the actual one.
     * @param name3 The name of the third player after the actual one.
     */
	public void exchangeView(String name1, String name2, String name3) {
		hideEverything();
		playerCards.setVisible(true);
		exchangePanel.setVisible(true);
		exchangePanel.giveCardLabel[0].setText("Card's index to give to "+name1);
		exchangePanel.giveCardLabel[1].setText("Card's index to give to "+name2);
		exchangePanel.giveCardLabel[2].setText("Card's index to give to "+name3);
		exchangePanel.exchangeButton.setEnabled(false);
	}
	/**
     * Updates the game view based on the provided data.
     * For data meanings go and see: TichuGame viewDataInt() and viewDataString() method descriptions
     *
     * @param ints    Integer array containing game-related data.
     * @param strings String array containing game-related data.
     */
	public void gameView(int[] ints, String[] strings) {
		hideEverything();
		roundCombo.setVisible(true);
		playerCards.setVisible(true);
		comboCards.setVisible(true);
		playerButtons.setVisible(true);
		for(int i=0; i<sidePanel.length; i++) {
			sidePanelCentered[i].setVisible(true);
			sidePanel[i].gameViewForSidePanel(i, ints, strings);
		}
		if(ints[0]==0) playerButtons.tichu.setEnabled(false);
		else playerButtons.tichu.setEnabled(true);
		roundCombo.gameViewForRoundCombo(ints, strings);
	}
	 /**
     * Sets the view to display the Dragon panel with specific opponent names.
     *
     * @param name1 The name of the opponent to the right.
     * @param name2 The name of the opponent to the left.
     */
	public void dragonView(String name1, String name2) {
		dragonPanel.opponentNames[0]=new String(name1);
		dragonPanel.opponentNames[1]=new String(name2);
		dragonPanel.opponent=new JComboBox<>(dragonPanel.opponentNames);
		dragonPanel.dragonPanelParts[1].add(dragonPanel.opponent);
		dragonPanel.setVisible(true);
		roundCombo.setVisible(false);
		playerButtons.setVisible(false);
	}
	/**
     * Sets the view to display the wish panel.
     */
	public void wishView() {
		wishPanel.setVisible(true);
		playerButtons.setVisible(false);
	}
	/**
     * Sets the view to display the end game panel with specific points and player names.
     *
     * @param points Integer array containing the points of each team.
     * @param names  String array containing the names of the players.
     */
	public void endView(int[] points, String[] names) {
		hideEverything();
		endGame.setView(points, names);
		endGame.setVisible(true);
	}
	
	/**
     * Initializes the graphical components of the TichuFrame, including menus and panels.
     */
	public void initView() {
	//MenuBar
		setJMenuBar(menuBar);
	//CENTER PANEL (CENTER), adding the panels that will appear here
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		center.add(menu);
		center.add(load);
		center.add(create);
		center.add(grandTichuPanel);
		center.add(exchangePanel);
		center.add(wishPanel);
		center.add(dragonPanel);
		center.add(roundCombo);
		comboCards.setLayout(new FlowLayout(FlowLayout.CENTER));
		center.add(comboCards);
		center.add(endGame);
		add(center, BorderLayout.CENTER);
	//PLAYER PANEL (SOUTH)
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
		playerCards.setLayout(new FlowLayout(FlowLayout.RIGHT));
		playerPanel.add(playerCards);
		playerPanel.add(playerButtons);
		add(playerPanel, BorderLayout.SOUTH);
	//Side player panels
		for(int i=0; i<sidePanel.length; i++) {
			sidePanelCentered[i]=new JPanel();
			sidePanelCentered[i].setLayout(new FlowLayout(FlowLayout.CENTER));
			sidePanel[i]=new SidePanel(i);
			sidePanelCentered[i].add(sidePanel[i]);
		}
		add(sidePanelCentered[0], BorderLayout.EAST);
		add(sidePanelCentered[1], BorderLayout.NORTH);
		add(sidePanelCentered[2], BorderLayout.WEST);
		newView();
	}
	
	
	/**
     * Sets the images of the cards for the actual player during the Grand Tichu phase.
     *
     * @param cardImages Array of card image paths.
     */
	public void setGrandTichuCards(String[] cardImages) {
		hand.clear();
		playerCards.removeAll();
		for(int i=0; i<cardImages.length; i++) {
			hand.add(new CardComponent(cardImages[i]));
			playerCards.add(hand.get(i));
			hand.get(i).setEnabled(false);
		}
		playerCards.setSize(playerCards.getPreferredSize());
	}
	/**
     * Moves to the next player in the Grand Tichu phase.
     *
     * @param name The name of the next player.
     */
	public void nextGrandTichuPlayer(String name) {
		grandTichuPanel.grandTichuCnt++;
		grandTichuPanel.grandTichuLabel.setText(name+", do you want to say grand Tichu?");
	}
	/**
     * Updates the exchange panel with selected exchange options.
     *
     * @param name1 The name of the first player after the actual one.
     * @param name2 The name of the second player after the actual one (team mate).
     * @param name3 The name of the third player after the actual one.
     */
	public void addSelectedExchanges(String name1, String name2, String name3) {
		int startIdx=3*exchangePanel.exchangeCnt;
		for(int i=startIdx; i<startIdx+3; i++) {
			exchangePanel.exchangesMade[i]=((int)exchangePanel.giveCardOptions.get(i-startIdx).getSelectedItem())-1;
		}
		exchangePanel.exchangeCnt++;
		exchangePanel.giveCardLabel[0].setText("Card's index to give to "+name1);
		exchangePanel.giveCardLabel[1].setText("Card's index to give to "+name2);
		exchangePanel.giveCardLabel[2].setText("Card's index to give to "+name3);
		for(int i=0; i<3; i++) {
			exchangePanel.giveCardOptions.get(i).setSelectedIndex(0);
		}
		exchangePanel.exchangeButton.setEnabled(false);
	}
	/**
     * Sets the images of the cards for the actual player during the game.
     *
     * @param cardImages Array of card image paths.
     * @param select     Indicates whether the cards should be selectable.
     */
	public void setHandCards(String[] cardImages, boolean select) {
		hand.clear();
		playerCards.removeAll();
		for(int i=0; i<cardImages.length; i++) {
			hand.add(new CardComponent(cardImages[i]));
			playerCards.add(hand.get(i));
			hand.get(i).setEnabled(select);
			hand.get(i).setSize(hand.get(i).getPreferredSize());
		}
		playerCards.setSize(playerCards.getPreferredSize());
	}
	 /**
     * Sets the images of the cards for the highest combination played in the round.
     *
     * @param cardImages Array of card image paths.
     */
	public void setComboCards(String[] cardImages) {
		combo.clear();
		comboCards.removeAll();
		if(cardImages==null) return;
		for(int i=0; i<cardImages.length; i++) {
			combo.add(new CardComponent(cardImages[i]));
			comboCards.add(combo.get(i));
			combo.get(i).setEnabled(false);
		}
		comboCards.setSize(comboCards.getPreferredSize());
	}
	
	 /**
     * Gets the indices of the chosen cards by the actual player.
     *
     * @return Boolean array indicating which cards are chosen.
     */
	public boolean[] getChosenIdxs() {
		boolean[] idxs=new boolean[hand.size()];
		for(int i=0; i<hand.size(); i++) {
			idxs[i]=hand.get(i).isSelected();
		}
		return idxs;
	}
	/**
     * Displays a popup dialog with a specified name and message.
     *
     * @param name    The title of the popup dialog.
     * @param message The message content of the popup dialog.
     */
	public void popupDialog(String name, String message) {
		Popup popup=new Popup(this, name, message);
		popup.setVisible(true);
	}
	 /**
     * Gets the opponent chosen by the player in the Dragon win scenario.
     *
     * @return True if the first combobox option (opponent to right) is selected, false otherwise.
     */
	public boolean getOpponent() {
		if(dragonPanel.opponent.getSelectedIndex()==0) return true;
		return false;
	}
	/**
     * Gets the wish chosen by the player in the Wish phase.
     *
     * @return The wish chosen by the player.
     */
	public String getWish() {
		return (String)wishPanel.wishComboBox.getSelectedItem();
	}
}
