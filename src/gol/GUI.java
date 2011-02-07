package gol;

import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

public class GUI extends JPanel {

	final int MARGIN = 10;

	private GameOfLife gol;

	public GUI(GameOfLife gol) {
		this.gol = gol;
		this.addMouseListener(new Mouse());
	}

	public Dimension getPreferedSize() {
		return new Dimension((10 * this.gol.getSize()) + (2 * MARGIN),
							 (10 * this.gol.getSize()) + (2 * MARGIN));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int y;
		int x;
		for (y = 0; y < this.gol.getSize(); y++) {
			for (x = 0; x < this.gol.getSize(); x++) {
				if (this.gol.cellAt(x, y).alive) {
					g.setColor(Color.GRAY);
				} else {
					g.setColor(Color.BLACK);
				}
				g.fillRect((MARGIN + 10*x),
						   (MARGIN + 10*y),
						   10,
						   10);
				g.setColor(Color.BLACK);
			}
		}
	}

	class Mouse extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				gol.step();
				repaint();
			} catch (Exception ex) {
				return;
			}
		}
	}

}