package gol;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

public class Cell {

	private boolean state;
	private boolean newState;
	private Cell[] neighbours;
	private CyclicBarrier barrier;

	public Cell(boolean state, CyclicBarrier barrier) {
		this.state = state;
		this.newState = state;
		this.neighbours = null;
		this.barrier = barrier;
	}

	public void setNeighbours(Cell[] neighbours) {
		this.neighbours = neighbours;
	}

	public void calcState() {
		if (this.neighbours == null) {
			return;
		}

		int aliveNeighbours = 0;
		for (Cell n : this.neighbours) {
			if (n.getState())
				aliveNeighbours += 1;
		}

		if (this.state && (aliveNeighbours == 2 || aliveNeighbours ==3))
			this.newState = true;
		else if (!this.state && aliveNeighbours == 3)
			this.newState = true;
		else
			this.newState = false;
		try {
			this.barrier.await();
		} catch (InterruptedException e) {
			System.out.println("thread interrupted");
			return;
		} catch (BrokenBarrierException e) {
			System.out.println("broken barrier");
			return;
		}
		this.state = this.newState;
	}

	public boolean getState() {
		return this.state;
	}

}