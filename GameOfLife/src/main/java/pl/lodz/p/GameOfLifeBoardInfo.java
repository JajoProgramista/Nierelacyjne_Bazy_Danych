package pl.lodz.p;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class GameOfLifeBoardInfo implements PropertyChangeListener {
    private final GameOfLifeCell[] line;
    private int aliveCells;
    private int deadCells;

    /**
     * Konstruktor inicjalizujący określoną linie.
     * @param line wiersz lub kolumna planszy, tablica komórek GameOfLifeCell.
     * @throws NullPointerException kiedy podana linia jest nullem.
     */
    public GameOfLifeBoardInfo(GameOfLifeCell[] line) {
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
        for (int i = 0; i < line.length; i++) {
            if (line[i] == null) {
                continue;
            }
            if (line[i].getCellValue() == cellValue) {
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
}
