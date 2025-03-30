package gamecontrol;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

/**
 * This class contains unit tests for the {@link Main} class, focusing on the save and load game functionalities.
 * It checks whether the game can be successfully saved to a file and loaded back.
 *
 * @author [Your Name]
 */
public class MainTest {
		
	String[] names={"ww ww", "www", "ww", "w"};
	String[] notNames={"www w","www","ww","w"};
	
	/**
     * Sets up the test environment by creating players and saving the game.
     */
	@Before
	public void setUp() {
		Main.game.createPlayers(names);
		Main.saveGame();
	}
	
	/**
     * Tests the save and load game functionalities.
     *
     * @throws Exception If an error occurs during the test execution.
     */
	@Test
	public void testLoadSave() throws Exception {
		File file=new File("SavedGames"+File.separator+"wwwwwwwwww.txt");
		assertTrue(file.exists());
		assertTrue(Main.loadGame(names));
		assertFalse(Main.loadGame(notNames));
	}
}
