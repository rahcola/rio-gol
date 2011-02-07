package gol;

import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class CellGroup implements Runnable {

	private ArrayList<Cell> cells;
	private Semaphore start_calc;
	private Semaphore calc_ready;
	private Semaphore start_set;
	private Semaphore set_ready;

	public CellGroup(ArrayList<Cell> cells,
					 Semaphore start_calc,
					 Semaphore calc_ready,
					 Semaphore start_set,
					 Semaphore set_ready) {
		this.cells = cells;
		this.start_calc = start_calc;
		this.calc_ready = calc_ready;
		this.start_set = start_set;
		this.set_ready = set_ready;
	}

	public void run() {
		while (true) {
			try {
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
			} catch (Exception e) {
				return;
			}
		}
	}

}