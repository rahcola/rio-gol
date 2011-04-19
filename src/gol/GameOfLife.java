package gol;

import java.util.concurrent.CyclicBarrier;

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

    public boolean cellAt(int x, int y) {
        try {
            return current_gen[(y * this.size) + x];
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public int getWidth() {
        return size;
    }

    public int getHeight() {
        return size;
    }

    public void step(final int times) {
        int cores = Runtime.getRuntime().availableProcessors();
        // atleast a row of cells per thread
        cores = Math.min(cores, this.size);
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

    private void swapBuffers() {
        boolean[] tmp = this.current_gen;
        this.current_gen = this.next_gen;
        this.next_gen = tmp;
    }

}