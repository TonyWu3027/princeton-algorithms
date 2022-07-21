import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // an UF with the site 0 being the top (virtual) node
    // and site n+1 being the bottom (virtual) node
    private WeightedQuickUnionUF connected;

    // size of the grid
    private int n;

    // a list of opened sites
    private boolean[] opened;

    // number of open sites
    private int openCount;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // validates n>0
        if (n <= 0) throw new IllegalArgumentException("n should be >0");

        // initialises instance variables
        this.n = n;
        this.connected = new WeightedQuickUnionUF(n*n+2);
        this.opened = new boolean[n*n+1];

        // connects the first row with the top node
        for (int i = 1; i <= n; i++) {
            this.connected.union(i, 0);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // validates indices
        this.validateIndex(row);
        this.validateIndex(col);

        // marks the site open
        int index = this.xyTo1D(row, col);
        this.opened[index] = true;

        // connects to open neighbours
        this.connectOpenNeighbours(row, col);

        // increments the count of open sites
        this.openCount++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        // validates indices
        this.validateIndex(row);
        this.validateIndex(col);

        // checks if the site is open or not
        int index = this.xyTo1D(row, col);
        return this.opened[index];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // validates indices
        this.validateIndex(row);
        this.validateIndex(col);

        // checks if the site is connected to the top site or not.
        // if so, it is full. Vice versa
        int index = this.xyTo1D(row, col);
        return this.connected.find(index) == this.connected.find(0) && this.isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.connected.find(0) == this.connected.find(this.n*this.n+1);
    }

    // maps the coordinate indices to an 1D index
    private int xyTo1D(int row, int col) {
        return this.n*(row-1) + col;
    }

    // validates whether a col or row is within prescribed range or not
    private void validateIndex(int i) {
        String msg = String.format("Index out of bound %d", i);
        if (i <= 0 || i > this.n) throw new IllegalArgumentException(msg);
    }

    // connects a site to its open neighbours
    private void connectOpenNeighbours(int row, int col) {
        int centre = this.xyTo1D(row, col);
        int up = this.xyTo1D(row-1, col);
        int down = this.xyTo1D(row+1, col);
        int left = this.xyTo1D(row, col-1);
        int right = this.xyTo1D(row, col+1);

        if (col == 1) {
            // connects to right neighbour if the right is open
            if (this.isOpen(row, col+1)) this.connected.union(centre, right);
        } else if (col == this.n) {
            // connects to left neighbour if the left is open
            if (this.isOpen(row, col-1)) this.connected.union(centre, left);
        } else {
            // connects to left/right  neighbour if the left/right is open
            if (this.isOpen(row, col+1)) this.connected.union(centre, right);
            if (this.isOpen(row, col-1)) this.connected.union(centre, left);
        }

        if (row == 1) {
            // connects to down neighbour if the down is open
            if (this.isOpen(row+1, col)) this.connected.union(centre, down);
        } else if (row == this.n) {
            // connects to up neighbour if the up is open
            if (this.isOpen(row-1, col)) this.connected.union(centre, up);
        } else {
            // connects to up/down  neighbour if the up/down is open
            if (this.isOpen(row+1, col)) this.connected.union(centre, down);
            if (this.isOpen(row-1, col)) this.connected.union(centre, up);
        }

        // Special treatment to prevent backwash
        // connects a site in the last row to the bottom node
        // ONLY if it has a neighbour(s) (up, left, right) that is connected to the top node
        if (row == this.n) {
            if (col == 1) {
                // bottom-left
                if (this.isFull(row-1, col) || this.isFull(row, col+1)) this.connected.union(centre, this.n*this.n+1);
            } else if (col == this.n) {
                // bottom-right
                if (this.isFull(row-1, col) || this.isFull(row, col-1)) this.connected.union(centre, this.n*this.n+1);
            } else {
                // any other bottom sites
                if (this.isFull(row - 1, col) || this.isFull(row, col - 1) || this.isFull(row, col
                        + 1)) {
                    this.connected.union(centre, this.n * this.n + 1);
                }
            }
        }

    }

    // test client (optional)
    public static void main(String[] args) { }
}
