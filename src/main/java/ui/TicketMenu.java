package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import train.*;

public class TicketMenu extends JDialog {

    private JComboBox<String> honnanBox;
    private JComboBox<String> hovaBox;
    private TrainHandler trainHandler;



    public TicketMenu(JFrame parent, TrainHandler th) {
        super(parent, "Jegyvásárlás", true);
        this.trainHandler = th;

        // Ablak alapok
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(false); 

        // Fő panel létrehozása
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25)); // Nagy belső margó az ablak szélétől

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Elemek közötti távolság
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Új jegy vásárlása");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(ModernComponents.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; 
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0); 
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        mainPanel.add(ModernComponents.createStyledLabel("Honnan:", 14), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        String[] allomasok = {"Budapest", "Debrecen", "Szeged", "Pécs", "Győr"};
        honnanBox = new JComboBox<>(allomasok);
        honnanBox.setEditable(true);
        //TODO: Kiegészítés
        ModernComponents.styleComponent(honnanBox);
        mainPanel.add(honnanBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        mainPanel.add(ModernComponents.createStyledLabel("Hova:", 14), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        hovaBox = new JComboBox<>(allomasok);
        ModernComponents.styleComponent(hovaBox); 
        if (hovaBox.getItemCount() > 1) hovaBox.setSelectedIndex(1); 
        mainPanel.add(hovaBox, gbc);
        

        gbc.gridx = 0; gbc.gridy = 4; 
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        
        JButton buyButton = ModernComponents.createModernButton("Keresés!", ModernComponents.BUTTON_COLOR, Color.WHITE);
        
        buyButton.addActionListener(e -> processPurchase());
        
        mainPanel.add(buyButton, gbc);

        this.add(mainPanel);
    }

    private void processPurchase() {
        String honnan = (String) honnanBox.getSelectedItem();
        String hova = (String) hovaBox.getSelectedItem();

        if(honnan.equals(hova)) {
            JOptionPane.showMessageDialog(this, "A kiindulás és cél nem lehet ugyanaz!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ITT TÖRTÉNIK A VÁSÁRLÁS LOGIKA (pl. TrainHandler hívása)
        System.out.println("Vásárlás: " + honnan + " -> " + hova);
        
        // Bezárjuk az ablakot sikeres vásárlás után
        dispose(); 
    }


   

    
}
