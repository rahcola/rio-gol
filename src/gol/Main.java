package gol;

public class Main {

	public static void main(String[] args) {
		GoLRules rules = new GoLRules();
		Cell c = new Cell(false, rules);
		Cell[] neighbours = new Cell[8];
		for (int i = 0; i < 5; i++) {
			neighbours[i] = new Cell(false, rules);
		}
		neighbours[5] = new Cell(true, rules);
		neighbours[6] = new Cell(true, rules);
		neighbours[7] = new Cell(true, rules);
		c.setNeighbours(neighbours);
		Cell[] cs = {c};
		GameOfLife game = new GameOfLife(cs);
		game.step();
		System.out.println(c.getState());
	}

}