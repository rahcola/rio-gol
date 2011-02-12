package gol;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

import gol.oscillators.Blinker;

public class Main {

	public static void main(String[] args) {
		System.out.println(Blinker.test());
		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JFrame frame = new JFrame();
					GameOfLife game = new Minimal();
					GUI gui = new GUI(game);
					frame.add(gui);
					frame.pack();
					frame.setVisible(true);
				}
			});
	}

}