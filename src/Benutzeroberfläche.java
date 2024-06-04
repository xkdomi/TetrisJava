import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Benutzeroberfläche extends JFrame {
    private Spiel spiel;
    private SpielfeldPanel spielfeldPanel;
    private static int highscore;
    private JLabel scoreLabel;
    private Timer timer;
    private int initialDelay;
    private int schwierigkeitsgrad;
    private boolean isPaused; // Neue Variable für den Pausestatus
    private static final Color[] THEME_DEFAULT = {Color.LIGHT_GRAY, Color.WHITE, Color.BLACK};
    private static final Color[] THEME_MODERN = {new Color(51, 153, 255), Color.WHITE, Color.BLACK};
    private Color[] currentTheme = THEME_DEFAULT; // Default theme
    private JComboBox<String> themeComboBox;


    // Methode zur Initialisierung der Benutzeroberfläche
    private void initUI() {
        spiel = new Spiel(); // Neues Spiel erstellen
        spielfeldPanel = new SpielfeldPanel(spiel); // Neues SpielfeldPanel erstellen

        // Setze Layout der Benutzeroberfläche
        setLayout(new BorderLayout());

        // Score-Label erstellen und zum BorderLayout.NORTH hinzufügen
        scoreLabel = new JLabel("Score: " + spiel.getScore() + "  Highscore: " + highscore);
        JPanel topPanel = new JPanel(new GridLayout(1, 2)); // Panel für Score und Level
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        // Create a panel for the left gap
        JPanel leftGapPanel = new JPanel();
        leftGapPanel.setPreferredSize(new Dimension(15, spielfeldPanel.getHeight())); // Adjust the width as needed
        add(leftGapPanel, BorderLayout.WEST);

        // SpielfeldPanel zum BorderLayout.CENTER hinzufügen
        add(spielfeldPanel, BorderLayout.CENTER); // SpielfeldPanel zur Benutzeroberfläche hinzufügen

        setTitle("Tetris"); // Titel des Fensters setzen
        setSize(345, 660); // Größe des Fensters setzen (SpielfeldPanel + NextTetrominoPanel)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Standard-Schließoperation setzen
        setLocationRelativeTo(null); // Fenster zentrieren



        // KeyListener hinzufügen, um Tastendrücke zu verarbeiten
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        spiel.nachLinksBewegen(); // Tetromino nach links bewegen
                        break;
                    case KeyEvent.VK_RIGHT:
                        spiel.nachRechtsBewegen(); // Tetromino nach rechts bewegen
                        break;
                    case KeyEvent.VK_DOWN:
                        spiel.fallen(); // Tetromino fallen lassen
                        break;
                    case KeyEvent.VK_UP:
                        spiel.drehen(); // Tetromino drehen
                        break;
                    case KeyEvent.VK_SPACE:
                        togglePause(); // Pause mit der Taste 'SPACE' umschalten
                        break;
                }
                spielfeldPanel.repaint(); // SpielfeldPanel neu zeichnen
            }
        });


    }

    public Benutzeroberfläche(int schwierigkeitsgrad) {
        this.schwierigkeitsgrad = schwierigkeitsgrad;
        setInitialDelay(schwierigkeitsgrad);
        initUI();
    }

    private void setInitialDelay(int schwierigkeitsgrad) {
        switch (schwierigkeitsgrad) {
            case 0://Leicht
                initialDelay = 800;
                break;
            case 1://Mittel
                initialDelay = 500;
                break;
            case 2://Schwer
                initialDelay = 250;
                break;
            case 3: // Sehr schwer
                initialDelay = 180;
                break;
            case 4: // unmöglich
                initialDelay = 3;
                break;
            default:
                initialDelay = 1000;
        }
    }



    // Methode zum Pausieren/Fortsetzen des Spiels
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop(); // Timer stoppen, wenn pausiert
            showPauseMenu(); // Pausemenü anzeigen
        } else {
            startTimer(); // Timer starten, wenn fortgesetzt
        }
    }

    // Methode zum Anzeigen des Pausemenüs
    private void showPauseMenu() {


        JDialog pauseDialog = new JDialog(this, "Pause", true);
        pauseDialog.setLayout(new BorderLayout());
        pauseDialog.setSize(350, 300);// größe des Pausemenüs
        pauseDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridLayout(4, 1));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Pause "); // Neue Überschrift
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel, BorderLayout.CENTER); // hinzufügen des Titels zum Panel

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Panel for the word "Menu"
        menuPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // Add some padding below the "Menu"

        JLabel menuLabel = new JLabel("Menü"); // Create the label with the word "Menu"
        menuLabel.setFont(new Font("Arial", Font.BOLD, 30));
        menuPanel.add(menuLabel); // Add the label to the panel

        contentPanel.add(titleLabel, BorderLayout.CENTER); // Headline is placed above the "Menu"
        contentPanel.add(menuPanel, BorderLayout.SOUTH); // "Menu" is placed below the headline




        JButton continueButton = new JButton("Fortsetzen");
        customizeButton(continueButton);
        continueButton.addActionListener(e -> {
            togglePause();
            pauseDialog.dispose();
        });

        JButton restartButton = new JButton("Neustart");
        customizeButton(restartButton);
        restartButton.addActionListener(e -> {
            pauseDialog.dispose();
            dispose();
            new Benutzeroberfläche(schwierigkeitsgrad).starten();
        });

        JButton exitButton = new JButton("Beenden");
        customizeButton(exitButton);
        exitButton.addActionListener(e -> {
            pauseDialog.dispose();
            System.exit(0);
        });

        JButton startScreenButton = new JButton("Startbildschirm"); // Added button for returning to the Startbildschirm
        customizeButton(startScreenButton);
        startScreenButton.addActionListener(e -> {
            pauseDialog.dispose();
            setVisible(false); // Hide the current Benutzeroberfläche
            new Startbildschirm().starten(); // Show the Startbildschirm
        });

        // Reihenfolge für die Buttons im Pausemenü
        contentPanel.add(continueButton); // Fortsetzen
        contentPanel.add(restartButton); // Beenden
        contentPanel.add(startScreenButton); // zum Startbilschirm
        contentPanel.add(exitButton); //Verlassen

        pauseDialog.add(contentPanel, BorderLayout.CENTER);
        pauseDialog.setVisible(true);
    }

    // Method to customize button appearance
    private void customizeButton(JButton button) {
        button.setBackground(new Color(0, 0, 128));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
    }

    // Methode zum Starten des Timers
    private void startTimer() {
        timer = new Timer(initialDelay, e -> {
            if (!spiel.istSpielVorbei() && !isPaused) { // Prüfen, ob nicht pausiert
                spiel.fallen();
                spielfeldPanel.repaint();
                if (spiel.getScore() > highscore) {
                    highscore = spiel.getScore();
                }
                updateScore();
            } else if (spiel.istSpielVorbei()) {
                ((Timer) e.getSource()).stop();
                showGameOverDialog();
            }
        });
        timer.start();
    }

    // Methode zum Aktualisieren des Score-Labels
    public void updateScore() {
        scoreLabel.setText("     Score: " + spiel.getScore() + "  Highscore: " + highscore + "   Schwierigkeit: " + getSchwierigkeitsgradString());
    }

    // Methode, um das Spiel zu starten
    public void starten() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true); // Benutzeroberfläche sichtbar machen
            startTimer(); // Timer starten, um das Tetromino periodisch fallen zu lassen
        });
    }

    // Methode, um das Spiel vorbei Dialog anzuzeigen
    private void showGameOverDialog() {
        String[] options = {"Neustart", "Schwierigkeit ändern", "Verlassen"};
        int option = JOptionPane.showOptionDialog(this,
                "Game Over!\nDein Score: " + spiel.getScore() + "\nHighscore: " + highscore + "\nWas möchtest du tun?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 16)); // Set font for message
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 14)); // Set font for buttons

        switch (option) {
            case 0: // Neustart
                dispose();
                new Benutzeroberfläche(schwierigkeitsgrad).starten();
                break;
            case 1: // Schwierigkeit ändern
                changeDifficulty();
                break;
            case 2: // Verlassen
                dispose();
                System.exit(0);
                break;
            default:
                dispose();
                System.exit(0);
                break;
        }
    }

    // Methode, um die Schwierigkeit zu ändern
    private void changeDifficulty() {
        String[] difficultyOptions = {"Leicht", "Mittel", "Schwer", "Sehr Schwer","Unmöglich"};
        int newDifficulty = JOptionPane.showOptionDialog(this,
                "Wähle einen neuen Schwierigkeitsgrad:",
                "Schwierigkeit ändern",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                difficultyOptions,
                difficultyOptions[schwierigkeitsgrad]);

        if (newDifficulty >= 0 && newDifficulty <= 4) {
            dispose();
            new Benutzeroberfläche(newDifficulty).starten();
        } else {
            showGameOverDialog();
        }
    }



    // Getter für den Schwierigkeitsgrad
    public int getSchwierigkeitsgrad() {
        return schwierigkeitsgrad;
    }

    // Setter für den Schwierigkeitsgrad
    public void setSchwierigkeitsgrad(int schwierigkeitsgrad) {
        this.schwierigkeitsgrad = schwierigkeitsgrad;

    }

    // Methode, um den Schwierigkeitsgrad als String zu erhalten
    private String getSchwierigkeitsgradString() {
        switch (schwierigkeitsgrad) {
            case 0: return "Leicht";
            case 1: return "Mittel";
            case 2: return "Schwer";
            case 3: return "Sehr Schwer";
            case 4: return "Unmöglich";
            default: return "Unbekannt";
        }
    }

    // Function to set the color theme
    public void setColorTheme(Color[] theme) {
        currentTheme = theme;
        // Update UI components with the new color theme
        updateUIComponents();

    }

    // Method to update UI components with the new color theme
    private void updateUIComponents() {
        // Update background colors, font colors, etc. of UI components based on the current theme
        getContentPane().setBackground(currentTheme[0]); // Background color
        scoreLabel.setForeground(currentTheme[1]); // Score label font color
        // Update other UI components as needed
    }

}
