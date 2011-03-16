package gol;

public class Cell {

	private volatile boolean alive;
	private boolean newAlive;
	private Cell[] neighbours;

	public Cell(boolean alive) {
		this.alive = alive;
		this.newAlive = alive;
		this.neighbours = null;
	}

	public void setNeighbours(Cell[] neighbours) {
		this.neighbours = neighbours;
	}

	public void calculateNewState() {
		if (this.neighbours == null) {
			return;
		}

		int aliveNeighbours = 0;
		for (Cell n : this.neighbours) {
			if (n.alive())
				aliveNeighbours += 1;
		}

		if (this.alive && (aliveNeighbours == 2 || aliveNeighbours ==3))
			this.newAlive = true;
		else if (!alive && aliveNeighbours == 3)
			this.newAlive = true;
		else
			this.newAlive = false;
	}

	public boolean alive() {
		return this.alive;
	}

	public void setNewState() {
		this.alive = this.newAlive;
	}

}