/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



class GameOfLifeCellTest {
    @Test
    void setNullNeigboursListShouldThrowException() {
        GameOfLifeCell cell = new GameOfLifeCell(true, new Locale("pl"));
        assertThrows(NullPointerException.class, () -> {
          cell.setNeighboursList(null);
        });
    }

    @Test
    void getCellsNeighboursTest() {
        GameOfLifeCell[] cellNeigbours = new GameOfLifeCell[8];

        cellNeigbours[0] = new GameOfLifeCell(false, new Locale("pl"));
        cellNeigbours[1] = new GameOfLifeCell(true, new Locale("pl"));
        cellNeigbours[2] = new GameOfLifeCell(true, new Locale("pl"));

        GameOfLifeCell cell = new GameOfLifeCell(true, new Locale("pl"));
        cell.setNeighboursList(cellNeigbours);
        assertFalse(cell.getNeighbourCells()[0].getCellValue());
        assertTrue(cell.getNeighbourCells()[1].getCellValue());
        assertTrue(cell.getNeighbourCells()[2].getCellValue());
    }

    @Test
    void toStringTest() {
        GameOfLifeCell[] neigh = new GameOfLifeCell[3];
        neigh[0] = new GameOfLifeCell(false, new Locale("pl"));
        neigh[1] = new GameOfLifeCell(true, new Locale("pl"));
        neigh[2] = new GameOfLifeCell(true, new Locale("pl"));
        GameOfLifeCell cell = new GameOfLifeCell(true, new Locale("pl"));
        cell.setNeighboursList(neigh);

        /*IO.println(cell.toString());*/
        String result = "GameOfLifeCell[Wartość komórki: =true,Sąsiedzi komórki: =false true true ]";
        assertEquals(result, cell.toString());
    }

    @Test
    void validEqualsTest() {
        GameOfLifeCell[] neigh1 = new GameOfLifeCell[4];
        neigh1[0] = new GameOfLifeCell(false, new Locale("pl"));
        neigh1[1] = new GameOfLifeCell(true, new Locale("pl"));
        neigh1[2] = new GameOfLifeCell(true, new Locale("pl"));
        neigh1[3] = null;

        GameOfLifeCell[] neigh2 = new GameOfLifeCell[4];
        neigh2[0] = new GameOfLifeCell(false, new Locale("pl"));
        neigh2[1] = new GameOfLifeCell(true, new Locale("pl"));
        neigh2[2] = new GameOfLifeCell(true, new Locale("pl"));
        neigh2[3] = null;

        GameOfLifeCell cell1 = new GameOfLifeCell(true, new Locale("pl"));
        GameOfLifeCell cell2 = new GameOfLifeCell(true, new Locale("pl"));
        cell1.setNeighboursList(neigh1);
        cell2.setNeighboursList(neigh2);

        assertTrue(cell1.equals(cell2));

        GameOfLifeCell cell3 = cell1;
        assertTrue(cell1.equals(cell3));
    }

    @Test
    void invalidEqualsTest() {
        GameOfLifeCell[] neigh1 = new GameOfLifeCell[3];
        neigh1[0] = new GameOfLifeCell(false, new Locale("pl"));
        neigh1[1] = new GameOfLifeCell(true, new Locale("pl"));
        neigh1[2] = new GameOfLifeCell(true, new Locale("pl"));

        // inni sąsiedzi
        GameOfLifeCell[] neigh2 = new GameOfLifeCell[3];
        neigh2[0] = new GameOfLifeCell(true, new Locale("pl"));
        neigh2[1] = new GameOfLifeCell(true, new Locale("pl"));
        neigh2[2] = new GameOfLifeCell(true, new Locale("pl"));

        GameOfLifeCell cell1 = new GameOfLifeCell(true, new Locale("pl"));
        GameOfLifeCell cell2 = new GameOfLifeCell(true, new Locale("pl"));
        cell1.setNeighboursList(neigh1);
        cell2.setNeighboursList(neigh2);

        assertFalse(cell1.equals(cell2));

        assertFalse(cell1.equals(null));
        assertFalse(cell1.equals(new PlainGameOfLifeSimulator()));
    }

    @Test
    void hashCodeMethodTest() {
        GameOfLifeCell[] neigh1 = new GameOfLifeCell[3];
        neigh1[0] = new GameOfLifeCell(false, new Locale("pl"));
        neigh1[1] = new GameOfLifeCell(true, new Locale("pl"));
        neigh1[2] = new GameOfLifeCell(true, new Locale("pl"));

        GameOfLifeCell[] neigh2 = new GameOfLifeCell[3];
        neigh2[0] = new GameOfLifeCell(false, new Locale("pl"));
        neigh2[1] = new GameOfLifeCell(true, new Locale("pl"));
        neigh2[2] = new GameOfLifeCell(true, new Locale("pl"));

        GameOfLifeCell cell1 = new GameOfLifeCell(true, new Locale("pl"));
        GameOfLifeCell cell2 = new GameOfLifeCell(true, new Locale("pl"));
        cell1.setNeighboursList(neigh1);
        cell2.setNeighboursList(neigh2);

        assertEquals(cell1.hashCode(), cell2.hashCode());
        assertTrue(cell1.equals(cell2));

        GameOfLifeCell cell3 = new GameOfLifeCell(false, new Locale("pl"));
        assertNotEquals(cell3.hashCode(), cell2.hashCode());
        assertFalse(cell3.equals(cell2));

    }

    @Test
    void cloneTest() throws CloneNotSupportedException {
        GameOfLifeCell cell = new GameOfLifeCell(true, new Locale("pl"));
        GameOfLifeCell cellCopy = cell.clone();

        assertNotSame(cell, cellCopy);
    }

    @Test
    void compareTest() {
        GameOfLifeCell cell = new GameOfLifeCell(true, new Locale("pl"));
        GameOfLifeCell cell2 = new GameOfLifeCell(false, new Locale("pl"));
        GameOfLifeCell cell3 = new GameOfLifeCell(true, new Locale("pl"));

        assertThrows(NullPointerException.class, () -> {
            cell.compareTo(null);
        });

        assertEquals(0, cell.compareTo(cell3));
        assertEquals(1, cell.compareTo(cell2));
        assertEquals(-1, cell2.compareTo(cell));
    }
}