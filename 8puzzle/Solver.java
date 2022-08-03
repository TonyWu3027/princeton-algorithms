import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private final Stack<Board> solutionSequence;
    private int moves;
    private boolean isSolvable;

    /**
     * Find a solution to the initial board (using the A* algorithm)
     * @param initial the puzzle board to solve
     */
    public Solver(Board initial) {
        // validate input
        if (initial == null) throw new IllegalArgumentException("initial board should not be null");

        solutionSequence = new Stack<>();
        MinPQ<SearchNode> initialPQ = new MinPQ<>(); // PQ to solve the initial board
        MinPQ<SearchNode> twinPQ = new MinPQ<>(); // PQ to solve the twin of the initial board

        // insert the initial nodes
        initialPQ.insert(new SearchNode(initial, 0, null));
        twinPQ.insert(new SearchNode(initial.twin(), 0, null));

        /**
         * Solve the initial board and its twin simultaneously.
         * If the twin is solved first, then it suggests that
         * the initial board is unsolvable
         */

        SearchNode node = initialPQ.delMin();
        SearchNode twinNode = twinPQ.delMin();
        while (!node.board.isGoal() && !twinNode.board.isGoal()) {
            // insert SearchNodes of its neighbours' to the PQ
            for (Board neighbour: node.board.neighbors()) {
                // ignore seen boards
                if (node.prev == null || !neighbour.equals(node.prev.board)) {
                    initialPQ.insert(new SearchNode(neighbour, node.moves + 1, node));
                }
            }
            node = initialPQ.delMin();

            // same procedure for the twin boards
            for (Board neighbour: twinNode.board.neighbors()) {
                if (twinNode.prev == null || !neighbour.equals(twinNode.prev.board)) {
                    twinPQ.insert(new SearchNode(neighbour, twinNode.moves + 1, twinNode));
                }
            }
            twinNode = twinPQ.delMin();
        }

        if (twinNode.board.isGoal()) {
            // if twin is solved, then the initial board is unsolvable
            this.isSolvable = false;
        } else {
            // the initial board is solved successfully
            this.isSolvable = true;
            this.moves = node.moves;

            // recreate the solution by back-tracing the dequeued nodes
            while (node != null) {
                solutionSequence.push(node.board);
                node = node.prev;
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {

        Board board;
        int moves;
        int manhattan;
        SearchNode prev;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.manhattan = board.manhattan();
        }

        /**
         * Compares the priority of this SearchNode against another.
         * The priority is the moves made to this board so for plus the manhattan distance
         * of this board.
         * @param that the object to be compared.
         * @return a positive integer if this has a higher priority over that,
         * a negative integer this has a lower priority over that,
         * 0 if the priorities are equal.
         */
        public int compareTo(SearchNode that) {
            return (this.moves + this.manhattan) - (that.moves + that.manhattan);
        }
    }

    /**
     * Is the initial board solvable?
     * @return true if the initial board is solvable, vice versa
     */
    public boolean isSolvable() {
        return this.isSolvable;
    }

    /**
     * Min number of moves to solve initial board; -1 if unsolvable
     * @return min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        if (!this.isSolvable) return -1;
        return this.moves;
    }

    /**
     * Sequence of boards in a shortest solution; null if unsolvable
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!this.isSolvable) return null;
        return this.solutionSequence;
    }

    // test client
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
