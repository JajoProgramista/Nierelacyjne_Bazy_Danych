/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public abstract class GameOfLifeBoardInfo implements PropertyChangeListener, Cloneable {
    private final List<GameOfLifeCell> line;
    private int aliveCells;
    private int deadCells;

    /**
     * Konstruktor inicjalizujący określoną linie.
     * @param line wiersz lub kolumna planszy, tablica komórek GameOfLifeCell.
     * @throws NullPointerException kiedy podana linia jest nullem.
     */
    public GameOfLifeBoardInfo(List<GameOfLifeCell> line) {
        if (line == null) {
            throw new NullPointerException("Podana tablica nie może być nullem");
        }

        this.line = line;
        aliveCells = countCells(true);
        deadCells = countCells(false);

        for (GameOfLifeCell cell : line) {
            if (cell == null) {
                continue;
            }
            cell.addPropertyChangeListener(this);
        }
    }

    /**
     * Funkcja zliczająca komórki, na podstawie ich wartości.
     * @param cellValue wartość boolean (true / false)
     * @return ilość komórek danego podanego typu
     */
    private int countCells(boolean cellValue) {
        int cellsNum = 0;
        for (int i = 0; i < line.size(); i++) {
            if (line.get(i) == null) {
                continue;
            }
            if (line.get(i).getCellValue() == cellValue) {
                cellsNum++;
            }
        }
        return cellsNum;
    }

    /**
     * Funkcja zwracająca ilość żywych komórek.
     * @return ilość żywych komórek (int)
     */
    public int countAliveCells() {
        return aliveCells;
    }

    /**
     * Funkcja zwracająca ilość martwych komórek.
     * @return ilość martwych komórek (int)
     */
    public int countDeadCells() {
        return deadCells;
    }

    /**
     * Funkcja listenera, po zarejestrowaniu komunikatu o zmianie wartości komórki
     * zależnie od danej wartości oldValue i newValue, aktualizuje pola aliveCells i deadCells.
     * @param evt A PropertyChangeEvent obiekt opisujący źródłowe zdarzenie
     *            i wartości które zostały zmienione
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("cellValue".equals(evt.getPropertyName())) {
            boolean oldValue = (boolean) evt.getOldValue();
            boolean newValue = (boolean) evt.getNewValue();

            if (oldValue && !newValue) {
                aliveCells--;
                deadCells++;
            } else if (!oldValue && newValue) {
                aliveCells++;
                deadCells--;
            }

        }
    }

    /**
     * Funkcja zwracająca opis obiektu w postaci stringa.
     * @return łańcuch znaków String
     */
    @Override
    public String toString() {
        StringBuilder lineStr = new StringBuilder();
        for (GameOfLifeCell cell : line) {
            lineStr.append(cell.getCellValue());
            lineStr.append(" ");
        }

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("Komórki: ", lineStr)
                .append("Ilość żywych: ", aliveCells)
                .append("Ilość martwych: ", deadCells)
                .toString();
    }

    /**
     * Funkcja porównująca ten obiekt z podanym na podstawie informacji o
     * wartościach poszczególnych komórek, liczbie żywych oraz martwych komórek.
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
        if (!(obj instanceof GameOfLifeBoardInfo)) {
            return false;
        }
        GameOfLifeBoardInfo game = (GameOfLifeBoardInfo) obj;

        boolean[] thisLineValues = new boolean[line.size()];
        boolean[] gameLineValues = new boolean[game.line.size()];

        for (int i = 0; i < thisLineValues.length; i++) {
            thisLineValues[i] = line.get(i).getCellValue();
            if (i < gameLineValues.length) {
                gameLineValues[i] = game.line.get(i).getCellValue();
            }
        }

        return new EqualsBuilder()
                .append(thisLineValues, gameLineValues)
                .append(aliveCells, game.aliveCells)
                .append(deadCells, game.deadCells)
                .isEquals();
    }

    /**
     * Metoda haszująca obiekt, na podstawie informacji o wartości
     * poszczególnych komórek liczbie żywych oraz martwych komórek.
     * @return wartość całkowita int hasha
     */
    @Override
    public int hashCode() {
        boolean[] lineValues = new boolean[line.size()];
        for (int i = 0; i < lineValues.length; i++) {
            lineValues[i] = line.get(i).getCellValue();
        }

        return new HashCodeBuilder(3, 7)
                .append(lineValues)
                .append(aliveCells)
                .append(deadCells)
                .toHashCode();
    }

    @Override
    public GameOfLifeBoardInfo clone() throws CloneNotSupportedException {
        try {
            GameOfLifeBoardInfo clone = (GameOfLifeBoardInfo) super.clone();
            for (int i = 0; i < this.line.size(); i++) {
                clone.line.set(i, this.line.get(i).clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new CloneNotSupportedException();
        }
    }
}
