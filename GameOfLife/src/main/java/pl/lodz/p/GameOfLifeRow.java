package pl.lodz.p;

public class GameOfLifeRow extends GameOfLifeBoardInfo {
    /**
     * Konstruktor inicjalizujący określony wiersz.
     * @param line wiersz planszy, tablica komórek GameOfLifeCell.
     */
    public GameOfLifeRow(GameOfLifeCell[] line) {
        super(line);
    }
}
