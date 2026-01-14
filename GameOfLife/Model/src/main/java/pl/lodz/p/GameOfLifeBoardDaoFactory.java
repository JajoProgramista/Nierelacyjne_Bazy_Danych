package pl.lodz.p;

import java.sql.SQLException;
import java.util.Locale;

public class GameOfLifeBoardDaoFactory {
    public static Dao<GameOfLifeBoard> getFileDao(String fileName, Locale locale) {
        return new FileGameOfLifeDaoFactory(fileName, locale);
    }

    public static Dao<GameOfLifeBoard> getDataBaseDao(String url, String user, String password,
                                                      Locale locale, String name)
            throws SQLException, ClassNotFoundException {
        return new JdbcGameOfLifeBoardDao(url, user, password, locale, name);
    }
}
