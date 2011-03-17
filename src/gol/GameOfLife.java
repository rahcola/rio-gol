package gol;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.LinkedList;

public class GameOfLife {

	private int width;
	private int height;
	private Cell[] currentGen;
	private LinkedList<Runnable> updaters;
	private ExecutorService pool;

	public GameOfLife(boolean[][] cells) {
		this.width = cells[0].length;
		this.height = cells.length;
		this.currentGen = new Cell[this.width * this.height];
		this.updaters = new LinkedList<Runnable>();
		this.pool = Executors.newCachedThreadPool();

		for (int y = 0; y < this.height; ++y) {
			for (int x = 0; x < this.width; ++x) {
				currentGen[(y * this.width) + x] = new Cell(cells[y][x]);
			}
		}
		for (int y = 0; y < this.height; ++y) {
			for (int x = 0; x < this.width; ++x) {
				currentGen[(y * this.width) + x].setNeighbours(neighbours(x, y));
			}
		}
		splitCellsToUpdaters();
	}

	public Cell cellAt(int x, int y) {
		try {
			return currentGen[(y * this.width) + x];
		} catch (IndexOutOfBoundsException e) {
			return new Cell(false);
		}
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void step() {
		LinkedList<Future> futures = new LinkedList<Future>();

		for (Runnable updater : this.updaters) {
			futures.add(pool.submit(updater));
		}
		for (Future future : futures) {
			try {
				future.get();
			} catch (ExecutionException e) {
				System.out.println("execution exception");
			} catch (InterruptedException e) {
				System.out.println("interrupted");
			}
		}
		for (Cell c : this.currentGen) {
			c.setState();
		}
	}

	private void splitCellsToUpdaters() {
		int nCores = Runtime.getRuntime().availableProcessors();
		// no point having more threads than cells
		nCores = Math.min(nCores, currentGen.length);
		final int cellsPerThread = currentGen.length / nCores;
		
		// nCores - 1 threads all get cellsPerThread of cells to update
		for (int t = 0; t < nCores - 1; t++) {
			final int cellsFrom = cellsPerThread * t;
			final int cellsTo = cellsPerThread * (t + 1);
			updaters.add(new Runnable() {
					public void run() {
						for (int c = cellsFrom; c < cellsTo; c++) {
							currentGen[c].calcState();
						}
					}
				});
		}
		// the last thread gets the rest of the cells
		final int rest = (nCores - 1) * cellsPerThread;
		updaters.add(new Runnable() {
				public void run() {
					for (int c = rest; c < currentGen.length; c++) {
						currentGen[c].calcState();
					}
				}
			});
	}

	private Cell[] neighbours(int x, int y) {
		Cell[] neighbours = {
			cellAt(x-1, y-1),
			cellAt(x, y-1),
			cellAt(x+1, y-1),
			cellAt(x-1, y),
			cellAt(x+1, y),
			cellAt(x-1, y+1),
			cellAt(x, y+1),
			cellAt(x+1, y+1)};
		return neighbours;
	}

}