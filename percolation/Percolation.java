import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // The index of the virtual top site, always 0
    private static final int virtualTop = 0;

    // An UF marking the connections between sites
    // with connectionGrid[0] being the virtual top site
    // and connectionGrid[n^2+1] being the virtual botoom site
    private final WeightedQuickUnionUF connectionGrid;

    // The index of the virtual bottom site
    private final int virtualBottom;

    // The length of the side (size) of the grid
    private final int n;

    // The open status of the sites,
    // site i is open if openStatus[i] is true
    private boolean[] openStatus;

    // The number of open sites
    private int openCount = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // validates argument n
        if (n <= 0) {
            String msg = String.format("n should be <= 0, but %d found", n);
            throw new IllegalArgumentException(msg);
        }

        // initialises the instance variables
        this.n = n;
        this.virtualBottom = n*n + 1;
        this.openStatus = new boolean[n*n+1];
        this.connectionGrid = new WeightedQuickUnionUF(n*n+2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateIndex(row);
        validateIndex(col);

        int site = this.xyTo1D(row, col);

        // skips if the site is already open
        if (this.openStatus[site]) return;

        // opens the site and increments counter
        this.openStatus[site] = true;
        this.openCount++;

        // connects to the virtual top site if the site is in the first row
        if (row == 1) this.connectionGrid.union(this.virtualTop, site);

        // connects to the virtual bottom site if the site is in the last row
        if (row == this.n) this.connectionGrid.union(this.virtualBottom, site);

        /* connects to neighbouring open sites */

        // if there is an open site to the right (row, col+1)
        if (col < this.n && this.isOpen(row, col+1)) {
            int right = this.xyTo1D(row, col+1);
            this.connectionGrid.union(site, right);
        }

        // if there is an open site to the left (row, col-1)
        if (col > 1 && this.isOpen(row, col-1)) {
            int left = this.xyTo1D(row, col-1);
            this.connectionGrid.union(site, left);
        }

        // if there is an open site above (row-1, col)
        if (row > 1 && this.isOpen(row-1, col)) {
            int above = this.xyTo1D(row-1, col);
            this.connectionGrid.union(site, above);
        }

        // if there is an open site below (row+1, col)
        if (row < this.n && this.isOpen(row+1, col)) {
            int below = this.xyTo1D(row+1, col);
            this.connectionGrid.union(site, below);
        }

    }

    /**
     * Is the site (row, col) open?
     * @param row the row co-ordinate
     * @param col the column co-ordinate
     * @return true if open, vice versa
     */
    public boolean isOpen(int row, int col) {
        validateIndex(row);
        validateIndex(col);

        return this.openStatus[this.xyTo1D(row, col)];
    }

    /**
     * Is the site (row, col) full?
     * @param row the row co-ordinate
     * @param col the column co-ordinate
     * @return true if full, vice versa
     */
    public boolean isFull(int row, int col) {
        validateIndex(row);
        validateIndex(col);

        int site = this.xyTo1D(row, col);

        return this.connectionGrid.find(this.virtualTop) == this.connectionGrid.find(site);
    }

    /**
     * Returns the number of open sites
     * @return the number of open sites
     */
    public int numberOfOpenSites() {
        return this.openCount;
    }

    /**
     * Does the system percolate?
     * @return true if the system percolates, vice versa
     */
    public boolean percolates() {
        // the system percolates if the virtual top site is connected to the virtual bottom site
        return this.connectionGrid.find(this.virtualTop) == this.connectionGrid.find(this.virtualBottom);
    }

    /**
     * Maps the (row, col) grid co-ordinates to an 1D index in range [1, n^2]
     * @param row the row co-ordinate
     * @param col the column co-ordinate
     * @return an 1D index in range [1, n^2]
     */
    private int xyTo1D(int row, int col) {
        return (row-1)*this.n + col;
    }

    /**
     * Validates if an index lies in range [1, n],
     * throw an <code>IllegalArgumentException</code> if not
     * @param index a row/col index
     * @throws IllegalArgumentException if an index is outside [1, n]
     */
    private void validateIndex(int index) {
        if (index < 1 || index > this.n) {
            String msg = String.format("Index should be between 1 and %d, but %d is found", this.n, index);
            throw new IllegalArgumentException(msg);
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        // recreating Timing Test 3
        int n = 4096;
        Stopwatch sw = new Stopwatch();

        Percolation perc = new Percolation(n);
        while (!perc.percolates()) {
            int row = StdRandom.uniform(1, n+1);
            int col = StdRandom.uniform(1, n+1);
            perc.open(row, col);
        }

        double time = sw.elapsedTime();
        StdOut.printf("n = %d, seconds = %f", n, time);
    }
}
