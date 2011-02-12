package gol.oscillators;

import gol.Util;
import gol.Cell;
import gol.GameOfLife;

public class Blinker extends GameOfLife {

	public Blinker() {
		super(createBlinker());
	}

	public static boolean test() {
		Blinker fresh = new Blinker();
		Blinker other = new Blinker();
		for (int i = 0; i < 10; i++) {
			other.step();
		}
		return other.equals(fresh);
	}

	private static Cell[][] createBlinker() {
		Cell[][] cells = new Cell[5][5];

		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells.length; x++) {
				cells[y][x] = new Cell(false);
			}
		}

		cells[2][1] = new Cell(true);
		cells[2][2] = new Cell(true);
		cells[2][3] = new Cell(true);

		Util.setNeighbours(cells);

		return cells;
	}

}