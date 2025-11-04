/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



class GameOfLifeCellTest {
    @Test
    void setNullNeigboursListShouldThrowException() {
        GameOfLifeCell cell = new GameOfLifeCell(true);
        assertThrows(NullPointerException.class, () -> {
          cell.setNeighboursList(null);
        });
    }

    @Test
    void getCellsNeighboursTest() {
        GameOfLifeCell[] cellNeigbours = new GameOfLifeCell[8];

        cellNeigbours[0] = new GameOfLifeCell(false);
        cellNeigbours[1] = new GameOfLifeCell(true);
        cellNeigbours[2] = new GameOfLifeCell(true);

        GameOfLifeCell cell = new GameOfLifeCell(true);

        cell.setNeighboursList(cellNeigbours);
        assertFalse(cell.getNeighbourCells()[0].getCellValue());
        assertTrue(cell.getNeighbourCells()[1].getCellValue());
        assertTrue(cell.getNeighbourCells()[2].getCellValue());
    }

}