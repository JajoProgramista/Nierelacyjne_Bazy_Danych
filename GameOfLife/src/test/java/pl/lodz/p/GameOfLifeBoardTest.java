package pl.lodz.p;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



class GameOfLifeBoardTest {

    @Test
    void constructorSizeBoardTestHeightShouldBe4WidthShouldBe8() {
        GameOfLifeBoard board = new GameOfLifeBoard(4,8, new PlainGameOfLifeSimulator());

        boolean[][] testingBoard = board.getBoard();
        assertEquals(4, testingBoard.length);
        assertEquals(8, testingBoard[0].length);

    }

    @Test
    void anotherConstructorCallGeneratesDifferentCellsOnBoard() {
        GameOfLifeBoard firstBoard = new GameOfLifeBoard(8,8, new PlainGameOfLifeSimulator());
        GameOfLifeBoard secondBoard = new GameOfLifeBoard(8,8, new PlainGameOfLifeSimulator());

        assertFalse(Arrays.deepEquals(firstBoard.getBoard(),secondBoard.getBoard()));

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
        GameOfLifeBoard game = new GameOfLifeBoard(8,8, new PlainGameOfLifeSimulator());
        boolean[][] board = {{true,false,true}, {false, false, true},
                {false, false, false}};
        assertFalse(Arrays.deepEquals(game.getBoard(), board));
        game.setBoard(board);
        assertTrue(Arrays.deepEquals(game.getBoard(), board));
    }

    @Test
    void validIndexGetCellTest() {
        boolean[][] board = {{true, false, false}, {false, true, true},
                {false, true, true}};
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

        boolean[][] board = {{true, false, false}, {false, true, true},
                {false, true, true}};
        game.setBoard(board);

        assertTrue(game.getBoard()[0][0]);
        game.setCell(0, 0, false);
        assertFalse(game.getBoard()[0][0]);

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