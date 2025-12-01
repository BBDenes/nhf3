package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import train.Train;
import train.TrainHandler;
import tickets.Ticket;
import tickets.Reservation;
import utilities.PurchaseController;

public class SummaryPanel extends JPanel {

    private TicketMenu controller;
    private PurchaseController purchase;
    private TrainHandler trainHandler;

    public SummaryPanel(TicketMenu controller, TrainHandler th, PurchaseController purchase) {
        this.controller = controller;
        this.trainHandler = th;
        this.purchase = purchase;

        setLayout(new BorderLayout());
        setBackground(ModernComponents.BACKGROUND_COLOR);
        
        initUI();
    }

    private void initUI() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        
        JLabel title = new JLabel("Rendelés összegzése");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(ModernComponents.TEXT_COLOR);
        headerPanel.add(title, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        Train train = trainHandler.getTrainByIndex(purchase.getTrainId());
        if (train != null) {
            String services = "Vonat: " + purchase.getStops().get(0) + "-> " + purchase.getStops().get(1) + " | " + train.getName() + " " + train.getType() + " | Szolgáltatások: ";
            if (train.getServices().contains("Buffet") || train.getServices().contains("Bufe")) services += "Büfé ";
            if (train.getServices().contains("FirstClass") || train.getServices().contains("1")) services += "1.o ";
            
            JLabel trainInfo = new JLabel(services);
            trainInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            trainInfo.setForeground(Color.LIGHT_GRAY);
            headerPanel.add(trainInfo, gbc);
        }

        add(headerPanel, BorderLayout.NORTH);

        JPanel ticketsContainer = new JPanel(new GridBagLayout());
        ticketsContainer.setBackground(ModernComponents.BACKGROUND_COLOR);
        
        GridBagConstraints listGbc = new GridBagConstraints();
        listGbc.gridx = 0; listGbc.gridy = 0;
        listGbc.weightx = 1.0;
        listGbc.fill = GridBagConstraints.HORIZONTAL;
        listGbc.insets = new Insets(0, 0, 10, 0);

        List<Ticket> tickets = purchase.getTicketsToBuy();
        int totalPrice = 0;

        if (tickets.isEmpty()) {
            JLabel emptyLbl = new JLabel("Nincs kiválasztott jegy.");
            emptyLbl.setForeground(Color.GRAY);
            ticketsContainer.add(emptyLbl, listGbc);
        } else {
            for (Ticket t : tickets) {
                JPanel ticketCard = createTicketCard(t);
                ticketsContainer.add(ticketCard, listGbc);
                listGbc.gridy++;
                totalPrice += t.getPrice();
            }
        }
        
        listGbc.weighty = 1.0;
        ticketsContainer.add(Box.createGlue(), listGbc);

        JScrollPane scrollPane = new JScrollPane(ticketsContainer);
        ModernComponents.styleScrollPane(scrollPane);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JLabel priceLabel = new JLabel("Végösszeg: " + totalPrice + " Ft");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        priceLabel.setForeground(ModernComponents.BUTTON_COLOR);
        priceLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        footerPanel.add(priceLabel, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBackground(ModernComponents.BACKGROUND_COLOR);

        JButton btnBuy = ModernComponents.createModernButton("VÁSÁRLÁS BEFEJEZÉSE", ModernComponents.BUTTON_COLOR, Color.WHITE);
        btnBuy.addActionListener(e -> finalizePurchase());

        buttons.add(btnBuy);
        footerPanel.add(buttons, BorderLayout.SOUTH);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createTicketCard(Ticket t) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(60, 63, 65));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 5));
        left.setBackground(new Color(60, 63, 65));
        
        JLabel nameLbl = new JLabel(t.getPassengerName());
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLbl.setForeground(Color.WHITE);
        
        String details = "";
        details += t.getTicketType();
        if (t instanceof Reservation) {
            Reservation res = (Reservation) t;
            details += " | Kocsi: " + res.getCoach() + " | Hely: " + res.getSeat();
            
            if (res.getPassId() != null && !res.getPassId().isEmpty()) { 
                details += " | Bérlet ID: " + res.getPassId();
            }
        } else {
            details += " | Helyjegy nélküli menetjegy";
        }
        JLabel infoLbl = new JLabel(details);
        infoLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLbl.setForeground(Color.LIGHT_GRAY);

        left.add(nameLbl);
        left.add(infoLbl);
        card.add(left, BorderLayout.CENTER);

        // Jobb oldal: Ár
        JLabel priceLbl = new JLabel(t.getPrice() + " Ft");
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLbl.setForeground(ModernComponents.BUTTON_COLOR);
        card.add(priceLbl, BorderLayout.EAST);

        return card;
    }

    private void finalizePurchase() {
        trainHandler.finalizeBooking(purchase);
        for (Ticket t : purchase.getTicketsToBuy()) {
            t.writeToHtml(); 
        }
        JOptionPane.showMessageDialog(this, "Vásárlás sikeres!\nA jegyeket elmentettük fájlokba.", "Siker", JOptionPane.INFORMATION_MESSAGE);
        controller.dispose();
    }
}
