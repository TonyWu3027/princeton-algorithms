import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private final int[][] tiles;
    private final int n;
    private int blankRow;
    private int blankCol;

    /**
     * Create a board from an n-by-n array of tiles, where tiles[row][col] = tile at (row, col)
     *
     * @param tiles an n-by-n array of tiles
     */
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[this.n][this.n];

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                this.tiles[i][j] = tiles[i][j];

                // find blank (0)
                if (tiles[i][j] == 0) {
                    this.blankRow = i;
                    this.blankCol = j;
                }
            }
        }
    }

    /**
     * String representation of this board.
     *
     * @return The first line contains the board size n;
     * the remaining n lines contains the n-by-n grid of tiles in row-major order,
     * using 0 to designate the blank square.
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.n);

        for (int i = 0; i < this.n; i++) {
            result.append("\n");
            for (int j = 0; j < this.n; j++) {
                result.append(String.format(" %d", this.tiles[i][j]));
            }
        }

        return result.toString();
    }

    /**
     * Returns board dimension n
     *
     * @return board dimension n
     */
    public int dimension() {
        return this.n;
    }

    /**
     * The Hamming distance of the board.
     *
     * @return number of tiles out of place
     */
    public int hamming() {
        int hammingDistance = 0;

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.tiles[i][j] != 0 && this.tiles[i][j] != this.goalPosition(i, j))
                    hammingDistance++;
            }
        }

        return hammingDistance;
    }

    /**
     * Sum of Manhattan distances between tiles and goal
     *
     * @return sum of the vertical and horizontal distances from the tiles to their goal positions
     */
    public int manhattan() {
        int manhattanSum = 0;

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                int num = this.tiles[i][j];
                if (num != 0 && num != this.goalPosition(i, j)) {
                    manhattanSum += Math.abs(((num - 1) / this.n) - i); // vertical distance
                    manhattanSum += Math.abs(((num - 1) % this.n) - j); // horizontal distance
                }
            }
        }

        return manhattanSum;
    }

    /**
     * Is this board the goal board?
     *
     * @return true if is goal, vice versa
     */
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    /**
     * Does this board equal y?
     *
     * @param y another object
     * @return true if
     * <ul>
     * <li>this and y refer the same object</li>
     * <li>OR this and y are both Board
     * <ul>
     *     <li>AND they are have the same size</li>
     *     <li>AND their corresponding tiles are in the same positions</li>
     * </ul>
     * </li>
     * </ul>
     * false otherwise.
     */
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;

        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) return false;
            }
        }

        return true;
    }

    //

    /**
     * All neighboring boards. A neighboring board of this board is obtained
     * by moving one tile towards the blank.
     * @return all neighboring boards
     */
    public Iterable<Board> neighbors() {
        // initialise an empty Stack (an Iterable)
        Stack<Board> neighbours = new Stack<Board>();

        // make 4 copies of the tiles
        int[][] up = new int[this.n][this.n];
        int[][] down = new int[this.n][this.n];
        int[][] left = new int[this.n][this.n];
        int[][] right = new int[this.n][this.n];

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                // copy the titles
                up[i][j] = this.tiles[i][j];
                down[i][j] = this.tiles[i][j];
                left[i][j] = this.tiles[i][j];
                right[i][j] = this.tiles[i][j];
            }
        }

        // there are tiles below the blank
        if (blankRow < this.n - 1) {
            // swap blank and tile below
            up[blankRow][blankCol] = up[blankRow + 1][blankCol];
            up[blankRow + 1][blankCol] = 0;
            // create a board with the updated tiles and push the board to stack
            neighbours.push(new Board(up));
        }

        // there are tiles above the blank
        if (blankRow > 0) {
            // swap blank and tile above
            down[blankRow][blankCol] = down[blankRow - 1][blankCol];
            down[blankRow - 1][blankCol] = 0;
            // create a board with the updated tiles and push the board to stack
            neighbours.push(new Board(down));
        }

        // there are tiles to the right of the blank
        if (blankCol < this.n - 1) {
            // swap blank and tile to the right
            left[blankRow][blankCol] = left[blankRow][blankCol + 1];
            left[blankRow][blankCol + 1] = 0;
            // create a board with the updated tiles and push the board to stack
            neighbours.push(new Board(left));
        }

        // there are tiles to the left of the blank
        if (blankCol > 0) {
            // swap blank and tile to the left
            right[blankRow][blankCol] = right[blankRow][blankCol - 1];
            right[blankRow][blankCol - 1] = 0;
            // create a board with the updated tiles and push the board to stack
            neighbours.push(new Board(right));
        }

        return neighbours;
    }

    /**
     * A board that is obtained by exchanging any pair of tiles.
     *
     * @return the board with any pair of tile exchanged
     */
    public Board twin() {
        int count = 0; // number of valid tiles found, expecting 2
        int[] swapTiles = new int[4]; // the [rowA, colA, rowB, colB] of the tiles to be swapped
        int[][] twinTiles = new int[this.n][this.n];

        // make a copy of the board
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                twinTiles[i][j] = this.tiles[i][j];
            }
        }

        /** find any 2 tiles around the blank **/

        // there are tiles below the blank
        if (blankRow < this.n - 1 && count < 4) {
            swapTiles[count++] = blankRow + 1;
            swapTiles[count++] = blankCol;
        }

        // there are tiles above the blank
        if (blankRow > 0 && count < 4) {
            swapTiles[count++] = blankRow - 1;
            swapTiles[count++] = blankCol;
        }

        // there are tiles to the right of the blank
        if (blankCol < this.n - 1 && count < 4) {
            swapTiles[count++] = blankRow;
            swapTiles[count++] = blankCol + 1;
        }

        // there are tiles to the left of the blank
        if (blankCol > 0 && count < 4) {
            swapTiles[count++] = blankRow;
            swapTiles[count++] = blankCol - 1;
        }

        // swap the two
        twinTiles[swapTiles[0]][swapTiles[1]] = this.tiles[swapTiles[2]][swapTiles[3]];
        twinTiles[swapTiles[2]][swapTiles[3]] = this.tiles[swapTiles[0]][swapTiles[1]];

        return new Board(twinTiles);
    }

    /**
     * Maps the (row, col) co-ordinates to its 1-dimensional goal position,
     * which is the correct answer in tile position (row, col) to an 8 Puzzle.
     *
     * @param row the row co-ordinate ranged [0, n-1]
     * @param col the column co-ordinate ranged [0, n-1]
     * @return the 1D goal position of (row, col) ranged [1, n^2]
     */
    private int goalPosition(int row, int col) {
        return row * this.n + col + 1;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // read filename from command-line input
        In in = new In(args[0]);

        // read input and populate tiles[][]
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        // create a board given the tiles
        Board b = new Board(tiles);

        StdOut.printf("Board:\n%s\n", b);

        StdOut.printf("dimension(): %d, expect %d\n", b.dimension(), n);

        StdOut.printf("hamming(): %d\n", b.hamming());

        StdOut.printf("manhatten(): %d\n", b.manhattan());

        StdOut.printf("isGoal(): %b\n", b.isGoal());

        Board another = new Board(tiles);

        StdOut.printf("equals(another): %b, expect %b\n", b.equals(another), true);

        Iterable<Board> neighbours = b.neighbors();

        StdOut.println("Neighbours:");

        for (Board neighbour : neighbours) StdOut.println(neighbour);

        StdOut.printf("twin():\n%s", b.twin());

    }

}
