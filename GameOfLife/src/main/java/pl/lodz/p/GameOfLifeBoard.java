package pl.lodz.p;

import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa odpowiedzialna za tworzenie planszy, oraz ustalanie
 * wartości komórek tej planszy w kolejnych posunięciach.
 */
public class GameOfLifeBoard {
    private GameOfLifeCell[][] board;
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

        board = new GameOfLifeCell[height][width];
        Random r = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = new GameOfLifeCell(r.nextBoolean());
            }
        }

        initializeCellsNeigbours(board);
    }

    /**
     * Funkcja inicjalizująca sąsiadów poszczególnych komórek planszy.
     * @param board plansza GameOfLifeBoard, tablica dwuwymiarowa
     */
    public void initializeCellsNeigbours(GameOfLifeCell[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                ArrayList<GameOfLifeCell> tempNeighbours = new ArrayList<>();
                for (int row = -1; row <= 1; row++) {
                    for (int col = -1; col <= 1; col++) {
                        if (row == 0 && col == 0) {
                            continue;
                        }
                        int rowPos = (i + row + board.length) % board.length;
                        int colPos = (j + col + board[0].length) % board[0].length;

                        tempNeighbours.add(board[rowPos][colPos]);
                    }
                }
                board[i][j].setNeigboursList(tempNeighbours);
            }
        }
    }

    /**
     * Setter dla planszy, ustawia planszę na podaną.
     *
     * @param board tablica dwuwymiarowa boolean
     */
    void setBoard(GameOfLifeCell[][] board) {
        this.board = new GameOfLifeCell[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, board[0].length);
        }

        initializeCellsNeigbours(this.board);
    }

    /**
     * Getter, zwraca kopię planszy ale z referencjami obiektów, nie kopiami.
     *
     * @return tablica dwuwymiarowa boolean
     */
    GameOfLifeCell[][] getBoard() {
        GameOfLifeCell[][] boardCopy = new GameOfLifeCell[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boardCopy[i][j] = board[i][j];

            }
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
        board[x][y].updateState(value);

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
        return board[x][y].getCellValue();
    }

    /**
     * Funkcja zwracająca obiekt GameOfLifeRow, określony wiersz planszy.
     * @param x wiersz planszy
     * @return obiekt GameOfLifeRow, wiersz planszy
     */
    GameOfLifeRow getRow(int x) {
        if (x < 0 || x >= board.length) {
            throw new ArrayIndexOutOfBoundsException("Nieprawidłowy wiersz, min: 0, "
                    + "max: " + (getBoard().length - 1));
        }

        GameOfLifeCell[] tempRow = new GameOfLifeCell[board[0].length];
        for (int i = 0; i < board[0].length; i++) {
            tempRow[i] = board[x][i];
        }

        return new GameOfLifeRow(tempRow);
    }

    /**
     * Funkcja zwracająca obiekt GameOfLifeRow, określoną kolumnę planszy.
     * @param y kolumna planszy
     * @return obiekt GameOfLifeRow, kolumna planszy
     */
    GameOfLifeColumn getColumn(int y) {
        if (y < 0 || y >= board[0].length) {
            throw new ArrayIndexOutOfBoundsException("Nieprawidłowa kolumna, min: 0, "
                    + "max: " + (getBoard()[0].length - 1));
        }

        GameOfLifeCell[] tempCol = new GameOfLifeCell[board.length];
        for (int i = 0; i < board.length; i++) {
            tempCol[i] = board[i][y];
        }

        return new GameOfLifeColumn(tempCol);
    }


    /**
     * Funkcja wywołująca doStep() z interfejsu.
     */
    public void doSimulationStep() {
        simulationType.doStep(this);
    }
}

