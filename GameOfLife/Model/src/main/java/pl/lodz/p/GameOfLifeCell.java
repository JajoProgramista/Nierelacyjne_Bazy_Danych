/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import exceptions.NoneNeigboursException;
import exceptions.NullException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class GameOfLifeCell implements Cloneable, Comparable<GameOfLifeCell> {
    private boolean value;
    private GameOfLifeCell[] neighbourCells;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final ResourceBundle bundle;
    private static final Logger logger = Logger.getLogger(GameOfLifeCell.class.getName());
    private final ArrayList<Boolean> previousStates = new ArrayList<>();

    /**
     * Konstruktor GameOfLifeCell tworzący komórkę planszy.
     * @param value wartość komórki true / false
     * @param locale lokalizacja (pl / en)
     */
    public GameOfLifeCell(boolean value, Locale locale) {
        bundle = ResourceBundle.getBundle("exceptions", locale);
        this.value = value;
        neighbourCells = new GameOfLifeCell[8];
    }

    /**
     * Ustawia liste sąsiadów komórki na podaną w argumencie funkcji.
     * @param neighbours lista sąsiadów typu GameOfLifeCell
     * @throws NoneNeigboursException jeżeli sąsiedzi nie istnieją.
     */
    void setNeighboursList(GameOfLifeCell[] neighbours) {
        if (neighbours == null) {
            logger.severe(bundle.getString("noneNeigbours"));
            throw new NoneNeigboursException(bundle.getString("noneNeigbours"));
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

            copyOfNeighCells[i] = neighbourCells[i];
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
            if (aliveNeigbours <= 1 || aliveNeigbours > 3) {
                return false;
            }
        } else {
            if (aliveNeigbours == 3) {
                return true;
            }
        }

        return this.value;
    }

    public void setPreviousState() {
        if (previousStates.isEmpty()) {
            return;
        }
        value = previousStates.removeLast();
    }

    /**
     * Funkcja aktualizująca stan komórki na podstawie podanego argumentu.
     * @param newState wartość boolean komórki (true / false)
     */
    public void updateState(boolean newState) {
        boolean previousValue = this.value;
        this.value = newState;
        support.firePropertyChange("cellValue", previousValue, value);
        previousStates.add(previousValue);
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

    /**
     * Funkcja zwracająca opis obiektu w postaci stringa.
     * @return łańcuch znaków String
     */
    @Override
    public String toString() {
        StringBuilder neighStr = new StringBuilder();
        for (GameOfLifeCell cell : neighbourCells) {
            neighStr.append(cell.getCellValue());
            neighStr.append(" ");
        }

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("Wartość komórki: ", value)
                .append("Sąsiedzi komórki: ", neighStr)
                .toString();
    }

    /**
     * Funkcja porównująca ten obiekt z podanym na podstawie informacji o
     * wartościach poszczególnych komórek sąsiadów, oraz wartości tej komórki.
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
        if (!(obj instanceof GameOfLifeCell)) {
            return false;
        }

        GameOfLifeCell cell = (GameOfLifeCell) obj;
        boolean[] thisNeighValues = new boolean[neighbourCells.length];
        boolean[] cellNeighValues = new boolean[cell.neighbourCells.length];

        ArrayList<GameOfLifeCell> thisNullValues = new ArrayList<>();
        ArrayList<GameOfLifeCell> cellNullValues = new ArrayList<>();

        for (int i = 0; i < thisNeighValues.length; i++) {
            if (neighbourCells[i] == null) {
                thisNullValues.add(null);
            } else {
                thisNeighValues[i] = neighbourCells[i].getCellValue();
            }

            if (i < cellNeighValues.length) {
                if (cell.neighbourCells[i] == null) {
                    cellNullValues.add(null);
                } else {
                    cellNeighValues[i] = cell.neighbourCells[i].getCellValue();
                }
            }
        }

        return new EqualsBuilder()
                .append(value, cell.value)
                .append(thisNeighValues, cellNeighValues)
                .append(thisNullValues, cellNullValues)
                .isEquals();
    }

    /**
     * Metoda haszująca obiekt, na podstawie informacji o wartości
     * poszczególnych komórek sąsiadów oraz wartości tej komórki.
     * @return wartość całkowita int hasha
     */
    @Override
    public int hashCode() {
        boolean[] neighValues = new boolean[neighbourCells.length];

        for (int i = 0; i < neighValues.length; i++) {
            if (neighbourCells[i] != null) {
                neighValues[i] = neighbourCells[i].getCellValue();
            }
        }
        return new HashCodeBuilder(3, 7)
                .append(value)
                .append(neighValues)
                .hashCode();
    }

    @Override
    public GameOfLifeCell clone() throws CloneNotSupportedException {
        try {
            GameOfLifeCell clone = (GameOfLifeCell) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public int compareTo(GameOfLifeCell cell) {
        if (cell == null) {
            logger.severe(bundle.getString("nullPointer"));
            throw new NullException(bundle.getString("nullPointer"));
        }
        return Boolean.compare(this.value, cell.value);
    }
}
