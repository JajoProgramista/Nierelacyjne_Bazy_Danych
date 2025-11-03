/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import java.util.HashMap;
import java.util.Map;

public class PlainGameOfLifeSimulator implements GameOfLifeSimulator {

    /**
     * Funkcja ustalająca wartość poszczególnych komórek planszy w następnym kroku gry
     * na podstawie wartości danej komórki oraz wartości komórek sąsiadów.
     * @param mainBoard plansza typu GameOfLifeBoard
     */

    @Override
    public void doStep(GameOfLifeBoard mainBoard) {
        Map<GameOfLifeCell, Boolean> cellsNextState = new HashMap<>();

        for (int i = 0; i < mainBoard.getBoard().size(); i++) {
            for (int j = 0; j < mainBoard.getBoard().getFirst().size(); j++) {
                GameOfLifeCell curCell = mainBoard.getBoard().get(i).get(j);
                cellsNextState.put(curCell, curCell.nextState());
            }
        }

        cellsNextState.forEach(GameOfLifeCell::updateState);

        /*boolean[][] tempBoard = new boolean[mainBoard.getBoard().size()][mainBoard.getBoard().getFirst().size()];

        for (int i = 0; i < mainBoard.getBoard().size(); i++) {
            for (int j = 0; j < mainBoard.getBoard().getFirst().size(); j++) {
                tempBoard[i][j] = mainBoard.getBoard().get(i).get(j).nextState();
            }
        }

        for (int i = 0; i < mainBoard.getBoard().size(); i++) {
            for (int j = 0; j < mainBoard.getBoard().getFirst().size(); j++) {
                mainBoard.getBoard().get(i).get(j).updateState(tempBoard[i][j]);
            }
        }*/
    }
}
