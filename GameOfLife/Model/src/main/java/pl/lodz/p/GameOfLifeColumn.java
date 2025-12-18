/*
 * Copyright (c) 2025 Timmy&Pitru
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package pl.lodz.p;

import java.util.List;
import java.util.Locale;

public class GameOfLifeColumn extends GameOfLifeBoardInfo {
    /**
     * Konstruktor inicjalizujący określoną kolumnę.
     * @param line kolumna planszy, tablica komórek GameOfLifeCell.
     */
    GameOfLifeColumn(List<GameOfLifeCell> line, Locale locale) {
        super(line, locale);
    }
}
