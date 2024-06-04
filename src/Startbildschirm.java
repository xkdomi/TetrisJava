import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Klasse für den Startbildschirm
class Startbildschirm extends JFrame {
    private JComboBox<String> schwierigkeitsgradComboBox; // ComboBox zur Auswahl des Schwierigkeitsgrads

    // Konstruktor zur Initialisierung des Startbildschirms
    public Startbildschirm() {
        initUI();
    }

    // Methode zur Initialisierung der Benutzeroberfläche des Startbildschirms
    private void initUI() {
        setTitle("Tetris Spiel");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel für die Überschrift
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Tetris by Dominik");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerPanel.add(headerLabel);

        // Panel für die Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10)); // 3 Zeilen für 2 Buttons und 1 ComboBox

        // Schwierigkeitsgrad-ComboBox erstellen
        schwierigkeitsgradComboBox = new JComboBox<>(new String[]{"Leicht", "Mittel", "Schwer","Sehr Schwer","Unmöglich"});
        schwierigkeitsgradComboBox.setFont(new Font("Arial", Font.BOLD, 20));

        // Start-Button erstellen
        JButton startButton = new JButton("Spiel Starten");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Wähle den Schwierigkeitsgrad basierend auf der Auswahl in der ComboBox
                int schwierigkeitsgrad = schwierigkeitsgradComboBox.getSelectedIndex();

                // Neues Spiel starten
                Benutzeroberfläche spiel = new Benutzeroberfläche(schwierigkeitsgrad);
                spiel.starten();

                // Startbildschirm ausblenden
                setVisible(false);
            }
        });

        // Beenden-Button erstellen
        JButton exitButton = new JButton("Beenden");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Programm beenden
                System.exit(0);
            }
        });

        // ComboBox und Buttons zum Panel hinzufügen

        buttonPanel.add(startButton);
        buttonPanel.add(schwierigkeitsgradComboBox);
        buttonPanel.add(exitButton);

        // Panels zum Fenster hinzufügen
        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Hintergrundfarbe des Panels setzen
        buttonPanel.setBackground(Color.LIGHT_GRAY);
    }

    // Methode zum Starten der Benutzeroberfläche
    public void starten() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setVisible(true); // Fenster sichtbar machen
            }
        });
    }
}