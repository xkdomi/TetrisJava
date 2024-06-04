import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class Startbildschirm extends JFrame {
    private JComboBox<String> schwierigkeitsgradComboBox;


    public Startbildschirm() {
        initUI();
    }

    private void initUI() {
        setTitle("Tetris");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 128)); // Farbverlauf für den Header
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Abstand zum Rand hinzufügen

        JLabel headerLabel = new JLabel("Tetris by Dominik");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Modernere Schriftart
        headerLabel.setForeground(Color.WHITE); // Textfarbe auf Weiß ändern
        headerPanel.add(headerLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Abstand zum Rand hinzufügen

        schwierigkeitsgradComboBox = new JComboBox<>(new String[]{"Leicht", "Mittel", "Schwer","Sehr Schwer","Unmöglich"});
        schwierigkeitsgradComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 20)); // Modernere Schriftart
        schwierigkeitsgradComboBox.setBackground(new Color(65, 105, 225));
        schwierigkeitsgradComboBox.setForeground(Color.WHITE); // Textfarbe auf Weiß ändern

        JButton startButton = new JButton("Spiel Starten");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Modernere Schriftart
        startButton.setBackground(new Color(0, 0, 128)); // Hintergrundfarbe der Schaltfläche Starten ändern
        startButton.setForeground(Color.WHITE); // Textfarbe auf Weiß ändern
        startButton.setFocusPainted(false); // Entferne den Fokusrahmen
        startButton.setBorder(new RoundedBorder(27)); // Verwende abgerundete Ecken


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int schwierigkeitsgrad = schwierigkeitsgradComboBox.getSelectedIndex();
                Benutzeroberfläche spiel = new Benutzeroberfläche(schwierigkeitsgrad);
                spiel.starten();
                setVisible(false);
            }
        });

        JButton exitButton = new JButton("Beenden");
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Modernere Schriftart
        exitButton.setBackground(new Color(0, 0, 128)); // Hintergrundfarbe der Schaltfläche Beenden ändern
        exitButton.setForeground(Color.WHITE); // Textfarbe auf Weiß ändern
        exitButton.setFocusPainted(false); // Entferne den Fokusrahmen
        exitButton.setBorder(new RoundedBorder(27)); // Verwende abgerundete Ecken
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(startButton);
        buttonPanel.add(schwierigkeitsgradComboBox);
        buttonPanel.add(exitButton);

        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        getContentPane().setBackground(Color.WHITE); // Hintergrundfarbe auf Weiß ändern
        setLocationRelativeTo(null);


        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null);
    }



    public void starten() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
}
