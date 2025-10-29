package pl.lodz.p;

public class GameOfLifeColumn extends GameOfLifeBoardInfo {
    /**
     * Konstruktor inicjalizujący określoną kolumnę.
     * @param line kolumna planszy, tablica komórek GameOfLifeCell.
     */
    GameOfLifeColumn(GameOfLifeCell[] line) {
        super(line);
    }
}
