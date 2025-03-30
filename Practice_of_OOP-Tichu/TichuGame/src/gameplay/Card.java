package gameplay;

import java.io.File;
import java.io.Serializable;

/**
 * The {@code Card} class represents a playing card in the Tichu game.
 * Each card has a suit, number, and associated points.
 * It implements the {@code Comparable} interface for card comparison and ordering.
 *
 * @author Tömöri Péter
 * @version 1.0
 */
public class Card implements Comparable<Card>, Serializable {

    private Suit suit;
    /**
     * value (number) of the cards (in order): 2, 3, ... 10, J=11, Q=12, K=13, A=14
     * value of special cards to distinguish: Dr=20, Phoenix=-1, Dog=0, Sparrow=1;
     */
    private int number;
    /**
     * Points: 5: 5, 10: 10, K: 10, Dragon: 25, Phoenix: -25
     */
    private int points;

    /**
     * Constructs a new {@code Card} with the specified suit, number, and points.
     *
     * @param s the suit of the card
     * @param n the number of the card
     * @param p the points associated with the card
     */
    Card(Suit s, int n, int p) {
        suit = s;
        number = n;
        points = p;
    }

    /**
     * Gets the suit of the card.
     *
     * @return the suit of the card
     */
    Suit getSuit() {
        return suit;
    }

    /**
     * Gets the number of the card.
     *
     * @return the number of the card
     */
    int getNumber() {
        return number;
    }

    /**
     * Gets the points associated with the card.
     *
     * @return the points of the card
     */
    int getPoints() {
        return points;
    }

    /**
     * Sets the points associated with the card.
     *
     * @param p the points to set
     */
    void setPoints(int p) {
        points = p;
    }

    /**
     * Gets the file path for the image of the card.
     *
     * @return the file path for the card image
     */
    public String getImagePath() {
        return "CardImage" + File.separator + suit.toString().substring(0, 2) + number + ".png";
    }

    /**
     * String representation of the card.
     *
     * @return a string representation of the card
     */
    @Override
    public String toString() {
        return suit.toString() + " " + Integer.toString(number) + " " + Integer.toString(points);
    }

    /**
     * Compares this card to another card for sorting.
     * Cards are first compared by number and then by suit.
     *
     * @param card the card to compare to
     * @return a negative integer, zero, or a positive integer if this card is less than, equal to,
     *         or greater than the specified card
     */
    @Override
    public int compareTo(Card card) {
        if (this.number == card.getNumber()) {
            return this.suit.compareTo(card.getSuit());
        } else return Integer.compare(this.number, card.getNumber());
    }
}
