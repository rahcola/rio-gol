package gol;

public class GameOfLife {

	private Cell[] cells;

	public GameOfLife(Cell[] cells) {
		this.cells = cells;
	}

	public void step() {
		for (Cell cell: this.cells)
			cell.run();
	}

}