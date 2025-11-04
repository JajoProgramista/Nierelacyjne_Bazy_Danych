/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameOfLifeBoardInfoTest {
    @Test
    void getRowTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3, 3, new PlainGameOfLifeSimulator());

        // 1 0 1
        // 0 0 1
        // 0 0 0
        GameOfLifeCell[][] board = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}};

        game.setBoard(board);

        // wiersz 0 -> 1 martwa, 2 żywe
        assertEquals(1, game.getRow(0).countDeadCells());
        assertEquals(2, game.getRow(0).countAliveCells());
    }


    @Test
    void getColTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(4, 3, new PlainGameOfLifeSimulator());

        // 1 0 1
        // 0 0 1
        // 0 0 0
        // 1 1 1

        GameOfLifeCell[][] board = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}, {new GameOfLifeCell(true), new GameOfLifeCell(true),
                new GameOfLifeCell(true)}};

        game.setBoard(board);

        // kolumna 2 -> 1 martwa, 3 żywe
        assertEquals(1, game.getColumn(2).countDeadCells());
        assertEquals(3, game.getColumn(2).countAliveCells());
    }


    @Test
    void nullRowGivenShouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
           GameOfLifeRow row = new GameOfLifeRow(null);
        });
    }

    @Test
    void nullColumnGivenShouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            GameOfLifeColumn col = new GameOfLifeColumn(null);
        });
    }


    @Test
    void rowIndexOutOfBoundTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3, 3, new PlainGameOfLifeSimulator());
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.getRow(3);
        });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.getRow(-1);
        });
    }

    @Test
    void columnIndexOutOfBoundTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3, 3, new PlainGameOfLifeSimulator());
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.getColumn(3);
        });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.getColumn(-1);
        });
    }

    @Test
    void listenerObserverTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(4, 3, new PlainGameOfLifeSimulator());
        // 1 0 1
        // 0 0 1
        // 0 0 0
        // 1 1 1
        GameOfLifeCell[][] board = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}, {new GameOfLifeCell(true), new GameOfLifeCell(true),
                new GameOfLifeCell(true)}};

        game.setBoard(board);
        var row = game.getRow(3);

        assertEquals(3, row.countAliveCells());
        assertEquals(0, row.countDeadCells());
        game.doSimulationStep();

        assertNotEquals(3,row.countAliveCells());
        assertNotEquals(0, row.countDeadCells());
    }

    @Test
    void propertyChangeTest() {
        List<GameOfLifeCell> cellsArray = Arrays.asList(new GameOfLifeCell[2]);
        GameOfLifeCell cell1 = new GameOfLifeCell(false);
        cellsArray.set(0, cell1);

        GameOfLifeRow row = new GameOfLifeRow(cellsArray);

        assertEquals(1, row.countDeadCells());
        PropertyChangeEvent evt = new PropertyChangeEvent(cell1, "cellValue", false, true);
        row.propertyChange(evt);

        assertEquals(0, row.countDeadCells());
    }

    @Test
    void propertyChangeSameValueTest() {
        GameOfLifeCell cell = new GameOfLifeCell(true);
        List<GameOfLifeCell> cells = Arrays.asList(cell);
        GameOfLifeRow row = new GameOfLifeRow(cells);

        PropertyChangeEvent evt = new PropertyChangeEvent(cell, "cellValue", true, true);
        row.propertyChange(evt);

        assertEquals(1, row.countAliveCells());

        evt = new PropertyChangeEvent(cell, "cellValue", false, false);
        row.propertyChange(evt);

        assertEquals(1, row.countAliveCells());
    }

    @Test
    void removeListenerTest() {
        GameOfLifeCell cell1 = new GameOfLifeCell(false);
        GameOfLifeCell cell2 = new GameOfLifeCell(true);
        List<GameOfLifeCell> cellsArray = Arrays.asList(cell1, cell2);

        GameOfLifeRow row = new GameOfLifeRow(cellsArray);

        assertEquals(1, row.countDeadCells());
        cell1.updateState(true);
        assertEquals(0, row.countDeadCells());

        cell1.removePropertyChangeListener(row);
        cell1.updateState(false);

        // Po usunięciu listenera, ilość martwych komórek nieaktulizuje się,
        // NIE jest równe 1.
        assertNotEquals(1, row.countDeadCells());
    }

    @Test
    public void propertyChangeListenerOtherMessageTest() {
        GameOfLifeCell cell1 = new GameOfLifeCell(false);
        List<GameOfLifeCell> cellsArray = Arrays.asList(new GameOfLifeCell[2]);

        cellsArray.set(0, cell1);

        GameOfLifeRow row = new GameOfLifeRow(cellsArray);
        assertEquals(1, row.countDeadCells());

        PropertyChangeEvent evt = new PropertyChangeEvent(cell1, "otherMessage", false,
                true);
        row.propertyChange(evt);

        assertEquals(1, row.countDeadCells());
    }

}