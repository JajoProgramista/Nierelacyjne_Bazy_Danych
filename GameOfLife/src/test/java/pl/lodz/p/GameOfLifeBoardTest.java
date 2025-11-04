/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



class GameOfLifeBoardTest {

    @Test
    void constructorSizeBoardTestHeightShouldBe4WidthShouldBe8() {
        GameOfLifeBoard board = new GameOfLifeBoard(4,8, new PlainGameOfLifeSimulator());

        GameOfLifeCell[][] testingBoard = board.getBoard();
        assertEquals(4, testingBoard.length);
        assertEquals(8, testingBoard[0].length);

    }


    @Test
    void anotherConstructorCallGeneratesDifferentCellsOnBoard() {
        GameOfLifeBoard firstBoard = new GameOfLifeBoard(8,8, new PlainGameOfLifeSimulator());
        GameOfLifeBoard secondBoard = new GameOfLifeBoard(8,8, new PlainGameOfLifeSimulator());


        boolean checkIsTheSame = true;
        for (int i = 0; i < firstBoard.getBoard().length; i++) {
            for (int j = 0; j < firstBoard.getBoard()[0].length; j++) {
                if (firstBoard.getCell(i,j) != secondBoard.getCell(i,j)) {
                    checkIsTheSame = false;
                }
            }
        }

        assertFalse(checkIsTheSame);

    }


    @Test
    void invalidBoardSizeConstructorTest() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            GameOfLifeBoard game = new GameOfLifeBoard(0, 1, new PlainGameOfLifeSimulator());
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            GameOfLifeBoard game = new GameOfLifeBoard(1, 0, new PlainGameOfLifeSimulator());
        });
    }

    @Test
    void nullSimulationTypeShouldThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            GameOfLifeBoard game = new GameOfLifeBoard(3, 3, null);
        });
    }

    @Test
    void getBoardTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());

        GameOfLifeCell[][] board = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}};

        boolean checkIsTheSame = true;
        for (int i = 0; i < game.getBoard().length; i++) {
            for (int j = 0; j < game.getBoard()[0].length; j++) {
                if (game.getCell(i,j) != board[i][j].getCellValue()) {
                    checkIsTheSame = false;
                }
            }
        }
        assertFalse(checkIsTheSame);

        game.setBoard(board);
        checkIsTheSame = true;

        for (int i = 0; i < game.getBoard().length; i++) {
            for (int j = 0; j < game.getBoard()[0].length; j++) {
                if (game.getCell(i,j) != board[i][j].getCellValue()) {
                    checkIsTheSame = false;
                }
            }
        }
        assertTrue(checkIsTheSame);
    }

    @Test
    void validIndexGetCellTest() {
        GameOfLifeCell[][] board = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        game.setBoard(board);

        assertFalse(game.getCell(0,1));
        assertTrue(game.getCell(0,0));
    }

    @Test
    void invalidRowIndexGetCellTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.getCell(-1,1);
        });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.getCell(3,1);
        });


    }

    @Test
    void invalidColumnIndexGetCellTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.getCell(0,-1);
        });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.getCell(0,3);
        });
    }

    @Test
    void validSetCellTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());

        GameOfLifeCell[][] board = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}};
        game.setBoard(board);

        assertTrue(game.getBoard()[0][0].getCellValue());
        game.setCell(0, 0, false);
        assertFalse(game.getBoard()[0][0].getCellValue());

    }

    @Test
    void invalidRowIndexSetCellTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.setCell(-1,1,true);
        });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.setCell(3,1,true);
        });

    }

    @Test
    void invalidColIndexSetCellTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.setCell(1,-1,true);
        });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            game.setCell(1,3,true);
        });

    }
}