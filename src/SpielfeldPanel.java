import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

// Klasse, die das Spielfeld des Tetris-Spiels darstellt
public class SpielfeldPanel extends JPanel {
    private Spiel spiel;
    private Map<Integer, Color> tetrominoColors; // Farben der Tetrominos

    // Konstruktor, um das SpielfeldPanel zu initialisieren
    public SpielfeldPanel(Spiel spiel) {
        this.spiel = spiel;
        this.tetrominoColors = new HashMap<>();
        generateTetrominoColors(); // Generiere zufällige Farben für die Tetrominos
    }


    // Methode zum Zeichnen des Spielfelds
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[][] grid = spiel.getGrid(); // Das Spielfeldgitter
        Tetromino currentTetromino = spiel.getCurrentTetromino(); // Das aktuelle Tetromino
        int blockSize = 30; // Größe eines Blocks
        int width = grid[0].length * blockSize; // Breite des Spielfelds
        int height = grid.length * blockSize; // Höhe des Spielfelds

        // Zeichne das Hintergrundgitter
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                g.drawRect(j * blockSize, i * blockSize, blockSize, blockSize);
            }
        }

        // Zeichne den Rahmen um das Spielfeld
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width, height);



        // Zeichne die platzierten Tetrominos mit einem weißen Rand
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != 0) {
                    Color tetrominoColor = tetrominoColors.get(grid[i][j]);
                    g.setColor(Color.WHITE);
                    g.fillRect(j * blockSize - 1, i * blockSize - 1, blockSize + 2, blockSize + 2);
                    g.setColor(tetrominoColor);
                    g.fillRect(j * blockSize, i * blockSize, blockSize, blockSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * blockSize, i * blockSize, blockSize, blockSize);
                }
            }
        }

        int[][] shape = currentTetromino.getShape();
        int x = currentTetromino.getX();
        int y = currentTetromino.getY();

        // Zeichne das aktuelle Tetronimo mit einem weißen Rand
        Color currentTetrominoColor = tetrominoColors.get(currentTetromino.getType());
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    g.setColor(Color.WHITE);
                    g.fillRect((x + j) * blockSize - 1, (y + i) * blockSize - 1, blockSize + 2, blockSize + 2);
                    g.setColor(currentTetrominoColor);
                    g.fillRect((x + j) * blockSize, (y + i) * blockSize, blockSize, blockSize);
                    g.setColor(Color.BLACK);
                    g.drawRect((x + j) * blockSize, (y + i) * blockSize, blockSize, blockSize);
                }
            }
        }
    }

    // Methode um verschiedene Farben für die versch. Tetrominotypen 
    private void generateTetrominoColors() {
        int[] tetrominoTypes = {1, 2, 3, 4, 5, 6, 7}; // Tetromino types: I, J, L, O, S, T, Z
        for (int type : tetrominoTypes) {
            tetrominoColors.put(type, getRandomColor());
        }
    }

    // Generiere zufällige Farben
    private Color getRandomColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return new Color(r, g, b);
    }

    private Color getColorForValue(int value) {
        Map<Integer, Color> colorMap = new HashMap<>();
        colorMap.put(1, Color.RED);
        colorMap.put(2, Color.GREEN);
        colorMap.put(3, Color.BLUE);
        colorMap.put(4, Color.YELLOW);
        colorMap.put(5, Color.ORANGE);
        colorMap.put(6, Color.CYAN);
        colorMap.put(7, Color.MAGENTA);
        return colorMap.getOrDefault(value, Color.WHITE);
    }



    }
