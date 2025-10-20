package pl.lodz.p;

public class PlainGameOfLifeSimulator implements GameOfLifeSimulator {

    /**
     * Funkcja ustalająca wartość poszczególnych komórek planszy
     * na podstawie wartości danej komórki oraz wartości komórek sąsiadów.
     * Komórka z wartością false, mająca dokładnie 3 sąsiadów z wartością true
     * zmienia swoją wartość na true.
     * Komórka z wartością true, mająca dokładnie 1 sąsiada lub mająca ich
     * ponad 3 zmiena swoją wartość na false.
     * W innych przypadkach komórki pozostają te same
     * Funkcja tworzy tymczasową tablicę do której przypisuje kolejne nowe wartości
     * komórek, następnie po sprawdzeniu wszystkich komórek z oryginalnej tablicy
     * podmienia tablicę board z pola prywatnego na nową tablicę.
     */

    @Override
    public void doStep(GameOfLifeBoard boardMain) {
        boolean[][] tempBoard = new boolean[boardMain.getBoard().length][boardMain.getBoard()[0].length];

        for (int i = 0; i < boardMain.getBoard().length; i++) {
            for (int j = 0; j < boardMain.getBoard()[0].length; j++) {
                int aliveNeighbours = 0;
                boolean curCell = boardMain.getBoard()[i][j];

                for (int row = -1; row <= 1; row++) {
                    for (int col = -1; col <= 1; col++) {
                        if (row == 0 && col == 0) {
                            continue;
                        }
                        int ipos = (i + row + boardMain.getBoard().length) % boardMain.getBoard().length;
                        int jpos = (j + col + boardMain.getBoard()[0].length) % boardMain.getBoard()[0].length;

                        if (boardMain.getBoard()[ipos][jpos]) {
                            aliveNeighbours++;
                        }
                    }
                }

                if (curCell) {
                    if (aliveNeighbours == 1 || aliveNeighbours > 3) {
                        curCell = false;
                    }
                } else {
                    if (aliveNeighbours == 3) {
                        curCell = true;
                    }
                }

                tempBoard[i][j] = curCell;
            }
        }

        boardMain.setBoard(tempBoard);
    }
}
