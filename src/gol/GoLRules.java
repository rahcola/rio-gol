package gol;

public class GoLRules {

	public GoLRules() {};

	public boolean newState(Cell cell, Cell[] neighbours) {
		int aliveNeighbours = 0;
		for (Cell n : neighbours) {
			if (n.getState())
				aliveNeighbours += 1;
		}

		if (cell.getState() && (aliveNeighbours == 2 || aliveNeighbours ==3))
			return true;
		else if (!cell.getState() && aliveNeighbours == 3)
			return true;
		else
			return false;
	}
}