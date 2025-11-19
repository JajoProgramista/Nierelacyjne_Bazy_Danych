package pl.lodz.p;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileGameOfLifeDaoFactory implements Dao <GameOfLifeBoard> {
    private final String filename;
    public FileGameOfLifeDaoFactory(String filename) {
        this.filename = filename;
    }
    @Override
    public GameOfLifeBoard read() {
        List<String> lines = new ArrayList<>();
        File file = new File(filename);

        if(!new File(filename).exists()) {
            throw new IllegalArgumentException("Plik nie istnieje!");
        }

        try(Scanner inputFile = new Scanner(file)) {
            while (inputFile.hasNext()) {
                String in = inputFile.nextLine();
                lines.add(in);
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Nie udało się otworzyć pliku: " + filename, e);
        }

        GameOfLifeBoard board = new GameOfLifeBoard(lines.getFirst().length(), lines.size(), new PlainGameOfLifeSimulator());
        GameOfLifeCell[][] tab = new GameOfLifeCell[lines.size()][lines.getFirst().length()];
        for(int i = 0; i < lines.size(); i++) {
            for(int j = 0; j < lines.getFirst().length(); j++) {
                if(lines.get(i).charAt(j) == '1') {
                    tab[i][j] = new GameOfLifeCell(true);
                } else {
                    tab[i][j] = new GameOfLifeCell(false);
                }
            }
        }

        board.setBoard(tab);
        return board;
    }

    @Override
    public void write(GameOfLifeBoard obj){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < obj.getBoard().length; i++) {
            for(int j = 0; j < obj.getBoard()[0].length; j++) {
                sb.append(obj.getBoard()[i][j].getCellValue() ? "1" : "0");
            }
            sb.append("\n");
        }

        try (PrintWriter outputFile = new PrintWriter(filename))
        {
            outputFile.println(sb.toString());
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
