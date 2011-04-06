package gol;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.LinkedList;

public class GameOfLife {

	private int size;
	private Cell[] currentGen;
    private LinkedList<LinkedList<Cell>> cell_lists;
    private Thread[] updaters;
    private CyclicBarrier setBarrier;
    private CyclicBarrier stepBarrier;

	public GameOfLife(boolean[] cells, int size) {
        this.size = size;
		this.currentGen = new Cell[this.size * this.size];

		for (int y = 0; y < this.size; ++y) {
			for (int x = 0; x < this.size; ++x) {
                int c = (y * this.size) + x;
				currentGen[c] = new Cell(cells[c]);
			}
		}
		for (int y = 0; y < this.size; ++y) {
			for (int x = 0; x < this.size; ++x) {
				currentGen[(y * this.size) + x].setNeighbours(neighbours(x, y));
			}
		}
        splitCells();
	}

	public Cell cellAt(int x, int y) {
		try {
			return currentGen[(y * this.size) + x];
		} catch (IndexOutOfBoundsException e) {
			return new Cell(false);
		}
	}

	public int getWidth() {
		return this.size;
	}

	public int getHeight() {
		return this.size;
	}

    public void step(final int times) {
        for (int i = 0; i < updaters.length; i++) {
            final int ii = i;
            Thread t = new Thread(new Runnable() {
                    public void run() {
                        for (int n = 0; n < times; n++) {
                            for (Cell c : cell_lists.get(ii)) {
                                c.calcState();
                            }
                            try {
                                setBarrier.await();
                            } catch (InterruptedException e) {
                                System.out.println("interrupted");
                            } catch (BrokenBarrierException e) {
                                System.out.println("broken barrier");
                            }

                            for (Cell c : cell_lists.get(ii)) {
                                c.setState();
                            }
                            try {
                                stepBarrier.await();
                            } catch (InterruptedException e) {
                                System.out.println("interrupted");
                            } catch (BrokenBarrierException e) {
                                System.out.println("broken barrier");
                            }
                        }
                    }
                });
            t.start();
            this.updaters[i] = t;
        }

        for (Thread t : this.updaters) {
            System.out.println("joining threads");
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
        }
    }

    private void splitCells() {
        int nCores = Runtime.getRuntime().availableProcessors();
        // no point having more threads than cells
        nCores = Math.min(nCores, currentGen.length);
        this.updaters = new Thread[nCores];
        this.cell_lists = new LinkedList<LinkedList<Cell>>();
        
        for (int i = 0; i < nCores; i++) {
            cell_lists.add(new LinkedList<Cell>());
        }
        for (int i = 0; i < this.currentGen.length; i++) {
            cell_lists.get(i % nCores).add(currentGen[i]);
        }

        this.setBarrier = new CyclicBarrier(nCores);
        this.stepBarrier = new CyclicBarrier(nCores);
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