package gol;

import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class CellGroup implements Runnable {

	private ArrayList<Cell> cells;
	private Semaphore start_calc;
	private Semaphore calc_ready;
	private Semaphore start_set;
	private Semaphore set_ready;

	public CellGroup(Semaphore start_calc,
					 Semaphore calc_ready,
					 Semaphore start_set,
					 Semaphore set_ready) {
		this.cells = new ArrayList<Cell>();
		this.start_calc = start_calc;
		this.calc_ready = calc_ready;
		this.start_set = start_set;
		this.set_ready = set_ready;
	}

	public void addCell(Cell cell) {
		this.cells.add(cell);
	}

	public void run() {
		while (true) {
			this.start_calc.acquireUninterruptibly();
			for (Cell cell : this.cells) {
				try {
					cell.calculateNewState();
				} catch (Exception e) {
					System.out.println(e);
					return;
				}
			}
			this.calc_ready.release();

			this.start_set.acquireUninterruptibly();
			for (Cell cell : this.cells) {
				cell.setNewState();
			}
			this.set_ready.release();
		}
	}

}