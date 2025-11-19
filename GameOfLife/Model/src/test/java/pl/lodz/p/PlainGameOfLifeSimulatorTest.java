/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PlainGameOfLifeSimulatorTest {

    @Test
    void deadCellWithExacly3AliveNeighboursShouldBeAliveInNextStep() {
        // 1 0 1
        // 0 0 1    [martwa komórka (1,1) ma dokładnie 3 żywych sąsiadów]
        // 0 0 0

        GameOfLifeCell[][] board = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}};

        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);


        assertFalse(game.getBoard()[1][1].getCellValue());
        game.doSimulationStep();
        assertTrue(game.getBoard()[1][1].getCellValue());
    }

    @Test
    void edgeTestOfDeadCellWithExacly3AliveNeighboursShouldBeAliveInNextStep() {
        //  0 0 1  [martwa komórka (0,0) ma dokładnie 3 żywych sąsiadów]
        //  0 0 0
        //  1 0 1

        GameOfLifeCell[][] board =
                {{new GameOfLifeCell(false),new GameOfLifeCell(false), new GameOfLifeCell(true)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(true), new GameOfLifeCell(false),
                                new GameOfLifeCell(true)}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertFalse(game.getBoard()[0][0].getCellValue());
        game.doSimulationStep();
        assertTrue(game.getBoard()[0][0].getCellValue());

    }

    @Test
    void deadCellWithMoreThan3NeighboursShouldBeDeadInNextStep() {
        // 1 0 1
        // 1 0 1    [martwa komórka (1,1) ma dokładnie 4 żywych sąsiadów]
        // 0 0 0

        GameOfLifeCell[][] board =
                {{new GameOfLifeCell(true),new GameOfLifeCell(false), new GameOfLifeCell(true)},
                        {new GameOfLifeCell(true), new GameOfLifeCell(false), new GameOfLifeCell(true)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false),
                                new GameOfLifeCell(false)}};

        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertFalse(game.getBoard()[1][1].getCellValue());
        game.doSimulationStep();
        assertFalse(game.getBoard()[1][1].getCellValue());
    }

    @Test
    void deadCellWithLessThan3NeighboursShouldBeDeadInNextStep() {
        // 1 0 1
        // 0 0 0    [martwa komórka (1,1) ma dokładnie 2 żywych sąsiadów]
        // 0 0 0

        GameOfLifeCell[][] board =
                {{new GameOfLifeCell(true),new GameOfLifeCell(false), new GameOfLifeCell(true)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false),
                                new GameOfLifeCell(false)}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertFalse(game.getBoard()[1][1].getCellValue());
        game.doSimulationStep();
        assertFalse(game.getBoard()[1][1].getCellValue());
    }

    @Test
    void edgeTestOfDeadCellWithNot3AliveNeighboursShouldBeDeadInNextStep() {
        // 0 0 1
        // 0 0 0    [martwa komórka (0,0) ma dokładnie 1 żywego sąsiada]
        // 0 0 0

        GameOfLifeCell[][] board =
                {{new GameOfLifeCell(false),new GameOfLifeCell(false), new GameOfLifeCell(true)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false),
                                new GameOfLifeCell(false)}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertFalse(game.getBoard()[0][0].getCellValue());
        game.doSimulationStep();
        assertFalse(game.getBoard()[0][0].getCellValue());
    }

    @Test
    void aliveCellWith2NeighboursShouldBeAliveInNextStep() {
        // 1 0 1
        // 0 0 0    [żywa komórka (0,0) ma dokładnie 2 żywych sąsiadów]
        // 1 0 0

        GameOfLifeCell[][] board =
                {{new GameOfLifeCell(true),new GameOfLifeCell(false), new GameOfLifeCell(true)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(true), new GameOfLifeCell(false),
                                new GameOfLifeCell(false)}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertTrue(game.getBoard()[0][0].getCellValue());
        game.doSimulationStep();
        assertTrue(game.getBoard()[0][0].getCellValue());
    }

    @Test
    void aliveCellWith3NeighboursShouldBeAliveInNextStep() {
        // 1 1 0
        // 1 1 0 [żywa komórka (0,0) ma dokładnie 3 sąsiadów]
        // 0 0 0

        GameOfLifeCell[][] board =
                {{new GameOfLifeCell(true),new GameOfLifeCell(true), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(true), new GameOfLifeCell(true), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false),
                                new GameOfLifeCell(false)}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertTrue(game.getBoard()[0][0].getCellValue());
        game.doSimulationStep();
        assertTrue(game.getBoard()[0][0].getCellValue());
    }

    @Test
    void edgeTestOfAliveCellWith3NeigboursShouldBeAliveInNextStep() {
        // 1 0 1
        // 0 0 0 [żywa komórka (0,0) ma dokładnie 3 sąsiadów]
        // 1 0 1

        GameOfLifeCell[][] board =
                {{new GameOfLifeCell(true),new GameOfLifeCell(false), new GameOfLifeCell(true)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(true), new GameOfLifeCell(false),
                                new GameOfLifeCell(true)}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertTrue(game.getBoard()[0][0].getCellValue());
        game.doSimulationStep();
        assertTrue(game.getBoard()[0][0].getCellValue());
    }

    @Test
    void aliveCellWith1NeigbourShouldBeDeadInNextStep() {
        // 1 1 0
        // 0 0 0 [żywa komórka (0,0) ma dokładnie 1 sąsiada]
        // 0 0 0

        GameOfLifeCell[][] board =
                {{new GameOfLifeCell(true),new GameOfLifeCell(true), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false),
                                new GameOfLifeCell(false)}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertTrue(game.getBoard()[0][0].getCellValue());
        game.doSimulationStep();
        assertFalse(game.getBoard()[0][0].getCellValue());
    }

    @Test
    void edgeTestOfAliveCellWith1NeigbourShouldBeDeadInNextStep() {
        // 1 0 0
        // 0 0 0 [żywa komórka (0,0) ma dokładnie 1 sąsiada]
        // 0 0 1

        GameOfLifeCell[][] board =
                {{new GameOfLifeCell(true),new GameOfLifeCell(false), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(false),
                                new GameOfLifeCell(true)}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertTrue(game.getBoard()[0][0].getCellValue());
        game.doSimulationStep();
        assertFalse(game.getBoard()[0][0].getCellValue());
    }

    @Test
    void aliveCellWithMoreThan3NeigboursShouldBeDeadInNextStep() {
        // 0 1 0
        // 1 1 1 [żywa komórka (1,1) ma dokładnie 4 sąsiadów]
        // 0 1 0

        GameOfLifeCell[][] board =
                {{new GameOfLifeCell(false),new GameOfLifeCell(true), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(true), new GameOfLifeCell(true), new GameOfLifeCell(true)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(true),
                                new GameOfLifeCell(false)}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertTrue(game.getBoard()[1][1].getCellValue());
        game.doSimulationStep();
        assertFalse(game.getBoard()[1][1].getCellValue());
    }

    @Test
    void edgeTestOfAliveCellWithMoreThan3NeigboursShouldBeDeadInNextStep() {
        // 1 0 0
        // 0 1 1 [żywa komórka (0,0) ma dokładnie 4 sąsiadów]
        // 0 1 1

        GameOfLifeCell[][] board =
                {{new GameOfLifeCell(true),new GameOfLifeCell(false), new GameOfLifeCell(false)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(true), new GameOfLifeCell(true)},
                        {new GameOfLifeCell(false), new GameOfLifeCell(true),
                                new GameOfLifeCell(true)}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertTrue(game.getBoard()[0][0].getCellValue());
        game.doSimulationStep();
        assertFalse(game.getBoard()[0][0].getCellValue());
    }
}
