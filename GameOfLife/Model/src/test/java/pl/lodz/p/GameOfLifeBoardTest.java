/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import GameOfLifeExceptions.InvalidBoardSizeException;
import GameOfLifeExceptions.NullException;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GameOfLifeBoardTest {

    @Test
    void constructorSizeBoardTestHeightShouldBe4WidthShouldBe8() {
        GameOfLifeBoard board = new GameOfLifeBoard(4,8, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("en"));

        GameOfLifeCell[][] testingBoard = board.getBoard();
        assertEquals(4, testingBoard.length);
        assertEquals(8, testingBoard[0].length);

        int cells = 0;

        for (int i = 0; i < testingBoard.length; i++) {
            for (int j = 0; j < testingBoard[0].length; j++) {
                if(testingBoard[i][j].getCellValue()) cells++;
            }
        }

        assertEquals(cells, 4);
    }


    @Test
    void anotherConstructorCallGeneratesDifferentCellsOnBoard() {
        GameOfLifeBoard firstBoard = new GameOfLifeBoard(8,8, DensityLevel.MEDIUM, new PlainGameOfLifeSimulator(),
                new Locale("pl"));
        GameOfLifeBoard secondBoard = new GameOfLifeBoard(8,8, DensityLevel.MEDIUM, new PlainGameOfLifeSimulator(),
                new Locale("pl"));

        int cells = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(firstBoard.getBoard()[i][j].getCellValue()) cells++;
            }
        }

        assertEquals(cells, 20);

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
        assertThrows(InvalidBoardSizeException.class, () -> {
            GameOfLifeBoard game = new GameOfLifeBoard(0, 1, DensityLevel.HIGH, new PlainGameOfLifeSimulator(),
                    new Locale("en"));
        });

        assertThrows(InvalidBoardSizeException.class, () -> {
            GameOfLifeBoard game = new GameOfLifeBoard(1, 0, DensityLevel.HIGH, new PlainGameOfLifeSimulator(),
                    new Locale("en"));
        });
    }

    @Test
    void nullSimulationTypeShouldThrowsException() {
        assertThrows(NullException.class, () -> {
            GameOfLifeBoard game = new GameOfLifeBoard(3, 3, DensityLevel.LOW, null,
                    new Locale("pl"));
        });
    }

    @Test
    void getBoardTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, DensityLevel.HIGH, new PlainGameOfLifeSimulator(),
                new Locale("pl"));

        int cells = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(game.getBoard()[i][j].getCellValue()) cells++;
            }
        }

        assertEquals(cells, 5);

        GameOfLifeCell[][] board = {{new GameOfLifeCell(true, new Locale("pl")),new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(false, new Locale("pl"))}};

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
        GameOfLifeCell[][] board = {{new GameOfLifeCell(true, new Locale("pl")),new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(false, new Locale("pl"))}};
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));
        game.setBoard(board);

        assertFalse(game.getCell(0,1));
        assertTrue(game.getCell(0,0));
    }

    @Test
    void invalidRowIndexGetCellTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));
        assertThrows(InvalidBoardSizeException.class, () -> {
            game.getCell(-1,1);
        });
        assertThrows(InvalidBoardSizeException.class, () -> {
            game.getCell(3,1);
        });


    }

    @Test
    void invalidColumnIndexGetCellTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));
        assertThrows(InvalidBoardSizeException.class, () -> {
            game.getCell(0,-1);
        });
        assertThrows(InvalidBoardSizeException.class, () -> {
            game.getCell(0,3);
        });
    }

    @Test
    void validSetCellTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));

        GameOfLifeCell[][] board = {{new GameOfLifeCell(true, new Locale("pl")),new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(false, new Locale("pl"))}};
        game.setBoard(board);

        assertTrue(game.getBoard()[0][0].getCellValue());
        game.setCell(0, 0, false);
        assertFalse(game.getBoard()[0][0].getCellValue());

    }

    @Test
    void invalidRowIndexSetCellTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));

        assertThrows(InvalidBoardSizeException.class, () -> {
            game.setCell(-1,1,true);
        });

        assertThrows(InvalidBoardSizeException.class, () -> {
            game.setCell(3,1,true);
        });

    }

    @Test
    void invalidColIndexSetCellTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));

        assertThrows(InvalidBoardSizeException.class, () -> {
            game.setCell(1,-1,true);
        });

        assertThrows(InvalidBoardSizeException.class, () -> {
            game.setCell(1,3,true);
        });

    }

    @Test
    void validEqualsTest() {
        GameOfLifeBoard game1 = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));
        GameOfLifeBoard game2 = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));

        GameOfLifeCell[][] board1 = {{new GameOfLifeCell(true, new Locale("pl")),new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(false, new Locale("pl"))}};
        game1.setBoard(board1);

        GameOfLifeCell[][] board2 = {{new GameOfLifeCell(true, new Locale("pl")),new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(false, new Locale("pl"))}};
        game2.setBoard(board2);

        GameOfLifeBoard game3 = game1;

        assertTrue(game1.equals(game2));
        assertTrue(game1.equals(game3));
    }

    @Test
    void invalidEqualsTest() {
        GameOfLifeBoard game1 = new GameOfLifeBoard(4,4, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));
        GameOfLifeBoard game2 = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));

        GameOfLifeCell[][] board = {{new GameOfLifeCell(true, new Locale("pl")),new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl"))},
                {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl"))},
                {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl"))}};
        game1.setBoard(board);


        assertFalse(game1.equals(game2));
        assertFalse(game1.equals(null));
        assertFalse(game1.equals(new PlainGameOfLifeSimulator()));
    }

    @Test
    void toStringMethodTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));
        GameOfLifeCell[][] board = {{new GameOfLifeCell(true, new Locale("pl")),new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(false, new Locale("pl"))}};
        game.setBoard(board);

        IO.println(game.toString());
        String result = "GameOfLifeBoard[\n"
                + "=true false true \n"
                + "false false true \n"
                + "false false false \n"
                + ",\n"
                + "=PlainGameOfLifeSimulator]";

        assertEquals(result, game.toString());

    }

    @Test
    void hashCodeTest() {
        GameOfLifeBoard game1 = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));
        GameOfLifeBoard game2 = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));


        GameOfLifeCell[][] board1 = {{new GameOfLifeCell(true, new Locale("pl")),new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(false, new Locale("pl"))}};
        game1.setBoard(board1);

        GameOfLifeCell[][] board2 = {{new GameOfLifeCell(true, new Locale("pl")),new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(false, new Locale("pl"))}};
        game2.setBoard(board2);

        assertEquals(game1.hashCode(), game2.hashCode());
        assertTrue(game1.equals(game2));

        GameOfLifeBoard game3 = new GameOfLifeBoard(3, 3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));
        assertNotEquals(game1.hashCode(), game3.hashCode());
        assertFalse(game1.equals(game3));

        GameOfLifeBoard game4 = game1;
        assertEquals(game1.hashCode(), game4.hashCode());
        assertTrue(game1.equals(game4));

    }

    @Test
    void cloneTest() throws CloneNotSupportedException {
        GameOfLifeBoard board = new GameOfLifeBoard(3, 3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));
        GameOfLifeCell cell1 = board.getBoard()[0][0];
        GameOfLifeCell[] cell1Neigbours = cell1.getNeighbourCells();

        GameOfLifeBoard boardCopy = board.clone();
        GameOfLifeCell cell1Copy = boardCopy.getBoard()[0][0];
        GameOfLifeCell[] cell1CopyNeigbours = cell1Copy.getNeighbourCells();

        boolean isCellsTheSame = false;
        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[0].length; j++) {
                if (board.getBoard()[i][j] == boardCopy.getBoard()[i][j]) {
                    isCellsTheSame = true;
                    break;
                }
            }
        }

        boolean isNeighboursTheSame = false;
        for (int i = 0; i < cell1Neigbours.length; i++) {
            if (cell1Neigbours[i] == cell1CopyNeigbours[i]) {
                isNeighboursTheSame = true;
                break;
            }
        }

        boolean isNeigboursCopyNull = false;
        for (int i = 0; i < cell1CopyNeigbours.length; i++) {
            if (cell1CopyNeigbours[i] == null) {
                isNeigboursCopyNull = true;
            }
        }
        assertNotSame(board, boardCopy);
        assertFalse(isCellsTheSame);
        assertFalse(isNeighboursTheSame);
        assertFalse(isNeigboursCopyNull);
    }
}