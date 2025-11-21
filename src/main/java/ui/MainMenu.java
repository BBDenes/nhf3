package ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import train.TrainHandler;

public class MainMenu extends JFrame {
    private JButton buyTicketButton;
    private JButton editTrainsButton;
    public MainMenu(TrainHandler th){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setTitle("Máv++");
        setSize(500,300);
        
        renderMainMenu(th);

    }

    private void renderMainMenu(TrainHandler th){
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(new Color(45, 45, 45));
        
        buyTicketButton = ModernComponents.createModernButton("Jegyvásárlás", new Color(46, 204, 113), Color.WHITE);
        editTrainsButton = ModernComponents.createModernButton("Vonat hozzáadása", new Color(52, 152, 219), Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 10, 10);

        buyTicketButton.addActionListener(e->{
            System.out.println(th.getTrains().toString());
            TicketMenu ticketWindow = new TicketMenu(this, th);
            ticketWindow.setVisible(true);
        });
        menuPanel.add(buyTicketButton, gbc);

    
        gbc.gridx = 0;
        gbc.gridy = 1;
        menuPanel.add(editTrainsButton, gbc);

        editTrainsButton.addActionListener(e->{
            AddTrainWindow trainAddWindow = new AddTrainWindow(this, th);
            trainAddWindow.setVisible(true);
        });
        this.add(menuPanel, BorderLayout.CENTER);
    }

}
