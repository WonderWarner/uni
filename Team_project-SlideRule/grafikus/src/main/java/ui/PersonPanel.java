package ui;

import model.Person;
import controller.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/** 
 * A személyeket megjelenítő panel. A személyekhez tartozó képeket jeleníti meg. A képeket a Controller osztályból kéri le.
 */
public class PersonPanel extends JComponent {

    /** 
     * A személy, akinek a panel tartozik.
     */
    private final Person person;

    /** 
     * A személyhez tartozó kép.
     */
    private BufferedImage image;

    /** 
     * Konstruktor, beállítja a személyhez tartozó képet.
     * 
     * @param person A személy, akinek a panel tartozik.
     */
    public PersonPanel(Person person){
        this.person = person;

        String imagePath = Controller.getPersonImage(person);

        try {
                image = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(50,50));
    }

    /** 
     * Kirajzolja a panelt.
     * Ha a személy megbénult, kirajzol egy sárga hátteret is.
     * 
     * @param g A rajzoló grafika objektum.
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image != null){
            g.drawImage(image, 0, 0, this);
            if(person.getStunRemaining() > 0){
                g.setColor(new Color(255,255,0,80));
                g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        }
    }
}