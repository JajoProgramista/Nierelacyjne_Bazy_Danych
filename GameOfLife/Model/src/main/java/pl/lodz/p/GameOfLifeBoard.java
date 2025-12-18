/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import exceptions.InvalidBoardSizeException;
import exceptions.NullException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

/**
 * Klasa odpowiedzialna za tworzenie planszy, oraz ustalanie
 * wartości komórek tej planszy w kolejnych posunięciach.
 */
public class GameOfLifeBoard implements Cloneable {

    private GameOfLifeCell[][] board;
    private final GameOfLifeSimulator simulationType;
    private Locale locale;
    private ResourceBundle bundle;

    /**
     * Konstruktor tworzący plansze z losowymi wartościami
     * true/false dla komórek.
     *
     * @param height ilość wierszy planszy
     * @param width ilość kolumn planszy
     * @param d gęstość komórek
     * @param simulationType typ symulacji jako klasa interfejsu GameOfLifeSimulator
     * @throws NullPointerException jeżeli typ symulacji będzie nullem
     * @throws IndexOutOfBoundsException jeżeli wiersz lub kolumna < 1
     */
    public GameOfLifeBoard(int height, int width, DensityLevel d, GameOfLifeSimulator simulationType,
                           Locale locale) {

        this.locale = locale;
        bundle = ResourceBundle.getBundle("exceptions", locale);

        if (height < 1) {
            throw new InvalidBoardSizeException(bundle.getString("outOfBoundsHeight"));
        }

        if (width < 1) {
            throw new InvalidBoardSizeException(bundle.getString("outOfBoundsWidth"));
        }

        if (simulationType == null) {
            throw new NullException(bundle.getString("nullPointer"));
        }

        int howManyCells;
        if (d.equals(DensityLevel.LOW)) {
            howManyCells = (int) Math.ceil(0.1 * height * width);
        } else if (d.equals(DensityLevel.MEDIUM)) {
            howManyCells = (int) Math.ceil(0.3 * height * width);
        } else {
            howManyCells = (int) Math.ceil(0.5 * height * width);
        }

        this.simulationType = simulationType;

        board = new GameOfLifeCell[height][width];
        Random r = new Random();

        int k = 1;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (k <= howManyCells) {
                    board[i][j] = new GameOfLifeCell(true, locale);
                    k++;
                } else {
                    board[i][j] = new GameOfLifeCell(false, locale);
                }
            }
        }

        List<GameOfLifeCell> tempList = new ArrayList<GameOfLifeCell>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tempList.add(board[i][j]);
            }
        }

        Collections.shuffle(tempList);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = tempList.get(i * width + j);
            }
        }
        initializeCellsNeighbours(this.board);
    }

    /**
     * Funkcja inicjalizująca sąsiadów poszczególnych komórek planszy.
     */
    void initializeCellsNeighbours(GameOfLifeCell[][] board) {
        int count;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                GameOfLifeCell[] tempNeighbours = new GameOfLifeCell[8];
                count = 0;
                for (int row = -1; row <= 1; row++) {
                    for (int col = -1; col <= 1; col++) {
                        if (row == 0 && col == 0) {
                            continue;
                        }
                        int rowPos = (i + row + board.length) % board.length;
                        int colPos = (j + col + board[0].length) % board[0].length;

                        tempNeighbours[count++] = board[rowPos][colPos];
                    }
                }
                board[i][j].setNeighboursList(tempNeighbours);
            }
        }
    }

    /**
     * Setter dla planszy, ustawia planszę na podaną.
     *
     * @param board tablica dwuwymiarowa boolean
     */
    public void setBoard(GameOfLifeCell[][] board) {
        this.board = new GameOfLifeCell[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, board[0].length);
        }

        initializeCellsNeighbours(this.board);
    }

    /**
     * Getter, zwraca kopię planszy ale z referencjami obiektów, nie kopiami.
     *
     * @return tablica dwuwymiarowa boolean
     */
    public GameOfLifeCell[][] getBoard() {
        GameOfLifeCell[][] boardCopy = new GameOfLifeCell[board.length][board[0].length];
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
     * @throws InvalidBoardSizeException gdy wartość wykracza poza zakres planszy
     */
    private void validateIndex(int x, int y) {
        if (x < 0 || x >= board.length) {
            throw new InvalidBoardSizeException(bundle.getString("invalidRow"));
        }
        if (y < 0 || y >= board[0].length) {
            throw new InvalidBoardSizeException(bundle.getString("invalidCol"));
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
     * @throws InvalidBoardSizeException gdy wartość wykracza poza zakres planszy
     */
    public boolean getCell(int x, int y) {
        validateIndex(x,y);
        return board[x][y].getCellValue();
    }

    /**
     * Funkcja zwracająca obiekt GameOfLifeRow, określony wiersz planszy.
     * @param x wiersz planszy
     * @return obiekt GameOfLifeRow, wiersz planszy
     * @throws ArrayIndexOutOfBoundsException kiedy podany index wiersza jest nieprawidłowy
     */
    public GameOfLifeRow getRow(int x) {
        if (x < 0 || x >= board.length) {
            throw new InvalidBoardSizeException(bundle.getString("invalidRow"));
        }

        List<GameOfLifeCell> tempRow = Arrays.asList(new GameOfLifeCell[board[0].length]);
        for (int i = 0; i < board[0].length; i++) {
            tempRow.set(i, board[x][i]);
        }

        return new GameOfLifeRow(tempRow, locale);
    }

    /**
     * Funkcja zwracająca obiekt GameOfLifeRow, określoną kolumnę planszy.
     * @param y kolumna planszy
     * @return obiekt GameOfLifeRow, kolumna planszy
     * @throws ArrayIndexOutOfBoundsException kiedy podany index kolumny jest nieprawidłowy
     */
    public GameOfLifeColumn getColumn(int y) {
        if (y < 0 || y >= board[0].length) {
            throw new InvalidBoardSizeException(bundle.getString("invalidCol"));
        }

        List<GameOfLifeCell> tempCol = Arrays.asList(new GameOfLifeCell[board.length]);
        for (int i = 0; i < board.length; i++) {
            tempCol.set(i, board[i][y]);
        }

        return new GameOfLifeColumn(tempCol, locale);
    }

    /**
     * Funkcja wywołująca doStep() z interfejsu.
     */
    public void doSimulationStep() {
        simulationType.doStep(this);
    }

    /**
     * Funkcja zwracająca opis obiektu w postaci stringa.
     * @return łańcuch znaków string
     */
    @Override
    public String toString() {
        StringBuilder boardValues = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boardValues.append(board[i][j].getCellValue());
                boardValues.append(" ");
            }
            boardValues.append('\n');
        }

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("\n", boardValues)
                .append("\n", simulationType.getClass().getSimpleName())
                .toString();
    }

    /**
     * Funkcja porównująca ten obiekt z podanym na podstawie informacji o
     * wartościach poszczególnych komórek, oraz o podanej klasie symulacji.
     * @param obj referencja do obiektu, który będzie porównywany.
     * @return wartość boolean prawda / fałsz
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GameOfLifeBoard)) {
            return false;
        }

        GameOfLifeBoard game = (GameOfLifeBoard) obj;
        boolean[][] thisCellValues = new boolean[board.length][board[0].length];
        boolean[][] gameCellValues = new boolean[game.board.length][game.board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                thisCellValues[i][j] = board[i][j].getCellValue();

                if (i < gameCellValues.length && j < gameCellValues[0].length) {
                    gameCellValues[i][j] = game.board[i][j].getCellValue();
                }
            }
        }

        return new EqualsBuilder()
                .append(thisCellValues, gameCellValues)
                .append(simulationType.getClass(), game.simulationType.getClass())
                .isEquals();
    }

    /**
     * Metoda haszująca obiekt, na podstawie informacji o wartości
     * poszczególnych komórek planszy.
     * @return wartość całkowita int hasha
     */
    @Override
    public int hashCode() {
        boolean[][] boardValues = new boolean[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boardValues[i][j] = board[i][j].getCellValue();
            }
        }
        return new HashCodeBuilder(3, 7)
                .append(boardValues)
                .append(simulationType.getClass())
                .hashCode();
    }

    @Override
    public GameOfLifeBoard clone() throws CloneNotSupportedException {
        try {
            GameOfLifeBoard clone = (GameOfLifeBoard) super.clone();
            clone.board = new GameOfLifeCell[this.board.length][this.board[0].length];

            for (int i = 0; i < this.board.length; i++) {
                for (int j = 0; j < this.board[0].length; j++) {
                    clone.board[i][j] = this.board[i][j].clone();
                }
            }

            clone.initializeCellsNeighbours(clone.board);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new CloneNotSupportedException();
        }
    }


    public void reverseCellValue(int x, int y) {
        board[x][y].updateState(!board[x][y].getCellValue());
    }
}

