package gol;

import java.util.concurrent.Semaphore;

public class Cell implements Runnable {

	public volatile boolean alive;
	private boolean newAlive;
	private Cell[] neighbours;
	private Semaphore signal_cells;
	private Semaphore signal_game;

	public Cell(boolean alive) {
		this.alive = alive;
		this.neighbours = null;
		this.signal_cells = null;
		this.signal_game = null;
	}

	public void run() {
		if (this.neighbours == null ||
			this.signal_cells == null ||
			this.signal_game == null) {
			return;
		}
		//while (true) {
			this.newAlive = calculateNewState();
			this.alive = newAlive;
			//}
	}

	public void setSyncs(Semaphore signal_cells, Semaphore signal_game) {
		this.signal_cells = signal_cells;
		this.signal_game = signal_game;
	}

	public void setNeighbours(Cell[] neighbours) {
		this.neighbours = neighbours;
	}

	public boolean getState() {
		return this.alive;
	}

	private boolean calculateNewState() {
		int aliveNeighbours = 0;
		for (Cell n : this.neighbours) {
			if (n.alive)
				aliveNeighbours += 1;
		}

		if (alive && (aliveNeighbours == 2 || aliveNeighbours ==3))
			return true;
		else if (!alive && aliveNeighbours == 3)
			return true;
		else
			return false;
	}
}