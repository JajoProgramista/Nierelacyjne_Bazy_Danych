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

        for (int i = 0; i < mainBoard.getBoard().length; i++) {
            for (int j = 0; j < mainBoard.getBoard()[0].length; j++) {
                GameOfLifeCell curCell = mainBoard.getBoard()[i][j];
                cellsNextState.put(curCell, curCell.nextState());
            }
        }

        cellsNextState.forEach((cell, v) -> cell.updateState(v));

    }

}
