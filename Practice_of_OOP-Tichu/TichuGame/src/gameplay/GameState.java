package gameplay;

/**
 * Enumeration representing different states of the game.
 * <p>
 * The possible game states include:
 * <ul>
 * <li>{@code PlayerInit}: Player initialization, game hasn't actually started yet.</li>
 * <li>{@code GrandTichu}: Grand Tichu phase, one and only opportunity to earn (or lose) 200 points.</li>
 * <li>{@code ExchangeCards}: Players exchange cards, giving and getting one from every other player.</li>
 * <li>{@code InRound}: Game is currently in game, every previous state is finished.</li>
 * <li>{@code InWish}: Wish phase, a player has to select what to wish.</li>
 * <li>{@code InDragon}: Dragon phase, winner player has to choose which opponent to give the  rounds' taken cards.</li>
 * <li>{@code Ended}: The state indicating that the game has ended.</li>
 * </ul>
 * Setting the game state can be useful for managing the flow of the game and ensuring
 * the correct view or actions are taken based on the current state.
 * If the game is in state X it means X should be done, it is the next thing after load
 * </p>
 */
public enum GameState {
	PlayerInit, GrandTichu, ExchangeCards, InRound, InWish, InDragon, Ended
}