package gameplay;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * The {@code TichuGame} class represents the core logic for managing a game of Tichu.
 * It keeps track of the game state, player actions, and scoring. The class includes methods
 * for handling Tichu declarations, managing rounds, updating player and team statistics, and
 * persisting game data. The game follows the rules of the Tichu card game and allows for
 * multiple rounds with scoring based on individual and team performances.
 * <p>
 * This class implements the {@code Serializable} interface to support serialization for saving
 * and loading game instances.
 *
 * @author Tömöri Péter
 * @version 1.0
 */
public class TichuGame implements Serializable {
	private int[] teamPoints=new int[2];
	private ArrayList<Player> players;
	private int actPlacement=1;
	private ArrayList<ArrayList<Card>> roundCards;
	private ComboType roundType=ComboType.None;
	private int actValue=-10;
	//If there is a card that should be played because of Sparrow
	//In this implementation a wish lasts only one round
	private int wish=0;
	//keep going until everyone passed
	private int pass=0;
	private int winnerIdx=-1;
	private boolean shouldIncreasePlacement=false;
	private boolean gameEnd=false;
	private GameState state;
	
	/**
     * Initializes a new Tichu game with default settings.
     */
    public TichuGame() {
        state = GameState.PlayerInit;
        teamPoints[0] = 0;
        teamPoints[1] = 0;
        players = new ArrayList<Player>();
        actPlacement = 1;
        wish = 0;
    }

    /**
     * Gets the current player placement.
     *
     * @return The current player placement.
     */
    public int getActPlacement() {
        return actPlacement;
    }

    /**
     * Sets the current player placement.
     *
     * @param count The player placement to set.
     */
    public void setActPlacement(int count) {
        actPlacement = count;
    }

    /**
     * Gets the index of the winning player.
     *
     * @return The index of the winning player.
     */
    public int getWinnerIdx() {
        return winnerIdx;
    }

    /**
     * Gets the points of each team.
     *
     * @return An array containing the points of each team.
     */
    public int[] getTeamPoints() {
        return teamPoints;
    }

    /**
     * Sets the points of each team.
     *
     * @param t1 Points for team 1.
     * @param t2 Points for team 2.
     */
    public void setTeamPoints(int t1, int t2) {
        teamPoints[0] = t1;
        teamPoints[1] = t2;
    }

    /**
     * Gets the names of the players.
     *
     * @return An array containing the names of the players.
     */
    public String[] getPlayerNames() {
        String[] names = new String[4];
        for (int i = 0; i < players.size(); i++) {
            names[i] = new String(players.get(i).getName());
        }
        return names;
    }

    /**
     * Generates a filename based on the players' names.
     *
     * @return The generated filename.
     */
    public String getFileName() {
		String fileName=new String("SavedGames"+File.separator);
		String[] names=getPlayerNames();
		for(int i=0; i<names.length; i++) {
			fileName=fileName.concat(names[i]);
		}
		fileName=fileName.concat(".txt");
		return fileName;
	}

    /**
     * Gets the list of players.
     *
     * @return The list of players.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Sets the list of players.
     *
     * @param players The list of players to set.
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Checks if the game has ended.
     *
     * @return True if the game has ended, false otherwise.
     */
    public boolean isGameEnd() {
        return gameEnd;
    }

    /**
     * Checks if the round has ended.
     *
     * @return True if the round has ended, false otherwise.
     */
    public boolean isRoundEnd() {
        if (pass < 4 - actPlacement && !gameEnd) return false;
        return true;
    }

    /**
     * Gets the current state of the game.
     *
     * @return The current state of the game.
     */
    public GameState getState() {
        return state;
    }

    /**
     * Sets the state of the game.
     *
     * @param state The state to set.
     */
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Creates a new game by storing players' names, creating a deck, and distributing cards to players.
     *
     * @param playerNames An array of player names.
     */
    public void createPlayers(String[] playerNames) {
		if(playerNames!=null) {
			for(int i=0; i<playerNames.length; i++) {
				players.add(new Player(playerNames[i]));
			}
		}
		createHand();
		state=GameState.GrandTichu;
	}

    /**
     * Shuffling the deck to the 4 players randomly
     *
     */
	private void createHand() {
		//creating deck
		ArrayList<Card> deck=new ArrayList<Card>();
		createDeck(deck);
		//shuffling cards randomly and giving 8 to each player
		for(int i=0; i<7; i++) {
			Collections.shuffle(deck);
		}
		for(Player i: players) {
			for(int j=0; j<14; j++) {
				i.addHandCard(deck.remove(0));
			}
		}
	}

    /**
     * Creates a deck of Tichu cards.
     *
     * @param deck The list to which the deck of cards will be added.
     */
    private void createDeck(ArrayList<Card> deck) {
		int cnt=0;
		for(Suit i: Suit.values()) {
			for(int j=0+2; j<13+2; j++) {						
				if(j==5) {
					deck.add(new Card(i, j, 5));
				} else if(j==10||j==13) {
					deck.add(new Card(i, j, 10));
				} else deck.add(new Card(i, j, 0));
			}
			if(cnt==3) break;
			else cnt++;
		}
		//adding the 4 speacial cards as well
		deck.add(new Card(Suit.Dragon, 20, 25));
		deck.add(new Card(Suit.Phoenix, -1, -25));
		deck.add(new Card(Suit.Dog, 0, 0));
		deck.add(new Card(Suit.Sparrow, 1, 0));
	}
	
    /**
     * Gets the images of the first 8 cards in a player's hand for deciding to say Grand Tichu.
     * Even if they already got their hand, they will only see the first 8 cards 
     *
     * @param idx The index of the player.
     * @return An array of image paths for the first 8 cards in the player's hand.
     */
	public String[] getGrandTichuCardImages(int idx) {
		String[] cardImages=new String[8];
		for(int i=0; i<8; i++) {
			cardImages[i]=new String(players.get(idx).handCards.get(i).getImagePath());
		}
		return cardImages;
	}
	/**
     * Gets the images of the hand cards for a player.
     *
     * @param idx The index of the player.
     * @return An array of image paths for the player's hand cards.
     */
	public String[] getHandCardImages(int idx) {
		String[] cardImages=new String[players.get(idx).getHandCards().size()];
		for(int i=0; i<cardImages.length; i++) {
			cardImages[i]=new String(players.get(idx).handCards.get(i).getImagePath());
		}
		return cardImages;
	}
	/**
     * Gets the images of the last played combo cards in the current round.
     *
     * @return An array of image paths for the last played combo cards.
     */
	public String[] getComboCardImages() {
		if(roundCards.size()==0) return null;
		ArrayList<Card> combo=roundCards.get(roundCards.size()-1);
		String[] cardImages=new String[combo.size()];
		for(int i=0; i<cardImages.length; i++) {
			cardImages[i]=new String(combo.get(i).getImagePath());
		}
		return cardImages;
	}
	 /**
     * Initiates the round where players decided to say Great Tichu or not.
     * Saving their decisions.
     *
     * @param saidGrandTichu An array indicating whether each player says Great Tichu.
     */
	public void greatTichuRound(boolean[] saidGrandTichu) {
		for(int i=0; i<saidGrandTichu.length; i++) {
			players.get(i).setSaidGrandTichu(saidGrandTichu[i]);
			if(saidGrandTichu[i]) {
				players.get(i).canSayTichu=false;
			}
		}
		state=GameState.ExchangeCards;
	}
	/**
     * Sorts the hands of all players.
     */
	public void sortHands() {
		for(Player i: players) {
			i.sortHandCards();
		}
	}
	 /**
     * Exchanges cards between players based on selected indices.
     * Saving exchanges
     *	first 3 card is what P1 choose to give to P2, P3 & P4
     * 	4th, 5th & 6th card was P2's and gave to P3, P4 & P1 etc.
     *
     * @param cardIdxs An array of indices indicating the cards to exchange.
     * 
     */
	public void exchangeCards(int[] cardIdxs) {
		Card[] answers=new Card[12];
		/**
		 * Getting out the selected cards from the players' hands
		 */
		for(int i=0; i<4; i++) {
			for(int j=0; j<3; j++) {
				answers[i*3+j]=players.get(i).getHandCards().remove(cardIdxs[i*3+j]);
				/*
				 * Because we get out a card we should take into count that its index changed
				 */
				if(j<=2) {
					if(j<=1) {
						if(cardIdxs[i*3+j]<cardIdxs[i*3+j+1]) cardIdxs[i*3+j+1]-=1;
						if(j==0) {
							if(cardIdxs[i*3+j]<cardIdxs[i*3+j+2]) cardIdxs[i*3+j+2]-=1;
						}
					}
				}
			}
		}
		/*
		 * And getting the cards what others gave
		 * Storing the 2 dimensional array in a one dimension array with modulo
		 *	Papirra felirva az osszefugges:
		 *	P1 kapott lapjainak az osszege 3 (az i és j indexek osszege)
		 *	P2 kapott lapjainak az osszege 4 vagy 0
		 *	P3 kapott lapjainak az osszege 1 vagy 5
		 * 	P4 kapott lapjainak az osszege 2
		 * Az i+j+1 modulo 4 megadja hogy hanyadik indexu jatekos kapja :)
		 */
		for(int i=0; i<4; i++) {
			for(int j=0; j<3; j++) {
				 players.get((i+j+1)%4).addHandCard(answers[i*3+j]);
			}
		}
		state=GameState.InRound;
	}
	/**
     * Sets the starting player for the first round of the game.
     * Always the player with the Sparrow starts the first round
     */
	public void setStartingPlayer() {
		boolean foundFirst=false;
		for(Player i: players) {
			if(!foundFirst) {
				for(Card j: i.getHandCards()) {
					if(j.getSuit().equals(Suit.Sparrow)) {
						i.setStarts(true);
						foundFirst=true;
						break;
					}
					else if(j.getNumber()>1) {
						i.setStarts(false);
						break;
					}
				}
			} else i.setStarts(false);
		}
	}
	/**
     * Finds out which player has to start the game and returns the index of the player.
     *
     * @return The index of the player who starts the game.
     */
	public int getNextPlayer() {
		for(int i=0; i<players.size()-1; i++) {
			if(players.get(i).getStarts()) return i;
		}
		return 3;
	}
	/**
     * Initializes the data for a new round, resetting round-specific variables.
     */
	public void initRound() {
		roundCards=new ArrayList<>();
		roundType=ComboType.None;
		actValue=-10;
		pass=0;
		winnerIdx=-1;
		shouldIncreasePlacement=false;
		gameEnd=false;
	}
	/**
     * Prepares data for the controller to forward to the display.
     * 	0: actual players canSayTichu value
	 *  1-3: other players' number of cards in hand
	 *  4-6: other players saidTichu values
	 *  7-9: other players' placement
	 *  10-12: roundCombo data (passes, value, wish)
     *
     * @return An array of integers representing various game data for display.
     */
	public int[] viewDataInt() {
		int[] data=new int[13];
		int idx=getNextPlayer();
		if(players.get(idx).getCanSayTichu()) data[0]=1;
		else data[0]=0;
		idx=(getNextPlayer()+1)%4;
		for(int i=0; i<3; i++) {
			int sideidx=(idx+i)%4;
			data[i+1]=players.get(sideidx).getHandCards().size();
			if(players.get(sideidx).getSaidGrandTichu()) data[i+4]=2;
			else if(players.get(sideidx).getSaidTichu()) data[i+4]=1;
			else data[i+4]=0;
			data[i+7]=players.get(sideidx).getPlacement();
		}
		data[10]=pass;
		data[11]=actValue;
		data[12]=wish;
		return data;
	}
	/**
     * Prepares data for the controller to forward to the display.
     * 	0-2: other Players' name
     * 	3-4: roundCombo data: type and winner player
     *
     * @return An array of strings representing various game data for display.
     */
	public String[] viewDataString() {
		String[] data=new String[5];
		int idx=(getNextPlayer()+1)%4;
		for(int i=0; i<3; i++) {
			int sideidx=(idx+i)%4;
			data[i]=players.get(sideidx).getName();
		}
		data[3]=roundType.name();
		if(winnerIdx==-1) data[4]=null;
		else data[4]=players.get(winnerIdx).getName();
		return data;
	}
	 /**
     * Modifies the view to display the previous player's data without changing the game state.
     *
     * @param modint An array of integers to modify based on the previous player's data.
     * @param modstr An array of strings to modify based on the previous player's data.
     */
	public void setViewToPrevPlayer(int[] modint, String[] modstr) {
		int idx=getNextPlayer();
		for(int i=0; i<3; i++) {
			int sideidx=(idx+i)%4;
			modstr[i]=players.get(sideidx).getName();
			modint[i+1]=players.get(sideidx).getHandCards().size();
			if(players.get(sideidx).getSaidGrandTichu()) modint[i+4]=2;
			else if(players.get(sideidx).getSaidTichu()) modint[i+4]=1;
			else modint[i+4]=0;
			modint[i+7]=players.get(sideidx).getPlacement();
		}
	}
	/**
     * Rotates to the next player in the circle and updates the game state accordingly.
     */
	private void setNextPlayer() {
		int startIdx=0;
		for(int i=0; i<players.size(); i++) {
			if(players.get(i).getStarts()) {
				startIdx=i;
				break;
			}
		}
		startIdx=(startIdx+1)%4;
		for(int i=0; i<players.size(); i++) {
			if(i==startIdx) players.get(i).setStarts(true);
			else players.get(i).setStarts(false);
		}
	}
	 /**
     * Indicates that a player has declared Tichu.
     */
	public void saidTichu() {
		players.get(getNextPlayer()).setSaidTichu(true);
		players.get(getNextPlayer()).setCanSayTichu(false);
	}
	/**
     * Attempts to play a combination based on the provided card indexes.
     * If they pushed the OK button, we check what they want to play
     * If its correct the move is made, if its not, then warning and replay
     *
     * @param cardIndexes An array of boolean values indicating the selected cards for the combo.
     * @return True if the move is valid and executed successfully, false otherwise.
     */
	public boolean playCombination(boolean[] cardIndexes) {
		int idx=getNextPlayer();
		boolean validCombo=true;
		boolean playedDog=false;
		/**
		 * Saving the played cards
		 */
		Combination combo=new Combination();
		for(int i=0; i<cardIndexes.length; i++) {
			if(cardIndexes[i]) combo.add(players.get(idx).getHandCards().get(i));
		}
		/**
		 * Checking what combo it is, setting the appropriate values
		 */
		combo.evalCombo();
		/**
		 * If the combination isn't determined yet - first move of the round (or Dog)
		 */
		if(actValue==-10) {
			if(combo.getType().equals(ComboType.None)||combo.getType().equals(ComboType.Pass)){
				validCombo=false;
			} else {
				if(combo.getValue()==0) {
					/**
					 * Case: First player plays the Dog
					 * The opposite player is the next (if they're still in)
					 */
					playedDog=true;
				} else if(combo.getValue()==-1) {
					/*
					 * Case: First player plays the Phoenix as Single
					 */
					roundType=ComboType.Single;
					actValue=1;
				} else if(combo.getValue()==1) {
					/**
					 * Case: First player plays the Sparrow (Single or Straight)
					 */
					actValue=combo.getValue();
					roundType=combo.getType();
				} else {
					/**
					 * Playing a "default" combination without special case
					 * Setting the combination type and the value
					 */
					actValue=combo.getValue();
					roundType=combo.getType();
				}
			}
		} else {
			/**
			 * When someone is the next (but not the first)
			 * First checking if there is a Wish and it can be satisfied
			 */
			if(wish!=0&&players.get(idx).canSatisfyWish(roundCards.get(roundCards.size()-1).size(), roundType, actValue, wish)) {
				ComboType comboType=combo.getType();
				validCombo=false;
				/*
				 * Type must be the same (or a bomb)
				 * And the value should also be higher
				 */
				if(combo.getValue()>actValue&&(comboType.equals(roundType)||comboType.equals(ComboType.Poker)||comboType.equals(ComboType.StraightFlush))) {
					for(Card c: combo.getCombo()) {
						if(c.getNumber()==wish) {
							validCombo=true;
							wish=0;
							break;
						}
					}
					if(validCombo) {
						actValue=combo.getValue();
						roundType=combo.getType();
						pass=0;
					}
				}
			} else if(combo.getValue()==-1&&roundType.equals(ComboType.Single)&&actValue<=14) {
				/*
				 * If they played phoenix as single card in the right moment
				 * Acceptable but value won't be higher
				 */
				pass=0;
			} else if(combo.getType().equals(ComboType.Poker)||combo.getType().equals(ComboType.StraightFlush)) {
				if(combo.getValue()<=actValue) {
					validCombo=false;
				} else {
					actValue=combo.getValue();
					roundType=combo.getType();
					pass=0;
				}
			} else if((!combo.getType().equals(roundType)||combo.getValue()<=actValue)&&!combo.getType().equals(ComboType.Pass)) {
				/**
				 * Giving not the same combo or a lower value
				 */
				validCombo=false;
			} else if(combo.getType().equals(ComboType.Pass)){
				pass++;
				/**
				 * If the combination is straight the lengths should also equal
				 */
			} else if(combo.getType().equals(ComboType.Straight)&&combo.getCombo().size()!=roundCards.get(roundCards.size()-1).size()) {
				validCombo=false;
			} else if(combo.getType().equals(ComboType.FollowingPairs)&&combo.getCombo().size()!=roundCards.get(roundCards.size()-1).size()) {
				validCombo=false;
			} else {
				actValue=combo.getValue();
				pass=0;
			}
		}
		if(!validCombo) {
			return false;
		}
		/**
		 * Making a valid move with putting new cards
		 */
		if(pass==0) {
			winnerIdx=idx;
			players.get(idx).setCanSayTichu(false);
			/*
			 * Adding combo to roundCards (and removing from hand)
			 */
			roundCards.add(combo.getCombo());
			for(Card c: combo.getCombo()) {
				players.get(idx).getHandCards().remove(c);
			}
		}
		/*
		 * If someone has previously won, and now another player puts a valid combo
		 */
		if(pass==0&&shouldIncreasePlacement) {
			actPlacement++;
			shouldIncreasePlacement=false;
		}
		/*
		 * These can only happen if the player played a valid combo
		 * If the player's hand is empty, they're out
		 */
		if(players.get(idx).getHandCards().isEmpty()) {
			players.get(idx).setPlacement(actPlacement);
			/*
			 * If we got our 4th player, the game ends
			 */
			if(actPlacement==3) {
				gameEnd=true;
				return true;
			}
			else shouldIncreasePlacement=true;
		}
		if(players.get(0).getPlacement()+players.get(2).getPlacement()==3
				||players.get(1).getPlacement()+players.get(3).getPlacement()==3) {
				gameEnd=true;
				return true;
		}
		if(playedDog) {
			setNextPlayer();
		}
		do {
			setNextPlayer();
		} while(players.get(getNextPlayer()).getPlacement()!=0);
		return true;
	}

	/**
     * Checks if the winner of the round won with a Dragon card.
     *
     * @return {@code true} if the winner won with a Dragon card, {@code false} otherwise.
     */
	public boolean wonWithDragon() {
		ArrayList<Card> lastCombo=roundCards.get(roundCards.size()-1);
		if(lastCombo.get(lastCombo.size()-1).getSuit().equals(Suit.Dragon)) {
			return true;
		}
		return false;
	}
	/**
     * Checks if Sparrow card was played in the last round.
     *
     * @return {@code true} if Sparrow was played, {@code false} otherwise.
     */
	public boolean isSparrowPlayed() {
		ArrayList<Card> lastCombo=roundCards.get(roundCards.size()-1);
		if((lastCombo.get(0).getSuit().equals(Suit.Sparrow))&&pass==0) {
			return true;
		}
		if(lastCombo.size()>=2) {
			if((lastCombo.get(1).getSuit().equals(Suit.Sparrow))&&pass==0) {
				return true;
			}
		}
		return false;
	}
	 /**
     * Sets the wish based on the provided string representation got from view through control.
     *
     * @param wishStr The string representation of the wish.
     */
	public void setWish(String wishStr) {
		switch(wishStr) {
		case "-": return;
		case "J": wish=11;
				break;
		case "Q": wish=12;
				break;
		case "K": wish=13;
				break;
		case "A": wish=14;
				break;
		default: wish=Integer.parseInt(wishStr); 
		}
	}
	/**
     * Handles the end of a round, distributing cards to the winner and setting up the next round (starting player)
     *
     * @param comboGetIdx The index of the player who gets the cards.
     */
	public void roundOver(int comboGetIdx) {
		/*
		 * If someone has previously won, and everyone passed
		 */
		if(shouldIncreasePlacement) {
			actPlacement++;
			shouldIncreasePlacement=false;
		}
		/**
		 * Giving the cards to the round's winner and setting it to be the next starter
		 * (if out then the next available)
		 */
		System.out.println("A kör vitt lapjai:");
		for(ArrayList<Card> combo: roundCards) {
			players.get(comboGetIdx).getTableCards().add(combo);
		}
		if(gameEnd) return;
		int nextStart=0;
		if(players.get(winnerIdx).getPlacement()!=0) {
			nextStart++;
			if(players.get((winnerIdx+1)%4).getPlacement()!=0) {
				nextStart++;
			}
		}
		winnerIdx+=nextStart;
		winnerIdx%=4;
		for(int i=0; i<players.size(); i++) {
			if(i==winnerIdx) players.get(i).setStarts(true);
			else players.get(i).setStarts(false);
		}
	}
	/**
     * Handles the end of the game, calculates points, and updates statistics.
     */
	public void gameOver() {
		int originalT1=teamPoints[0];
		int originalT2=teamPoints[1];
		/**
		 * add or subtract the Tichu points
		 */
		for(int i=0; i<players.size(); i++) {
			System.out.println(players.get(i).getPlacement());
			if(players.get(i).getSaidGrandTichu()) {
				if(players.get(i).getPlacement()==1) teamPoints[i%2]+=200;
				else teamPoints[i%2]-=200;
			} else if(players.get(i).getSaidTichu()) {
				if(players.get(i).getPlacement()==1) teamPoints[i%2]+=100;
				else teamPoints[i%2]-=100;
			}
		}
		/*
		 * 200-0 if a team won
		 */
		if(players.get(0).getPlacement()+players.get(2).getPlacement()==3) {
			System.out.println("Az első csapat instant nyert");
			teamPoints[0]+=200;
			setInstantWinStat(0);
		} else if(players.get(1).getPlacement()+players.get(3).getPlacement()==3) {
			System.out.println("A második csapat instant nyert");
			teamPoints[1]+=200;
			setInstantWinStat(1);
		} else {
			/*
			 * else the points earned with cards matters
			 * The last player's hand will be the oppontent's
			 * The last player's table will be the first player's
			 */
			putLastCards(players);
			/*
			 * adding the hand points
			 */
			for(int i=0; i<players.size(); i++) {
				if(players.get(i).getPlacement()==0) players.get(i).setPlacement(4);
				players.get(i).evalPoints();
				teamPoints[i%2]+=players.get(i).getPoints();
			}
		}
		/*
		 * Updating statistics
		 */
		setPlayerPointsTichu();
		updateGameStat(originalT1, originalT2);
		for(int i=0; i<2; i++) {
			boolean win=false;
			if((teamPoints[i]-originalT1)>=(teamPoints[(i+1)%2]-originalT2)) win=true;
			int teamGamePoint=teamPoints[0]-originalT1;
			if(i==1) teamGamePoint=teamPoints[1]-originalT2;
			updateTeamStat(players.get(i).getName()+players.get(i+2).getName(), teamGamePoint, win);
		}
		for(int i=0; i<4; i++) {
			boolean win=false;
			if((teamPoints[i%2]-originalT1)>=(teamPoints[(i+1)%2]-originalT2)) win=true;
			boolean tichu=false;
			Player player=players.get(i);
			if((player.getSaidGrandTichu()||player.getSaidTichu())&&player.getPlacement()==1) tichu=true;
			updatePlayerStat(player.getName(), player.getPoints(), win, player.getPlacement(), tichu);
		}
		/*
		 * clearing the player and game data for the next game,
		 * in case a save happens
		 */
		for(Player player: players) {
			player.getHandCards().clear();
			player.getTableCards().clear();
			player.setSaidGrandTichu(false);
			player.setSaidTichu(false);
			player.setPoints(0);
			player.setStarts(false);
			player.setCanSayTichu(true);
			player.setPlacement(0);
		}
		actPlacement=1;
		roundCards.clear();
		roundType=ComboType.None;
		actValue=-10;
		wish=0;
		pass=0;
		winnerIdx=-1;
		shouldIncreasePlacement=false;
		gameEnd=false;
		state=GameState.Ended;
		System.out.println("(Játék végén)\nElső csapat pont:"+teamPoints[0]);
		System.out.println("Második csapat pont:"+teamPoints[1]);
	}
	/**
     * Transfers the last player's hand to the proper players.
     * Updates the player's and opponent's table cards.
     * 	Last player's hand will be the opponents.
     * 	First player will get the lasts taken combinations.
     *
     * @param players The list of players.
     */
	public void putLastCards(ArrayList<Player> players) {
		int firstIdx=0;
		for(int i=0; i<players.size(); i++) {
			if(players.get(i).getPlacement()==1) {
				firstIdx=i;
				break;
			}
		}
		for(int i=0; i<players.size(); i++) {
			if(players.get(i).getPlacement()==0) {
				System.out.println("Az utolsó játékos keze: "+players.get(i).getHandCards());
				int opponent=((i+1)%4);
				ArrayList<Card> lastHand=new ArrayList<Card>();
				for(Card c: players.get(i).getHandCards()) {
					lastHand.add(c);
				}
				players.get(opponent).getTableCards().add(lastHand);
				players.get(i).getHandCards().clear();
				for(ArrayList<Card> combo: players.get(i).getTableCards()) {
					players.get(firstIdx).getTableCards().add(combo);
				}
				players.get(i).getTableCards().clear();
				break;
			}
		}
	}
	/**
	 * In case of a 200-0 victory, updates the statistics with precise values.
	 *
	 * @param winIdx The index of the winning player (precisely team).
	 */
	private void setInstantWinStat(int winIdx) {
		players.get(winIdx).setPoints(100);
		players.get(winIdx+1).setPlacement(4);
		players.get(winIdx+2).setPoints(100);
		players.get((winIdx+3)%4).setPlacement(4);
	}
	/**
	 * Updates player points based on Tichu declarations (Grand Tichu and Tichu) for Statistics.
	 */
	private void setPlayerPointsTichu() {
		for(Player pl: players) {
			if(pl.getSaidGrandTichu()) {
				if(pl.getPlacement()==1) {
					pl.setPoints(pl.getPoints()+200);
				} else pl.setPoints(pl.getPoints()-200);
			} else if(pl.getSaidTichu()) {
				if(pl.getPlacement()==1) {
					pl.setPoints(pl.getPoints()+100);
				} else pl.setPoints(pl.getPoints()-100);
			}
		}
	}
	
	/**
	 * Updates the game statistics after the game has ended.
	 *
	 * @param originalT1 Original team 1 points before the game.
	 * @param originalT2 Original team 2 points before the game.
	 */
	private void updateGameStat(int originalT1, int originalT2) {
		String fileName="Stat"+File.separator+"games.txt";
		String gameStr=readFileToString(fileName);
		String newLine="";
		for(int i=0; i<4; i++) {
			newLine=newLine.concat(players.get(i).getName()+" "+players.get(i).getPlacement()+" ");
		}
		String t1point=((Integer)(teamPoints[0]-originalT1)).toString();
		String t2point=((Integer)(teamPoints[1]-originalT2)).toString();
		newLine=newLine.concat(t1point+" "+t2point+"\n");
		gameStr=gameStr.concat(newLine);
		writeFile(fileName, gameStr);
	}
	/**
	 * Updates team statistics after the game has ended.
	 *
	 * @param team  The team name.
	 * @param point The points earned/lost in the game.
	 * @param win   Whether the team won or lost.
	 */
	private void updateTeamStat(String team, int point, boolean win) {
		System.out.println("Team "+team+point);
		String fileName="Stat"+File.separator+"teams.txt";
		String teamStr=readFileToString(fileName);
		String[] teams=teamStr.split("\n");
		String newLine="";
		int idx=searchInStrArr(team, teams);
		if(idx==-1) {
			String p=((Integer)point).toString();
			String w;
			if(win) w="100.00";
			else w="0.00";
			newLine=team+" 1 "+p+" "+w+"\n";
		} else {
			newLine=teams[idx];
			String[] attr=newLine.split(" ");
			int num=Integer.parseInt(attr[1]);
			int points=Integer.parseInt(attr[2]);
			points+=point;
			double wcnt=Double.parseDouble(attr[3]);
			wcnt=(wcnt*num)/100;
			num++;
			if(win) wcnt++;
			wcnt=(wcnt/num)*100;
			newLine=attr[0]+" "+((Integer)num).toString()+" "+((Integer)points).toString()+" "+((Double)wcnt).toString()+"\n";
		}
		teamStr=newLine;
		for(int i=0; i<teams.length; i++) {
			if(i==idx) continue;
			teamStr=teamStr.concat(teams[i]+"\n");
		}
		writeFile(fileName, teamStr);
	}
	/**
	 * Updates player statistics after the game has ended.
	 *
	 * @param name      The player's name.
	 * @param point     The points earned/lost in the game.
	 * @param win       Whether the player won or lost.
	 * @param placement The final placement of the player.
	 * @param tichu     Whether the player declared Tichu and won during the game.
	 */
	private void updatePlayerStat(String name, int point, boolean win, int placement, boolean tichu) {
		String fileName="Stat"+File.separator+"players.txt";
		String playerStr=readFileToString(fileName);
		String[] players=playerStr.split("\n");
		String newLine="";
		int idx=searchInStrArr(name, players);
		if(idx==-1) {
			String p=((Integer)point).toString();
			String w="0.00";
			if(win) w="100.00";
			String t="0";
			if(tichu) t="1";
			newLine=name+" 1 "+p+" "+p+" "+w+" "+((Integer)placement).toString()+" "+t+"\n";
		} else {
			newLine=players[idx];
			String[] attr=newLine.split(" ");
			int num=Integer.parseInt(attr[1]);
			int points=Integer.parseInt(attr[2]);
			points+=point;
			int maxpoint=Integer.parseInt(attr[3]);
			if(maxpoint<point) maxpoint=point;
			
			double wcnt=Double.parseDouble(attr[4]);
			double pcnt=Double.parseDouble(attr[5]);
			wcnt=(wcnt*num)/100;
			pcnt=(pcnt*num+placement)/(num+1);
			num++;
			if(win) wcnt++;
			wcnt=(wcnt/num)*100;
			int tcnt=Integer.parseInt(attr[6]);
			if(tichu) tcnt++;
			newLine=attr[0]+" "+((Integer)num).toString()+" "+((Integer)points).toString()+" "+((Integer)maxpoint).toString()+" "+((Double)wcnt).toString()+" "+((Double)pcnt).toString()+" "+((Integer)tcnt).toString()+"\n";
		}
		playerStr=newLine;
		for(int i=0; i<players.length; i++) {
			if(i==idx) continue;
			playerStr=playerStr.concat(players[i]+"\n");
		}
		writeFile(fileName, playerStr);
	}
	/**
	 * Searches for a string in an array of strings based on the beginning.
	 *
	 * @param name The string to search for in the beginning of strings.
	 * @param arr  The array of strings to search in.
	 * @return The index of the string in the array, or -1 if not found.
	 */
	private int searchInStrArr(String name, String[] arr) {
		if(arr.length==0) return -1;
		for(int i=0; i<arr.length; i++) {
			if(arr[i].length()<=name.length()) continue;
			if(arr[i].substring(0, name.length()+1).equals(name+" ")) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * Writes a modified file back.
	 *
	 * @param fileName The name of the file to write.
	 * @param text     The text to write to the file.
	 */
	private void writeFile(String fileName, String text) {
		try {
		 	BufferedWriter bw=new BufferedWriter(new FileWriter(fileName));
			bw.write(text);
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Reads a whole file to one string, adding endlines.
	 * Used to print the file to the information Dialog and updating Stat files.
	 *
	 * @param fileName The name of the file to read.
	 * @return The content of the file as a string.
	 */
    public static String readFileToString(String fileName) {
    	File file=new File(fileName);
    	if(!file.exists()) {
    		return "Váratlan hiba történt";
    	}
    	Scanner sc=null;
		try {
			sc=new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Váratlan hiba történt";
		}
		String text="";
		while(sc.hasNext()) {
			text=text.concat(sc.nextLine());
			text=text.concat("\n");
		}
    	sc.close();
    	return text;
    }
    /**
     * Generates a string representation of the TichuGame object.
     *
     * @return A string representation of the TichuGame.
     */
	public String toString() {
		return "TichuGame [state=" +state + " teampoints=" + teamPoints + " players=" + players + ", actPlacement=" + actPlacement + ", roundCards=" + roundCards + ", roundType=" + roundType + ", actValue=" + actValue + ", wish=" + wish + ", pass=" + pass + ", winnerIdx=" + winnerIdx +", shouldIncreasePlacement=" + shouldIncreasePlacement + ", gameEnd=" + gameEnd + "]";
	}
}