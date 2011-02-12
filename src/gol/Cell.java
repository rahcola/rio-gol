package gol;

public class Cell {

	public volatile boolean alive;
	private boolean newAlive;
	private Cell[] neighbours;

	public Cell(boolean alive) {
		this.alive = alive;
		this.neighbours = null;
	}

	public void setNeighbours(Cell[] neighbours) {
		this.neighbours = neighbours;
	}

	public void calculateNewState() throws Exception {
		if (this.neighbours == null) {
			throw new Exception("cell not initialized");
		}

		int aliveNeighbours = 0;
		for (Cell n : this.neighbours) {
			if (n.alive)
				aliveNeighbours += 1;
		}

		if (alive && (aliveNeighbours == 2 || aliveNeighbours ==3))
			this.newAlive = true;
		else if (!alive && aliveNeighbours == 3)
			this.newAlive = true;
		else
			this.newAlive = false;
	}

	public void setNewState() {
		this.alive = this.newAlive;
	}

	public boolean equals(Cell other) {
		return this.alive == other.alive;
	}
}