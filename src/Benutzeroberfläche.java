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
    private boolean isPaused;

    private void initUI() {
        spiel = new Spiel();
        spielfeldPanel = new SpielfeldPanel(spiel);

        setLayout(new BorderLayout());

        scoreLabel = new JLabel("Score: " + spiel.getScore() + "  Highscore: " + highscore);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(new Color(50, 50, 50));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(scoreLabel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);
        add(createLeftGapPanel(), BorderLayout.WEST);
        add(spielfeldPanel, BorderLayout.CENTER);
        add(createRightGapPanel(), BorderLayout.EAST);

        setTitle("Tetris");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        spiel.nachLinksBewegen();
                        break;
                    case KeyEvent.VK_RIGHT:
                        spiel.nachRechtsBewegen();
                        break;
                    case KeyEvent.VK_DOWN:
                        spiel.fallen();
                        break;
                    case KeyEvent.VK_UP:
                        spiel.drehen();
                        break;
                    case KeyEvent.VK_SPACE:
                        togglePause();
                        break;
                }
                spielfeldPanel.repaint();
            }
        });

        startTimer();
    }

    public Benutzeroberfläche(int schwierigkeitsgrad) {
        this.schwierigkeitsgrad = schwierigkeitsgrad;
        setInitialDelay(schwierigkeitsgrad);
        initUI();
    }

    private void setInitialDelay(int schwierigkeitsgrad) {
        switch (schwierigkeitsgrad) {
            case 0:
                initialDelay = 800;
                break;
            case 1:
                initialDelay = 500;
                break;
            case 2:
                initialDelay = 250;
                break;
            case 3:
                initialDelay = 180;
                break;
            case 4:
                initialDelay = 3;
                break;
            default:
                initialDelay = 1000;
        }
    }

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

    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            showPauseMenu();
        } else {
            startTimer();
        }
    }

    private void showPauseMenu() {
        JDialog pauseDialog = new JDialog(this, "Pause", true);
        pauseDialog.setLayout(new BorderLayout());
        pauseDialog.setSize(350, 300);
        pauseDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridLayout(4, 1));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Pause");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel);

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


    //GameOverMenü

    private void showGameOverMenu() {
        if (spiel.getScore() > highscore) {
            highscore = spiel.getScore();
        }

        JDialog gameOverDialog = new JDialog(this, "Game Over", true);
        gameOverDialog.setLayout(new BorderLayout());
        gameOverDialog.setSize(600, 300);
        gameOverDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridLayout(4, 1));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Game Over");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel);

        JLabel scoreLabel = new JLabel("Your Score: " + spiel.getScore()+"  High Score: " + highscore);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(scoreLabel);


        JButton restartButton = new JButton("Neustart");
        customizeButton(restartButton);
        restartButton.addActionListener(e -> {
            gameOverDialog.dispose();
            dispose();
            new Benutzeroberfläche(schwierigkeitsgrad).starten();
        });

        JButton mainMenuButton = new JButton("Startbildschirm");
        customizeButton(mainMenuButton);
        mainMenuButton.addActionListener(e -> {
            gameOverDialog.dispose();
            dispose();
            new Startbildschirm().setVisible(true);
        });

        JButton exitButton = new JButton("Beenden");
        customizeButton(exitButton);
        exitButton.addActionListener(e -> {
            gameOverDialog.dispose();
            dispose();
        });

        contentPanel.add(restartButton);
        contentPanel.add(mainMenuButton);
        contentPanel.add(exitButton);

        gameOverDialog.add(contentPanel, BorderLayout.CENTER);
        gameOverDialog.setVisible(true);
    }

    private void customizeButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(0, 0, 128));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(27));
        button.setPreferredSize(new Dimension(250, 50));
    }

    private JPanel createLeftGapPanel() {
        JPanel leftGapPanel = new JPanel();
        leftGapPanel.setPreferredSize(new Dimension(20, spielfeldPanel.getHeight()));
        leftGapPanel.setBackground(new Color(240, 240, 240));
        return leftGapPanel;
    }

    private JPanel createRightGapPanel() {
        JPanel rightGapPanel = new JPanel();
        rightGapPanel.setPreferredSize(new Dimension(5, spielfeldPanel.getHeight()));
        rightGapPanel.setBackground(new Color(240, 240, 240));
        return rightGapPanel;
    }

    public void starten() {
        EventQueue.invokeLater(() -> {
            setVisible(true);
        });
    }

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
