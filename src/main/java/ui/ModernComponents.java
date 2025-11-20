package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ModernComponents {

    public static final Color BACKGROUND_COLOR = new Color(45, 45, 45);       
    public static final Color TEXT_COLOR = new Color(240, 240, 240);          
    public static final Color INPUT_BG_COLOR = new Color(255, 255, 255);      
    public static final Color BUTTON_COLOR = new Color(46, 204, 113);         
    public static final Color BUTTON_HOVER_COLOR = new Color(39, 174, 96);

    public static JLabel createStyledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_COLOR);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lbl;
    }

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
                btn.setBackground(BUTTON_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BUTTON_COLOR);
            }
        });
        return btn;
    }

    public static void styleComponent(JComponent comp) {
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comp.setBackground(INPUT_BG_COLOR);
        comp.setBorder(new LineBorder(new Color(200, 200, 200), 1));
    }

}
