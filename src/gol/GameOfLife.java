package gol;

import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class GameOfLife {

	private Semaphore start_calc;
	private Semaphore calc_ready;
	private Semaphore start_set;
	private Semaphore set_ready;
	private Cell[][] cells;
	private ArrayList<CellGroup> groups;

	public GameOfLife(Cell[][] cells) {
		this.cells = cells;
		this.start_calc = new Semaphore(0);
		this.calc_ready = new Semaphore(0);
		this.start_set = new Semaphore(0);
		this.set_ready = new Semaphore(0);
		this.groups = new ArrayList<CellGroup>();

		splitCellsForProcessors();
		for (CellGroup group : this.groups) {
			new Thread(group).start();
		}
	}

	private void splitCellsForProcessors() {
		int nProcessors = Runtime.getRuntime().availableProcessors();
		for (int i = 0; i < nProcessors; i++) {
			this.groups.add(new CellGroup(start_calc,
										  calc_ready,
										  start_set,
										  set_ready));
		}

		int i = 0;
		for (Cell[] row : this.cells) {
			for (Cell cell : row) {
				this.groups.get(i % nProcessors).cells.add(cell);
				i += 1;
			}
		}
	}

	public void step() {
		this.start_calc.release(groups.size());
		this.calc_ready.acquireUninterruptibly(groups.size());
		this.start_set.release(groups.size());
		this.set_ready.acquireUninterruptibly(groups.size());
	}

	public int getSize() {
		return cells.length;
	}

	public Cell cellAt(int x, int y) {
		return cells[y][x];
	}

	public boolean equals(GameOfLife other) {
		if (this.getSize() == other.getSize()) {
			for (int y = 0; y < cells.length; y++) {
				for (int x = 0; x < cells.length; x++) {
					if (!this.cells[y][x].equals(other.cells[y][x])) {
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

}