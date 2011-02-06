package gol;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

import java.util.concurrent.Semaphore;

public class GameOfLife extends JPanel {

	final int MARGIN = 10;

	private Semaphore signal_cells;
	private Semaphore signal_game;
	private Cell[][] cells;

	public GameOfLife(Cell[][] cells) {
		this.cells = cells;
		this.signal_cells = new Semaphore(0);
		this.signal_game = new Semaphore(0);
		for (Cell[] row : this.cells) {
			for (Cell cell : row) {
				cell.setSyncs(this.signal_cells, this.signal_game);
			}
		}
		for (Cell[] row: this.cells) {
			for (Cell cell : row) {
				new Thread(cell).start();
			}
		}
	}

	public void step() throws InterruptedException {
		this.repaint();
	}

	public Dimension getPreferedSize() {
		return new Dimension((10 * this.cells.length) + (2 * MARGIN),
							 (10 * this.cells[0].length) + (2 * MARGIN));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int y;
		int x;
		for (y = 0; y < this.cells.length; y++) {
			for (x = 0; x < this.cells[y].length; x++) {
				if (this.cells[y][x].alive) {
					g.setColor(Color.RED);
				} else {
					g.setColor(Color.BLUE);
				}
				g.fillRect((MARGIN + 10*x),
						   (MARGIN + 10*y),
						   10,
						   10);
				g.setColor(Color.BLACK);
			}
		}
	}
}