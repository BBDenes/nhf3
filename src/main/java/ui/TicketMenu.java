package ui;

import javax.swing.*;
import java.awt.*;
import train.*;

// JDialog-ból származtatjuk, mert ez egy "felugró" ablak lesz
public class TicketMenu extends JDialog {

    private JComboBox<String> honnanBox;
    private JComboBox<String> hovaBox;
    private JSpinner darabSpinner;
    private TrainHandler trainHandler; // Ha szükség van az adatokra

    // A konstruktorban kérjük el a szülő ablakot (parent) és az adatokat (th)
    public TicketMenu(JFrame parent, TrainHandler th) {
        super(parent, "Jegyvásárlás", true); // true = MODÁLIS (blokkolja a szülőt)
        this.trainHandler = th;
        
        this.setSize(400, 300);
        this.setLocationRelativeTo(parent); // A szülőhöz képest középre
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- 1. HONNAN ---
        gbc.gridx = 0; gbc.gridy = 0;
        this.add(new JLabel("Honnan:"), gbc);

        gbc.gridx = 1;
        String[] allomasok = {"Budapest", "Debrecen", "Szeged", "Pécs"}; 
        honnanBox = new JComboBox<>(allomasok);
        this.add(honnanBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        this.add(new JLabel("Hova:"), gbc);

        gbc.gridx = 1;
        hovaBox = new JComboBox<>(allomasok);
        this.add(hovaBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        this.add(new JLabel("Utasok száma:"), gbc);

        gbc.gridx = 1;
        SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
        darabSpinner = new JSpinner(model);
        this.add(darabSpinner, gbc);

        // --- 4. VÁSÁRLÁS GOMB ---
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2; // Két oszlopot foglaljon el
        JButton vasarlasGomb = new JButton("Vásárlás");
        
        vasarlasGomb.addActionListener(e -> {
            vasarlasFeldolgozasa();
        });
        
        this.add(vasarlasGomb, gbc);
    }

    private void vasarlasFeldolgozasa() {
        String honnan = (String) honnanBox.getSelectedItem();
        String hova = (String) hovaBox.getSelectedItem();
        int darab = (Integer) darabSpinner.getValue();

        if(honnan.equals(hova)) {
            JOptionPane.showMessageDialog(this, "A kiindulás és cél nem lehet ugyanaz!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ITT TÖRTÉNIK A VÁSÁRLÁS LOGIKA (pl. TrainHandler hívása)
        System.out.println("Vásárlás: " + honnan + " -> " + hova + ", " + darab + " fő.");
        
        // Bezárjuk az ablakot sikeres vásárlás után
        dispose(); 
    }
}
