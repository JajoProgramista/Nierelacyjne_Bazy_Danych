package pl.lodz.p;

import java.util.Random;

/**
 * Klasa odpowiedzialna za tworzenie planszy, oraz ustalanie
 * wartości komórek tej planszy w kolejnych posunięciach.
 */
public class GameOfLifeBoard {
    private boolean[][] board;
    private final GameOfLifeSimulator simulationType;

    /**
     * Konstruktor tworzący plansze z losowymi wartościami
     * true/false dla komórek.
     *
     * @param height ilość wierszy planszy
     * @param width ilość kolumn planszy
     * @param simulationType typ symulacji jako klasa interfejsu GameOfLifeSimulator
     * @throws NullPointerException jeżeli typ symulacji będzie nullem
     * @throws IndexOutOfBoundsException jeżeli wiersz lub kolumna < 1
     */
    public GameOfLifeBoard(int height, int width, GameOfLifeSimulator simulationType) {
        if (height < 1) {
            throw new IndexOutOfBoundsException("Nieprawidłowa wysokość planszy, min: 1");
        }

        if (width < 1) {
            throw new IndexOutOfBoundsException("Nieprawidłowa szerokość planszy, min: 1");
        }

        if (simulationType == null) {
            throw new NullPointerException("Podaj poprawny rodzaj symulacji! Nie Null");
        }
        this.simulationType = simulationType;

        board = new boolean[height][width];
        Random r = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = r.nextBoolean();
            }
        }
    }

    /**
     * Setter dla planszy, ustawia planszę na podaną.
     *
     * @param board tablica dwuwymiarowa boolean
     */
    public void setBoard(boolean[][] board) {
        this.board = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, board[0].length);
        }
    }

    /**
     * Getter, zwraca kopię planszy.
     *
     * @return tablica dwuwymiarowa boolean
     */
    public boolean[][] getBoard() {
        boolean[][] boardCopy = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, board[0].length);
        }
        return boardCopy;
    }

    /**
     * Funkcja walidująca przekazywane indexy wiersza i kolumny.
     *
     * @param x index wiersza planszy
     * @param y index kolumny planszy
     */
    private void validateIndex(int x, int y) {
        if (x < 0 || x >= board.length) {
            throw new ArrayIndexOutOfBoundsException("Nieprawidłowy wiersz, min: 0, "
                    + "max: " + (getBoard().length - 1));
        }
        if (y < 0 || y >= board[0].length) {
            throw new ArrayIndexOutOfBoundsException("Nieprawidłowa kolumna, min: 0, "
                    + "max: " + (getBoard()[0].length - 1));
        }
    }

    /**
     * Setter ustawiający wartość komórki na planszy.
     *
     * @param x index wiersza planszy
     * @param y index kolumny planszy
     * @param value wartość ustawianej komórki
     */
    public void setCell(int x, int y, boolean value) {
        validateIndex(x,y);
        board[x][y] = value;

    }

    /**
     * Getter dla poszczególnej komórki planszy.
     *
     * @param x index wiersza planszy
     * @param y index kolmuny planszy
     * @return wartość boolean komórki planszy
     * @throws ArrayIndexOutOfBoundsException gdy wartość wykracza poza zakres planszy
     */
    public boolean getCell(int x, int y) {
        validateIndex(x,y);
        return board[x][y];
    }

    /**
     * Funkcja wywołująca doStep() z interfejsu.
     */
    public void doSimulationStep() {
        simulationType.doStep(this);
    }
}

