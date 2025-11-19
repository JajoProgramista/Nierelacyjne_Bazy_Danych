/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import java.util.List;

public class GameOfLifeRow extends GameOfLifeBoardInfo {
    /**
     * Konstruktor inicjalizujący określony wiersz.
     * @param line wiersz planszy, tablica komórek GameOfLifeCell.
     */
    public GameOfLifeRow(List<GameOfLifeCell> line) {
        super(line);
    }
}
