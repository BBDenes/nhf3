package ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import train.TrainHandler;
import utilities.PurchaseController;



public class MainMenu extends JFrame {
    private JButton buyTicketButton;
    private JButton editTrainsButton;

    private PurchaseController purchase;

    public MainMenu(TrainHandler th){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setTitle("Máv++");
        setSize(500,300);
        setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
        this.purchase = new PurchaseController();
        renderMainMenu(th);

    }

    private void renderMainMenu(TrainHandler th){

        //TODO: Fájl menü, ahol id szerint lehet törölni a vonatot
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(new Color(45, 45, 45));
        
        
        buyTicketButton = ModernComponents.createModernButton("Jegyvásárlás", new Color(46, 204, 113), Color.WHITE);
        editTrainsButton = ModernComponents.createModernButton("Admin panel", new Color(52, 152, 219), Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 10, 10);

        buyTicketButton.addActionListener(e->{
            System.out.println(th.getTrains().toString());
            TicketMenu ticketWindow = new TicketMenu(this, th, purchase);
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
