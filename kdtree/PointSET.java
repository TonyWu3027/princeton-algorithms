import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> tree;

    /**
     * Construct an empty set of points
     */
    public PointSET() {
        this.tree = new TreeSet<>();
    }

    /**
     * Is the set empty?
     * @return true if empty, vice versa
     */
    public boolean isEmpty() {
        return this.tree.isEmpty();
    }

    /**
     * Number of points in the set
     * @return number of points in the set
     */
    public int size() {
        return this.tree.size();
    }

    /**
     * Add the point to the set (if it is not already in the set)
     * @param p the point to be added
     * @throws IllegalArgumentException if null is given
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point should not be null");
        this.tree.add(p);
    }

    /**
     * Does the set contain point p?
     * @param p the point to be checked
     * @return true if contains, vice versa
     * @throws IllegalArgumentException if null is given
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point should not be null");
        return this.tree.contains(p);
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        for (Point2D p: this.tree) {
            StdDraw.point(p.x(), p.y());
        }
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

        for (Point2D p: this.tree) {
            if (rect.contains(p)) inside.push(p);
        }

        return inside;
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

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = p;

        for (Point2D that: this.tree) {
            if (p.distanceSquaredTo(that) < minDistance) {
                minDistance = p.distanceSquaredTo(that);
                nearestPoint = that;
            }
        }

        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) { }
}
