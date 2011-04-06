package gol;

public class Cell {

    private boolean state;
    private boolean new_state;
	private Cell[] neighbours;

	public Cell(boolean state) {
        this.state = state;
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
            this.new_state = true;
		else if (!this.state && aliveNeighbours == 3)
            this.new_state = true;
		else
            this.new_state = false;
	}

	public boolean getState() {
		return this.state;
	}

    public void setState() {
        this.state = this.new_state;
    }

}