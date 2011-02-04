package gol;

public class Cell implements Runnable {

	public boolean alive;
	private boolean newAlive;
	private Cell[] neighbours;
	private GoLRules rules;

	public Cell(boolean alive, GoLRules rules) {
		this.alive = alive;
		this.neighbours = null;
		this.rules = rules;
	}

	public void run() {
		if (this.neighbours == null)
			return;

		this.newAlive = calculateNewState();
		this.alive = newAlive;
	}

	public void setNeighbours(Cell[] neighbours) {
		this.neighbours = neighbours;
	}

	public boolean getState() {
		return this.alive;
	}

	private boolean calculateNewState() {
		return this.rules.newState(this, this.neighbours);
	}

}