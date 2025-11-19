package pl.lodz.p;

public class GameOfLifeBoardDaoFactory {
    public static Dao<GameOfLifeBoard> getFileDao(String fileName) {
        return new FileGameOfLifeDaoFactory(fileName);
    }
}
