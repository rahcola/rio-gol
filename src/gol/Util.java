package gol;

public class Util {
	public static void setNeighbours(Cell[][] cells) {
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells.length; x++) {
				Cell[] neighbours = {neighbourOrDead(cells, x-1,y-1),
									 neighbourOrDead(cells, x, y-1),
									 neighbourOrDead(cells, x+1, y-1),
									 neighbourOrDead(cells, x-1, y),
									 neighbourOrDead(cells, x+1, y),
									 neighbourOrDead(cells, x-1, y+1),
									 neighbourOrDead(cells, x, y+1),
									 neighbourOrDead(cells, x+1, y+1)};
				cells[y][x].setNeighbours(neighbours);
			}
		}
	}

	private static Cell neighbourOrDead(Cell[][]cells, int x, int y) {
		try {
			return cells[y][x];
		} catch (IndexOutOfBoundsException e) {
			return new Cell(false);
		}
	}
}