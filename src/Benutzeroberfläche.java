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

        // SpielfeldPanel zum BorderLayout.CENTER hinzufügen
        add(spielfeldPanel, BorderLayout.CENTER); // SpielfeldPanel zur Benutzeroberfläche hinzufügen

        setTitle("Tetris"); // Titel des Fensters setzen
        setSize(325, 660); // Größe des Fensters setzen (SpielfeldPanel + NextTetrominoPanel)
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
        pauseDialog.setLayout(new GridLayout(3, 1));
        pauseDialog.setSize(300, 200);
        pauseDialog.setLocationRelativeTo(this);

        JButton continueButton = new JButton("Fortsetzen");
        continueButton.setFont(new Font("Arial", Font.BOLD, 20));
        continueButton.addActionListener(e -> {
            togglePause();
            pauseDialog.dispose();
        });

        JButton restartButton = new JButton("Neustart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.addActionListener(e -> {
            pauseDialog.dispose();
            dispose();
            new Benutzeroberfläche(schwierigkeitsgrad).starten();
        });

        JButton exitButton = new JButton("Beenden");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.addActionListener(e -> {
            pauseDialog.dispose();
            System.exit(0);
        });

        pauseDialog.add(continueButton);
        pauseDialog.add(restartButton);
        pauseDialog.add(exitButton);

        pauseDialog.setVisible(true);
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
        scoreLabel.setText("Score: " + spiel.getScore() + "  Highscore: " + highscore + "  " + getSchwierigkeitsgradString());
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


}
