package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * The CardComponent class represents a graphical component for displaying player cards
 * that can be chosen during the game.
 */
public class CardComponent extends JComponent {
    
    /** The width of a card component. */
    private static final int CARD_WIDTH = 45;

    /** The height of a card component. */
    private static final int CARD_HEIGHT = 135;

    /** The image of the card to be displayed. */
    private BufferedImage cardImage;

    /** Indicates whether the card is selected or not. */
    private boolean selected = false;

    /**
     * Constructs a CardComponent with the specified image file.
     * 
     * @param imagePath The file path of the image to display on the card.
     */
    public CardComponent(String imagePath) {
        try {
            cardImage = ImageIO.read(new File(imagePath));
        } catch (IIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        addMouseListener(new CardMouseListener());
        
        // Set preferred size based on the orientation of the card
        if (imagePath.substring(14, 16).equals("ud")) {
            setPreferredSize(new Dimension(60, 25));
        } else if (imagePath.substring(14, 16).equals("lr")) {
            setPreferredSize(new Dimension(25, 60));
        } else {
            setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        }
    }

    /**
     * Paints the card component.
     * 
     * @param g The graphics context.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cardImage != null) {
            g.drawImage(cardImage, 0, 0, this);
            if (selected) {
                // Chosen cards will be outlined in red
                g.setColor(Color.RED);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        }
    }

    /**
     * Checks if the card is currently selected.
     * 
     * @return true if the card is selected, false otherwise.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * The CardMouseListener class is an internal class that handles mouse events
     * for the CardComponent.
     */
    public class CardMouseListener extends MouseAdapter {
        
        /**
         * Called when the mouse is released.
         * Toggles the selected state of the card and triggers a repaint.
         * 
         * @param e The MouseEvent.
         */
        public void mouseReleased(MouseEvent e) {
            if (isEnabled()) {
                selected = !selected;
                repaint();
            }
        }
    }
}
