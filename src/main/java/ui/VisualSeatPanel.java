package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import train.Coach;
import train.Train;
import train.TrainHandler;
import utilities.PurchaseController;

public class VisualSeatPanel extends JPanel {

    private TicketMenu controller;
    private TrainHandler trainHandler;
    private PurchaseController purchase;

    // Ezeket a változókat áthoztuk ide, mert csak ehhez a nézethez kellenek
    private JPanel seatMapPanel;
    private JLabel coachInfoLabel;
    private List<Integer> reservedSeatIds = new ArrayList<>();

    public VisualSeatPanel(TicketMenu controller, TrainHandler th, PurchaseController purchase) {
        this.controller = controller;
        this.trainHandler = th;
        this.purchase = purchase;

        setLayout(new BorderLayout());
        setBackground(ModernComponents.BACKGROUND_COLOR);

        initUI();
    }

    private void initUI() {
        Train train = trainHandler.getTrainByIndex(purchase.getTrainId());
        int trainId = purchase.getTrainId();
        boolean isFirstClass = purchase.isFirstClass();

        // --- FEJLÉC ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        
        JLabel title = new JLabel("Válasszon ki " + purchase.getPassengerCount() + " helyet a térképen!");  
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(ModernComponents.TEXT_COLOR);
        headerPanel.add(title);
        add(headerPanel, BorderLayout.NORTH);

        // --- KÖZÉP (Kocsi választó + Ülés térkép) ---
        JPanel centerContainer = new JPanel(new BorderLayout());
        
        // Felső sáv (Kocsik)
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

        for (Coach c : coaches) {
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

        // Infó címke
        coachInfoLabel = new JLabel("Kérjük, válasszon egy kocsit a fenti sávból!");
        coachInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        coachInfoLabel.setForeground(ModernComponents.BUTTON_COLOR);
        coachInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coachInfoLabel.setBorder(new EmptyBorder(10, 0, 10, 0)); 
        
        topWrapper.add(coachInfoLabel, BorderLayout.SOUTH);
        centerContainer.add(topWrapper, BorderLayout.NORTH);

        // Ülés térkép panel
        seatMapPanel = new JPanel();
        seatMapPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        seatMapPanel.setLayout(new GridBagLayout());
        
        JScrollPane seatScroll = new JScrollPane(seatMapPanel);
        ModernComponents.styleScrollPane(seatScroll);
        seatScroll.setBorder(null);
        centerContainer.add(seatScroll, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        // --- LÁBLÉC (Gombok) ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(ModernComponents.BACKGROUND_COLOR);
        
        JButton btnCancel = ModernComponents.createModernButton("Mégse", Color.GRAY, Color.WHITE);
        btnCancel.addActionListener(e -> {
            reservedSeatIds.clear();
            // Visszahívunk a controllerbe, hogy jelenítse meg a vonatlistát
            // Ehhez a TicketMenu-ben lennie kell egy showTrainListPanel metódusnak (vagy hasonló)
            // Itt most újra keresünk, hogy visszakapjuk a listát
            List<Train> results = trainHandler.searchTrains(purchase.getStops().get(0), purchase.getStops().get(1));
            controller.showTrainListPanel(results); 
        });
        footer.add(btnCancel);

        JButton advanceBtn = ModernComponents.createModernButton("Tovább!", ModernComponents.BUTTON_COLOR, ModernComponents.TEXT_COLOR);
        advanceBtn.addActionListener(e -> {
            if (reservedSeatIds.size() > purchase.getPassengerCount()) {
                JOptionPane.showMessageDialog(this, "Több a kijelölt szék, mint az utas!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (reservedSeatIds.size() < purchase.getPassengerCount()) {
                JOptionPane.showMessageDialog(this, "Kevesebb a kijelölt szék, mint az utas!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            purchase.setSeatsToReserve(reservedSeatIds);
            
            try {
                trainHandler.reserveSpecificSeats(purchase);
                JOptionPane.showMessageDialog(this, "Sikeres foglalás!");
                controller.showSummaryPanel(); // Átlépés az összegzésre
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hiba: " + ex.getMessage());
            }
        });

        footer.add(advanceBtn);
        add(footer, BorderLayout.SOUTH);

        // Kezdőállapot betöltése
        if (firstValidCoach != null) {
            renderSeatMap(firstValidCoach, trainId, isFirstClass);
        }
    }

    private void renderSeatMap(Coach c, int trainId, boolean isFirstClass) {
        String osztaly = c.isFirstClass() ? "1. Osztály" : "2. Osztály";
        coachInfoLabel.setText("Jelenlegi kocsi: " + c.getId() + ". számú kocsi (" + osztaly + ")");
        
        seatMapPanel.removeAll(); // Törlés újrarajzolás előtt

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
            
            if (!isReserved) {
                seatBtn.addActionListener(e -> {
                    int id = Integer.parseInt(seatBtn.getText());
                    int globalSeatId = purchase.getGlobalSeatId(c.getId(), id);
                    boolean isSelected = reservedSeatIds.contains(globalSeatId);
                    
                    if (isSelected) {
                        seatBtn.setBackground(ModernComponents.BUTTON_COLOR); // Eredeti szín visszaállítása
                        reservedSeatIds.remove(Integer.valueOf(globalSeatId));
                    } else {
                        reservedSeatIds.add(Integer.valueOf(globalSeatId));
                        seatBtn.setBackground(seatBtn.getBackground().darker().darker()); // Kijelölt szín
                    }
                    
                });
                
                // Ha már korábban kiválasztottuk (pl. kocsiváltás után visszajövünk), színezzük be
                int globalSeatId = purchase.getGlobalSeatId(c.getId(), i);
                if (reservedSeatIds.contains(globalSeatId)) {
                    seatBtn.setBackground(ModernComponents.BUTTON_COLOR.darker().darker());
                }
            }
            
            grid.add(seatBtn);
        }

        seatMapPanel.add(grid);
        seatMapPanel.revalidate();
        seatMapPanel.repaint();
    }
}