package pl.lodz.p;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileGameOfLifeDaoFactoryTest {
    @Test
    void readTest() throws FileNotFoundException {
        FileGameOfLifeDaoFactory d1 =
                (FileGameOfLifeDaoFactory) GameOfLifeBoardDaoFactory.getFileDao("plik_testowy1.txt");
        GameOfLifeBoard game = new GameOfLifeBoard(5, 5, new PlainGameOfLifeSimulator());

        d1.write(game);
        GameOfLifeBoard game2 = d1.read();
        boolean theSameValues = true;

        for (int i = 0; i < game.getBoard().length; i++) {
            for (int j = 0; j < game.getBoard()[0].length; j++) {
                if (game.getCell(i, j) != game2.getCell(i, j)) {
                    theSameValues = false;
                }
            }
        }
        assertTrue(theSameValues);
    }

    @Test
    void readNeighboursTest() throws FileNotFoundException {
        GameOfLifeBoardDaoFactory dao = new GameOfLifeBoardDaoFactory();
        FileGameOfLifeDaoFactory d1 = (FileGameOfLifeDaoFactory) dao.getFileDao("plik_testowy.txt");
        GameOfLifeBoard game = d1.read();

        int count = 0;
        for (int i = 0; i < game.getBoard()[2][2].getNeighbourCells().length; i++) {
            if (game.getBoard()[2][2].getNeighbourCells()[i].getCellValue()) {
                count++;
            }
        }

        assertEquals(5, count);
    }

    @Test
    void invalidFileNameReadTest() {
        FileGameOfLifeDaoFactory d1 =
                (FileGameOfLifeDaoFactory) GameOfLifeBoardDaoFactory.getFileDao("nieIstnieje.txt");
        assertThrows(FileNotFoundException.class, () -> {
            d1.read();
        });
    }

}