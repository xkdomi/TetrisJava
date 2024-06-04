import java.util.Random;

// Klasse, die ein Tetromino darstellt
public class Tetromino {

    // Definition der verschiedenen Tetromino-Formen
    public static final int[][] I = {{1, 1, 1, 1}};
    public static final int[][] J = {{1, 0, 0}, {1, 1, 1}};
    public static final int[][] L = {{0, 0, 1}, {1, 1, 1}};
    public static final int[][] O = {{1, 1}, {1, 1}};
    public static final int[][] S = {{0, 1, 1}, {1, 1, 0}};
    public static final int[][] T = {{0, 1, 0}, {1, 1, 1}};
    public static final int[][] Z = {{1, 1, 0}, {0, 1, 1}};

    private int[][] form; // Die aktuelle Form des Tetrominos
    private int x, y; // Die Position des Tetrominos
    private int type; // Der Typ des Tetrominos (1 = I, 2 = J, 3 = L, 4 = O, 5 = S, 6 = T, 7 = Z)
    private int orientation; // Die Orientierung des Tetrominos (0 = 0°, 1 = 90°, 2 = 180°, 3 = 270°)

    // Konstruktor, um ein Tetromino mit einer bestimmten Form und einem bestimmten Typ zu erstellen
    public Tetromino(int[][] form, int type) {
        this.form = form;
        this.x = 4; // Initiale X-Position
        this.y = 0; // Initiale Y-Position
        this.type = type;
        this.orientation = 0; // Initiale Orientierung
    }

    // Methode, um ein zufälliges Tetromino zu generieren
    public static Tetromino zufälligesTetromino() {
        Random zufall = new Random();
        int type = zufall.nextInt(7) + 1; // Zufälliger Typ von 1 bis 7
        int[][] form;
        switch (type) {
            case 1: form = I; break;
            case 2: form = J; break;
            case 3: form = L; break;
            case 4: form = O; break;
            case 5: form = S; break;
            case 6: form = T; break;
            case 7: form = Z; break;
            default: throw new IllegalArgumentException("Ungültiger Tetromino-Typ");
        }
        return new Tetromino(form, type);
    }

    // Methode, um das Tetromino zu bewegen
    public void bewegen(int dx, int dy) {
        this.x += dx; // Bewege um dx in X-Richtung
        this.y += dy; // Bewege um dy in Y-Richtung
    }

    // Methode, um das Tetromino zu drehen
    public int[][] berechneGedrehteForm() {
        int reihen = form.length;
        int spalten = form[0].length;
        int[][] gedrehteForm = new int[spalten][reihen];

        // Drehe die Form um 90 Grad im Uhrzeigersinn
        for (int i = 0; i < reihen; i++) {
            for (int j = 0; j < spalten; j++) {
                gedrehteForm[j][reihen - 1 - i] = form[i][j];
            }
        }

        return gedrehteForm;
    }




    // Methode, um die aktuelle Form des Tetrominos zurückzugeben
    public int[][] getShape() {
        return form;
    }

    // Methode, um die X-Position des Tetrominos zurückzugeben
    public int getX() {
        return x;
    }

    // Methode, um die Y-Position des Tetrominos zurückzugeben
    public int getY() {
        return y;
    }

    // Methode, um den Typ des Tetrominos zurückzugeben
    public int getType() {
        return type;
    }

    // Methode, um die Position des Tetrominos zu setzen
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setForm(int[][] form) {
        this.form = form;
    }
}