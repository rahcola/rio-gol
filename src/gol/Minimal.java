package gol;

public class Minimal extends GameOfLife {

    public Minimal() {
        super(create(), 800);
    }


    private static boolean[][] create() {
        int size = 800;
        boolean[][] cells = new boolean[800][800];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                cells[y][x] = false;
            }
        }

        cells[100][100] = true;
        cells[100][101] = true;
        cells[100][102] = true;
        cells[101][100] = true;
        cells[102][101] = true;

        return cells;
    }

}