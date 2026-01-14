package pl.lodz.p;

import exceptions.DataBaseException;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;

public class DataBaseTest {
    @Test
    void writeAndReadTest() throws SQLException, ClassNotFoundException, FileNotFoundException {
        GameOfLifeBoard game = new GameOfLifeBoard(3,3, DensityLevel.LOW, new PlainGameOfLifeSimulator(),
                new Locale("pl"));

        GameOfLifeCell[][] board = {{new GameOfLifeCell(true, new Locale("pl")),new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(true, new Locale("pl"))}, {new GameOfLifeCell(false, new Locale("pl")), new GameOfLifeCell(false, new Locale("pl")),
                new GameOfLifeCell(false, new Locale("pl"))}};
        game.setBoard(board);
        Connection conTest;
        try (JdbcGameOfLifeBoardDao dao = (JdbcGameOfLifeBoardDao)
                GameOfLifeBoardDaoFactory.getDataBaseDao(
                        "jdbc:postgresql://localhost:5432/nbddb",
                        "nbd", "nbdpassword",
                        new Locale("pl"),
                        "plansza")) {

            dao.deleteByName("plansza");
            dao.write(game);
            GameOfLifeBoard game2 = dao.read();
            boolean isTheSame = true;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (game.getCell(i, j) != game2.getCell(i, j)) {
                        isTheSame = false;
                        break;
                    }
            }
        }

            assertTrue(isTheSame);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void invalidReadTest() {
        try (JdbcGameOfLifeBoardDao dao = (JdbcGameOfLifeBoardDao)
                GameOfLifeBoardDaoFactory.getDataBaseDao(
                        "jdbc:postgresql://localhost:5432/nbddb",
                        "nbd", "nbdpassword",
                        new Locale("pl"),
                        "test")) {
            assertThrows(DataBaseException.class, () -> {
                GameOfLifeBoard game = dao.read();
            });
            GameOfLifeBoard game = dao.read();

        }
        catch (DataBaseException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("exceptions", new Locale("pl"));
            assertSame(bundle.getString("boardReadError"), e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        try (JdbcGameOfLifeBoardDao dao = (JdbcGameOfLifeBoardDao)
                GameOfLifeBoardDaoFactory.getDataBaseDao(
                        "jdbc:postgresql://localhost:5432/nbddb",
                        "nbd", "nbdpasswordd",
                        new Locale("pl"),
                        "test")) {
        }
        catch (DataBaseException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("exceptions", new Locale("pl"));
            assertSame(bundle.getString("invalidDataBaseValues"), e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
