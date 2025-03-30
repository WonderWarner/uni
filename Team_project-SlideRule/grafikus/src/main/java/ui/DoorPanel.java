package ui;

import controller.Controller;
import model.Room;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** 
 * Az ajtókat megjelenítő panel. A szobákhoz tartozó szín alapján jeleníti meg az ajtókat.
 * A szoba színét a Controller osztályból kéri le.
 */
public class DoorPanel extends JComponent {

    /** 
     * A szoba, amelyhez az ajtó tartozik.
     */
    private final Room room;

    /** 
     * A szoba színe.
     */
    private final Color color;

    /** 
     * Konstruktor, beállítja a szoba színét és a panel méretét.
     * A kért méret 50 x 80 px.
     * 
     * @param room A szoba, amelyhez az ajtó tartozik.
     */
    public DoorPanel(Room room){
        this.room = room;
        color = Controller.getRoomColor(room);
        setPreferredSize(new Dimension(50,80));
    }
    
    /** 
     * Beállítja a panel kijelöltségét.
     * A kijelölést egy új határoló vonallal jelzi.
     * 
     * @param selected A kijelöltség állapota.
     */
    public void setSelected(boolean selected) {
        if(selected) {
            setBorder(BorderFactory.createLineBorder(Color.WHITE));
        } else {
            setBorder(null);
        }
    }

    /** 
     * Visszaadja a szobát.
     * 
     * @return A szoba.
     */
    public Room getRoom() {
        return room;
    }
    
    /** 
     * Kirajzolja a panelt.
     * A rajzoláshoz betölt egy ajtó képet is.
     * 
     * @param g A rajzoló objektum.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            BufferedImage image = ImageIO.read(new File("resources"+ File.separator+"door.png"));
            g.setColor(color);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(image,0, 0, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
