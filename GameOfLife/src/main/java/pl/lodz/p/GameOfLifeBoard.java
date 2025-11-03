/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Klasa odpowiedzialna za tworzenie planszy, oraz ustalanie
 * wartości komórek tej planszy w kolejnych posunięciach.
 */
public class GameOfLifeBoard {

    private List<List<GameOfLifeCell>> board;
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

        board = Arrays.asList(new List[height]);
        Random r = new Random();
        for (int i = 0; i < height; i++) {
            List<GameOfLifeCell> tempRow = Arrays.asList(new GameOfLifeCell[width]);

            for (int j = 0; j < width; j++) {
                tempRow.set(j, new GameOfLifeCell(r.nextBoolean()));
            }

            board.set(i, tempRow);
        }

        initializeCellsNeigbours(board);
    }

    /**
     * Funkcja inicjalizująca sąsiadów poszczególnych komórek planszy.
     * @param board plansza GameOfLifeBoard, tablica dwuwymiarowa
     */
    public void initializeCellsNeigbours(List<List<GameOfLifeCell>> board) {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.getFirst().size(); j++) {
                ArrayList<GameOfLifeCell> tempNeighbours = new ArrayList<>();
                for (int row = -1; row <= 1; row++) {
                    for (int col = -1; col <= 1; col++) {
                        if (row == 0 && col == 0) {
                            continue;
                        }
                        int rowPos = (i + row + board.size()) % board.size();
                        int colPos = (j + col + board.getFirst().size()) % board.getFirst().size();

                        tempNeighbours.add(board.get(rowPos).get(colPos));
                    }
                }
                board.get(i).get(j).setNeigboursList(tempNeighbours);
            }
        }
    }

    /**
     * Setter dla planszy, ustawia planszę na podaną.
     *
     * @param board tablica dwuwymiarowa boolean
     */
    void setBoard(List<List<GameOfLifeCell>> board) {
        this.board = Arrays.asList(new List[board.size()]);
        for (int i = 0; i < board.size(); i++) {

            List<GameOfLifeCell> tempRow = Arrays.asList(new GameOfLifeCell[board.getFirst().size()]);
            for (int j = 0; j < board.getFirst().size(); j++) {
                tempRow.set(j, new GameOfLifeCell(board.get(i).get(j).getCellValue()));
            }

            this.board.set(i, tempRow);
        }

        initializeCellsNeigbours(this.board);
    }

    /**
     * Getter, zwraca kopię planszy ale z referencjami obiektów, nie kopiami.
     *
     * @return tablica dwuwymiarowa boolean
     */
    List<List<GameOfLifeCell>> getBoard() {
        List<List<GameOfLifeCell>> boardCopy = Arrays.asList(new List[board.size()]);
        for (int i = 0; i < board.size(); i++) {

            List<GameOfLifeCell> tempRow = Arrays.asList(new GameOfLifeCell[board.getFirst().size()]);
            for (int j = 0; j < board.getFirst().size(); j++) {
                tempRow.set(j, board.get(i).get(j));
            }

            boardCopy.set(i, tempRow);
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
        if (x < 0 || x >= board.size()) {
            throw new ArrayIndexOutOfBoundsException("Nieprawidłowy wiersz, min: 0, "
                    + "max: " + (getBoard().size() - 1));
        }
        if (y < 0 || y >= board.getFirst().size()) {
            throw new ArrayIndexOutOfBoundsException("Nieprawidłowa kolumna, min: 0, "
                    + "max: " + (getBoard().getFirst().size() - 1));
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
        board.get(x).get(y).updateState(value);

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
        return board.get(x).get(y).getCellValue();
    }

    /**
     * Funkcja zwracająca obiekt GameOfLifeRow, określony wiersz planszy.
     * @param x wiersz planszy
     * @return obiekt GameOfLifeRow, wiersz planszy
     */
    GameOfLifeRow getRow(int x) {
        if (x < 0 || x >= board.size()) {
            throw new ArrayIndexOutOfBoundsException("Nieprawidłowy wiersz, min: 0, "
                    + "max: " + (getBoard().size() - 1));
        }

        List<GameOfLifeCell> tempRow = Arrays.asList(new GameOfLifeCell[board.getFirst().size()]);
        for (int i = 0; i < board.getFirst().size(); i++) {
            tempRow.set(i, board.get(x).get(i));
        }

        return new GameOfLifeRow(tempRow);
    }

    /**
     * Funkcja zwracająca obiekt GameOfLifeRow, określoną kolumnę planszy.
     * @param y kolumna planszy
     * @return obiekt GameOfLifeRow, kolumna planszy
     */
    GameOfLifeColumn getColumn(int y) {
        if (y < 0 || y >= board.getFirst().size()) {
            throw new ArrayIndexOutOfBoundsException("Nieprawidłowa kolumna, min: 0, "
                    + "max: " + (getBoard().getFirst().size() - 1));
        }

        List<GameOfLifeCell> tempCol = Arrays.asList(new GameOfLifeCell[board.size()]);
        for (int i = 0; i < board.size(); i++) {
            tempCol.set(i, board.get(i).get(y));
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

