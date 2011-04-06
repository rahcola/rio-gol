package gol;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		GameOfLife game = new Minimal();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			game.step();
		}
		long stop = System.currentTimeMillis();
		System.out.println(stop - start);
		game.shutdown();
        /*
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
        */
	}
}
