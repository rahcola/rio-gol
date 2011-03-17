package gol;

public class GameOfLife {

	private int width;
	private int height;
	private boolean[] currentGen;
	private boolean[] nextGen;

	public GameOfLife(boolean[][] cells) {
		this.width = cells[0].length;
		this.height = cells.length;
		this.currentGen = new boolean[this.width * this.height];
		this.nextGen = new boolean[this.width * this.height];
		for (int y = 0; y < this.height; ++y) {
			for (int x = 0; x < this.width; ++x) {
				currentGen[(y * this.width) + x] = cells[y][x];
			}
		}
	}

	public boolean cellAt(int x, int y) {
		try {
			return currentGen[(y * this.width) + x];
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void step() {
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				nextGen[(y * width) + x] = newState(x, y);
			}
		}
		swapBuffers();
	}

	private void swapBuffers() {
		boolean[] tmp = currentGen;
		currentGen = nextGen;
		nextGen = tmp;
	}

	private boolean newState(int x, int y) {
		boolean alive = cellAt(x, y);

		int liveNeighbours = 0;
		for (boolean n : neighbours(x, y)) {
			if (n)
				liveNeighbours += 1;
		}
		if (alive && (liveNeighbours == 2 || liveNeighbours ==3))
			return true;
		else if (!alive && liveNeighbours == 3)
			return true;
		else
			return false;
	}

	private boolean[] neighbours(int x, int y) {
		boolean[] neighbours = {
			cellAt(x-1, y-1),
			cellAt(x, y-1),
			cellAt(x+1, y-1),
			cellAt(x-1, y),
			cellAt(x+1, y),
			cellAt(x-1, y+1),
			cellAt(x, y+1),
			cellAt(x+1, y+1)};
		return neighbours;
	}

	/*
	private Semaphore start_calc;
	private Semaphore calc_ready;
	private Semaphore start_set;
	private Semaphore set_ready;
	private Cell[][] cells;
	private ArrayList<CellGroup> groups;
	private LinkedList<Thread> threads;

	public GameOfLife(Cell[][] cells) {
		this.cells = cells;
		this.start_calc = new Semaphore(0);
		this.calc_ready = new Semaphore(0);
		this.start_set = new Semaphore(0);
		this.set_ready = new Semaphore(0);
		this.groups = new ArrayList<CellGroup>();
		this.threads = new LinkedList<Thread>();

		splitCellsForProcessors();
		for (CellGroup group : this.groups) {
			Thread thread = new Thread(group);
			this.threads.add(thread);
			thread.start();
		}
	}

	private void splitCellsForProcessors() {
		int nProcessors = Runtime.getRuntime().availableProcessors();
		if (nProcessors > getWidth() * getHeight()) {
			//no point having more threads than cells
			nProcessors = getWidth() * getHeight();
		}

		for (int i = 0; i < nProcessors; i++) {
			this.groups.add(new CellGroup(start_calc,
										  calc_ready,
										  start_set,
										  set_ready));
		}

		int group = 0;
		for (Cell[] row : this.cells) {
			for (Cell cell : row) {
				this.groups.get(group % nProcessors).addCell(cell);
				group += 1;
			}
		}
	}

	public void step() {
		this.start_calc.release(groups.size());
		this.calc_ready.acquireUninterruptibly(groups.size());
		this.start_set.release(groups.size());
		this.set_ready.acquireUninterruptibly(groups.size());
	}

	public void stop() {
		for (CellGroup group : this.groups) {
			group.stop();
		}
		for (Thread thread : this.threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				System.out.println("thread was interrupted");
			}
		}
	}

	public int getWidth() {
		return cells[0].length;
	}

	public int getHeight() {
		return cells.length;
	}

	public Cell cellAt(int x, int y) {
		return cells[y][x];
	}
	*/
}