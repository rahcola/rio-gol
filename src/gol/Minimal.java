package gol;

public class Minimal extends GameOfLife {

	public Minimal() {
		super(create(), 800);
	}


	private static boolean[] create() {
        int size = 800;
		boolean[] cells = new boolean[size * size];

		for (int y = 0; y < cells.length; y++) {
            cells[y] = false;
		}

		cells[100 + (107 * size)] = true;
		cells[102 + (105 * size)] = true;
		cells[102 + (107 * size)] = true;
		cells[102 + (108 * size)] = true;
		cells[103 + (105 * size)] = true;
		cells[103 + (107 * size)] = true;
		cells[104 + (105 * size)] = true;
		cells[105 + (103 * size)] = true;
		cells[106 + (101 * size)] = true;
		cells[106 + (103 * size)] = true;
		return cells;
	}
}
