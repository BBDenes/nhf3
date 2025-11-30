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

    private JPanel createPassengersMenu() {
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
        switchToPanel(createResultsMenu(results));

    }

    private JPanel createResultsMenu(List<Train> results) {
        List<String> stops = purchase.getStops();
        setResizable(true);
        setSize(650, 600);
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        listPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Elérhető vonatok: " + stops.get(0) + " -> " + stops.get(1));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ModernComponents.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        listPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel cardsContainer = new JPanel(new GridBagLayout());
        cardsContainer.setBackground(ModernComponents.BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0); 

        if (results.isEmpty()) {
            JLabel noResult = new JLabel("Sajnos nincs járat ezen a napon.");
            noResult.setForeground(Color.GRAY);
            noResult.setHorizontalAlignment(SwingConstants.CENTER);
            cardsContainer.add(noResult, gbc);
        } else {
            
            cardsContainer.add(ModernComponents.createStyledLabel("Helyfoglalás Módja:", 12), gbc);
            
            gbc.gridy = 1;
            gbc.weightx = 0.5;
            
            String[] reserveOptions = {"Automatikus", "Tulajdonság megadása", "Grafikus"};
            this.reserveSelector = new JComboBox<>(reserveOptions);
            cardsContainer.add(reserveSelector, gbc);

            gbc.weightx = 1.0;
            gbc.gridx = 0;
            gbc.gridy = 2;
            
            for (Train v : results) {
                JPanel card = createTrainCard(v, stops.get(0), stops.get(1));
                cardsContainer.add(card, gbc);
                gbc.gridy++;
            }
        }

        gbc.weighty = 1.0;
        cardsContainer.add(Box.createGlue(), gbc);

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        ModernComponents.styleScrollPane(scrollPane);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        listPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(ModernComponents.BACKGROUND_COLOR);
        JButton backBtn = ModernComponents.createModernButton("Vissza az utasokhoz", new Color(100, 100, 100), Color.WHITE);
        backBtn.addActionListener(e -> {
            switchToPanel(createPassengersMenu());
        });
        footer.add(backBtn);
        
        listPanel.add(footer, BorderLayout.SOUTH);

        return listPanel;
    }


    private JPanel createTrainCard(Train v, String from, String to) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(60, 63, 65)); 
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        Stop startStop = v.getStopByName(from);
        Stop endStop = v.getStopByName(to);
        
        String leave = (startStop != null) ? startStop.getLeave().toString() : "??:??";
        String arrive = (endStop != null) ? endStop.getArrive().toString() : "??:??";

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(2, 5, 2, 5);

        g.gridx = 0; g.gridy = 0; g.weightx = 0.6;
        JLabel nameLabel = new JLabel(v.getName() + " (" + v.getId() + ")");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(ModernComponents.BUTTON_COLOR);
        card.add(nameLabel, g);

        g.gridy = 1;
        JLabel timeLabel = new JLabel(leave + " -> " + arrive);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        timeLabel.setForeground(Color.WHITE);
        card.add(timeLabel, g);

        g.gridy = 2;
        String services = "Szolgáltatások: ";
        services += v.getServices();

        JLabel serviceLabel = new JLabel(services);
        serviceLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        serviceLabel.setForeground(Color.LIGHT_GRAY);
        card.add(serviceLabel, g);
        
        g.gridx = 1; g.gridy = 0; g.gridheight = 3; g.weightx = 0.4;
        g.anchor = GridBagConstraints.CENTER;
        
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        rightPanel.setBackground(new Color(60, 63, 65)); 
        
        
        JButton secondClassBtn = ModernComponents.createModernButton("2. osztályú jegy", ModernComponents.BUTTON_COLOR, Color.WHITE);
        secondClassBtn.addActionListener(e -> {
            purchase.setTrain(v.getId());
            purchase.setFirstClass(false);
            navigateToSeatSelection();
        });
        rightPanel.add(secondClassBtn);
        
        JButton firstClassBtn = ModernComponents.createModernButton("1. osztályú jegy", ModernComponents.BUTTON_COLOR, Color.WHITE);
        firstClassBtn.addActionListener(e -> {
            purchase.setTrain(v.getId());
            purchase.setFirstClass(true);
            navigateToSeatSelection();
        });
        if(v.getServices().contains(Train.FIRST_CLASS_TXT)){rightPanel.add(firstClassBtn);} //TODO: vonat... itt... eh?
        
        
        card.add(rightPanel, g);

        return card;
    }

    private void navigateToSeatSelection() {
        String mode = (String) reserveSelector.getSelectedItem();
        System.out.println("Választott vonat: " + purchase.getTrainId() + ", Osztály: " + (purchase.isFirstClass() ? "1." : "2.") + ", Mód: " + mode);

        switch (mode) {
            case "Automatikus":
                processAutomaticReservation();
                break;
            case "Tulajdonság megadása":
                switchToPanel(createAttributeSelectionPanel());
                break;
            case "Grafikus":
                switchToPanel(createVisualReservePanel());
                break;  
        }
    }

    private void processAutomaticReservation() {
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

    private JPanel createAttributeSelectionPanel() {
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

    
   

    private JPanel seatMapPanel; 
    
    private JPanel createVisualReservePanel() {
        Train train = trainHandler.getTrainByIndex(purchase.getTrainId());
        int trainId = purchase.getTrainId();
        boolean isFirstClass = purchase.isFirstClass();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ModernComponents.BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        
        JLabel title = new JLabel("Válasszon ki " + purchase.getPassengerCount() + " helyet a térképen!");  
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(ModernComponents.TEXT_COLOR);
        headerPanel.add(title);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout());
        
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(ModernComponents.BACKGROUND_COLOR);

        JPanel coachStrip = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        coachStrip.setBackground(new Color(40, 40, 40)); 
        
        JScrollPane coachScroll = new JScrollPane(coachStrip);
        ModernComponents.styleScrollPane(coachScroll);
        coachScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        coachScroll.setBorder(null);
        coachScroll.setPreferredSize(new Dimension(600, 100));

        List<Coach> coaches = train.getCoaches();
        Coach firstValidCoach = null;

        for (int i = 0; i < coaches.size(); i++) {
            Coach c = coaches.get(i);
            if (c.isFirstClass() == isFirstClass) {
                if (firstValidCoach == null) firstValidCoach = c;
                JButton coachBtn = ModernComponents.createCoachButton(c);
                coachBtn.addActionListener(e -> {
                    seatMapPanel.removeAll();
                    renderSeatMap(c, trainId, isFirstClass);

                });
                coachStrip.add(coachBtn);
            }
        }
        topWrapper.add(coachScroll, BorderLayout.CENTER);

        coachInfoLabel = new JLabel("Kérjük, válasszon egy kocsit a fenti sávból!");
        coachInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        coachInfoLabel.setForeground(ModernComponents.BUTTON_COLOR);
        coachInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coachInfoLabel.setBorder(new EmptyBorder(10, 0, 10, 0)); 
        
        topWrapper.add(coachInfoLabel, BorderLayout.SOUTH);

        centerContainer.add(topWrapper, BorderLayout.NORTH);

        seatMapPanel = new JPanel();
        seatMapPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        seatMapPanel.setLayout(new GridBagLayout());
        
        JScrollPane seatScroll = new JScrollPane(seatMapPanel);
        ModernComponents.styleScrollPane(seatScroll);
        seatScroll.setBorder(null);
        centerContainer.add(seatScroll, BorderLayout.CENTER);

        mainPanel.add(centerContainer, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(ModernComponents.BACKGROUND_COLOR);
        JButton btnCancel = ModernComponents.createModernButton("Mégse", Color.GRAY, Color.WHITE);
        btnCancel.addActionListener(e ->{
            reservedSeatIds.clear();
            List<Train> results = trainHandler.searchTrains(purchase.getStops().get(0), purchase.getStops().get(1));
            switchToPanel(createResultsMenu(results));
        });
        footer.add(btnCancel);

        JButton advanceBtn = ModernComponents.createModernButton("Tovább!", ModernComponents.BUTTON_COLOR, ModernComponents.TEXT_COLOR);
        advanceBtn.addActionListener(e -> {
            if (reservedSeatIds.size() > purchase.getPassengerCount()) {
                JOptionPane.showMessageDialog(this, "Több a kijelölt szék, mint az utas!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }else if(reservedSeatIds.size() < purchase.getPassengerCount()){
                JOptionPane.showMessageDialog(this, "Kevesebb a kijelölt szék, mint az utas!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            purchase.setSeatsToReserve(reservedSeatIds);
            
            try {
                trainHandler.reserveSpecificSeats(purchase);
                JOptionPane.showMessageDialog(this, "Sikeres foglalás!");
                showSummaryPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hiba: " + ex.getMessage());
            }
            
            showSummaryPanel();
            
        });

        footer.add(advanceBtn);

        mainPanel.add(footer, BorderLayout.SOUTH);

        if (firstValidCoach != null) {
            renderSeatMap(firstValidCoach, trainId, isFirstClass);
        }

        return mainPanel;
    }
    
    List<Integer> reservedSeatIds = new ArrayList<>();
    private void renderSeatMap(Coach c, int trainId, boolean isFirstClass) {
        String osztaly = c.isFirstClass() ? "1. Osztály" : "2. Osztály";
        coachInfoLabel.setText("Jelenlegi kocsi: " + c.getId() + ". számú kocsi (" + osztaly + ")");
        int rows = (int) Math.ceil((double) c.getCapacity() / 4);
        JPanel grid = new JPanel(new GridLayout(rows, 5, 10, 10));
        grid.setBackground(ModernComponents.BACKGROUND_COLOR);
        grid.setBorder(new EmptyBorder(20, 20, 20, 20));

        List<Integer> reservedSeats = c.getReservedSeatIds();

        for (int i = 1; i <= c.getCapacity(); i++) {
            if (i > 1 && (i - 1) % 2 == 0 && (i - 1) % 4 != 0) {
                grid.add(Box.createGlue());
            }
            boolean isReserved = reservedSeats.contains(i);
            JButton seatBtn = ModernComponents.createSeatButton(i, isReserved);
            if (!reservedSeats.contains(i)) {
                seatBtn.addActionListener(e -> {
                    int id = Integer.parseInt(seatBtn.getText());
                    int globalSeatId = purchase.getGlobalSeatId(c.getId(), id); //1-indexelt a Seat lista
                    boolean isSelected = reservedSeatIds.contains(globalSeatId);
                    if(isSelected){
                        seatBtn.setBackground(seatBtn.getBackground().brighter());
                        reservedSeatIds.remove(Integer.valueOf(globalSeatId));
                        
                    }else{
                        
                        reservedSeatIds.add(Integer.valueOf(globalSeatId));
                        seatBtn.setBackground(seatBtn.getBackground().darker());
                    }
                    seatMapPanel.revalidate();
                    seatMapPanel.repaint();
                });

            }
            
            grid.add(seatBtn);
        }

        seatMapPanel.add(grid);
        seatMapPanel.revalidate();
        seatMapPanel.repaint();
    }

    public void showSummaryPanel() {
        // Átadjuk a controllert, a handlert és az adatokat tartalmazó purchase objektumot
        switchToPanel(new SummaryPanel(this, trainHandler, purchase));
    }


}
