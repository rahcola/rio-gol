package gol;

import java.util.concurrent.CyclicBarrier;

/*
  Double buffered approach with flat boolean arrays.

  Current generation is held in one buffer and the next generation is
  calculated to another. When the calculations are complete, swap the
  array references so that current_gen will always hold to most
  up-to-date generation. This ensures that the next generation is
  calculated from a single current generation, held in current_gen.

  Parallelization is done using shared memory in form of current_gen and
  next_gen. Synchronization barriers are used to signal the swap of
  buffers.
  */
public class GameOfLife {

    private boolean[] current_gen;
    private boolean[] next_gen;
    private int size;

    public GameOfLife(boolean[][] cells) {
        this.size = cells.length;
        this.current_gen = new boolean[size * size];
        this.next_gen = new boolean[size * size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                current_gen[(y * size) + x] = cells[y][x];
            }
        }
    }

    /*
      Indexing a flat buffer with matrix coordinates requires a simple
      calculation. Assume cells outside the buffer dead.
    */
    public boolean cellAt(int x, int y) {
        if (x >= this.size || y >= this.size || x < 0 || y < 0) {
            return false;
        }
        return current_gen[(y * this.size) + x];
    }

    /* For visualizationing with a GUI */
    public int getWidth() {
        return size;
    }

    public int getHeight() {
        return size;
    }

    /*
      Calculate the life _times_ generations forward.

      For efficiency, generations should be calculated in batches so
      that threads are created only once. Give one thread atleast a row
      of cells to update, as the update operation is rather fast and
      threads are memory intensive.

      Synchronize using two barries, one to signal that the new
      generation is calculated and the buffers can be swapped, and
      another one to signal that the buffers are swapped and the
      calculations can continue.
    */
    public void step(final int times, int cores) {
        if (cores == 0) {
            cores = Runtime.getRuntime().availableProcessors();
        }
        // atleast a row of cells per thread
        cores = Math.min(cores, this.size);
        if (cores == 1) {
            serialStep(times);
            return;
        }
        // for syncing when to swap buffers
        final CyclicBarrier swapBarrier = new CyclicBarrier(cores + 1);
        // for syncing that the buffers have been swapped
        final CyclicBarrier swappedBarrier = new CyclicBarrier(cores + 1);

        Thread[] threads = new Thread[cores];
        final int rows_per_thread = this.size / cores;
        
        int row = 0;
        for (int c = 0; c < cores; c++) {
            final int from = row;
            final int to = Math.min(row + rows_per_thread, this.size);
            threads[c] = new Thread(new Runnable() {
                    public void run() {
                        for (int t = 0; t < times; t++) {
                            for (int y = from; y < to; y++) {
                                for (int x = 0; x < size; x++) {
                                    next_gen[(y * size) + x] = newState(x, y);
                                }
                            }
                            try {
                                swapBarrier.await();
                                swappedBarrier.await();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                });
            threads[c].start();
            row += rows_per_thread;
        }
        
        for (int t = 0; t < times; t++) {
            try {
                swapBarrier.await();
                swapBuffers();
                swappedBarrier.await();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /*
      Calculate the life _times_ generations forward.
      
      This method is for verification and for use in single core enviroments.
    */
    public void serialStep(int times) {
        int i = 0;
        for (int t = 0; t < times; t++) {
            for (int y = 0; y < this.size; y++) {
                for (int x = 0; x < this.size; x++) {
                    i = (y * this.size) + x;
                    this.next_gen[i] = newState(x, y);
                }
            }
            swapBuffers();
        }
    }

    /*
      Calculate new state based on the current state of the cell and
      its eight neighbours.

      Cell dead or alive with three neighbours alive lives.
      Cell alive with two neighbours alive lives.
      Every other cell dies.
    */
    private boolean newState(int x, int y) {
        boolean state = current_gen[(y * size) + x];
        int alive = 0;
        for (boolean n : neighbours(x, y)) {
            if (n)
                alive += 1;
        }

        if (state && (alive == 2 || alive ==3))
            return true;
        else if (!state && alive == 3)
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

    /* Swap the buffer references */
    private void swapBuffers() {
        boolean[] tmp = this.current_gen;
        this.current_gen = this.next_gen;
        this.next_gen = tmp;
    }

    /* Two games are equal if they contain the exact same life in current_gen. */
    public boolean equals(GameOfLife other) {
        if (this.size != other.size) {
            return false;
        }

        int i = 0;
        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                i = (y * this.size) + x;
                if (this.current_gen[i] != other.current_gen[i]) {
                    return false;
                }
            }
        }
        return true;
    }

}
