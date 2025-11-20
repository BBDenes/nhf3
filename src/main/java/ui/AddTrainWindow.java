package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import train.TrainHandler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTrainWindow extends JDialog {

    private DefaultListModel<String> kocsikListModel;
    private DefaultListModel<String> megallokListModel;
    private JPanel mainPanel;
    private JTextField idField, nameField, coachAdd, stopAdd;
    private JComboBox<String> typeSelect;
    private String[] trainTypes = {"IC", "Személy", "RailJet", "Zónázó"};

    public AddTrainWindow(JFrame parent, TrainHandler th) {
        super(parent, "Új vonat felvétele", true); // Modális ablak
        
        setSize(600, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30)); // Margók

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Új vonat hozzáadása");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ModernComponents.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        int gridYCounter = 0;
        
        gbc.gridx = 0; gbc.gridy = gridYCounter++; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        mainPanel.add(ModernComponents.createStyledLabel("Vonat neve:", 14), gbc);

        gbc.gridx = 1; gbc.gridy = gridYCounter++; gbc.weightx = 0.7;
        nameField = new JTextField();
        ModernComponents.styleComponent(nameField);
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(ModernComponents.createStyledLabel("Vonat ID:", 14), gbc);

        gbc.gridx = 1; gbc.gridy = gridYCounter++;
        idField = new JTextField();
        ModernComponents.styleComponent(idField);
        mainPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = gridYCounter++;
        typeSelect = new JComboBox<>(trainTypes);
        gbc.insets = new Insets(5, 5, 5, 200);
        mainPanel.add(typeSelect, gbc);

        gbc.gridx = 0; gbc.gridy = gridYCounter++; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5); 
        JLabel carLabel = new JLabel("Csatolt kocsik:");
        carLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        carLabel.setForeground(ModernComponents.BUTTON_COLOR); 
        mainPanel.add(carLabel, gbc);

        gbc.gridy = gridYCounter++;
        gbc.weighty = 0.3; 
        gbc.fill = GridBagConstraints.BOTH;
        
        kocsikListModel = new DefaultListModel<>();
        JList<String> kocsikList = ModernComponents.createStyledList(kocsikListModel);
        JScrollPane kocsikScroll = new JScrollPane(kocsikList);
        styleScrollPane(kocsikScroll);
        mainPanel.add(kocsikScroll, gbc);

        gbc.gridy = gridYCounter++;
        gbc.weighty = 0;
        JLabel coachText = ModernComponents.createStyledLabel("Adja meg a hozzáadni kívánt kocsit: id;férőhely;bicikli;kerekesszék;firstclass;buffet", 12);
        mainPanel.add(coachText, gbc);

        gbc.gridy = gridYCounter++;
        coachAdd = new JTextField();
        mainPanel.add(coachAdd, gbc);
        

        JPanel coachButtonsPanel = createButtonPanel("Kocsi hozzáadása", "Kocsi törlése");
        gbc.gridy = gridYCounter++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(coachButtonsPanel, gbc);


        gbc.gridy = gridYCounter++;
        gbc.insets = new Insets(20, 5, 5, 5);
        JLabel stopLabel = new JLabel("Megállók és menetrend:");
        stopLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        stopLabel.setForeground(ModernComponents.BUTTON_COLOR);
        mainPanel.add(stopLabel, gbc);

        gbc.gridy = gridYCounter++;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        
        megallokListModel = new DefaultListModel<>();
        JList<String> megallokList = ModernComponents.createStyledList(megallokListModel);
        JScrollPane megallokScroll = new JScrollPane(megallokList);
        styleScrollPane(megallokScroll);
        mainPanel.add(megallokScroll, gbc);

        JPanel stopButtonsPanel = createButtonPanel("Megálló hozzáadása", "Megálló törlése");
        gbc.gridy = gridYCounter++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(stopButtonsPanel, gbc);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(ModernComponents.BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JButton saveButton = ModernComponents.createModernButton("VONAT MENTÉSE", ModernComponents.BUTTON_COLOR, Color.WHITE);
        saveButton.addActionListener(saveButtonListener);
        
        JButton cancelButton = ModernComponents.createModernButton("Mégsem", new Color(100, 100, 100), Color.WHITE);
        cancelButton.addActionListener(e -> dispose());

        bottomPanel.add(cancelButton);
        bottomPanel.add(saveButton);

        // Összeállítás
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private ActionListener saveButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Add train: " + typeSelect.getSelectedItem() + " " + idField.getText() + " " + nameField.getText());
        }
        
    };
    

    private void styleScrollPane(JScrollPane scroll) {
        scroll.getViewport().setBackground(ModernComponents.BACKGROUND_COLOR);
        scroll.setBorder(null);
    }

    private JPanel createButtonPanel(String addText, String removeText) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setBackground(ModernComponents.BACKGROUND_COLOR);
        
        JButton btnAdd = ModernComponents.createModernButton("+ " + addText, new Color(70, 70, 70), Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdd.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        JButton btnRemove = ModernComponents.createModernButton("- " + removeText, new Color(70, 70, 70), Color.WHITE);
        btnRemove.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRemove.setBorder(new EmptyBorder(8, 15, 8, 15));

        p.add(btnAdd);
        p.add(Box.createHorizontalStrut(10));
        p.add(btnRemove);
        
        return p;
    }
}
