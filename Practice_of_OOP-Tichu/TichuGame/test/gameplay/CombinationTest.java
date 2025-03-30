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
 * The {@code CombinationTest} class is responsible for testing the functionality of the {@link Combination}
 * class, which represents combinations of cards in the Tichu card game. This test class utilizes JUnit and
 * parameterized testing to cover various scenarios and combinations.
 * <p>
 * The tests cover the evaluation of different combinations, such as singles, pairs, following pairs, drills,
 * full houses, straights, straight flushes, and more. Each test case includes the expected combination type,
 * value, presence of the Phoenix card, and presence of the Dragon card.
 * <p>
 * This class is annotated with JUnit annotations for test execution, and it uses the {@link Parameterized}
 * runner to run the parameterized tests with different input combinations.
 *
 * @author Tömöri Péter
 * @version 1.0
 */
@RunWith(Parameterized.class)
public class CombinationTest {

	Combination combo;
	ArrayList<Card> cards;
	ComboType type;
	int value;
	boolean isPhoenixIn;
	boolean isDragonIn;
	
	/**
	 * Constructs a new {@code CombinationTest} instance with the specified parameters.
	 *
	 * @param clist       The list of cards to be used in the combination.
	 * @param t           The expected combination type.
	 * @param v           The expected value of the combination.
	 * @param p           Indicates whether the Phoenix card is expected to be in the combination.
	 * @param d           Indicates whether the Dragon card is expected to be in the combination.
	 */
	public CombinationTest(ArrayList<Card> clist, ComboType t, int v, boolean p, boolean d) {
		combo=new Combination();
		for(Card card: clist) {
			combo.add(card);
		}
		combo.evalCombo();
		cards=clist;
		Collections.sort(cards);
		type=t;
		value=v;
		isPhoenixIn=p;
		isDragonIn=d;
	}
	
	/**
	 * Tests whether the cards in the combination match the expected cards.
	 */
	@Test
	public void testCards() {
		assertEquals(cards.size(), combo.getCombo().size());
		for(int i=0; i<cards.size(); i++) {
			assertEquals(cards.get(i), combo.getCombo().get(i));
		}
	}
	
	/**
	 * Tests whether the combination type matches the expected type.
	 */
	@Test
	public void testType() {
		assertEquals(combo.getType(), type);
	}
	
	/**
	 * Tests whether the combination value matches the expected value.
	 */
	@Test
	public void testValue() {
		assertEquals(combo.getValue(), value);
	}
	
	/**
	 * Tests whether the presence of the Phoenix card in the combination matches the expected value.
	 */
	@Test
	public void testPhoenix() {
		assertEquals(combo.getIsPhoenixIn(), isPhoenixIn);
	}
	
	/**
	 * Tests whether the presence of the Dragon card in the combination matches the expected value.
	 */
	@Test
	public void testDragon() {
		assertEquals(combo.getIsDragonIn(), isDragonIn);
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
		//Pass
		cards.add(new ArrayList<>());
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Pass, -5, false, false});
		//Solo
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Single, 2, false, false});
		//Solo 2.0
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 7, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Single, 7, false, false});
		//Pair
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 2, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Pair, 2, false, false});
		//Pair 2.0
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 13, 10));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 13, 10));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Pair, 13, false, false});
		//Pair with Phoenix
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Pair, 2, true, false});
		//Following Pairs
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 3, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 3, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.FollowingPairs, 2, false, false});
		//Following Pairs with Phoenix
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 4, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 3, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 3, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.FollowingPairs, 2, true, false});
		//Drill
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 9, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 9, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 9, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Drill, 9, false, false});
		//Drill with Phoenix
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 9, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 9, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Drill, 9, true, false});
		//Full House
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 13, 10));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 13, 10));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 13, 10));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.FullHouse, 13, false, false});
		//Full House with Phoenix
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 13, 10));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 13, 10));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.FullHouse, 13, true, false});
		//Straight
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 3, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 4, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 6, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Straight, 2, false, false});
		//Straight with Sparrow
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Sparrow, 1, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 3, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 4, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 6, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Straight, 1, false, false});
		//Straight with Phoenix at end
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 3, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 4, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 6, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Straight, 3, true, false});
		//Straight with Phoenix at start (ending with Ace)
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 12, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 13, 10));
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 14, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 11, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Straight, 10, true, false});
		//Straight with Phoenix in middle
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 12, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 8, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 13, 10));
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 9, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 11, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Straight, 8, true, false});
		//Dragon single
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Dragon, 20, 25));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Single, 20, false, true});
		//Phoenix single
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Single, -1, true, false});
		//Dog single
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Dog, 0, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Single, 0, false, false});
		//Sparrow single
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Sparrow, 1, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Single, 1, false, false});
		//Poker
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 8, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 8, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 8, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 8, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Poker, 96, false, false});
		//Straight Flush
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 3, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 4, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 6, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 7, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.StraightFlush, 288, false, false});
		//Invalid combo (Dragon and Phoenix)
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		cards.get(cards.size()-1).add(new Card(Suit.Dragon, 20, 25));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.None, -10, true, true});
		//Invalid combo (two following cards)
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 3, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.None, -10, false, false});
		//Invalid combo (not following pairs)
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 8, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 8, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.None, -10, false, false});
		//Invalid combo (Poker with Phoenix)
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 8, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 8, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Star, 8, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.None, -10, true, false});
		//Straight Flush but with Phoenix -> simple Straight
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 3, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 6, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 7, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.Straight, 2, true, false});
		//Invalid combo with Phoenix
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 2, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 9, 0));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.None, -10, true, false});
		//Invalid full house with Phoenix and Dog
		cards.add(new ArrayList<>());
		cards.get(cards.size()-1).add(new Card(Suit.Dog, 0, 0));
		cards.get(cards.size()-1).add(new Card(Suit.Sword, 5, 5));
		cards.get(cards.size()-1).add(new Card(Suit.Phoenix, -1, -25));
		cards.get(cards.size()-1).add(new Card(Suit.Jade, 13, 10));
		cards.get(cards.size()-1).add(new Card(Suit.Pagoda, 13, 10));
		params.add(new Object[] {cards.get(cards.size()-1), ComboType.None, -10, true, false});
		return params;
	}
}
