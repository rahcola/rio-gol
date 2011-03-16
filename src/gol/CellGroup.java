package gol;

import java.util.concurrent.Semaphore;
import java.util.LinkedList;

public class CellGroup implements Runnable {

	private LinkedList<Cell> cells;
	private Semaphore start_calc;
	private Semaphore calc_ready;
	private Semaphore start_set;
	private Semaphore set_ready;
	private boolean run;

	public CellGroup(Semaphore start_calc,
					 Semaphore calc_ready,
					 Semaphore start_set,
					 Semaphore set_ready) {
		this.cells = new LinkedList<Cell>();
		this.start_calc = start_calc;
		this.calc_ready = calc_ready;
		this.start_set = start_set;
		this.set_ready = set_ready;
		this.run = true;
	}

	public void addCell(Cell cell) {
		this.cells.add(cell);
	}

	public void run() {
		while (this.run) {
			this.start_calc.acquireUninterruptibly();
			for (Cell cell : this.cells) {
				cell.calculateNewState();
			}
			this.calc_ready.release();

			this.start_set.acquireUninterruptibly();
			for (Cell cell : this.cells) {
				cell.setNewState();
			}
			this.set_ready.release();
		}
	}

	public void stop() {
		this.run = false;
	}

}