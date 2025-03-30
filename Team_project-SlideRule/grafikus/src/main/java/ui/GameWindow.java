package ui;

import controller.Controller;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/** 
 * A játékot megjelenítő ablak. A játékosokat és a játék állapotát jeleníti meg.
 * Az ablakban a játékosok paneljei vannak elrendezve, és a játékosok váltakozva jönnek sorra.
 * Az ablakban található menüben lehet új játékot indítani, kilépni a játékból, illetve információkat kérni a játékról.
 * Az információk egy info.txt fájlból kerülnek beolvasásra.
 */
public class GameWindow extends JFrame {

    /** 
     * A játékosokat és a hozzájuk tartozó panelt tartalmazó HashMap.
     */
    private final HashMap<Student,PlayerPanel> playerPanels = new HashMap<>();

    /** 
     * Az éppen aktív játékos panelje.
     */
    private PlayerPanel actPlayerPanel;

    /** 
     * A játék vége állapotát tároló változó.
     */
    private boolean gameEnded = false;

    /** 
     * Konstruktor, inicializálja az ablakot, a menüt, és a játékosok paneljeit.
     * Az ablak címe "Logarléc" lesz, mérete 800 x 500 px.
     * Az ablakot a megadott játékosokkal hozza létre.
     * Az 1. játékos lesz az aktív játékos.
     * 
     * @param players A játékosok listája.
     */
    public GameWindow(List<Student> players){
        super("Logarléc");
        setMinimumSize(new Dimension(800,500));
        initMenu();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        for( Student player : players ){
            playerPanels.put( player, new PlayerPanel(player, "Player"+(players.indexOf(player)+1)));
        }
        actPlayerPanel = playerPanels.get( players.get(0) );
        add( actPlayerPanel );

        pack();
    }


    /**
     * Inicializálja a menüt.
     * A menüben lehet új játékot indítani, kilépni a játékból, illetve információkat kérni a játékról.
     * Beállítja a grafikus felület objektumainak callback metódusait.
     * Az információk egy info.txt fájlból kerülnek beolvasásra.
     */
    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem menuNewGame = new JMenuItem("New Game");
        JMenuItem menuExit = new JMenuItem("Exit");
        JMenuItem menuInfo = new JMenuItem("Info");
        menuBar.add(menu);
        menu.add(menuNewGame);
        menu.add(menuExit);
        menu.add(menuInfo);
        menuExit.addActionListener( e -> System.exit(0));
        menuNewGame.addActionListener(e->{
            gameEnded = true;
            dispose();
            Controller.showMenu();
        });
        menuInfo.addActionListener( e -> {
            String infoText=readFileToString("resources"+ File.separator+"info.txt");
            JOptionPane.showMessageDialog(null, infoText, "Logarléc: Rules and How-To", JOptionPane.INFORMATION_MESSAGE);
        });
        setJMenuBar(menuBar);
    }

    /** 
     * Segédfüggvény, beolvassa a megadott fájlt egy String-be.
     * 
     * @param fileName A beolvasandó fájl neve.
     * @return A beolvasott fájl tartalma.
     */
    private String readFileToString(String fileName) {
        File file=new File(fileName);
        if(!file.exists()) {
            return "Unexpected Error: file not found";
        }
        Scanner sc;
        try {
            sc=new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Unexpected Error: file not found";
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
     * Megjeleníti a megadott játékost.
     * 
     * @param player A megjelenítendő játékos.
     */
    public void showPlayer( Student player ){
        remove(actPlayerPanel);
        actPlayerPanel = playerPanels.get(player);
        add(actPlayerPanel);
        reDraw();
    }

    /** 
     * Frissíti az ablakot, feltéve ha a játék még fut.
     */
    public void reDraw(){
        if( gameEnded ) return;
        actPlayerPanel.reDraw();
        pack();
        revalidate();
        repaint();
    }

    /** 
     * Visszaadja az éppen aktív játékos paneljét.
     * 
     * @return Az éppen aktív játékos panelje.
     */
    public PlayerPanel getActPlayerPanel(){
        return actPlayerPanel;
    }

    /** 
     * Beállítja a játék vége állapotát.
     */
    public  void endGame(){
        gameEnded = true;
    }
}
