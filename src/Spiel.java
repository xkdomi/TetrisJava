public class Spiel {
    private int[][] grid; // Das Spielfeld
    private Tetromino currentTetromino; // Das aktuelle Tetromino
    private boolean isGameOver; // Status, ob das Spiel vorbei ist
    private int score; // Der Punktestand

    // Konstruktor zur Initialisierung des Spiels
    public Spiel() {
        grid = new int[20][10]; // Beispielhafte Größe des Spielfelds
        currentTetromino = Tetromino.zufälligesTetromino(); // Zufälliges Tetromino generieren
        isGameOver = false; // Spiel ist nicht vorbei
        score = 0; // Anfangspunktestand
    }


    // Methode, um das Tetromino nach links zu bewegen
    public void nachLinksBewegen() {
        if (canMove(currentTetromino, -1, 0)) {
            currentTetromino.bewegen(-1, 0);
        }
    }


    // Methode, um das Tetromino nach rechts zu bewegen
    public void nachRechtsBewegen() {
        if (canMove(currentTetromino, 1, 0)) {
            currentTetromino.bewegen(1, 0);
        }
    }

    // Methode, um das Tetromino zu drehen
    public void drehen() {
        int[][] gedrehteForm = currentTetromino.berechneGedrehteForm();
        if (canMove(currentTetromino, 0, 0, gedrehteForm)) {
            currentTetromino.setForm(gedrehteForm);
        }
    }

    // Methode, um das Tetromino fallen zu lassen
    public void fallen() {
        if (canMove(currentTetromino, 0, 1)) {
            currentTetromino.bewegen(0, 1);
        } else {
            placeTetromino();
            clearLines();
            currentTetromino = Tetromino.zufälligesTetromino();
            if (!canMove(currentTetromino, 0, 0)) {
                isGameOver = true;
            }
        }
    }

    // Methode, um zu prüfen, ob sich das Tetromino bewegen kann
    private boolean canMove(Tetromino tetromino, int dx, int dy) {
        return canMove(tetromino, dx, dy, tetromino.getShape());
    }

    // Methode, um zu prüfen, ob sich das Tetromino mit einer bestimmten Form bewegen kann
    private boolean canMove(Tetromino tetromino, int dx, int dy, int[][] form) {
        int newX = tetromino.getX() + dx;
        int newY = tetromino.getY() + dy;
        for (int i = 0; i < form.length; i++) {
            for (int j = 0; j < form[i].length; j++) {
                if (form[i][j] != 0) {
                    int x = newX + j;
                    int y = newY + i;
                    if (x < 0 || x >= grid[0].length || y < 0 || y >= grid.length || grid[y][x] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Methode, um das Tetromino auf dem Spielfeld zu platzieren
    private void placeTetromino() {
        int[][] form = currentTetromino.getShape();
        int x = currentTetromino.getX();
        int y = currentTetromino.getY();
        for (int i = 0; i < form.length; i++) {
            for (int j = 0; j < form[i].length; j++) {
                if (form[i][j] != 0) {
                    grid[y + i][x + j] = currentTetromino.getType();
                }
            }
        }
    }


    // Methode, um vollständige Linien zu löschen und den Punktestand zu erhöhen
    private void clearLines() {
        for (int i = 0; i < grid.length; i++) {
            boolean fullLine = true;
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 0) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                removeLine(i);
                score += 100; // Punkte für jede gelöschte Linie
            }
        }
    }


    // Methode, um eine vollständige Linie zu entfernen
    private void removeLine(int lineIndex) {
        for (int i = lineIndex; i > 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = grid[i - 1][j];
            }
        }
        for (int j = 0; j < grid[0].length; j++) {
            grid[0][j] = 0;
        }
    }


    // Getter für das Spielfeld
    public int[][] getGrid() {
        return grid;
    }

    // Getter für das aktuelle Tetromino
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    // Methode, um den Status des Spiels abzufragen
    public boolean istSpielVorbei() {
        return isGameOver;
    }


    // Getter für den Punktestand
    public int getScore() {
        return score;
    }
}