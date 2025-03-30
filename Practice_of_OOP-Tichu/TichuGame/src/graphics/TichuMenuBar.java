package graphics;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Represents the menu bar for the Tichu game's graphical user interface.
 * It includes menus for game-related actions and help-related information.
 */
public class TichuMenuBar extends JMenuBar {
    
	/** Game menu containing items related to game actions. */
    JMenu gameMenu = new JMenu("Game");

    /** Help menu containing items providing assistance and information. */
    JMenu helpMenu = new JMenu("Help");

    /** Menu item for returning to the main menu. */
    JMenuItem menuItem = new JMenuItem("Main Menu");

    /** Menu item for saving the current game state. */
    JMenuItem saveItem = new JMenuItem("Save");

    /** Menu item for loading a previously saved game state. */
    JMenuItem loadItem = new JMenuItem("Load");

    /** Menu item for exiting the Tichu game. */
    JMenuItem exitItem = new JMenuItem("Exit");

    /** Menu item for viewing game statistics. */
    JMenuItem statItem = new JMenuItem("Statistics");

    /** Menu item for accessing the rules of the Tichu game. */
    JMenuItem ruleItem = new JMenuItem("Rules");

    /** Menu item for accessing a 'How-to' guide. */
    JMenuItem howtoItem = new JMenuItem("\'How-to\'");
	
    /**
     * Constructs a new TichuMenuBar.
     * Initializes the menus, menu items, and their hierarchical structure.
     */
    public TichuMenuBar() {
        //Add items to Game menu
        gameMenu.add(menuItem);
        gameMenu.add(saveItem);
        gameMenu.add(loadItem);
        gameMenu.add(exitItem);
        gameMenu.add(statItem);
        // Add Game menu to the MenuBar
        add(gameMenu);
        //Add items to Help menu
        helpMenu.add(ruleItem);
        helpMenu.add(howtoItem);
        //Add Help menu to the MenuBar
        add(helpMenu);
    }
}