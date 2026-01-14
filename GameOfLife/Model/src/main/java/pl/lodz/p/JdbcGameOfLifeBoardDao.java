package pl.lodz.p;

import exceptions.DataBaseException;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class JdbcGameOfLifeBoardDao implements Dao<GameOfLifeBoard>, AutoCloseable {
    private final Connection connection;
    private final ResourceBundle bundle;
    private final String name;

    public JdbcGameOfLifeBoardDao(String url, String user,
                                  String password, Locale locale, String name)
            throws ClassNotFoundException, SQLException {
        bundle = ResourceBundle.getBundle("exceptions", locale);
        this.name = name;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new DataBaseException(bundle.getString("invalidDataBaseValues"));
        }
    }

    @Override
    public void close() throws Exception {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            throw new DataBaseException(bundle.getString("closeError"));
        }
    }

    @Override
    public GameOfLifeBoard read() throws FileNotFoundException, SQLException {
        String selectBoard = "SELECT id, width, height FROM board WHERE name = ?";
        String selectCells = "SELECT rowIndex, colIndex, cellValue FROM cell WHERE boardId = ?";

        try (PreparedStatement boardState =
                connection.prepareStatement(selectBoard)) {
            boardState.setString(1, name);
            ResultSet result = boardState.executeQuery();

            if (!result.next()) {
                throw new DataBaseException(bundle.getString("boardNotFound"));
            }

            int boardId = result.getInt(1);
            int boardWidth = result.getInt(2);
            int boardHeight = result.getInt(3);

            GameOfLifeBoard game = new GameOfLifeBoard(boardHeight, boardWidth,
                    DensityLevel.LOW, new PlainGameOfLifeSimulator(), bundle.getLocale());

            try (PreparedStatement cellState =
                            connection.prepareStatement(selectCells)) {
                cellState.setInt(1, boardId);
                ResultSet rsCells = cellState.executeQuery();

                while (rsCells.next()) {
                    int row = rsCells.getInt(1);
                    int col = rsCells.getInt(2);
                    boolean value = rsCells.getBoolean(3);

                    game.setCell(row, col, value);
                }
            } catch (Exception e) {
                throw new DataBaseException(bundle.getString("cellsReadError"));
            }

            return game;
        } catch (Exception e) {
            throw new DataBaseException(bundle.getString("boardReadError"));
        }
    }

    @Override
    public void write(GameOfLifeBoard obj) throws SQLException {
        String insertBoard = "INSERT INTO board(name, width, height) VALUES (?, ?, ?)";
        String insertCell = "INSERT INTO cell(boardId, rowIndex, colIndex, cellValue) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement boardState =
                     connection.prepareStatement(insertBoard, Statement.RETURN_GENERATED_KEYS)) {

            boardState.setString(1, name);
            boardState.setInt(2, obj.getBoard()[0].length);
            boardState.setInt(3, obj.getBoard().length);
            boardState.executeUpdate();

            ResultSet result = boardState.getGeneratedKeys();

            if (!result.next()) {
                throw new DataBaseException(bundle.getString("keyGenerationError"));
            }

            int boardId = result.getInt(1);
            GameOfLifeCell[][] board = obj.getBoard();
            try (PreparedStatement cellState =
                    connection.prepareStatement(insertCell)) {
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[0].length; j++) {

                        cellState.setInt(1, boardId);
                        cellState.setInt(2, i);
                        cellState.setInt(3, j);
                        cellState.setBoolean(4, board[i][j].getCellValue());
                        cellState.executeUpdate();
                    }
                }
            } catch (Exception e) {
                throw new DataBaseException(bundle.getString("cellsWriteError"));
            }
            connection.commit();
        } catch (Exception e) {
            throw new DataBaseException(bundle.getString("boardWriteError"));
        }
    }

    public void deleteByName(String name) {
        String sql = "DELETE FROM board WHERE name = ?";
        try (PreparedStatement boardState = connection.prepareStatement(sql)) {
            boardState.setString(1, name);
            boardState.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseException(bundle.getString("boardDeleteError"));
        }
    }
}
