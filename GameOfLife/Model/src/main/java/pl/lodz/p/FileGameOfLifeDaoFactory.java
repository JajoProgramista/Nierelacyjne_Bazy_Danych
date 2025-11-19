package pl.lodz.p;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileGameOfLifeDaoFactory implements Dao<GameOfLifeBoard> {
    private final String filename;

    public FileGameOfLifeDaoFactory(String filename) {
        this.filename = filename;
    }

    @Override
    public GameOfLifeBoard read() throws FileNotFoundException {
        List<String> lines = new ArrayList<>();
        File file = new File(filename);

        if (!new File(filename).exists()) {
            throw new FileNotFoundException("Plik nie istnieje!");
        }

        try (Scanner inputFile = new Scanner(file)) {
            while (inputFile.hasNext()) {
                String in = inputFile.nextLine();
                lines.add(in);
            }
        }


        GameOfLifeBoard board = new GameOfLifeBoard(lines.getFirst().length(),
                lines.size(), new PlainGameOfLifeSimulator());
        GameOfLifeCell[][] tab = new GameOfLifeCell[lines.size()][lines.getFirst().length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.getFirst().length(); j++) {
                boolean value = lines.get(i).charAt(j) == '1';
                tab[i][j] = new GameOfLifeCell(value);
            }
        }

        board.setBoard(tab);
        return board;
    }

    @Override
    public void write(GameOfLifeBoard obj) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < obj.getBoard().length; i++) {
            for (int j = 0; j < obj.getBoard()[0].length; j++) {
                sb.append(obj.getBoard()[i][j].getCellValue() ? "1" : "0");
            }
            sb.append("\n");
        }

        try (PrintWriter outputFile = new PrintWriter(filename)) {
            outputFile.println(sb);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }
}
