package gameplay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Represents a player.
 * <p>
 * The player has a name, a set of hand cards, table cards earnt during game,
 * information about whether they said Grand Tichu or Tichu, points, starting status,
 * ability to say Tichu, and placement in the game.
 * </p>
 * <p>
 * The class provides methods to manipulate player attributes such as setting the name,
 * getting hand and table cards, evaluating points, checking if the player can satisfy a wish,
 * and more.
 * </p>
 * <p>
 * This class is designed to be serialized, enabling saving and loading player states during the game.
 * </p>
 *
 * @see Card
 * @see ComboType
 */
public class Player implements Serializable {
	
	protected String name;
	protected ArrayList<Card> handCards;
	protected ArrayList<ArrayList<Card>> tableCards;
	protected boolean saidGrandTichu;
	protected boolean saidTichu;
	protected int points;
	protected boolean starts;
	protected boolean canSayTichu;
	protected int placement;
	
	Player(String n) {
		name=n;
		handCards=new ArrayList<Card>();
		tableCards=new ArrayList<ArrayList<Card>>();
		points=0;
		saidGrandTichu=false;
		saidTichu=false;
		starts=false;
		canSayTichu=true;
		placement=0;
	}
	
    /**
     * Sets the name of the player.
     *
     * @param n The new name for the player.
     */
	void setName(String n) { name=n; }
    /**
     * Retrieves the name of the player.
     *
     * @return The name of the player.
     */
	public String getName() { return name; }
    /**
     * Retrieves the hand cards of the player.
     *
     * @return The hand cards of the player.
     */
	public ArrayList<Card> getHandCards() { return handCards; }
    /**
     * Adds a card to the hand of the player.
     *
     * @param c The card to be added.
     */
	void addHandCard(Card c) { handCards.add(c); }
    /**
     * Sorts the hand cards of the player.
     */
	void sortHandCards() { Collections.sort(handCards); }
    /**
     * Retrieves the table cards of the player.
     *
     * @return The table cards of the player.
     */
	ArrayList<ArrayList<Card>> getTableCards() { return tableCards; }
    /**
     * Sets whether the player said Grand Tichu.
     *
     * @param sgt The new value for whether the player said Grand Tichu.
     */
	void setSaidGrandTichu(boolean sgt) { saidGrandTichu=sgt; }
    /**
     * Retrieves whether the player said Grand Tichu.
     *
     * @return Whether the player said Grand Tichu.
     */
	boolean getSaidGrandTichu() { return saidGrandTichu; }
    /**
     * Sets whether the player said Tichu.
     *
     * @param st The new value for whether the player said Tichu.
     */
	void setSaidTichu(boolean st) { saidTichu=st; }
    /**
     * Retrieves whether the player said Tichu.
     *
     * @return Whether the player said Tichu.
     */
	boolean getSaidTichu() { return saidTichu; }
    /**
     * Sets the points of the player.
     *
     * @param p The new points value for the player.
     */
	void setPoints(int p) { points=p; }
    /**
     * Retrieves the points of the player.
     *
     * @return The points of the player.
     */
	int getPoints() { return points; }
    /**
     * Evaluates the points of the player based on table cards.
     * This method updates the player's points attribute.
     */
	void evalPoints() {
		for(ArrayList<Card> combo: tableCards) {
			for(Card c: combo) {
				points+=c.getPoints();
			}
		}
	}
    /**
     * Sets whether the player starts the round.
     *
     * @param first The new value for whether the player starts the round.
     */
	void setStarts(boolean first) { starts=first; }
    /**
     * Retrieves whether the player is the next to move.
     *
     * @return Whether the player is the next.
     */
	boolean getStarts() { return starts; }
    /**
     * Sets whether the player can say Tichu.
     *
     * @param cst The new value for whether the player can say Tichu.
     */
	void setCanSayTichu(boolean cst) { canSayTichu=cst; }
    /**
     * Retrieves whether the player can say Tichu.
     *
     * @return Whether the player can say Tichu.
     */
	boolean getCanSayTichu() { return canSayTichu; }
    /**
     * Sets the placement of the player in the game.
     *
     * @param number The placement number.
     */
	void setPlacement(int number) { placement=number; }
    /**
     * Retrieves the placement of the player in the game.
     *
     * @return The placement number.
     */
	int getPlacement() { return placement; }
    /**
     * Checks if the player can satisfy a wish with the given parameters.
     * Phoenix don't have to be played to satisfy a wish
     * Only have to satisfy wish in case of Single and Straight comboType-s
     *
     * @param size  The size of the latest combination.
     * @param type  The type of the latest combination.
     * @param value The value of the latest combination.
     * @param wish  The wished number.
     * @return True if the player can satisfy the wish, false otherwise.
     */
	public boolean canSatisfyWish(int size, ComboType type, int value, int wish) {
		if(wish<=value||getHandCards().size()<size) {
			/*
			 * if the player has bomb with the wished number they have to put it down as well
			 * only implemented for Poker, not for StraightFlush
			 */
			int cnt=0;
			for(Card c: handCards) {
				if(c.getNumber()==wish) cnt++;
			}
			if(cnt==4) return true;
			else {
				return false;
			}
		}
		switch(type) {
		case Single:
			for(Card c: handCards) {
				if(c.getNumber()==wish) return true;
			}
			break;
		/**
		 * If we get here, we already know, that the size of the hand is at least the size as the combo
		 */
		case Straight:
			/**
			 * In case of Straight duplicate values doesn't matter
			 * Creating a "set" of values to check if they can make Straight from hand
			 * We don't need phoenix dog and dragon (sparrow can't be in)
			 */
			ArrayList<Integer> valuesInHand=new ArrayList<>();
			for(Card c: handCards) {
				if(!valuesInHand.contains(c.getNumber())) {
					if(c.getNumber()!=-1&&c.getNumber()!=0&&c.getNumber()!=20) valuesInHand.add(c.getNumber());
				}
			}
			int mistake=0;
			/**
			 * Creating every possible good combination and checking if there is any actually good one
			 */
			for(int i=size-1; i<valuesInHand.size(); i++) {
				mistake=0;
				for(int j=i; j>i-size+1; j--) {
					mistake+=(valuesInHand.get(j)-valuesInHand.get(j-1)-1);
					if(mistake>0) {
						break;
					}
				}
				if(mistake==0&&value<valuesInHand.get(i-size+1)&&wish>=valuesInHand.get(i-size+1)&&wish<=valuesInHand.get(i)) {
					return true;
				}
			}
			break;
		default:
			break;
		}
		return false;
	}
    /**
     * Provides a string representation of the player.
     *
     * @return A string representation of the player.
     */
    @Override
	public String toString() {
		return "Player [name=" + name + ", handCards=" + handCards + ", tableCards=" + tableCards + ", saidGrandTichu="
				+ saidGrandTichu + ", saidTichu=" + saidTichu + ", points=" + points + ", starts=" + starts
				+ ", canSayTichu=" + canSayTichu + ", placement=" + placement + "]";
	}
}
