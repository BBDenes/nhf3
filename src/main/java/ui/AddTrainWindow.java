package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import train.*;

public class AddTrainWindow extends JDialog {

    private JComboBox<String> typeSelect;
    private final String[] trainTypes = {"IC", "Személy", "RailJet", "Zónázó"};
    private List<Coach> coachesToAdd;
    private List<Stop> stopsToAdd;
    private TrainHandler th;
    private JTextField idField, nameField;

    public AddTrainWindow(JFrame parent, TrainHandler th) {
        super(parent, "Új vonat felvétele", true);
        coachesToAdd = new LinkedList<>();
        stopsToAdd = new LinkedList<>();
        this.th = th;
        
        setSize(800, 600); 
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        renderWindow();
    }

    private void renderWindow() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        renderBasicInfo(topPanel);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        DefaultListModel<String> kocsikModel = new DefaultListModel<>();
        JList<String> coachListUI = ModernComponents.createStyledList(kocsikModel);
        JTextField coachAddField = new JTextField();
        ModernComponents.styleComponent(coachAddField);

        JPanel coachesPanel = createSectionPanel(
            "Csatolt kocsik",
            kocsikModel, coachListUI,
            "Kocsi: id;férőhely;bicikli;szék;osztály;büfé",
            coachAddField,
            e-> coachAddListener(kocsikModel, coachAddField),
            e -> {
                removeFromList(kocsikModel, coachListUI);
                coachesToAdd.remove(coachListUI.getSelectedIndex());
            }
        );

        DefaultListModel<String> megallokModel = new DefaultListModel<>();
        JList<String> megallokListUI = ModernComponents.createStyledList(megallokModel);
        JTextField stopAddField = new JTextField();
        ModernComponents.styleComponent(stopAddField);

        JPanel stopsPanel = createSectionPanel(
            "Megállók és menetrend",
            megallokModel, megallokListUI,
            "Megálló: Név;Érk;Ind",
            stopAddField,
            e -> stopAddListener(megallokModel, stopAddField),
            e -> removeFromList(megallokModel, megallokListUI)
        );

        centerPanel.add(coachesPanel);
        centerPanel.add(stopsPanel);
        
        add(centerPanel, BorderLayout.CENTER);

        renderFooterSection();
    }

    private void renderBasicInfo(JPanel p) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Új vonat hozzáadása");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ModernComponents.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        gbc.insets = new Insets(0, 0, 20, 0);
        p.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        p.add(ModernComponents.createStyledLabel("Név:", 14), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.4;
        nameField = new JTextField();
        ModernComponents.styleComponent(nameField);
        p.add(nameField, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        p.add(ModernComponents.createStyledLabel("ID:", 14), gbc);

        gbc.gridx = 3; gbc.weightx = 0.4;
        idField = new JTextField();
        ModernComponents.styleComponent(idField);
        p.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        p.add(ModernComponents.createStyledLabel("Típus:", 14), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3; 
        typeSelect = new JComboBox<>(trainTypes);
        p.add(typeSelect, gbc);
    }
    private JPanel createSectionPanel(String title, DefaultListModel<String> model, JList<String> listUI, 
                                      String hint, JTextField inputField, 
                                      ActionListener onAdd, ActionListener onRemove) {
        
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(ModernComponents.BACKGROUND_COLOR);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(ModernComponents.BUTTON_COLOR);
        panel.add(lblTitle, BorderLayout.NORTH);
        
        //TODO: esetleg majd táblázat lista helyett, talán szebb lenne
        JScrollPane scroll = new JScrollPane(listUI);
        ModernComponents.styleScrollPane(scroll); 
        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottomPart = new JPanel(new GridBagLayout());
        bottomPart.setBackground(ModernComponents.BACKGROUND_COLOR);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(2, 0, 2, 0);
        g.weightx = 1.0;
        g.gridx = 0;

        JLabel lblHint = ModernComponents.createStyledLabel(hint, 12);
        lblHint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        bottomPart.add(lblHint, g);

        bottomPart.add(inputField, g);

        // Gombok
        JPanel btnPanel = createButtonPanel("+ Hozzáad", "- Töröl", onAdd, onRemove);
        bottomPart.add(btnPanel, g);

        panel.add(bottomPart, BorderLayout.SOUTH);

        return panel;
    }
    private void removeFromList(DefaultListModel<String> model, JList<String> listUI) {
        int selectedIndex = listUI.getSelectedIndex();
        if (selectedIndex != -1) {
            model.remove(selectedIndex);
        }
    }

    private JPanel createButtonPanel(String addText, String removeText, ActionListener onAdd, ActionListener onRemove) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setBackground(ModernComponents.BACKGROUND_COLOR);
        
        JButton btnAdd = ModernComponents.createModernButton(addText, new Color(70, 70, 70), Color.WHITE); // Rövidebb szöveg
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdd.setBorder(new EmptyBorder(8, 10, 8, 10));
        btnAdd.addActionListener(onAdd);
        
        JButton btnRemove = ModernComponents.createModernButton(removeText, new Color(70, 70, 70), Color.WHITE);
        btnRemove.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRemove.setBorder(new EmptyBorder(8, 10, 8, 10));
        btnRemove.addActionListener(onRemove);

        p.add(btnAdd);
        p.add(Box.createHorizontalStrut(10));
        p.add(btnRemove);
        
        return p;
    }

    private void renderFooterSection() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JButton saveButton = ModernComponents.createModernButton("VONAT MENTÉSE", ModernComponents.BUTTON_COLOR, Color.WHITE);
        saveButton.addActionListener(e -> {
            try{
                int newId = Integer.parseInt(idField.getText());

                th.addTrain(newId, nameField.getText(), (String) typeSelect.getSelectedItem() , coachesToAdd, stopsToAdd);
                System.out.println("Vonat hozzaadva");
                dispose();
            }catch(Exception trainException) {
                JOptionPane.showMessageDialog(saveButton, "Hiba: " + trainException.getMessage());
            }
        });
        
        JButton cancelButton = ModernComponents.createModernButton("Mégsem", new Color(100, 100, 100), Color.WHITE);
        cancelButton.addActionListener(e -> dispose());

        JButton resetTrainButton = ModernComponents.createModernButton("Vonat visszaállítása", Color.RED, ModernComponents.TEXT_COLOR);
        resetTrainButton.addActionListener(e-> {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Adja meg a vonat számát: "));
            try {
                th.resetReservation(id);
                JOptionPane.showMessageDialog(resetTrainButton, "Vonat alaphelyzetbe állítva.","Siker!", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(resetTrainButton, "Nincs ilyen vonat!","Hiba", JOptionPane.ERROR_MESSAGE);
            }
            
        });

        JButton deleteTrainBtn = ModernComponents.createModernButton("Vonat törlése", Color.RED, ModernComponents.TEXT_COLOR);
        deleteTrainBtn.addActionListener(e->{
            int id = Integer.parseInt(JOptionPane.showInputDialog("Adja meg a vonat számát: "));
            try {
                th.deleteTrainByIndex(id);
                JOptionPane.showMessageDialog(resetTrainButton,"Vonat törölve.","Siker!", JOptionPane.INFORMATION_MESSAGE);
            
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(deleteTrainBtn, "Nincs ilyen vonat!","Hiba", JOptionPane.ERROR_MESSAGE);
            }
        });


        bottomPanel.add(cancelButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(resetTrainButton);
        bottomPanel.add(deleteTrainBtn);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }


    private void coachAddListener(DefaultListModel<String> model,  JTextField sourceField){
        String text = sourceField.getText();
        if (text != null && !text.trim().isEmpty()) {
            try {
                Coach newCoach = new Coach(text);
                coachesToAdd.add(newCoach);
                
                model.addElement(newCoach.toString());
                sourceField.setText("");
                
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                sourceField.setText("");
            }
        }
    }

    private void stopAddListener(DefaultListModel<String> model,  JTextField sourceField){
        String text = sourceField.getText();
        if (text != null && !text.trim().isEmpty()) {
            try {
                Stop newStop = new Stop(text);
                stopsToAdd.add(newStop);
                
                model.addElement(newStop.toString());
                sourceField.setText("");
                
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                sourceField.setText("");
            }
        }
    }
}