package ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.*;

import train.TrainHandler;

public class MainMenu extends JFrame {
    private JButton buyTicketButton;
    private JButton editTrainsButton;
    public MainMenu(TrainHandler t){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setTitle("Máv++");
        setSize(500,300);
        
        renderMainMenu(t);

    }

    private void renderMainMenu(TrainHandler t){
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(new Color(45, 45, 45));
        
        buyTicketButton = new JButton("Jegyvásárlás");
        editTrainsButton = new JButton("Vonatok módosítása");
        styleButton(buyTicketButton, new Color(46, 204, 113)); 
        styleButton(editTrainsButton, new Color(52, 152, 219));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 10, 10);

        buyTicketButton.addActionListener(e->{
            TicketMenu jegyAblak = new TicketMenu(this, t);
            jegyAblak.setVisible(true);
        });
        menuPanel.add(buyTicketButton, gbc);

    
        gbc.gridx = 0;
        gbc.gridy = 1;
        menuPanel.add(editTrainsButton, gbc);
        this.add(menuPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn, Color bgColor) {
    btn.setBackground(bgColor);
    btn.setForeground(Color.WHITE); // Fehér szöveg
    btn.setFocusPainted(false); // A kattintáskori idegesítő keret eltüntetése
    btn.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modern, olvasható betűtípus
    btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // Szellős belső margó, keret nélkül
    
    // Kéz kurzor, ha fölé viszed az egeret
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Opcionális: Ha azt akarod, hogy teljesen "lapos" legyen és ne legyen 3D hatása
    // (Ez operációs rendszertől függően változhat, de általában segít):
    btn.setContentAreaFilled(false);
    btn.setOpaque(true);
}
}
