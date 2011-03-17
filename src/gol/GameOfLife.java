package gol;

import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.LinkedList;

public class GameOfLife {

	private Semaphore start_calc;
	private Semaphore calc_ready;
	private Semaphore start_set;
	private Semaphore set_ready;
	private Cell[][] cells;
	private ArrayList<CellGroup> groups;
	private LinkedList<Thread> threads;

	public GameOfLife(Cell[][] cells) {
		this.cells = cells;
		this.start_calc = new Semaphore(0);
		this.calc_ready = new Semaphore(0);
		this.start_set = new Semaphore(0);
		this.set_ready = new Semaphore(0);
		this.groups = new ArrayList<CellGroup>();
		this.threads = new LinkedList<Thread>();

		splitCellsForProcessors();
		for (CellGroup group : this.groups) {
			Thread thread = new Thread(group);
			this.threads.add(thread);
			thread.start();
		}
	}

	private void splitCellsForProcessors() {
		int nProcessors = Runtime.getRuntime().availableProcessors();
		if (nProcessors > getWidth() * getHeight()) {
			//no point having more threads than cells
			nProcessors = getWidth() * getHeight();
		}

		for (int i = 0; i < nProcessors; i++) {
			this.groups.add(new CellGroup(start_calc,
										  calc_ready,
										  start_set,
										  set_ready));
		}

		int group = 0;
		for (Cell[] row : this.cells) {
			for (Cell cell : row) {
				this.groups.get(group % nProcessors).addCell(cell);
				group += 1;
			}
		}
	}

	public void step() {
		this.start_calc.release(groups.size());
		this.calc_ready.acquireUninterruptibly(groups.size());
		this.start_set.release(groups.size());
		this.set_ready.acquireUninterruptibly(groups.size());
	}

	public void stop() {
		for (CellGroup group : this.groups) {
			group.stop();
		}
		step();
		for (Thread thread : this.threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				System.out.println("thread was interrupted");
			}
		}
	}

	public int getWidth() {
		return cells[0].length;
	}

	public int getHeight() {
		return cells.length;
	}

	public Cell cellAt(int x, int y) {
		return cells[y][x];
	}

}