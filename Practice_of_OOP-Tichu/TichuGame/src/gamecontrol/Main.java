package gamecontrol;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import gameplay.GameState;
import gameplay.TichuGame;
import graphics.TichuFrame;


/**
 * The main class for the Tichu game control. Handles initialization, event listeners,
 * and controls the flow of the Tichu game.
 * Connects the game logics and the graphic user interface.
 * 
 * Note: The game does not differentiate between lowercase and uppercase letters when loading.
 * 
 * @author Tömöri Péter
 * @version 1.0
 */
public class Main {
	/**
     * The instance of the TichuGame class representing the current game state.
     */
	static TichuGame game=new TichuGame();
	/**
     * The instance of the TichuFrame class representing the graphical user interface.
     */
	static TichuFrame view=new TichuFrame();
	
	/**
     * The main method that initializes the game, adds event listeners, and makes the GUI visible.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        addListeners();
        addMenuListeners();
        view.setVisible(true);
    }
    
    /**
     * Every time a new game starts, this method is called to clear the previous data,
     * including creating a new instance of the game and view.
     * Useful for example when after game ends, players return to the Menu,
     * 		or when the game is loaded which was already in run
     */
    public static void newInit() {
    	view.setVisible(false);
    	game=new TichuGame();
    	view=new TichuFrame();
    	addListeners();
    	addMenuListeners();
    	view.setVisible(true);
    }
    /**
     * Attach event listeners to view components.
     */
    private static void addListeners() {
    	view.getExitButton().addActionListener(new ExitButtonActionListener());
    	view.getNewButton().addActionListener(new NewButtonActionListener());
    	view.getWantLoadButton().addActionListener(new WantToLoadListener());
    	view.getStatButton().addActionListener(new StatButtonListener());
    	view.getMakeLoadButton().addActionListener(new LoadButtonActionListener());
    	view.getCreateButton().addActionListener(new CreateButtonActionListener());
        view.getYesButton().addActionListener(new YNButtonActionListener());
        view.getNoButton().addActionListener(new YNButtonActionListener());
        view.getExchangeButton().addActionListener(new ExchangeButtonActionListener());
        view.getTichuButton().addActionListener(new TichuListener());
        view.getOkButton().addActionListener(new OkButtonActionListener());
        view.getDragonButton().addActionListener(new DragonButtonListener());
        view.getWishButton().addActionListener(new WishButtonListener());
        view.getEndContinue().addActionListener(new ContinueButtonListener());
        view.getEndSaveAndExit().addActionListener(new SaveAndExitButtonListener());
        view.getEndExit().addActionListener(new ExitButtonActionListener());
        view.getEndMenu().addActionListener(new MenuButtonActionListener());
    }
    /**
     * Attach event listeners to menu components.
     */
    private static void addMenuListeners() {
    	view.getMenuItem().addActionListener(new MenuButtonActionListener());
    	view.getSaveItem().addActionListener(new SaveButtonActionListener());
    	view.getLoadItem().addActionListener(new WantToLoadListener());
    	view.getExitItem().addActionListener(new ExitButtonActionListener());
    	view.getStatItem().addActionListener(new StatButtonListener());
    	view.getRuleItem().addActionListener(new RuleShowActionListener());
    	view.getHowtoItem().addActionListener(new HowtoActionListener());
    }
    /**
     * ActionListener for showing the rules dialog.
     */
    private static class RuleShowActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			showRulesDialog();
		}
	}
    /**
     * Shows the rules dialog.
     */
    private static void showRulesDialog() {
    	String rules=TichuGame.readFileToString("Docs"+File.separator+"rules.txt");
        JOptionPane.showMessageDialog(null, rules, "Tichu Game Rules", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * ActionListener for showing the how-to guide dialog.
     */
    private static class HowtoActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			showHowtoDialog();
		}
	}
    /**
     * Shows the how-to guide dialog.
     */
    private static void showHowtoDialog() {
    	String howto=TichuGame.readFileToString("Docs"+File.separator+"howto.txt");
        JOptionPane.showMessageDialog(null, howto, "How To Guide", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * ActionListener for showing the statistics dialog.
     */
    private static class StatButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			showStatDialog();
		}
	}
    /**
     * Shows the statistics dialog.
     * 
     * Note: can be designed better with JTable (version 2.0)
     */
    private static void showStatDialog() {
    	String stat;
    	stat="P1-P4 name and placement; T1(P1, P3) points; T2(P2, P4) points\n";
    	stat=stat.concat(TichuGame.readFileToString("Stat"+File.separator+"games.txt"));
        JOptionPane.showMessageDialog(null, stat, "Statistics - Games' data", JOptionPane.INFORMATION_MESSAGE);
        stat="Team name; Num of games; Sum of points; Win %\n";
        stat=stat.concat(TichuGame.readFileToString("Stat"+File.separator+"teams.txt"));
        JOptionPane.showMessageDialog(null, stat, "Statistics - Teams' data", JOptionPane.INFORMATION_MESSAGE);
        stat="Player name; Num of games; Sum of points; Max point; Win %; Avg placement; Tichu Successes\n";
        stat=stat.concat(TichuGame.readFileToString("Stat"+File.separator+"players.txt"));
        JOptionPane.showMessageDialog(null, stat, "Statistics - Players' data", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * ActionListener for exiting the game.
     */
	private static class ExitButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			System.exit(0);
		}
	}
	/**
     * ActionListener for starting a new game.
     * first step: creating players
     */
	private static class NewButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			view.createView();
		}
	}
	/**
     * ActionListener for creating a new game after player names are entered.
     * Deck should be created as well for Grand Tichu round
     */
	private static class CreateButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			game.createPlayers(view.getPlayerNamesCreate());
			view.popupDialog("Shift screen", "Next player: "+game.getPlayerNames()[0]);
			grandTichuStarts();
		}
	}
	/**
     * Starts the grand Tichu phase after creating players.
     */
	private static void grandTichuStarts() {
		view.setGrandTichuCards(game.getGrandTichuCardImages(0));
		view.grandTichuView(game.getPlayers().get(0).getName());
	}
	/**
	 * ActionListener for handling the response to the grand Tichu declaration (YES/NO).
	 * Sets the grand Tichu declaration for the current player and progresses the game accordingly.
	 */
	private static class YNButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			view.popupDialog("Shift screen", "Next player: "+game.getPlayerNames()[(view.getGrandTichuCnt()+1)%4]);
			JButton yn=(JButton)ae.getSource();
			if(yn.getText()=="YES") {
				view.setSaidGrandTichu(view.getGrandTichuCnt(), true);
			} else {
				view.setSaidGrandTichu(view.getGrandTichuCnt(), false);
			}
			if(view.getGrandTichuCnt()==3) {
				game.greatTichuRound(view.getSaidGrandTichu());
				game.sortHands();
				exchangeStarts();
			}
			else {
				view.setGrandTichuCards(game.getGrandTichuCardImages(view.getGrandTichuCnt()+1));
				view.nextGrandTichuPlayer(game.getPlayers().get(view.getGrandTichuCnt()+1).getName());
			}
		}
	}
	/**
	 * Initiates the card exchange phase after finishing the great Tichu round.
	 */
	private static void exchangeStarts() {
		view.setHandCards(game.getHandCardImages(0), false);
		view.exchangeView(game.getPlayers().get(1).getName(), game.getPlayers().get(2).getName(), game.getPlayers().get(3).getName());
	}
	/**
	 * ActionListener for handling the card exchange process during the exchange phase.
	 * Progresses the game after all exchanges are made.
	 */
	private static class ExchangeButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			int cnt=view.getExchangeCnt();
			view.addSelectedExchanges(game.getPlayers().get((cnt+2)%4).getName(), game.getPlayers().get((cnt+3)%4).getName(), game.getPlayers().get(cnt).getName());
			if(cnt==3) {
				game.exchangeCards(view.getExchangesMade());
				game.sortHands();
				game.setStartingPlayer();
				game.initRound();
				newPlayerMoves();
			} else {
				view.setHandCards(game.getHandCardImages(cnt+1), false);
				view.popupDialog("Shift screen", "Next player: "+game.getPlayerNames()[(cnt+1)%4]);
			}
		}
	}
	/**
	 * Initiates the player moves phase after finishing the exchange round.
	 */
	private static void newPlayerMoves() {
		int startIdx=game.getNextPlayer();
		view.popupDialog("Shift screen", "Next player: "+game.getPlayerNames()[startIdx]);
		view.setHandCards(game.getHandCardImages(startIdx), true);
		view.setComboCards(game.getComboCardImages());
		view.gameView(game.viewDataInt(), game.viewDataString());
	}
	/**
	 * ActionListener for handling the Tichu declaration by a player.
	 * Marks that the player has declared Tichu and disables the Tichu button.
	 */
    private static class TichuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            game.saidTichu();
            view.getTichuButton().setEnabled(false);
        }
    }
    /**
     * ActionListener for handling the Dragon button click.
     * Resolves the Dragon-related actions based on the game state and progresses the game accordingly.
     * Giving the Dragon-taken cards to an opponent.
     */
    private static class DragonButtonListener implements ActionListener {
    	public void actionPerformed(ActionEvent ae) {
    		game.setState(GameState.InRound);
    		int idx=game.getWinnerIdx();
    		if(view.getOpponent()) game.roundOver((idx+1)%4);
    		else game.roundOver((idx+3)%4);
    		if(!game.isGameEnd()) {
    			game.initRound();
    			newPlayerMoves();
    		} else {
    			game.gameOver();
    			view.endView(game.getTeamPoints(), game.getPlayerNames());
    		}
    	}
    }
    /**
     * ActionListener for handling the Wish button click.
     * Sets the wish made by the player and progresses the game.
     */
    private static class WishButtonListener implements ActionListener {
    	public void actionPerformed(ActionEvent ae) {
    		game.setState(GameState.InRound);
    		game.setWish(view.getWish());
    		newPlayerMoves();
    	}
    }
    /**
     * ActionListener for handling the Ok button click during a player's turn.
     * Validates the selected combination and progresses the game accordingly.
     */
    private static class OkButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			boolean success=game.playCombination(view.getChosenIdxs());
			if(!success) {
				view.popupDialog("Error", "Not a valid combination, please try again!");
				return;
			}
			/*
			 * If round ended the cards are given to the winner
			 * And the next player to start is determined
			 */
			if(game.isRoundEnd()) {
				/**
				 * If a player wins a round with the Dragon, the cards should be given to an opponent
				 */
				if(game.wonWithDragon()) {
					game.setState(GameState.InDragon);
					makeDragonView();
				} else {
					game.roundOver(game.getWinnerIdx());
					if(!game.isGameEnd()) {
						game.initRound();
						newPlayerMoves();
					} else {
						/**
						 * If the game ended, the last player's cards are put to their place
						 * The points will be summed and new game can start if necessary
						 */
						game.gameOver();
						view.endView(game.getTeamPoints(), game.getPlayerNames());
					}
				}
			/**
			 * If round didn't end yet, we should check if Sparrow was played
			 */
			} else if(game.isSparrowPlayed()) {
				game.setState(GameState.InWish);
				view.wishView();
			} else {
				newPlayerMoves();
			}
		}
	}
    /**
     * Prepares the view for the Dragon round, displaying relevant information.
     */
    public static void makeDragonView() {
  		int winIdx=game.getWinnerIdx();
		String name1=game.getPlayers().get((winIdx+1)%4).getName();
		String name2=game.getPlayers().get((winIdx+3)%4).getName();
		view.gameView(game.viewDataInt(), game.viewDataString());
		view.setHandCards(game.getHandCardImages(winIdx), false);
		view.dragonView(name1, name2);
  	}
    
    /**
     * ActionListener for returning to the Menu.
     * Saves the game state and initializes a new game.
     */
  	private static class MenuButtonActionListener implements ActionListener {
  		public void actionPerformed(ActionEvent ae) {
  			saveGame();
  			newInit();
  		}
  	}
  	/**
  	 * ActionListener for handling the "Continue" button click after a game ends without reaching the goal.
  	 * Initiates a new game with the same team points and player names to continue playing.
  	 */
    private static class ContinueButtonListener implements ActionListener {
    	public void actionPerformed(ActionEvent ae) {
    		int[] teampoints=game.getTeamPoints();
    		String[] names=game.getPlayerNames();
    		newInit();
    		game.setTeamPoints(teampoints[0], teampoints[1]);
    		game.createPlayers(names);
			grandTichuStarts();
			view.setVisible(true);
    	}
    }
    /**
     * ActionListener for changing to Load view
     * Saves the current game state, initializes a new game, and opens the load game view.
     */
    private static class WantToLoadListener implements ActionListener {
    	public void actionPerformed(ActionEvent ae) {
    		saveGame();
  			newInit();
    		view.loadView();
    	}
    }
    /**
     * ActionListener for handling the "Load" button click in the load game view.
     * Attempts to load a game with the provided player names and updates the view accordingly.
     * In case of fail, a pop-up dialog appears, and we return to the main menu
     */
	private static class LoadButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			boolean success=false;
    		try {
    			success=loadGame(view.getPlayerNamesLoad());
    		} catch(Exception e) {
    			success=false;
    			view.popupDialog("Error", "Loading unsuccessful! Try again.");
    			newInit();
    			e.printStackTrace();
    		}
    		if(success) {
    			setViewAfterLoad(game.getState());
    			view.popupDialog("Success", "Loading succeeded :)");
    		} else {
    			newInit();
    			view.popupDialog("Error", "Loading unsuccessful! Try again.");
    		}
		}
	}
	/**
	 * ActionListener for handling the "Save" button click.
	 * Saves the current game state to a file.
	 */
	private static class SaveButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			saveGame();
		}
	}
	/**
	 * ActionListener for handling the "Save and Exit" button click.
	 * Saves the current game state and exits the application.
	 */
	private static class SaveAndExitButtonListener implements ActionListener {
    	public void actionPerformed(ActionEvent ae) {
    		saveGame();
    		System.exit(0);
    	}
    }
	/**
	 * Saves the current game state to a file.
	 */
  	public static void saveGame() {
  		if(game.getState().equals(GameState.PlayerInit)) return;
  		String fileName=game.getFileName();
        File file=new File(fileName);
        try {
        	FileOutputStream fos=new FileOutputStream(file);
        	ObjectOutputStream oos=new ObjectOutputStream(fos);
        	oos.writeObject(game);
        	oos.close();
        } catch(IOException e) {
			view.popupDialog("Error", "Couldn't save game :(");
			e.printStackTrace();
		}
        if(fileName!=null) {
			view.popupDialog("Success", "Game saved: "+file.getName());
		} else {
			view.popupDialog("Error", "Couldn't save game :(");
		}
  	}
  	
  	/**
  	 * Loads a game from a file and updates the game state.
  	 *
  	 * @param playerNames An array of player names used to construct the file name.
  	 * @return True if loading was successful, false otherwise.
  	 * @throws IOException            If an I/O error occurs.
  	 * @throws ClassNotFoundException If the class of a serialized object cannot be found.
  	 */
  	public static boolean loadGame(String [] playerNames) throws IOException, ClassNotFoundException {
  		String fileName=new String("SavedGames"+File.separator);
		for(int i=0; i<playerNames.length; i++) {
			fileName=fileName.concat(playerNames[i]);
		}
		fileName=fileName.concat(".txt");
		File file=new File(fileName);
		if(!file.exists()) return false;
  		FileInputStream fis=new FileInputStream(file);
  		ObjectInputStream ois=new ObjectInputStream(fis);
  		game=(TichuGame)ois.readObject();
  		ois.close();
  		return true;
  	}
  	/**
  	 * Updates the view after loading a game based on the current game state.
  	 *
  	 * @param state The current game state after loading.
  	 */
  	public static void setViewAfterLoad(GameState state) {
		switch(state) {
		case PlayerInit:
			view.createView();
			return;
		case GrandTichu:
			view.popupDialog("Shift screen", "Next player: "+game.getPlayerNames()[0]);
			grandTichuStarts();
			return;
		case ExchangeCards:
			view.popupDialog("Shift screen", "Next player: "+game.getPlayerNames()[0]);
			exchangeStarts();
			return;
		case InRound:
			newPlayerMoves();
			return;
		case InWish:
			int wishIdx=game.getWinnerIdx();
			view.popupDialog("Shift screen", "Next player: "+game.getPlayerNames()[wishIdx]);
			/*
			 * The game already went to the next player
			 * GameView would show the sidePlayerPanels bad (actual player would be on the left)
			 * So it is needed to override the viewData that we get from game
			 */
			int[] modint=game.viewDataInt();
			String[] modstr=game.viewDataString();
			game.setViewToPrevPlayer(modint, modstr);
			view.gameView(modint, modstr);
			view.wishView();
			return;
		case InDragon:
			int winIdx=game.getWinnerIdx();
			view.popupDialog("Shift screen", "Next player: "+game.getPlayerNames()[winIdx]);
			makeDragonView();
			return;
		case Ended:
			game.createPlayers(null);
			view.popupDialog("Shift screen", "Next player: "+game.getPlayerNames()[0]);
			grandTichuStarts();
		}
	}
}