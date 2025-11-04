/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class GameOfLifeCell {
    private boolean value;
    private GameOfLifeCell[] neighbourCells;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Konstruktor GameOfLifeCell tworzący komórkę planszy.
     * @param value wartość komórki true / false
     */
    public GameOfLifeCell(boolean value) {
        this.value = value;
        neighbourCells = new GameOfLifeCell[8];
    }

    /**
     * Ustawia liste sąsiadów komórki na podaną w argumencie funkcji.
     * @param neighbours lista sąsiadów typu GameOfLifeCell
     */
    void setNeighboursList(GameOfLifeCell[] neighbours) {
        if (neighbours == null) {
            throw new NullPointerException("Komórka sąsiadująca nie może być nullem!");
        }

        neighbourCells = neighbours;
    }

    /**
     * Getter zwracający wartość boolean komórki.
     * @return wartość boolean (true/false)
     */
    public boolean getCellValue() {
        return value;
    }

    /**
     * Getter zwracający listę sąsiadów.
     * @return listę z obiektami GameOfLifeCell
     */
    public GameOfLifeCell[] getNeighbourCells() {
        GameOfLifeCell[] copyOfNeighCells = new GameOfLifeCell[8];

        for (int i = 0; i < 8; i++) {
            if (neighbourCells[i] == null) {
                continue;
            }

            boolean value = neighbourCells[i].getCellValue();
            copyOfNeighCells[i] = new GameOfLifeCell(value);
        }

        return copyOfNeighCells;
    }

    /**
     * Funkcja obliczająca następny stan komórki, na podstawie liczby sąsiadów
     * oraz stanu danej komórki.
     * @return wartość boolean komórki (true / false)
     *
     *
     * */
    public boolean nextState() {
        int aliveNeigbours = 0;
        for (GameOfLifeCell cell : neighbourCells) {
            if (cell.getCellValue()) {
                aliveNeigbours++;
            }
        }

        if (this.value) {
            if (aliveNeigbours == 1 || aliveNeigbours > 3) {
                return false;
            }
        } else {
            if (aliveNeigbours == 3) {
                return true;
            }
        }

        return this.value;
    }

    /**
     * Funkcja aktualizująca stan komórki na podstawie podanego argumentu.
     * @param newState wartość boolean komórki (true / false)
     */
    public void updateState(boolean newState) {
        boolean previousValue = this.value;
        this.value = newState;
        support.firePropertyChange("cellValue", previousValue, value);
    }

    /**
     * Funkcja dodająca listenera, nasłuchującego zmian komórki.
     * @param listener Obiekt klasy, która nasłuchuje i ma zaimplementowany
     *                 interfejs PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }

    /**
     * Funkcja usuwająca listenera.
     * @param listener Obiekt klasy, która nasłuchuje i ma zaimplementowany
     *                 interfejs PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.support.removePropertyChangeListener(listener);
    }

}
