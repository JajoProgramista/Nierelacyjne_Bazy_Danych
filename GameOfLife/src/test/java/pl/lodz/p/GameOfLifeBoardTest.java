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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

    @Test
    void validEqualsTest() {
        GameOfLifeBoard game1 = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        GameOfLifeBoard game2 = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());

        GameOfLifeCell[][] board1 = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}};
        game1.setBoard(board1);

        GameOfLifeCell[][] board2 = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}};
        game2.setBoard(board2);

        GameOfLifeBoard game3 = game1;

        assertTrue(game1.equals(game2));
        assertTrue(game1.equals(game3));
    }

    @Test
    void invalidEqualsTest() {
        GameOfLifeBoard game1 = new GameOfLifeBoard(4,4, new PlainGameOfLifeSimulator());
        GameOfLifeBoard game2 = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());

        GameOfLifeCell[][] board = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true), new GameOfLifeCell(false)},
                {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true), new GameOfLifeCell(false)}, {new GameOfLifeCell(false),
                new GameOfLifeCell(false), new GameOfLifeCell(false), new GameOfLifeCell(false)},
                {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false), new GameOfLifeCell(false)}};
        game1.setBoard(board);


        assertFalse(game1.equals(game2));
        assertFalse(game1.equals(null));
        assertFalse(game1.equals(new PlainGameOfLifeSimulator()));
    }

    @Test
    void toStringMethodTest() {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        GameOfLifeCell[][] board = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}};
        game.setBoard(board);

        /*IO.println(game.toString());*/
        String result = "GameOfLifeBoard[Plansza:\n"
                + "=true false true \n"
                + "false false true \n"
                + "false false false \n"
                + ",Rodzaj symulacji: =PlainGameOfLifeSimulator]";

        //assertEquals(result, game.toString());

    }

    @Test
    void hashCodeTest() {
        GameOfLifeBoard game1 = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());
        GameOfLifeBoard game2 = new GameOfLifeBoard(3,3, new PlainGameOfLifeSimulator());


        GameOfLifeCell[][] board1 = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}};
        game1.setBoard(board1);

        GameOfLifeCell[][] board2 = {{new GameOfLifeCell(true),new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(true)}, {new GameOfLifeCell(false), new GameOfLifeCell(false),
                new GameOfLifeCell(false)}};
        game2.setBoard(board2);

        assertEquals(game1.hashCode(), game2.hashCode());
        assertTrue(game1.equals(game2));

        GameOfLifeBoard game3 = new GameOfLifeBoard(3, 3, new PlainGameOfLifeSimulator());
        assertNotEquals(game1.hashCode(), game3.hashCode());
        assertFalse(game1.equals(game3));

        GameOfLifeBoard game4 = game1;
        assertEquals(game1.hashCode(), game4.hashCode());
        assertTrue(game1.equals(game4));


    }
}