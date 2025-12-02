package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import train.Coach;

/**
 * Segédosztály modern komponensek létrehozásához, hogy egységes legyen a programban
*/

public class ModernComponents {

    // Színpaletta
    public static final Color BACKGROUND_COLOR = new Color(45, 45, 45);       
    public static final Color TEXT_COLOR = new Color(240, 240, 240);          
    public static final Color INPUT_BG_COLOR = new Color(255, 255, 255);      
    public static final Color BUTTON_COLOR = new Color(46, 204, 113);
    public static final Color ACCENT_COLOR = new Color(60, 63, 65);    

    /**
     * Létrehoz egy stílusos JLabelt a megadott szöveggel és betűmérettel
     * @param text : A címke szövege
     * @param fontSize : A betűméret
     * @return : A stílusos JLabel
     */
    public static JLabel createStyledLabel(String text, int fontSize) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_COLOR);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        return lbl;
    }

    /**
     * Létrehoz egy modern stílusú gombot a megadott szöveggel és színekkel
     * @param text : A gomb szövege
     * @param background : A gomb háttérszíne
     * @param foregroundColor : A gomb szövegének színe
     * @return : A JButton
     */
    public static JButton createModernButton(String text, Color background, Color foregroundColor) {
        JButton btn = new JButton(text);
        btn.setBackground(background);
        btn.setForeground(foregroundColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 30, 12, 30)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false); 
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                double factor = 1 - (15.0 / 100.0);
                int newRed = (int) Math.round(background.getRed()*factor);
                int newGreen = (int) Math.round(background.getGreen()*factor);
                int newBlue = (int) Math.round(background.getBlue()*factor);
                btn.setBackground(new Color(newRed, newGreen, newBlue));
                
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(background);
            }
        });
        return btn;
    }

    /**
     * JComponent stílusát állítja be
     * @param comp : A komponens
     */
    public static void styleComponent(JComponent comp) {
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comp.setBackground(INPUT_BG_COLOR);
        comp.setBorder(new LineBorder(new Color(200, 200, 200), 1));
    }

    /**
     * Létrehoz egy JList-et a megadott modell alapján, a színpaletta használatával
     * @param model : A lista modellje
     * @return : A stílusos JList
     */
    public static JList<String> createStyledList(DefaultListModel<String> model) {
        JList<String> list = new JList<>(model);
        list.setBackground(new Color(60, 63, 65)); // Kicsit világosabb szürke mint a háttér
        list.setForeground(ModernComponents.TEXT_COLOR);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setBorder(new LineBorder(new Color(80, 80, 80), 1));
        return list;
    }

    /**
     * JScrollPane stílusának beállítása a színpaletta használatával
     * @param scroll : A JScrollPane
     */
    public static void styleScrollPane(JScrollPane scroll) {
        scroll.getViewport().setBackground(ModernComponents.BACKGROUND_COLOR);
        scroll.setBorder(null);
    }

    /**
     * Létrehoz egy gombot egy kocsi reprezentálására
     * @param c : A kocsi
     * @return : A kocsi gombja
     */
    public static JButton createCoachButton(Coach c) {
        String label = "<html><center>Kocsi #" + c.getId() + "<br/>";
        
        if (c.isFirstClass()) label += "1. ";
        if (c.getBicycleCapacity() > 0) label += "bicikli ";
        if (c.getWheelchairCapacity() > 0) label += "akadálymentes ";
        if (c.isBuffetCar()) label += "Büfé ";
        
        label += "</center></html>";

        JButton btn = new JButton(label);
        btn.setPreferredSize(new Dimension(120, 70));
        btn.setBackground(new Color(70, 70, 70));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(90, 90, 90));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(70, 70, 70));
            }
        });

        return btn;
    }

    /**
     * Létrehoz egy ülés gombot a grafikus helyválasztáshoz
     * @param seatNum : Az ülés száma
     * @param isReserved : Igaz, ha az ülés foglalt
     * @return : Az ülés gombja
     */
    public static JButton createSeatButton(int seatNum, boolean isReserved) {
        JButton btn = new JButton(String.valueOf(seatNum));
        btn.setPreferredSize(new Dimension(50, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        if (isReserved) {
            btn.setBackground(new Color(200, 60, 60));
            btn.setForeground(Color.WHITE);
            btn.setEnabled(false);
        } else {
            btn.setBackground(ModernComponents.BUTTON_COLOR);
            btn.setForeground(Color.WHITE);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        return btn;
    }

}
