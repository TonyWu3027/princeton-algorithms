import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph G;

    /**
     * Constructor takes a digraph (not necessarily a DAG)
     *
     * @param G a directed graph (digraph)
     */
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("The digraph should not be null");

        this.G = new Digraph(G);

        // TODO: cache the lengths and ancestors
    }

    /**
     * Length of the shortest ancestral path between v and w; -1 if no such path
     *
     * @param v a vertex v in the digraph
     * @param w a vertex w in the digraph
     * @return length of the shortest ancestral path between v and w; -1 if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int length(int v, int w) {
        this.validate(v);
        this.validate(w);

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(this.G, w);

        return this.length(vBFS, wBFS);
    }


    /**
     * Length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no
     * such path
     *
     * @param v a set of vertices v in the digraph
     * @param w a set of vertices w in the digraph
     * @return length of the shortest ancestral path between any vertex in v and any vertex in w; -1
     * if no such path
     * @throws IllegalArgumentException unless v and w are not null and for every vertex i in v and
     *                                  w {@code 0 <= i < V}
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("The arguments should not be null");

        this.validate(v);
        this.validate(w);

        // no such path if either iterable is empty
        if (this.isEmpty(v) || this.isEmpty(w)) return -1;

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(this.G, w);

        return this.length(vBFS, wBFS);
    }

    private int length(BreadthFirstDirectedPaths vBFS, BreadthFirstDirectedPaths wBFS) {
        int distance;
        int minDistance = Integer.MAX_VALUE;

        // traverse all vertices that are connected to both v and w
        // to find the vertex with minimal sum of distances to v and to w
        for (int i = 0; i < this.G.V(); i++) {
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                distance = vBFS.distTo(i) + wBFS.distTo(i);

                if (distance < minDistance) minDistance = distance;
            }
        }

        return minDistance == Integer.MAX_VALUE ? -1 : minDistance;
    }

    /**
     * A common ancestor of v and w that participates in the shortest ancestral path; -1 if no such
     * path
     *
     * @param v a vertex v in the digraph
     * @param w a vertex w in the digraph
     * @return a common ancestor of v and w that participates in the shortest ancestral path; -1 if no
     * such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int ancestor(int v, int w) {
        this.validate(v);
        this.validate(w);

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(this.G, w);

        return this.ancestor(vBFS, wBFS);
    }


    /**
     * A common ancestor that participates in the shortest ancestral path; -1 if no such path
     *
     * @param v a set of vertices v in the digraph
     * @param w a set of vertices w in the digraph
     * @return a common ancestor that participates in the shortest ancestral path; -1 if no such path
     * @throws IllegalArgumentException unless v and w are not null and for every vertex i in v and
     *                                  w {@code 0 <= i < V}
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("The arguments should not be null");

        this.validate(v);
        this.validate(w);

        // no such path if either iterable is empty
        if (this.isEmpty(v) || this.isEmpty(w)) return -1;

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(this.G, w);

        return this.ancestor(vBFS, wBFS);
    }

    private int ancestor(BreadthFirstDirectedPaths vBFS, BreadthFirstDirectedPaths wBFS) {
        int distance;
        int minDistance = Integer.MAX_VALUE;
        int minAncestor = -1;

        // traverse all vertices that are connected to both v and w
        // to find the vertex with minimal sum of distances to v and to w
        for (int i = 0; i < this.G.V(); i++) {
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                distance = vBFS.distTo(i) + wBFS.distTo(i);

                if (distance < minDistance) {
                    minDistance = distance;
                    minAncestor = i;
                }
            }
        }

        return minAncestor;
    }

    /**
     * Is a given vertex within the prescribed range of the digraph?
     *
     * @param v a given vertex
     * @throws IllegalArgumentException unless the {@code v} is not null and {@code 0 <= v < V}
     */
    private void validate(Integer v) {
        if (v == null) throw new IllegalArgumentException("Vertex is null");

        if (v < 0 || v >= this.G.V()) {
            String msg = String.format("Vertex %d is not between 0 and %d", v, this.G.V() - 1);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Is a given vertex iterable not empty and
     * is each vertex within the prescribed range of the digraph?
     *
     * @param vertices an iterable of vertex
     * @throws IllegalArgumentException unless each vertex is not null and {@code 0 <= v < V} and
     *                                  the iterable is not empty or null
     */
    private void validate(Iterable<Integer> vertices) {
        if (vertices == null) throw new IllegalArgumentException("Vertices iterable is null");

        // check each vertex
        for (Integer vertex : vertices) {
            validate(vertex);
        }
    }

    /**
     * Is an {@code Iterable} empty?
     *
     * @param i an iterable
     * @return true if empty; vice versa
     */
    private boolean isEmpty(Iterable<Integer> i) {
        return !i.iterator().hasNext();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
