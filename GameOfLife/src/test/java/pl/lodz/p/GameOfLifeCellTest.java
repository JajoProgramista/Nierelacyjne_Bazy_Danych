/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

    @Test
    void toStringTest() {
        GameOfLifeCell[] neigh = new GameOfLifeCell[3];
        neigh[0] = new GameOfLifeCell(false);
        neigh[1] = new GameOfLifeCell(true);
        neigh[2] = new GameOfLifeCell(true);
        GameOfLifeCell cell = new GameOfLifeCell(true);
        cell.setNeighboursList(neigh);

        /*IO.println(cell.toString());*/
        String result = "GameOfLifeCell[Wartość komórki: =true,Sąsiedzi komórki: =false true true ]";
        assertEquals(result, cell.toString());
    }

    @Test
    void validEqualsTest() {
        GameOfLifeCell[] neigh1 = new GameOfLifeCell[4];
        neigh1[0] = new GameOfLifeCell(false);
        neigh1[1] = new GameOfLifeCell(true);
        neigh1[2] = new GameOfLifeCell(true);
        neigh1[3] = null;

        GameOfLifeCell[] neigh2 = new GameOfLifeCell[4];
        neigh2[0] = new GameOfLifeCell(false);
        neigh2[1] = new GameOfLifeCell(true);
        neigh2[2] = new GameOfLifeCell(true);
        neigh2[3] = null;

        GameOfLifeCell cell1 = new GameOfLifeCell(true);
        GameOfLifeCell cell2 = new GameOfLifeCell(true);
        cell1.setNeighboursList(neigh1);
        cell2.setNeighboursList(neigh2);

        assertTrue(cell1.equals(cell2));

        GameOfLifeCell cell3 = cell1;
        assertTrue(cell1.equals(cell3));
    }

    @Test
    void invalidEqualsTest() {
        GameOfLifeCell[] neigh1 = new GameOfLifeCell[3];
        neigh1[0] = new GameOfLifeCell(false);
        neigh1[1] = new GameOfLifeCell(true);
        neigh1[2] = new GameOfLifeCell(true);

        // inni sąsiedzi
        GameOfLifeCell[] neigh2 = new GameOfLifeCell[3];
        neigh2[0] = new GameOfLifeCell(true);
        neigh2[1] = new GameOfLifeCell(true);
        neigh2[2] = new GameOfLifeCell(true);

        GameOfLifeCell cell1 = new GameOfLifeCell(true);
        GameOfLifeCell cell2 = new GameOfLifeCell(true);
        cell1.setNeighboursList(neigh1);
        cell2.setNeighboursList(neigh2);

        assertFalse(cell1.equals(cell2));

        assertFalse(cell1.equals(null));
        assertFalse(cell1.equals(new PlainGameOfLifeSimulator()));
    }

    @Test
    void hashCodeMethodTest() {
        GameOfLifeCell[] neigh1 = new GameOfLifeCell[3];
        neigh1[0] = new GameOfLifeCell(false);
        neigh1[1] = new GameOfLifeCell(true);
        neigh1[2] = new GameOfLifeCell(true);

        GameOfLifeCell[] neigh2 = new GameOfLifeCell[3];
        neigh2[0] = new GameOfLifeCell(false);
        neigh2[1] = new GameOfLifeCell(true);
        neigh2[2] = new GameOfLifeCell(true);

        GameOfLifeCell cell1 = new GameOfLifeCell(true);
        GameOfLifeCell cell2 = new GameOfLifeCell(true);
        cell1.setNeighboursList(neigh1);
        cell2.setNeighboursList(neigh2);

        assertEquals(cell1.hashCode(), cell2.hashCode());
        assertTrue(cell1.equals(cell2));

        GameOfLifeCell cell3 = new GameOfLifeCell(false);
        assertNotEquals(cell3.hashCode(), cell2.hashCode());
        assertFalse(cell3.equals(cell2));

    }
}