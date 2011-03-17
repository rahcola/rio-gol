package gol;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

public class GUI extends JPanel implements ActionListener {

	final int MARGIN = 0;
	final int CELL_SIZE = 5;

	private GameOfLife gol;
	private boolean updating;
	private Timer timer;

	public GUI(GameOfLife gol) {
		this.gol = gol;
		this.updating = false;
		this.timer = new Timer(100, this);
		this.addMouseListener(new Mouse());
	}

	public void actionPerformed(ActionEvent e) {
		this.gol.step();
		this.repaint();
	}

	public Dimension getPreferedSize() {
		return new Dimension((CELL_SIZE * this.gol.getWidth()) + (2 * MARGIN),
							 (CELL_SIZE * this.gol.getHeight()) + (2 * MARGIN));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (int y = 0; y < this.gol.getHeight(); y++) {
			for (int x = 0; x < this.gol.getWidth(); x++) {
				if (this.gol.cellAt(x, y).getState()) {
					g.setColor(Color.GRAY);
				} else {
					g.setColor(Color.BLACK);
				}
				g.fillRect((MARGIN + CELL_SIZE*x),
						   (MARGIN + CELL_SIZE*y),
						   CELL_SIZE,
						   CELL_SIZE);
				g.setColor(Color.BLACK);
			}
		}
	}

	class Mouse extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (updating) {
				timer.stop();
				updating = false;
			} else {
				updating = true;
				timer.start();
			}
		}
	}

}