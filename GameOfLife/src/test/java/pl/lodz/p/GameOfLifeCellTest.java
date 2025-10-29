package pl.lodz.p;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameOfLifeCellTest {
    @Test
    void setNullNeigboursListShouldThrowException() {
        GameOfLifeCell cell = new GameOfLifeCell(true);
        assertThrows(NullPointerException.class, () -> {
          cell.setNeigboursList(null);
        });
    }

    @Test
    void getCellsNeighboursTest() {
        ArrayList<GameOfLifeCell> cellNeigbours = new ArrayList<>();

        cellNeigbours.add(new GameOfLifeCell(false));
        cellNeigbours.add(new GameOfLifeCell(true));
        cellNeigbours.add(new GameOfLifeCell(true));

        GameOfLifeCell cell = new GameOfLifeCell(true);
        assertEquals(0,cell.getNeighbourCells().size());
        cell.setNeigboursList(cellNeigbours);
        assertEquals(3,cell.getNeighbourCells().size());

    }

}