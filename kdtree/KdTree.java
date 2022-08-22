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
        private boolean isVertical; // whether this node is vertical or not

        public Node(Point2D p, RectHV rect, boolean isVertical) {
            this.p = p;
            this.rect = rect;
            this.isVertical = isVertical;
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

        this.root = recursivelyInsert(this.root, null, p, 0);
    }

    /**
     * Insert a point to the 2d tree recursively
     * @param at the current node to check
     * @param parent the parent node to <code>at</code>
     * @param p the point to insert
     * @param orientation < 0 for left/below and >=0 for right/above
     */
    private Node recursivelyInsert(Node at, Node parent, Point2D p, double orientation) {
        // Insert at the child of a leaf (null link)
        if (at == null) {
            // make unit square the Rect for the root
            if (this.n++ == 0) return new Node(p, new RectHV(0, 0, 1, 1), true);

            RectHV rect;

            if (parent.isVertical) {
                if (orientation < 0) {
                    // the next rect is the left sub rect of the current rect
                    // divided by the x co-ord
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                      parent.p.x(), parent.rect.ymax());
                } else {
                    // the next rect is the right sub rect of the current rect
                    // divided by the x co-ord
                    rect = new RectHV(parent.p.x(), parent.rect.ymin(),
                                      parent.rect.xmax(), parent.rect.ymax());
                }
            } else {
                if (orientation < 0) {
                    // the next rect is the lower sub rect of the current rect
                    // divided by the y co-ord
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                      parent.rect.xmax(), parent.p.y());
                } else {
                    // the next rect is the upper sub rect of the current rect
                    // divided by the y co-ord
                    rect = new RectHV(parent.rect.xmin(), parent.p.y(),
                                      parent.rect.xmax(), parent.rect.ymax());
                }
            }

            return new Node(p, rect, !parent.isVertical);

        }

        double displacement = at.isVertical ? p.x() - at.p.x() : p.y() - at.p.y();

        if (displacement < 0) {
            at.lb = recursivelyInsert(at.lb, at, p, displacement);
        } else {
            at.rt = recursivelyInsert(at.rt, at, p, displacement);
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
        return recursivelyContains(this.root, p);
    }

    /**
     * Search a point to the 2d tree recursively
     * @param at the current node to check
     * @param p the target point
     * @return true if <code>p</code> exists in the 2d tree, vice versa
     */
    private boolean recursivelyContains(Node at, Point2D p) {
        // p does not exist when nodes are run out
        if (at == null) return false;

        if (at.p.equals(p)) return true;

        if (at.isVertical) {
            if (p.x() < at.p.x()) return recursivelyContains(at.lb, p);
            else return recursivelyContains(at.rt, p);
        } else {
            if (p.y() < at.p.y()) return recursivelyContains(at.lb, p);
            else return recursivelyContains(at.rt, p);
        }
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        recursivelyDraw(this.root);
    }

    /**
     * Draw all points and their subdivisions in a 2d tree inorder recursively
     * @param at the current node to check
     */
    private void recursivelyDraw(Node at) {
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
        if (at.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x, at.rect.ymin(), x, at.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(at.rect.xmin(), y, at.rect.xmax(), y);
        }

        // draw left subtree
        recursivelyDraw(at.lb);
        // draw right subtree
        recursivelyDraw(at.rt);
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
        if (at == null) return;

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
        if (query.distanceSquaredTo(closest.p) <= at.rect.distanceSquaredTo(query)) return closest;

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
                if (at.lb.rect.distanceSquaredTo(query) < at.rt.rect.distanceSquaredTo(query)) {
                    // query left subtree first if query is on the same side (contained by left's rect)
                    Node nextClosest = recursivelyNearest(at.lb, closest, query);
                    return recursivelyNearest(at.rt, nextClosest, query);
                } else {
                    // else (if query is on the right child's side or sitting on the boundary)
                    // query right subtree first
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
        // test range() for empty tree
        RectHV rect = new RectHV(0, 0, 1, 1);
        tree.range(rect);
    }
}
