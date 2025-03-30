package ui;

import controller.Controller;
import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/** 
 * Az Itemeket megjelenítő panel. Az Itemekhez tartozó képeket és attribútumokat jeleníti meg.
 * Az attribútumokat a Controller osztályból kéri le.
 */
public class ItemPanel extends JComponent {

    /** 
     * Az Item, amelyhez a panel tartozik.
     */
    private final Item item;

    /** 
     * Az Itemhez tartozó kép.
     */
    private BufferedImage image;

    /** 
     * Az Item attribútumait tartalmazó menü.
     */
    private final JPopupMenu attributes;

    /** 
     * Konstruktor, beállítja az Itemhez tartozó képet és attribútumokat.
     * A panelen lévő attribútumokat a Controller osztályból kéri le.
     * Hozzáad egy egérfigyelőt, amely megjeleníti az attribútumokat, ha az egér a panel fölé kerül.
     * A preferált méret 50 x 50 lesz.
     * 
     * @param item Az Item, amelyhez a panel tartozik.
     */
    public ItemPanel(Item item){
        this.item = item;
        String imagePath = Controller.getItemImage(item);
        attributes = new JPopupMenu();

        ArrayList<String> lines = Controller.getItemAttributes(item);
        attributes.add(lines.get(0));
        if(lines.size() > 1) {
            attributes.addSeparator();
            for(int i = 1; i < lines.size(); i++){
                attributes.add(lines.get(i));
            }
        }

        this.add(attributes);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                attributes.setVisible(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                attributes.show(e.getComponent(), 0, e.getComponent().getHeight());
                attributes.setVisible(true);
            }
        });

        try {
            image = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(50,50));
    }

    /** 
     * Kirajzolja a panelt.
     * Ha a megjelenítendő tárgy IntervalItem, és a tárgy aktiválva van, beállít egy zöld hátteret is.
     * 
     * @param g A rajzoló objektum.
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image != null){
            g.drawImage(image, 0, 0, this);
            if(item instanceof IntervalItem interval && interval.isActivated()){
                g.setColor(new Color(0,255,0,80));
                g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        }
    }

    /** 
     * Visszaadja az Item-et.
     * 
     * @return Az Item.
     */
    public Item getItem(){
        return item;
    }

    /** 
     * Beállítja a panel kijelöltségét.
     * A kijelöltséget egy új határoló vonallal jelzi.
     * 
     * @param selected A kijelöltség állapota.
     */
    public void setSelected(boolean selected) {
        if(selected) {
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        } else {
            setBorder(null);
        }
    }
}
