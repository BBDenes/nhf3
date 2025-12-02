package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import train.Train;
import train.Stop;
import utilities.PurchaseController;

/**
 * Panel az elérhető vonatok listázására és a jegyvásárlás indítására.
 *
 * A panel felelős a megadott vonatok megjelenítéséért kártya nézetben,
 * a helyfoglalás módjának kiválasztásáért (automatikus, tulajdonság alapú,
 * grafikus) valamint a felhasználói navigációért a kapcsolódó TicketMenu
 * vezérlő felé.
 *
 * A panel egy TicketMenu vezérlőt és a PurchaseController-t használja a
 * vásárlási adatok átadására és a további UI-átmenetek indítására.
 */
public class TrainListPanel extends JPanel {


    private TicketMenu controller;

    
    private PurchaseController purchase;

    private JComboBox<String> reserveSelector;

    /**
     * Konstruktor a TrainListPanel osztályhoz
     * @param controller : A szülő TicketMenu példány
     * @param purchase : A vásárlásvezérlő példány
     * @param results : A megjelenítendő vonatok listája
     */
    public TrainListPanel(TicketMenu controller, PurchaseController purchase, List<Train> results) {
        this.controller = controller;
        this.purchase = purchase;

        setLayout(new BorderLayout());
        setBackground(ModernComponents.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        this.setSize(700, 600);
        
        initUI(results);
    }
    
    /**
     * Inicializálja a vizuális elemeket a panelhez a megadott vonatok alapján.
     * Létrehoz egy cím részt, a vonat kártyákat, a görgethető tartalmat és a
     * láblécet a vissza gombbal.
     *
     * @param results A megjelenítendő vonatok listája (nem lehet null, de lehet üres).
     */
    private void initUI(List<Train> results) {
        // --- FEJLÉC ---
        List<String> stops = purchase.getStops();
        JLabel titleLabel = new JLabel("Elérhető vonatok: " + stops.get(0) + " -> " + stops.get(1));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ModernComponents.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- TARTALOM (Kártyák) ---
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
            // Helyfoglalás mód választó
            cardsContainer.add(ModernComponents.createStyledLabel("Helyfoglalás Módja:", 12), gbc);
            
            gbc.gridy = 1;
            gbc.weightx = 0.5;
            
            String[] reserveOptions = {"Automatikus", "Tulajdonság megadása", "Grafikus"};
            this.reserveSelector = new JComboBox<>(reserveOptions);
            cardsContainer.add(reserveSelector, gbc);

            gbc.weightx = 1.0;
            gbc.gridx = 0;
            gbc.gridy = 2;
            
            // Vonatok generálása
            for (Train v : results) {
                JPanel card = createTrainCard(v, stops.get(0), stops.get(1));
                cardsContainer.add(card, gbc);
                gbc.gridy++;
            }
        }

        gbc.weighty = 1.0; // Kitöltő elem
        cardsContainer.add(Box.createGlue(), gbc);

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        ModernComponents.styleScrollPane(scrollPane);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);

        // --- LÁBLÉC ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(ModernComponents.BACKGROUND_COLOR);
        
        JButton backBtn = ModernComponents.createModernButton("Vissza az utasokhoz", new Color(100, 100, 100), Color.WHITE);
        backBtn.addActionListener(e -> {
            controller.createPassengersMenu();
        });
        footer.add(backBtn);
        
        add(footer, BorderLayout.SOUTH);
    }

    /**
     * Létrehoz és visszaad egy kártya-szerű JPanel-t, amely a megadott vonat
     * információit jeleníti meg (név, azonosító, indulás/érkezés időpontok,
     * szolgáltatások) és a hozzá tartozó jegyvásárlási gombokat.
     *
     * @param v A megjelenítendő {@link Train} objektum
     * @param from Indulási megálló neve
     * @param to A járat érkezési megállójának neve (a célállomás neve)
     * @return Egy JPanel, amely tartalmazza a vonat adatait és a jegyvásárló gombokat
     */
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
        String services = "Szolgáltatások: " + v.getServices();
        JLabel serviceLabel = new JLabel(services);
        serviceLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        serviceLabel.setForeground(Color.LIGHT_GRAY);
        card.add(serviceLabel, g);
        
        // Jobb oldal: Gombok
        g.gridx = 1; g.gridy = 0; g.gridheight = 3; g.weightx = 0.4;
        g.anchor = GridBagConstraints.CENTER;
        
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        rightPanel.setBackground(new Color(60, 63, 65)); 
        
        JButton secondClassBtn = ModernComponents.createModernButton("2. osztályú jegy", ModernComponents.BUTTON_COLOR, Color.WHITE);
        secondClassBtn.addActionListener(e -> {
            purchase.setTrain(v.getId());
            purchase.setFirstClass(false);
            handleNavigation(); 
        });

        rightPanel.add(secondClassBtn);
        System.out.println("Vonat szolgáltatások: " + v.getServices());
        if(v.getServices().contains(Train.FIRST_CLASS_TXT)){
            JButton firstClassBtn = ModernComponents.createModernButton("1. osztályú jegy", ModernComponents.BUTTON_COLOR, Color.WHITE);
            firstClassBtn.addActionListener(e -> {
                purchase.setTrain(v.getId());
                purchase.setFirstClass(true);
                handleNavigation();
            });
            rightPanel.add(firstClassBtn);
        }
        
        card.add(rightPanel, g);
        return card;
    }


    /**
     * Kezeli a helyfoglalás módjától függő navigációt.
     * A választott elemtől függően a TicketMenu különböző nézeteihez
     * navigál: automatikus helyfoglalás, attribútum alapú kiválasztás vagy
     * grafikus helyfoglalás.
     */
    private void handleNavigation() {
        String mode = (String) reserveSelector.getSelectedItem();
        System.out.println("Navigáció: " + mode);

        switch (mode) {
            case "Automatikus":
                controller.processAutomaticReservation();
                break;
            case "Tulajdonság megadása":
                controller.showAttributeSelectionPanel();
                break;
            case "Grafikus":
                controller.showVisualSeatPanel();
                break;  
        }
    }
}