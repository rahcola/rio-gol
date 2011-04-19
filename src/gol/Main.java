package gol;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    private static int steps;
    
    public static void main(String[] args) {
        GameOfLife game = new GameOfLife(readBoard());
        long start = System.currentTimeMillis();
        game.step(steps);
        long stop = System.currentTimeMillis();
        System.out.println((stop - start) / 1000);

        /*
          Visualizing a 800x800 board is a bit intens, but doable.

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame frame = new JFrame();
                    GameOfLife game = new GameOfLife(readBoard());
                    GUI gui = new GUI(game);
                    frame.add(gui);
                    frame.pack();
                    frame.setVisible(true);
                }
            });
        */
    }

    public static boolean[][] readBoard() {
        try {
            Scanner reader = new Scanner(new File("/fs-1/2/kerola/life_800_10000.txt"));
            int size = reader.nextInt();
            steps = reader.nextInt();
            boolean[][] board = new boolean[size][size];
            for (int j = 0; j < size; j++) {
                for (int i = 0; i < size; i++) {
                    board[j][i] = reader.nextInt() == 1;
                }
            }
            return board;
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.exit(1);
            return null;
        }
    }
}
