package gol;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.LinkedList;

public class GameOfLife {

	private int size;
	private boolean[] currentGen;
    private boolean[] nextGen;
	private LinkedList<Runnable> updaters;
	private ExecutorService pool;
    private CyclicBarrier setBarrier;

	public GameOfLife(boolean[] cells, int size) {
        this.size = size;
		this.currentGen = Arrays.copyOf(cells, cells.length);
		this.nextGen = Arrays.copyOf(cells, cells.length);
		this.updaters = new LinkedList<Runnable>();
		this.pool = Executors.newCachedThreadPool();

		splitCellsToUpdaters();
	}

	public boolean cellAt(int x, int y) {
		try {
			return currentGen[(y * this.size) + x];
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public int getWidth() {
		return this.size;
	}

	public int getHeight() {
		return this.size;
	}

	public void step() {
		for (Runnable updater : this.updaters) {
			pool.execute(updater);
		}

		try {
			this.setBarrier.await();
		} catch (InterruptedException e) {
			System.out.println("interrupted");
		} catch (BrokenBarrierException e) {
			System.out.println("broken barrier");
		}

        swapBuffers();
	}

	public void shutdown() {
		this.pool.shutdown();
	}

	private void splitCellsToUpdaters() {
		int nCores = Runtime.getRuntime().availableProcessors();
		// no point having more threads than cells
		nCores = Math.min(nCores, currentGen.length);

		this.setBarrier = new CyclicBarrier(nCores + 1);

		final int cellsPerThread = currentGen.length / nCores;
		
		// nCores - 1 threads all get cellsPerThread of cells to update
		for (int t = 0; t < nCores - 1; t++) {
			final int cellsFrom = cellsPerThread * t;
			final int cellsTo = cellsPerThread * (t + 1);
			updaters.add(new Runnable() {
					public void run() {
						for (int c = cellsFrom; c < cellsTo; c++) {
							nextGen[c] = newState(currentGen[c],
                                                  neighbours(c % size,
                                                             c / size));
                            try {
                                setBarrier.await();
                            } catch (InterruptedException e) {
                                System.out.println("interrupted");
                            } catch (BrokenBarrierException e) {
                                System.out.println("broken barrier");
                            }
						}
                    }
				});
		}

		// the last thread gets the rest of the cells
		final int rest = (nCores - 1) * cellsPerThread;
		updaters.add(new Runnable() {
				public void run() {
					for (int c = rest; c < currentGen.length; c++) {
                        nextGen[c] = newState(currentGen[c],
                                              neighbours(c % size,
                                                         c / size));
                        try {
                            setBarrier.await();
                        } catch (InterruptedException e) {
                            System.out.println("interrupted");
                        } catch (BrokenBarrierException e) {
                            System.out.println("broken barrier");
                        }
					}
				}
			});
	}

    private boolean newState(boolean state, boolean[] neighbours) {
		int aliveNeighbours = 0;
		for (boolean n : neighbours) {
			if (n)
                aliveNeighbours += 1;
		}

		if (state && (aliveNeighbours == 2 || aliveNeighbours ==3))
			return true;
		else if (!state && aliveNeighbours == 3)
			return true;
		else
			return false;
    }

    private void swapBuffers() {
        boolean[] tmp = this.currentGen;
        this.currentGen = this.nextGen;
        this.nextGen = tmp;
    }

	private boolean[] neighbours(int x, int y) {
		boolean[] neighbours = {
			cellAt(x-1, y-1),
			cellAt(x, y-1),
			cellAt(x+1, y-1),
			cellAt(x-1, y),
			cellAt(x+1, y),
			cellAt(x-1, y+1),
			cellAt(x, y+1),
			cellAt(x+1, y+1)
        };
		return neighbours;
	}

}