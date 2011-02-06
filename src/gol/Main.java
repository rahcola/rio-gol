
package gol;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					setGUI();
				}
			});
	}

	private static void setGUI() {
		JFrame frame = new JFrame();
		frame.add(testGame());
		frame.pack();
		frame.setVisible(true);
	}

	private static GameOfLife testGame() {
		Cell[][] cells = new Cell[20][20];

		int y;
		int x;
		for (y = 0; y < 20; y++) {
			for (x = 0; x < 20; x++) {
				cells[y][x] = new Cell(false);
			}
		}
		//glider
		cells[10][10] = new Cell(true);
		cells[11][11] = new Cell(true);
		cells[12][9] = new Cell(true);
		cells[12][10] = new Cell(true);
		cells[12][11] = new Cell(true);
		for (y = 0; y < 20; y++) {
			for (x = 0; x < 20; x++) {
				setNeighbours(cells, cells[y][x], x, y);
			}
		}
		GameOfLife game = new GameOfLife(cells);
		try {
			game.step();
		} catch (Exception e) {
			return game;
		}
		return game;
	}

	private static void setNeighbours(Cell[][] cells, Cell cell, int x, int y) {
		Cell[] neighbours = {neighbourOrDead(cells, x-1,y-1),
							 neighbourOrDead(cells, x, y-1),
							 neighbourOrDead(cells, x+1, y-1),
							 neighbourOrDead(cells, x-1, y),
							 neighbourOrDead(cells, x+1, y),
							 neighbourOrDead(cells, x-1, y+1),
							 neighbourOrDead(cells, x, y+1),
							 neighbourOrDead(cells, x+1, y+1)};
		cell.setNeighbours(neighbours);
	}

	private static Cell neighbourOrDead(Cell[][]cells, int x, int y) {
		try {
			return cells[y][x];
		} catch (Exception e) {
			return new Cell(false);
		}
	}
}