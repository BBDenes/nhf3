package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.io.*;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ModernComponents {

    public static final Color BACKGROUND_COLOR = new Color(45, 45, 45);       
    public static final Color TEXT_COLOR = new Color(240, 240, 240);          
    public static final Color INPUT_BG_COLOR = new Color(255, 255, 255);      
    public static final Color BUTTON_COLOR = new Color(46, 204, 113);         

    public static JLabel createStyledLabel(String text, int fontSize) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_COLOR);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
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

    public static void styleComponent(JComponent comp) {
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comp.setBackground(INPUT_BG_COLOR);
        comp.setBorder(new LineBorder(new Color(200, 200, 200), 1));
    }

    public static JList<String> createStyledList(DefaultListModel<String> model) {
        JList<String> list = new JList<>(model);
        list.setBackground(new Color(60, 63, 65)); // Kicsit világosabb szürke mint a háttér
        list.setForeground(ModernComponents.TEXT_COLOR);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setBorder(new LineBorder(new Color(80, 80, 80), 1));
        return list;
    }

    public static void styleScrollPane(JScrollPane scroll) {
        scroll.getViewport().setBackground(ModernComponents.BACKGROUND_COLOR);
        scroll.setBorder(null);
    }
}
