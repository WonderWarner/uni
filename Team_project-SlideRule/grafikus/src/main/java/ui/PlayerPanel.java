package ui;

import controller.Controller;
import model.Item;
import model.Person;
import model.Room;
import model.Student;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** 
 * A játékosokat megjelenítő panel. A játékosokhoz tartozó információkat jeleníti meg, valamint lehetőséget ad a játékosok által végrehajtható műveletek elvégzésére.
 */
public class PlayerPanel extends JComponent {

    /** 
     * A játékos, akinek a panel tartozik.
     */
    private final Student student;

    /** 
     * A játékos neve.
     */
    private final String name;

    /** 
     * Az ajtókat megjelenítő panel.
     */
    private final JPanel doorsPanel = new JPanel();

    /** 
     * A személyeket megjelenítő panel.
     */
    private final JPanel peopleInRoomPanel = new JPanel();

    /** 
     * A szobában tárgyakat megjelenítő panel.
     */
    private final JPanel itemsInRoomPanel = new JPanel();

    /** 
     * A játékos által tartott tárgyakat megjelenítő panel.
     */
    private final JPanel itemsInHandPanel = new JPanel();

    /** 
     * A játékos által kiválasztott ajtó.
     */
    private DoorPanel doorSelected = null;

    /** 
     * A játékos által kiválasztott tárgy a szobában.
     */
    private ItemPanel itemInRoomSelected = null;

    /** 
     * A játékos által kiválasztott tárgy a kezében.
     */
    private ItemPanel itemInHandSelected = null;

    /** 
     * A játékhoz kapcsolódó hasznos állapokat megjelenítő panel.
     */
    private final JPanel statusPanel = new JPanel();

    /** 
     * Az ajtón való belépésre szolgáló gomb.
     */
    private final JButton enterButton = new JButton("Enter");

    /** 
     * A tárgy felvételére szolgáló gomb.
     */
    private final JButton pickUpButton = new JButton("Pick Up");

    /** 
     * A tárgy eldobására szolgáló gomb.
     */
    private final JButton dropButton = new JButton("Drop");

    /** 
     * A tárgy aktiválására szolgáló gomb.
     */
    private final JButton activateButton = new JButton("Activate");

    /** 
     * Konstruktor, beállítja a játékost és a nevét.
     * Létrehozza a panelen megjelenítendő részeket, és hozzáadja őket a panelhez.
     * Beállítja a gombokhoz tartozó eseménykezelőket.
     * A panel hátterét a játékos szobájának színére állítja. 
     * 
     * @param student A játékos, akinek a panel tartozik.
     * @param name A játékos neve.
     */
    public PlayerPanel(Student student, String name){
        this.student = student;
        this.name = name;

        Color roomColor = Controller.getRoomColor( student.getLocation() );

        JPanel buttonsPanel = new JPanel();

        doorsPanel.setBackground(roomColor);
        peopleInRoomPanel.setBackground(roomColor);
        itemsInRoomPanel.setBackground(roomColor);

        itemsInHandPanel.setBackground( Color.lightGray );
        buttonsPanel.setBackground(Color.lightGray);
        statusPanel.setBackground(Color.lightGray);

        enterButton.addActionListener(e -> Controller.enterButtonPressed());
        pickUpButton.addActionListener(e -> Controller.pickUpButtonPressed());
        dropButton.addActionListener(e -> Controller.dropButtonPressed());
        activateButton.addActionListener(e -> Controller.activateButtonPressed());

        enterButton.setEnabled(false);
        pickUpButton.setEnabled(false);
        dropButton.setEnabled(false);
        activateButton.setEnabled(false);

        buttonsPanel.add(enterButton);
        buttonsPanel.add(pickUpButton);
        buttonsPanel.add(dropButton);
        buttonsPanel.add(activateButton);

        Border border = BorderFactory.createLineBorder(Color.BLACK);

        doorsPanel.setBorder(border);
        peopleInRoomPanel.setBorder(border);
        itemsInRoomPanel.setBorder(border);
        itemsInHandPanel.setBorder(border);
        buttonsPanel.setBorder(border);
        statusPanel.setBorder(border);

        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));

        addSmallPanels();

        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );

        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BoxLayout(topPanel,BoxLayout.X_AXIS) );
        topPanel.add(doorsPanel);
        topPanel.add(peopleInRoomPanel);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout( new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.add(itemsInRoomPanel);
        middlePanel.add(itemsInHandPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(buttonsPanel);
        bottomPanel.add(statusPanel);

        add(topPanel);
        add(middlePanel);
        add(bottomPanel);

        doorsPanel.setSize( 400, 200 );
        peopleInRoomPanel.setSize( 400, 200 );
        itemsInRoomPanel.setSize( 400, 200 );
        itemsInHandPanel.setSize(400,200);

    }

    /** 
     * Hozzáadja a felülethez a kisebb paneleket. Státuszpanelt, ajtókat, személyeket, tárgyakat.
     */
    private void addSmallPanels(){
        addStatusPanel();
        addDoorPanel();

        for(Person personInRoom : student.getLocation().getPeopleInRoom()){
            if( personInRoom != student ){
                peopleInRoomPanel.add( new PersonPanel(personInRoom) );
            }
        }

        addItemRoomPanel();
        addItemHandPanel();
    }

    /** 
     * Legerálja a státuszpanelt és hozzáadja a felülethez.
     * A státuszpanel tartalmazza a játékos nevét, a játékost, aki a panelhez tartozik, a szoba kapacitását, ragadóságát, gázos-e a szoba, és hogy átok van-e a szobában.
     * Hozzáad egy gombot, amellyes a játékos befejezheti a körét.
     */
    private void addStatusPanel() {
        statusPanel.add( new JLabel(name));
        statusPanel.add(new PersonPanel(student));
        int stun=student.getStunRemaining();
        if(stun > 0 ) statusPanel.add(new JLabel("Stunned for "+stun+" turns"));
        statusPanel.add( new JLabel("Room capacity: "+student.getLocation().getCapacity()));
        statusPanel.add( new JLabel("Room stickiness: "+student.getLocation().getStickiness()));
        if(student.getLocation().isGas()) statusPanel.add( new JLabel("Room is gassed"));
        if(student.getLocation().isCurseActive()) statusPanel.add( new JLabel("Room is cursed right now"));

        JButton endButton = new JButton("End Turn");
        endButton.addActionListener(e -> Controller.endButtonPressed());
        statusPanel.add( endButton );

        statusPanel.add( new JLabel(Controller.getTurnsLeft()+" turns left."));
        statusPanel.add( new JLabel("Actions remaining: "+Controller.getActionsRemaining()));
    }

    /** 
     * Hozzáadja a felülethez az ajtókat megjelenítő paneleket.
     * Az ajtókra kattintva a játékos át tud lépni a szobák között.
     * Ha az ajtóra kattint a játékos, akkor az ajtó kijelölődik, és a belépés gomb aktiválódik.
     */
    private void addDoorPanel() {
        for(Room neighbour : student.getLocation().getNeighbours()){
            DoorPanel doorPanel = new DoorPanel(neighbour);
            doorPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    DoorPanel lastSelected = doorSelected;
                    doorSelected = doorPanel;
                    if(doorSelected == lastSelected) {
                        doorSelected = null;
                        lastSelected.setSelected(false);
                        enterButton.setEnabled(false);
                        return;
                    }
                    enterButton.setEnabled(true);
                    doorSelected.setSelected(true);
                    if(lastSelected != null) lastSelected.setSelected(false);
                }
            });
            doorsPanel.add( doorPanel );
        }
    }


    /** 
     * Hozzáadja a felülethez a szobában található tárgyakat megjelenítő paneleket.
     * A tárgyakra kattintva a játékos fel tudja venni a tárgyat.
     * Ha a tárgyra kattint a játékos, akkor a tárgy kijelölődik, és a felvétel gomb aktiválódik.
     */
    private void addItemRoomPanel() {
        for(Item itemInRoom : student.getLocation().getItemsInRoom()){
            ItemPanel itemPanel = new ItemPanel(itemInRoom);
            itemPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    ItemPanel lastSelected = itemInRoomSelected;
                    itemInRoomSelected = itemPanel;
                    if(itemInRoomSelected == lastSelected) {
                        itemInRoomSelected = null;
                        lastSelected.setSelected(false);
                        pickUpButton.setEnabled(false);
                        return;
                    }
                    pickUpButton.setEnabled(true);
                    itemInRoomSelected.setSelected(true);
                    if(lastSelected != null) lastSelected.setSelected(false);
                }
            });
            itemsInRoomPanel.add(itemPanel);
        }
    }

    /** 
     * Hozzáadja a felülethez a játékos által tartott tárgyakat megjelenítő paneleket.
     * A tárgyakra kattintva a játékos aktiválhatja a tárgyat.
     * Ha a tárgyra kattint a játékos, akkor a tárgy kijelölődik, és az aktiválás gomb aktiválódik.
     */
    private void addItemHandPanel() {
        for( Item itemInHand : student.getItemsInHand() ){
            ItemPanel itemPanel = new ItemPanel(itemInHand);
            itemPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    ItemPanel lastSelected = itemInHandSelected;
                    itemInHandSelected = itemPanel;
                    if(itemInHandSelected == lastSelected) {
                        itemInHandSelected = null;
                        lastSelected.setSelected(false);
                        activateButton.setEnabled(false);
                        dropButton.setEnabled(false);
                        return;
                    }
                    activateButton.setEnabled(true);
                    dropButton.setEnabled(true);
                    itemInHandSelected.setSelected(true);
                    if(lastSelected != null) lastSelected.setSelected(false);
                }
            });
            itemsInHandPanel.add(itemPanel);
        }
    }

    /** 
     * Frissíti a panelen megjelenített információkat.
     * Az ajtókat, személyeket, tárgyakat újrarajzolja.
     * A kijelöléseket visszaállítja.
     */
    public void reDraw(){
        doorsPanel.removeAll();
        peopleInRoomPanel.removeAll();
        itemsInRoomPanel.removeAll();
        itemsInHandPanel.removeAll();
        statusPanel.removeAll();

        doorSelected = null;
        itemInRoomSelected = null;
        itemInHandSelected = null;

        enterButton.setEnabled(false);
        pickUpButton.setEnabled(false);
        dropButton.setEnabled(false);
        activateButton.setEnabled(false);

        addSmallPanels();

        Color roomColor = Controller.getRoomColor(student.getLocation());
        doorsPanel.setBackground(roomColor);
        peopleInRoomPanel.setBackground(roomColor);
        itemsInRoomPanel.setBackground(roomColor);
    }

    /** 
     * Visszaadja a játékost.
     * 
     * @return A játékos.
     */
    public DoorPanel getDoorSelected(){
        return doorSelected;
    }

    /** 
     * Visszaadja a játékos által kiválasztott tárgyat, ami a szobában van.
     * 
     * @return A tárgy.
     */
    public ItemPanel getItemInRoomSelected(){
        return itemInRoomSelected;
    }

    /** 
     * Visszaadja a játékos által kiválasztott tárgyat, ami a kezében van.
     * 
     * @return A tárgy.
     */
    public ItemPanel getItemInHandSelected(){
        return  itemInHandSelected;
    }
}
