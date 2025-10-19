package pl.lodz.p;

import java.util.Random;

/**
 * Klasa odpowiedzialna za tworzenie planszy, oraz ustalanie
 * wartości komórek tej planszy w kolejnych posunięciach.
 */
public class GameOfLifeBoard {
    private boolean[][] board;

    /**
     * Konstruktor tworzący plansze z losowymi wartościami
     * true/false dla komórek.
     *
     * @param height ilość wierszy planszy
     * @param width ilość kolumn planszy
     */
    public GameOfLifeBoard(int height, int width) {
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
     * Getter dla poszczególnej komórki planszy.
     *
     * @param i index wiersza planszy
     * @param j index kolmuny planszy
     * @return wartość boolean komórki planszy
     * @throws ArrayIndexOutOfBoundsException gdy wartość wykracza poza zakres planszy
     */
    public boolean getCell(int i, int j) {
        if (i < 0 || i >= board.length) {
            throw new ArrayIndexOutOfBoundsException("Nieprawidłowy wiersz, min: 0, "
                    + "max: " + (getBoard().length - 1));
        }
        if (j < 0 || j >= board[0].length) {
            throw new ArrayIndexOutOfBoundsException("Nieprawidłowa kolumna, min: 0, "
                    + "max: " + (getBoard()[0].length - 1));
        }
        return board[i][j];
    }

    /**
     * Funkcja ustalająca wartość poszczególnych komórek planszy
     * na podstawie wartości danej komórki oraz wartości komórek sąsiadów.
     * Komórka z wartością false, mająca dokładnie 3 sąsiadów z wartością true
     * zmienia swoją wartość na true.
     * Komórka z wartością true, mająca dokładnie 1 sąsiada lub mająca ich
     * ponad 3 zmiena swoją wartość na false.
     * W innych przypadkach komórki pozostają te same
     * Funkcja tworzy tymczasową tablicę do której przypisuje kolejne nowe wartości
     * komórek, następnie po sprawdzeniu wszystkich komórek z oryginalnej tablicy
     * podmienia tablicę board z pola prywatnego na nową tablicę.
     */
    public void doStep() {
        boolean[][] tempBoard = new boolean[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int aliveNeighbours = 0;
                boolean curCell = board[i][j];

                for (int row = -1; row <= 1; row++) {
                    for (int col = -1; col <= 1; col++) {
                        if (row == 0 && col == 0) {
                            continue;
                        }
                        int ipos = (i + row + board.length) % board.length;
                        int jpos = (j + col + board[0].length) % board[0].length;

                        if (board[ipos][jpos]) {
                            aliveNeighbours++;
                        }
                    }
                }

                if (curCell) {
                    if (aliveNeighbours == 1 || aliveNeighbours > 3) {
                        curCell = false;
                    }
                } else {
                    if (aliveNeighbours == 3) {
                        curCell = true;
                    }
                }

                tempBoard[i][j] = curCell;
            }
        }

        for (int i = 0; i < board.length; i++) {
            System.arraycopy(tempBoard[i],0, board[i],0, board[0].length);
        }
    }
}

