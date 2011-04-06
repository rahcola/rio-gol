package gol;

public class Minimal extends GameOfLife {

	public Minimal() {
		super(create());
	}


	private static boolean[][] create() {
		boolean[][] cells = new boolean[800][800];

		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells.length; x++) {
				cells[y][x] = false;
			}
		}

		cells[100][107] = true;
		cells[102][105] = true;
		cells[102][107] = true;
		cells[102][108] = true;
		cells[103][105] = true;
		cells[103][107] = true;
		cells[104][105] = true;
		cells[105][103] = true;
		cells[106][101] = true;
		cells[106][103] = true;
		return cells;
	}
}
