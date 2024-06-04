import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        startTimer(); // Timer starten
    }

    public Benutzeroberfläche(int schwierigkeitsgrad) {
        this.schwierigkeitsgrad = schwierigkeitsgrad;
        setInitialDelay(schwierigkeitsgrad);
        initUI();
    }

    private void setInitialDelay(int schwierigkeitsgrad) {
        switch (schwierigkeitsgrad) {
            case 0: // Leicht
                initialDelay = 800;
                break;
            case 1: // Mittel
                initialDelay = 500;
                break;
            case 2: // Schwer
                initialDelay = 250;
                break;
            case 3: // Sehr schwer
                initialDelay = 180;
                break;
            case 4: // Unmöglich
                initialDelay = 3;
                break;
            default:
                initialDelay = 1000;
        }
    }

    // Methode zum Starten des Timers
    private void startTimer() {
        timer = new Timer(initialDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!spiel.istSpielVorbei()) {
                    spiel.fallen();
                    scoreLabel.setText("Score: " + spiel.getScore() + "  Highscore: " + highscore);
                    spielfeldPanel.repaint();
                } else {
                    timer.stop();
                    showGameOverMenu();
                }
            }
        });
        timer.start();
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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Pause "); // Neue Überschrift
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel, BorderLayout.CENTER); // hinzufügen des Titels zum Panel

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 20, 0));
        JLabel menuLabel = new JLabel("Menü");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 30));
        menuPanel.add(menuLabel);

        contentPanel.add(titleLabel, BorderLayout.CENTER);
        contentPanel.add(menuPanel, BorderLayout.SOUTH);

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
            dispose();
        });

        contentPanel.add(continueButton);
        contentPanel.add(restartButton);
        contentPanel.add(exitButton);

        pauseDialog.add(contentPanel, BorderLayout.CENTER);
        pauseDialog.setVisible(true);
    }



    // Methode zum Anzeigen des Game-Over-Menüs
    private void showGameOverMenu() {
        // Update high score if current score is greater
        if (spiel.getScore() > highscore) {
            highscore = spiel.getScore();
        }

        JDialog gameOverDialog = new JDialog(this, "Game Over", true);
        gameOverDialog.setLayout(new BorderLayout());
        gameOverDialog.setSize(600, 400);
        gameOverDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridLayout(4, 1));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Game Over");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel);

        JLabel scoreLabel = new JLabel("Dein Score: " + spiel.getScore() + "   \nHighScore:  " + highscore );
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(scoreLabel);

        JButton mainMenuButton = new JButton("Zum Startbildschirm");
        customizeButton(mainMenuButton);
        mainMenuButton.addActionListener(e -> {
            gameOverDialog.dispose();
            dispose();
            new Startbildschirm().setVisible(true); // Gehe zum Startbildschirm
        });


        JButton restartButton = new JButton("Neustart");
        customizeButton(restartButton);
        restartButton.addActionListener(e -> {
            gameOverDialog.dispose();
            dispose();
            new Benutzeroberfläche(schwierigkeitsgrad).starten();
        });

        JButton exitButton = new JButton("Beenden");
        customizeButton(exitButton);
        exitButton.addActionListener(e -> {
            gameOverDialog.dispose();
            dispose();
        });

        contentPanel.add(restartButton);
        contentPanel.add(exitButton);
        contentPanel.add(mainMenuButton);
        gameOverDialog.add(contentPanel, BorderLayout.CENTER);
        gameOverDialog.setVisible(true);
    }

    // Methode zum Anpassen der Schaltflächen
    private void customizeButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(0, 0, 128));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(27));
    }

    public void starten() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }

    // Eigene Klasse für abgerundete Kanten der Schaltflächen
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
