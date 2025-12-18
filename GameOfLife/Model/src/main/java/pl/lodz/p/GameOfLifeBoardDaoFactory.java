package pl.lodz.p;

import java.util.Locale;

public class GameOfLifeBoardDaoFactory {
    public static Dao<GameOfLifeBoard> getFileDao(String fileName, Locale locale) {
        return new FileGameOfLifeDaoFactory(fileName, locale);
    }
}
