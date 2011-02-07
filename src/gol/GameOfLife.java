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

		for (ArrayList<Cell> cell_list : splitForProcessors()) {
			this.groups.add(new CellGroup(cell_list,
										  start_calc,
										  calc_ready,
										  start_set,
										  set_ready));
		}

		for (CellGroup group : this.groups) {
			new Thread(group).start();
		}
	}

	private ArrayList<Cell>[] splitForProcessors() {
		int nProcessors = Runtime.getRuntime().availableProcessors();
		ArrayList<Cell>[] cell_lists = new ArrayList[nProcessors];

		for (int i = 0; i < cell_lists.length; i++) {
			cell_lists[i] = new ArrayList<Cell>();
		}

		int i = 0;
		for (Cell[] row : this.cells) {
			for (Cell cell : row) {
				cell_lists[i % nProcessors].add(cell);
			}
		}
		return cell_lists;
	}

	public void step() throws InterruptedException {
		this.start_calc.release(groups.size());
		this.calc_ready.acquire(groups.size());
		this.start_set.release(groups.size());
		this.set_ready.acquire(groups.size());
	}

	public int getSize() {
		return cells.length;
	}

	public Cell cellAt(int x, int y) {
		return cells[y][x];
	}

}