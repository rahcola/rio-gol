package gol;

public class Minimal extends GameOfLife {

	public Minimal() {
		super(createMinimal());
	}

	private static Cell[][] createMinimal() {
		Cell[][] cells = new Cell[200][200];

		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells.length; x++) {
				cells[y][x] = new Cell(false);
			}
		}

		cells[100][107] = new Cell(true);
		cells[102][105] = new Cell(true);
		cells[102][107] = new Cell(true);
		cells[102][108] = new Cell(true);
		cells[103][105] = new Cell(true);
		cells[103][107] = new Cell(true);
		cells[104][105] = new Cell(true);
		cells[105][103] = new Cell(true);
		cells[106][101] = new Cell(true);
		cells[106][103] = new Cell(true);

		Util.setNeighbours(cells);

		return cells;
	}

}