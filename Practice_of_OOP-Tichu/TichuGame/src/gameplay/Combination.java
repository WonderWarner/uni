package gameplay;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The {@code Combination} class represents a combination of cards in the Tichu game.
 * It includes methods for evaluating the type, value, and validity of the combination.
 * Each combination has a list of cards, a combination type, a value, and flags indicating
 * the presence of the Phoenix and Dragon cards.
 *
 * @author Tömöri Péter
 * @version 1.0
 */
public class Combination {

    private ArrayList<Card> combo;
    private ComboType type;
    /**
     *  if the Phoenix is played as a single card the value will be -1
     *  and the game will know this is the case and accepts the combination without increasing the value
     */
    private int value;
    private boolean isPhoenixIn;
    private boolean isDragonIn;

    /**
     * Constructs a new {@code Combination} with default values.
     * The combination is initially set as invalid with a value of -10.
     */
    Combination() {
        combo = new ArrayList<Card>();
        type = ComboType.None;
        value = -10;
        isPhoenixIn = false;
        isDragonIn = false;
    }

    /**
     * Gets the list of cards in the combination.
     *
     * @return the list of cards in the combination
     */
    ArrayList<Card> getCombo() {
        return combo;
    }

    /**
     * Gets the type of the combination.
     *
     * @return the type of the combination
     */
    ComboType getType() {
        return type;
    }

    /**
     * Gets the value associated with the combination.
     *
     * @return the value of the combination
     */
    int getValue() {
        return value;
    }

    /**
     * Checks if the Phoenix card is present in the combination.
     *
     * @return true if the Phoenix card is in the combination, false otherwise
     */
    boolean getIsPhoenixIn() {
        return isPhoenixIn;
    }

    /**
     * Checks if the Dragon card is present in the combination.
     *
     * @return true if the Dragon card is in the combination, false otherwise
     */
    boolean getIsDragonIn() {
        return isDragonIn;
    }

    /**
     * Adds a card to the combination.
     *
     * @param card the card to add to the combination
     */
    void add(Card card) {
        combo.add(card);
    }

    /**
     * Removes a card from the combination.
     *
     * @param card the card to remove from the combination
     * @return the removed card, or null if the card was not found
     */
    Card remove(Card card) {
        if (combo.size() == 0) return null;
        int idx;
        for (idx = 0; idx < combo.size(); idx++) {
            if (combo.get(idx).equals(card)) {
            	return combo.remove(idx);
            }
        }
        return null;
    }

    /**
     * Evaluates the combination to determine its type, value, and other attributes.
     * This is the only setter of every attribute
     * If the combination is FullHouse, Straight, StraightFlush or Poker, the value of the combination is already set in the check
     */
	void evalCombo() {
		if(combo.size()==0) {
			type=ComboType.Pass;
			value=-5;
			return;
		}
		Collections.sort(combo);
		isPhoenixIn=combo.get(0).getSuit().equals(Suit.Phoenix);
		isDragonIn=combo.get(combo.size()-1).getSuit().equals(Suit.Dragon);
		/**
		 * We only get here if array's length is not 0
		 */
		if(combo.size()==1) {
			value=combo.get(0).getNumber();
            type=ComboType.Single;
            return;
        } else if(isPair()) {
        	value=combo.get(1).getNumber();
        	type=ComboType.Pair;
        	return;
        } else if(isDrill()) {
        	value=combo.get(1).getNumber();
        	type=ComboType.Drill;
        	return;
        } else if(isFullHouse()) {
        	type=ComboType.FullHouse;
        	return;
        } else if(isPoker()) {
        	type=ComboType.Poker;
        	return;
        } else if(isFollowingPairs()) {
        	value=combo.get(1).getNumber();
        	type=ComboType.FollowingPairs;
        	return;
        } else if(isStraightFlush()) {
        	type=ComboType.StraightFlush;
        	return;
        } else if(isStraight()) {
        	type=ComboType.Straight;
        	return;
        } else {
        	value=-10;
        	type=ComboType.None;
        }
	}
	
	/**
	 * Checks if the combination is a pair.
	 *
	 * @return true if the combination is a pair, false otherwise
	 */
	boolean isPair() {
		//we can only get here if the length is at least 2
		if(combo.size()!=2) return false;
		if(combo.get(0).getNumber()==combo.get(1).getNumber()) return true;
		if(isPhoenixIn&&combo.get(1).getNumber()>1&&combo.get(1).getNumber()<15) return true;
		return false;
	}
	
	/**
	 * Checks if the combination is a drill.
	 *
	 * @return true if the combination is a drill, false otherwise
	 */
	boolean isDrill() {
		if(combo.size()!=3) return false;
		if(combo.get(0).getNumber()==combo.get(1).getNumber()&&combo.get(0).getNumber()==combo.get(2).getNumber()) return true;
		if(isPhoenixIn&&combo.get(1).getNumber()==combo.get(2).getNumber()) return true;
		return false;
	}
	
	/**
	 * Checks if the combination is a full house.
	 * Depending on the drills and pairs order the cases can be:
	 * DDDPP, PH P1 P1 P2 P2, PDDDS, PPDDD, PSDDD
	 * @return true if the combination is a full house, false otherwise
	 */
	boolean isFullHouse() {
		if(combo.size()!=5) return false;
		Combination lower=new Combination();
		Combination higher=new Combination();
		/**
		 * Checking if the order is DDDPP
		 */
		for(int i=0; i<3; i++) {
			lower.add(combo.get(i));
		}
		for(int i=3; i<5; i++) {
			higher.add(combo.get(i));
		}
		/**
		 * Works because the default is that isPhoenixIn=false and it's already in order
		 */
		if(lower.isDrill()&&higher.isPair()) {
			value=lower.combo.get(0).getNumber();
			return true;
		}
		/**
		 * Checking if the order is PH P1 P1 P2 P2
		 * The Phoenix automatically creates a Drill with the higher pair
		 * easier to start with this, because to check PPDDD we should rearrange
		 * but for this we don't need to
		 */
		Combination midlow=new Combination();
		for(int i=1; i<3; i++) {
			midlow.add(combo.get(i));
		}
		if(isPhoenixIn&&midlow.isPair()&&higher.isPair()) {
			value=higher.combo.get(0).getNumber();
			return true;
		}
		/**
		 * Checking if the order is PDDDS
		 * The number of the single card must be valid as well (can't be Dragon)
		 */
		midlow.add(combo.get(3));
		if(isPhoenixIn&&midlow.isDrill()&&combo.get(4).getNumber()<15) {
			value=midlow.combo.get(0).getNumber();
			return true;
		}
		/**
		 * Checking if the order is PPDDD
		 * by putting the last of the lower to the higher
		 */
		higher.add(lower.remove(lower.combo.get(2)));
		if(lower.isPair()&&higher.isDrill()) {
			value=higher.combo.get(0).getNumber();
			return true;
		}
		/*
		 * Checking if the order is PSDDD
		 * the number of the single card must be valid as well (can't be Dog or Sparrow)
		 */
		if(isPhoenixIn&&higher.isDrill()&&combo.get(1).getNumber()>1) {
			value=midlow.combo.get(0).getNumber();
			return true;
		}
		return false;
	}
	/**
	 * Checks if the combination is a poker.
	 * Poker is the small bomb
	 * Phoenix can't be used
	 * 	The value is higher than everything except any StraightFlush and
	 * 	it must be at least 21, but the number also matters to distinguish pokers as well.
	 *	value=12*number (at least 24, at most 168)
	 *
	 * @return true if the combination is a poker, false otherwise
	 */
	boolean isPoker() {
		if(combo.size()!=4) return false;
		int number=combo.get(0).getNumber();
		for(int i=1; i<4; i++) {
			if(combo.get(i).getNumber()!=number) return false;
		}
		value=12*number;
		return true;
	}
	
	/**
	 * Checks if the combination is a sequence of pairs.
	 *
	 * @return true if the combination is a sequence of pairs, false otherwise
	 */
	boolean isFollowingPairs() {
		int size=combo.size();
		if(size%2!=0) return false;
		if(isDragonIn) return false;
		/**
		 *  Case if there is no Phoenix
		 */
		if(!isPhoenixIn) {
			int expected=combo.get(0).getNumber();
			for(int i=0; i<size; i+=2) {
				if(combo.get(i).getNumber()!=combo.get(i+1).getNumber()||combo.get(i).getNumber()!=expected) return false;
				expected++;
			}
			return true;
		} else {
			/**
			 * Case there is Phoenix in combo
			 * Good cases that can happen: Last card will be the single
			 * First card after Phoenix will be the single
			 * Somewhere in between will be a single
			 */
			boolean mistake=false;
			int expected=combo.get(1).getNumber();
			int end=size-1;
			int i;
			for(i=1; i<end; i+=2) {
				if(combo.get(i).getNumber()==combo.get(i+1).getNumber()&&expected==combo.get(i).getNumber()) {
					expected++;
				} else if(expected==combo.get(i).getNumber()&&(expected+1)==combo.get(i+1).getNumber()) {
					/**
					 * Case it already made mistake and now made another one
					 */
					if(mistake) {
						return false;
					}
					expected++;
					mistake=true;
					i--;
				} else 
					/**
					 * If the card increased with more than 1, we can't complete two pairs
					 */
					return false;
			}
			/**
			 * At the end it's still not a valid sequence of pairs, if it contained Phoenix still there was no mistakes=everything was in pair
			 * Except the case when the last card is the solo, but in that case it should be exactly one number higher than the previous one
			 */
			if(!mistake&&combo.get(end).getNumber()!=expected) {
				return false;
			} else if((!mistake&&i==end)||(mistake&&i==size)) {
				return true;
			} else return false;
		}
	}
	
	/**
	 * Checks if the combination is a straight flush.
	 * 
	 * StraightFlush is the big bomb
	 * Phoenix can't be used
	 * The value is higher than everything except
	 *	StraightFlush with the same size but higher starting number or
	 *	a StraightFlush that is bigger
	 * Value must be at least 168 (12*14, the highest Poker)
	 *	calculating value: 4 * starting number * size * size
	 *	min: 2, 3, 4, 5, 6; 4*2*5*5=200
	 *	max: 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14; 4*2*13*13=1352
	 *  The aim is, that no matter how high the starting card will be, the size is the primer aspect
	 *	5*5 (min size) is bigger than the highest card that we can start with 10
	 *
	 * @return true if the combination is a straight flush, false otherwise
	 */
	boolean isStraightFlush() {
		/**
		 * At least size of 5 cards, can't contain any special cards
		 */
		if(combo.size()<5||combo.get(0).getNumber()<2||isDragonIn) return false;
		/**
		 * Every card should have the same Suit and follow each other directly
		 */
		Suit expectedSuit=combo.get(0).getSuit();
		int expectedNum=combo.get(0).getNumber()+1;
        for(int i=1; i<combo.size(); i++) {
            if(combo.get(i).getSuit()!=expectedSuit||combo.get(i).getNumber()!=expectedNum) {
                return false;
            } else expectedNum++;
        }
        value=4*combo.get(0).getNumber()*combo.size()*combo.size();
        return true;
    }
	
	/**
	 * Checks if the combination is a straight.
	 * Value is the lowest Card in the Straight
	 * Phoenix will be the highest Card possible
	 *
	 * @return true if the combination is a straight, false otherwise
	 */
	boolean isStraight() {
		/**
		 * Size: at least 5, can't contain Dog and Dragon
		 */
		if(combo.size()<5) return false;
		if(combo.get(0).getNumber()==0||combo.get(1).getNumber()==0||isDragonIn) return false;
		if(!isPhoenixIn) {
			int expectedNum=combo.get(0).getNumber()+1;
	        for(int i=1; i<combo.size(); i++) {
	            if(combo.get(i).getNumber()!=expectedNum) {
	                return false;
	            } else expectedNum++;
	        }
	        value=combo.get(0).getNumber();
	        return true;
		}
		else {
			int mistake=0;
			for(int i=2; i<combo.size(); i++) {
				mistake+=combo.get(i).getNumber()-combo.get(i-1).getNumber()-1;
				if(mistake>1) return false;
			}
			if(mistake==1) value=combo.get(1).getNumber();
			else if(combo.get(combo.size()-1).getNumber()==14) value=combo.get(1).getNumber()-1;
			else value=combo.get(1).getNumber();
			return true;
		}
	}
	/**
	 * Compares the type of this combination with another combination.
	 *
	 * @param other the other combination to compare
	 * @return true if the types of the combinations are equal, false otherwise
	 */
	boolean typeEquals(Combination other) {
		return (this.combo.equals(other.combo));
	}
}
