package gameplay;

/**
 * Enumeration representing different types of card combinations in a card game.
 * <p>
 * The possible combination types include:
 * <ul>
 * <li>{@code Single}: A single card.</li>
 * <li>{@code Pair}: A combination of two cards with the same rank.</li>
 * <li>{@code Drill}: A combination of three cards with the same rank.</li>
 * <li>{@code FullHouse}: A combination of three cards of one rank and two cards of another rank.</li>
 * <li>{@code Poker}: A combination of four cards with the same rank.</li>
 * <li>{@code FollowingPairs}: A sequence of pairs in increasing rank order.</li>
 * <li>{@code StraightFlush}: A sequence of cards in increasing rank order, all of the same suit.</li>
 * <li>{@code Straight}: A sequence of cards in increasing rank order.</li>
 * <li>{@code None}: Represents no specific combination type.</li>
 * <li>{@code Pass}: Represents a pass (empty combination).</li>
 * </ul>
 * </p>
 */
enum ComboType {
	Single, Pair, Drill, FullHouse, Poker, FollowingPairs, StraightFlush, Straight, None, Pass
}
