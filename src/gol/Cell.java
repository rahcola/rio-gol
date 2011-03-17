package gol;

public class Cell {

	private boolean state;
	private boolean newState;
	private Cell[] neighbours;

	public Cell(boolean state) {
		this.state = state;
		this.newState = state;
		this.neighbours = null;
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
	}

	public boolean getState() {
		return this.state;
	}

	public void setState() {
		this.state = this.newState;
	}

}