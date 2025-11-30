package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import train.Coach;
import train.Stop;
import train.Train;
import train.TrainHandler;
import utilities.PurchaseController;

public class TicketMenu extends JDialog {

    private List<JTextField> utasNevMezok = new ArrayList<>();
    private List<JCheckBox> passCheckFields = new ArrayList<>();
    private List<JTextField> passIdFields = new ArrayList<>();
    private JSpinner passengerNumSpinner;
    private JComboBox<String> reserveSelector;

    private JPanel currentPanel;
    private JLabel coachInfoLabel;
    private JComboBox<String> honnanBox;
    private JComboBox<String> hovaBox;
    private TrainHandler trainHandler;

    private PurchaseController purchase;


    public TicketMenu(JFrame parent, TrainHandler th, PurchaseController purchase) {
        super(parent, "Jegyvásárlás", true);
        this.trainHandler = th;
        this.purchase = purchase;
        renderSearchMenu();
    }

    private void renderSearchMenu(){
        setSize(450, 400);
        setResizable(false); 

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
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
        ModernComponents.styleComponent(honnanBox);
        mainPanel.add(honnanBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        mainPanel.add(ModernComponents.createStyledLabel("Hova:", 14), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        hovaBox = new JComboBox<>(allomasok);
        ModernComponents.styleComponent(hovaBox); 
        if (hovaBox.getItemCount() > 1) hovaBox.setSelectedIndex(1); 
        mainPanel.add(hovaBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        mainPanel.add(ModernComponents.createStyledLabel("Utasok száma:", 14), gbc);

        gbc.gridx = 1;
        passengerNumSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        mainPanel.add(passengerNumSpinner, gbc);    
        

        gbc.gridx = 0; gbc.gridy = 4; 
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        
        JButton nextButton = ModernComponents.createModernButton("Tovább az utasok adataihoz", ModernComponents.BUTTON_COLOR, Color.WHITE);
        
        nextButton.addActionListener(e -> {
            purchase.setStops((String)honnanBox.getSelectedItem(), (String)hovaBox.getSelectedItem());
            purchase.setPassengerNum((int) passengerNumSpinner.getValue());
            switchToPanel(createPassengersMenu());
        });
        
        mainPanel.add(nextButton, gbc);

        this.add(mainPanel);
        currentPanel = mainPanel;
    }

    public JPanel createPassengersMenu() {
        JPanel passengerPanel = new JPanel(new GridBagLayout());
        passengerPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        passengerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel("Utasok adatainak megadása");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ModernComponents.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passengerPanel.add(titleLabel, gbc);
        gbc.gridy++;

        JPanel listPanel = new JPanel(new GridBagLayout()); // Belső panel a scrollozáshoz
        listPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        GridBagConstraints listGbc = new GridBagConstraints();
        listGbc.fill = GridBagConstraints.HORIZONTAL;
        listGbc.weightx = 1.0;
        listGbc.gridx = 0; listGbc.gridy = 0;

        utasNevMezok.clear();
        passCheckFields.clear();
        passIdFields.clear();


        for (int i = 0; i < purchase.getPassengerCount(); i++) {
            JPanel singleUserPanel = createSinglePassengerRow(i + 1);
            listPanel.add(singleUserPanel, listGbc);
            listGbc.gridy++;
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        ModernComponents.styleScrollPane(scrollPane);
        scrollPane.setBorder(null);
        
        gbc.weighty = 1.0; // Kapja meg a helyet
        gbc.fill = GridBagConstraints.BOTH;
        passengerPanel.add(scrollPane, gbc);

        // Gombok (Vissza / Keresés)
        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        
        
        JButton searchTrainButton = ModernComponents.createModernButton("Vonatok keresése", ModernComponents.BUTTON_COLOR, Color.WHITE);
        searchTrainButton.addActionListener(e ->processPassengerData());

        buttonPanel.add(searchTrainButton);
        
        passengerPanel.add(buttonPanel, gbc);

        return passengerPanel;
    }

    private JPanel createSinglePassengerRow(int sorszam) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(ModernComponents.ACCENT_COLOR);
        p.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(5, 0, 5, 0),
            BorderFactory.createLineBorder(new Color(80, 80, 80))
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 10, 5, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        
        g.gridx = 0; g.gridy = 0; g.weightx = 0.3;
        p.add(ModernComponents.createStyledLabel(sorszam + ". Utas neve:", 14), g);

        g.gridx = 1; g.weightx = 0.7;
        JTextField txtNev = new JTextField();
        ModernComponents.styleComponent(txtNev);
        utasNevMezok.add(txtNev);
        p.add(txtNev, g);

        g.gridx = 0; g.gridy = 1; g.weightx = 0.3;
        JCheckBox passCheckbox = new JCheckBox("Van bérlete? Bérlet azonosító: ");
        passCheckbox.setBackground(new Color(60, 63, 65));
        passCheckbox.setForeground(Color.WHITE);
        passCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passCheckFields.add(passCheckbox);
        p.add(passCheckbox, g);

        g.gridx = 1; g.weightx = 0.7;
        JTextField passField = new JTextField();
        ModernComponents.styleComponent(passField);
        passField.setEnabled(false);
        passField.setToolTipText("Adja meg a bérlet számát");
        passIdFields.add(passField);
        p.add(passField, g);

        passCheckbox.addActionListener(e -> passField.setEnabled(passCheckbox.isSelected()));

        return p;
    }


    private void switchToPanel(JPanel newPanel) {
        if (currentPanel != null) {
            this.remove(currentPanel);
        }
        
        currentPanel = newPanel;
        this.add(currentPanel);
        
        this.revalidate();
        this.repaint();
    }

    private void processPassengerData() {
        List<String> pNames = new ArrayList<>();
        List<String> pIds = new ArrayList<>();
        
        for (int i = 0; i < utasNevMezok.size(); i++) {
            String nev = utasNevMezok.get(i).getText();
            boolean vanBerlet = passCheckFields.get(i).isSelected();
            String berletId = passIdFields.get(i).getText();
            
            if (nev.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, (i+1) + ". utas neve hiányzik!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (vanBerlet && berletId.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, (i+1) + ". utas bérletszáma hiányzik!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            pNames.add(nev);
            pIds.add(berletId);
        }
    
        purchase.setPassengers(pNames, pIds);
        List<Train> results = trainHandler.searchTrains(purchase.getStops().get(0), purchase.getStops().get(1));
        this.setSize(700, 600);
        switchToPanel(new TrainListPanel(this, purchase, results));

    }

    public void processAutomaticReservation() {
        try {
            trainHandler.reserveAutomaticSeats(purchase);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Hiba a foglalásánál:\n" + e.getMessage(), 
                "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, "Sikeres foglalás", "Siker", JOptionPane.INFORMATION_MESSAGE);
        System.out.println(purchase.getTicketsToBuy().toString());
        showSummaryPanel();
    }

    public JPanel createAttributeSelectionPanel() {
        boolean isFirstClass = purchase.isFirstClass();
        int id = purchase.getTrainId();
        Train currentTrain = trainHandler.getTrainByIndex(id);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ModernComponents.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        // Cím
        JLabel title = new JLabel("Helyjegy tulajdonságok kiválasztása");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(ModernComponents.TEXT_COLOR);
        panel.add(title, gbc);
        
        gbc.gridy++;
        JLabel subTitle = new JLabel(currentTrain.getName() + " - " + (isFirstClass ? "1. Osztály" : "2. Osztály"));
        subTitle.setForeground(Color.LIGHT_GRAY);
        panel.add(subTitle, gbc);

        JPanel optionsContainer = new JPanel(new GridBagLayout());
        optionsContainer.setBackground(ModernComponents.BACKGROUND_COLOR);
        
        List<JCheckBox> accessibleChecks = new ArrayList<>();
        List<JCheckBox> bycicleChecks = new ArrayList<>();
        
        GridBagConstraints subGbc = new GridBagConstraints();
        subGbc.insets = new Insets(5, 5, 5, 5);
        subGbc.fill = GridBagConstraints.HORIZONTAL;
        subGbc.gridx = 0; subGbc.gridy = 0;

        List<String> pNames = purchase.getPassengerNames();
        for (int i = 0; i < pNames.size(); i++) {
            String utasNeve = pNames.get(i);
            
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.setBackground(new Color(60, 63, 65));
            row.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            
            JLabel nameLbl = new JLabel(utasNeve + ": ");
            nameLbl.setForeground(Color.WHITE);
            nameLbl.setPreferredSize(new Dimension(100, 20));
            
            JCheckBox chkAccessible = new JCheckBox("Akadálymentes kocsi");
            chkAccessible.setBackground(new Color(60, 63, 65)); chkAccessible.setForeground(Color.WHITE);
            accessibleChecks.add(chkAccessible);

            JCheckBox chkBycicle = new JCheckBox("Kerékpárhely");
            chkBycicle.setBackground(new Color(60, 63, 65)); chkBycicle.setForeground(Color.WHITE);
            bycicleChecks.add(chkBycicle);

            row.add(nameLbl);
            row.add(chkAccessible);
            row.add(chkBycicle);
            
            optionsContainer.add(row, subGbc);
            subGbc.gridy++;
        }

        gbc.gridy++;
        gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(optionsContainer), gbc);

        gbc.gridy++; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton btnReserve = ModernComponents.createModernButton("Lefoglalás", ModernComponents.BUTTON_COLOR, Color.WHITE);



        btnReserve.addActionListener(e -> {

            List<Boolean> accMask = new LinkedList<>();
            List<Boolean> bycMask = new LinkedList<>();
            
            for (int i = 0; i < accessibleChecks.size(); i++) {
                JCheckBox acc = accessibleChecks.get(i);
                JCheckBox bycicle = bycicleChecks.get(i);

                accMask.add(acc.isSelected());
                bycMask.add(bycicle.isSelected());
            }

            purchase.setMasks(accMask, bycMask);

            processAutomaticReservation();
        });
        
        panel.add(btnReserve, gbc);

        return panel;
    }

    public void showTrainListPanel(List<Train> results) {
        switchToPanel(new TrainListPanel(this, purchase, results));
    }
    
    public void showVisualSeatPanel() {
        switchToPanel(new VisualSeatPanel(this, trainHandler, purchase));
    }

    public void showSummaryPanel() {
        switchToPanel(new SummaryPanel(this, trainHandler, purchase));
    }


}
