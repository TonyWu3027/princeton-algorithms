import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;

    private int n;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }

    /**
     * Construct an empty set of points
     */
    public KdTree() {
        this.n = 0;
    }

    /**
     * Is the set empty?
     * @return true if empty, vice versa
     */
    public boolean isEmpty() {
        return this.n == 0;
    }

    /**
     * Number of points in the set
     * @return number of points in the set
     */
    public int size() {
        return this.n;
    }

    /**
     * Add the point to the set (if it is not already in the set)
     * @param p the point to be added
     * @throws IllegalArgumentException if null is given
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point should not be null");

        if (contains(p)) return;

        RectHV rect = new RectHV(0, 0, 1, 1);

        this.root = recursivelyInsert(this.root, p, false, rect);
    }

    /**
     * Insert a point to the 2d tree recursively
     * @param at the current node to check
     * @param p the point to insert
     * @param odd whether the 0-indexed number of the level in the 2d tree <code>at</code> is at is
     *            odd or not. (E.g. <code>odd</code> is <code>false</code> when <code>at</code> is the root
     *            but <code>true</code> for its children)
     * @param rect the axis-aligned rectangle correspond to the node
     */
    private Node recursivelyInsert(Node at, Point2D p, boolean odd, RectHV rect) {
        // Insert at the child of a leaf (null link)
        if (at == null) {
            this.n++;
            return new Node(p, rect, null, null);
        }

        RectHV nextRect;

        if (odd) {
            // check y co-ord at odd level
            if (p.y() < at.p.y()) {
                // the next rect is the lower sub rect of the current rect
                // divided by the y co-ord
                nextRect = new RectHV(at.rect.xmin(), at.rect.ymin(), at.rect.xmax(), at.p.y());
                at.lb = recursivelyInsert(at.lb, p, false, nextRect);
            } else {
                // the next rect is the upper sub rect of the current rect
                // divided by the y co-ord
                nextRect = new RectHV(at.rect.xmin(), at.p.y(), at.rect.xmax(), at.rect.ymax());
                at.rt = recursivelyInsert(at.rt, p, false, nextRect);
            }
        } else {
            // check x co-ord at even level
            if (p.x() < at.p.x()) {
                // the next rect is the left sub rect of the current rect
                // divided by the x co-ord
                nextRect = new RectHV(at.rect.xmin(), at.rect.ymin(), at.p.x(), at.rect.ymax());
                at.lb = recursivelyInsert(at.lb, p, true, nextRect);
            } else {
                // the next rect is the right sub rect of the current rect
                // divided by the x co-ord
                nextRect = new RectHV(at.p.x(), at.rect.ymin(), at.rect.xmax(), at.rect.ymax());
                at.rt = recursivelyInsert(at.rt, p, true, nextRect);
            }
        }

        return at;
    }

    /**
     * Does the set contain point p?
     * @param p the point to be checked
     * @return true if contains, vice versa
     * @throws IllegalArgumentException if null is given
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point should not be null");
        if (this.isEmpty()) return false;
        return recursivelyContains(this.root, p, false);
    }

    /**
     * Search a point to the 2d tree recursively
     * @param at the current node to check
     * @param p the target point
     * @param odd whether the 0-indexed number of the level in the 2d tree <code>at</code> is at is
     *            odd or not. (E.g. <code>odd</code> is <code>false</code> when <code>at</code> is the root
     *            but <code>true</code> for its children)
     * @return true if <code>p</code> exists in the 2d tree, vice versa
     */
    private boolean recursivelyContains(Node at, Point2D p, boolean odd) {
        // p does not exist when nodes are run out
        if (at == null) return false;

        if (at.p.equals(p)) return true;

        if (odd) {
            // check y co-ord at odd level
            if (p.y() < at.p.y()) return recursivelyContains(at.lb, p, false);
            else return recursivelyContains(at.rt, p, false);
        } else {
            // check x co-ord at even level
            if (p.x() < at.p.x()) return recursivelyContains(at.lb, p, true);
            else return recursivelyContains(at.rt, p, true);
        }
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        recursivelyDraw(this.root, false);
    }

    /**
     * Draw all points and their subdivisions in a 2d tree inorder recursively
     * @param at the current node to check
     * @param odd whether the 0-indexed number of the level in the 2d tree <code>at</code> is at is
     *            odd or not. (E.g. <code>odd</code> is <code>false</code> when <code>at</code> is the root
     *            but <code>true</code> for its children)
     */
    private void recursivelyDraw(Node at, boolean odd) {
        if (at == null) return;

        double x = at.p.x();
        double y = at.p.y();

        // draw the node itself
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(x, y);

        // draw the splits
        // red for vertical splits and blue for horizontal splits
        StdDraw.setPenRadius(0.005);
        if (odd) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(at.rect.xmin(), y, at.rect.xmax(), y);
        } else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x, at.rect.ymin(), x, at.rect.ymax());
        }

        // draw left subtree
        recursivelyDraw(at.lb, !odd);
        // draw right subtree
        recursivelyDraw(at.rt, !odd);
    }

    /**
     * All points that are inside the rectangle (or on the boundary)
     * @param rect the rectangle to be checked against
     * @return all points that are inside the rectangle (or on the boundary)
     * @throws IllegalArgumentException if null is given
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle should not be null");

        Stack<Point2D> inside = new Stack<>();

        recursivelyRange(this.root, rect, inside);

        return inside;
    }

    /**
     * Recursively traverse all points in a 2d tree and push all points that are contained
     * by the query rectangle to the given stack
     * @param at the current node of the 2d tree
     * @param query the query rectangle
     * @param inside a stack of such points
     */
    private void recursivelyRange(Node at, RectHV query, Stack<Point2D> inside) {
        // if the corresponding rect at the node does not intersect the query rect,
        // there is no need to explore that node and its subtree
        if (!at.rect.intersects(query)) return;

        // if the current point is in the query rectangle,
        // push to stack
        if (query.contains(at.p)) inside.push(at.p);

        // check left subtree is there is left
        if (at.lb != null) recursivelyRange(at.lb, query, inside);

        // check right subtree is there is left
        if (at.rt != null) recursivelyRange(at.rt, query, inside);
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     * @param p the point to be checked against
     * @return a nearest neighbor in the set to point p; null if the set is empty
     * @throws IllegalArgumentException if null is given
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point should not be null");
        if (this.isEmpty()) return null;

        return recursivelyNearest(this.root, this.root, p).p;
    }

    private Node recursivelyNearest(Node at, Node closest, Point2D query) {
        // if the closest point discovered so far is closer than the distance between
        // the query point and the rectangle corresponding to a node,
        // there is no need to explore that node and its subtree
        if (query.distanceSquaredTo(closest.p) < at.rect.distanceSquaredTo(query)) return closest;

        // update closest if current node is closer
        if (query.distanceSquaredTo(at.p) < query.distanceSquaredTo(closest.p)) closest = at;

        // if not both children are null
        if (!(at.lb == null && at.rt == null)) {
            // traverse right subtree if left is null
            if (at.lb == null) return recursivelyNearest(at.rt, closest, query);
            // traverse left subtree if right is null
            else if (at.rt == null) return recursivelyNearest(at.lb, closest, query);
            // if both subtrees exist
            else {
                // query left subtree first if query is on the same side (contained by left's rect)
                if (at.lb.rect.contains(query)) {
                    Node nextClosest = recursivelyNearest(at.lb, closest, query);
                    return recursivelyNearest(at.rt, nextClosest, query);
                }
                // else (if query is on the right child's side or sitting on the boundary)
                // query right subtree first
                else {
                    Node nextClosest = recursivelyNearest(at.rt, closest, query);
                    return recursivelyNearest(at.lb, nextClosest, query);
                }
            }
        }

        // return closest at leaf, the leaf is checked against as well
        return closest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.5, 0.5));
    }
}
