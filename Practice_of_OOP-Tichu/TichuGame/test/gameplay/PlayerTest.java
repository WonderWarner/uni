package gameplay;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This class contains parameterized unit tests for the {@link Player} class.
 * It focuses on testing the behavior of the {@code canSatisfyWish} method under various scenarios.
 *
 * @author Tömöri Péter
 * @version 1.0
 */
@RunWith(Parameterized.class)
public class PlayerTest {

	Player player;
	ArrayList<Card> cards;
	int size;
	ComboType type;
	int value;
	int wish;
	boolean canSatisfyWish;
	
	/**
     * Constructs a new {@code PlayerTest} instance with the specified parameters.
     *
     * @param cards             The list of cards to be added to the player's hand.
     * @param size              The expected size of the combination.
     * @param type              The expected combination type.
     * @param value             The expected value of the combination.
     * @param wish              The wish value for the test.
     * @param canSatisfyWish    The expected result of the {@code canSatisfyWish} method.
     */
	public PlayerTest(ArrayList<Card> cards, int size, ComboType type, int value, int wish, boolean canSatisfyWish) {
		player=new Player("A");
		for(Card card: cards) {
			player.addHandCard(card);
		}
		player.sortHandCards();
		this.cards=cards;
		Collections.sort(cards);
		this.size=size;
		this.type=type;
		this.value=value;
		this.wish=wish;
		this.canSatisfyWish=canSatisfyWish;
	}
	
	/**
     * Tests the {@code canSatisfyWish} method with various combinations of cards and wishes.
     */
	@Test
	public void testWish() {
		boolean methodSays=player.canSatisfyWish(size, type, value, wish);
		System.out.println(player.getHandCards());
		assertEquals(canSatisfyWish, methodSays);
	}
	
	 /**
     * Provides parameters for the parameterized tests.
     *
     * @return A list of object arrays representing different test scenarios with card combinations.
     */
	@Parameters
	public static List<Object[]> parameters() {
		List<Object[]> params = new ArrayList<Object[]>();
		ArrayList<ArrayList<Card>> cards=new ArrayList<>();
		//Can, single
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		params.add(new Object[] {cards.get(cards.size()-1), 1, ComboType.Single, 1, 2, true});
		//Can't (value), single
		params.add(new Object[] {cards.get(cards.size()-1), 1, ComboType.Single, 7, 2, false});
		//Can't (no card), single
		params.add(new Object[] {cards.get(cards.size()-1), 1, ComboType.Single, 1, 6, false});
		//Can't (both value and wish), single
		params.add(new Object[] {cards.get(cards.size()-1), 1, ComboType.Single, 4, 3, false});
		//Can, single 2.0
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 3, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 4, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 6, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 7, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 7, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 8, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 11, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 12, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 13, 10));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 10, 10));
		params.add(new Object[] {cards.get(cards.size()-1), 1, ComboType.Single, 4, 7, true});
		//Can, straight
		params.add(new Object[] {cards.get(cards.size()-1), 5, ComboType.Straight, 3, 7, true});
		params.add(new Object[] {cards.get(cards.size()-1), 6, ComboType.Straight, 2, 6, true});
		//Can't (value), straight
		params.add(new Object[] {cards.get(cards.size()-1), 6, ComboType.Straight, 3, 4, false});
		params.add(new Object[] {cards.get(cards.size()-1), 5, ComboType.Straight, 6, 6, false});
		//Can't (size), straight
		params.add(new Object[] {cards.get(cards.size()-1), 5, ComboType.Straight, 6, 13, false});
		params.add(new Object[] {cards.get(cards.size()-1), 7, ComboType.Straight, 2, 5, false});
		//Can't (no card), straight
		params.add(new Object[] {cards.get(cards.size()-1), 5, ComboType.Straight, 1, 14, false});
		//Can, straight 2.0
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 3, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 4, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 6, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 7, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 7, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 8, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 11, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 12, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 13, 10));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 10, 10));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 14, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 9, 0));
		params.add(new Object[] {cards.get(cards.size()-1), 5, ComboType.Straight, 3, 4, true});
		params.add(new Object[] {cards.get(cards.size()-1), 8, ComboType.Straight, 6, 10, true});
		return params;
	}
}
