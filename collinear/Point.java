/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.y == that.y) {
            if (this.x == that.x) return Double.NEGATIVE_INFINITY; // two points are equal
            else return +0.0; // two points are horizontal
        } else if (this.x == that.x) {
            return Double.POSITIVE_INFINITY; // two points are vertical
        } else {
            return ((double) (that.y - this.y)) / ((double) (that.x - this.x)); // general case
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (this.y == that.y) {
            // break tie with x-coordinates
            return this.x - that.x;
        } else {
            return this.y - that.y;
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new BySlope();
    }

    /**
     * A Comparator that compares two points by the slope they make with the invoking point.
     * Formally, the point (x1, y1) is less than the point (x2, y2)
     * if and only if the slope (y1 − y0) / (x1 − x0) is less than the slope (y2 − y0) / (x2 − x0)
     */
    private class BySlope implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            return Double.compare(slopeTo(p1), slopeTo(p2));
        }
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        Point p0 = new Point(1, 1);
        Point p1 = new Point(2, 1); // horizontal to p0
        Point p2 = new Point(1, 3); // vertical to p0
        Point p3 = new Point(2, 2);
        Point p4 = new Point(4, 4);
        Point p5 = new Point(-2, 2);
        Point p6 = new Point(-2, -2);
        Point p7 = new Point(1, 1); // repeated point
        Point[] points = {p0, p1, p2, p3, p4, p5, p6, p7};
        Point[] pointsNaturalOrder = {p6, p0, p7, p1, p5, p3, p2, p4};
        Point[] pointsBySlope = {p7, p0, p5, p1, p6, p3, p4, p2};

        StdOut.println("Testing sorting by natural order (ordering by x-coordinates, break tie by y-coordinates");
        Arrays.sort(points);

        StdOut.println("Sorted:");
        for (Point p: points) {
            StdOut.print(p);
        }

        StdOut.println("\nExpected:");
        for (Point p: pointsNaturalOrder) {
            StdOut.print(p);
        }

        StdOut.println();
        StdOut.println("\nTesting sorting by slope");
        StdOut.printf("Invoking point: %s\n", p0);
        Arrays.sort(points, p0.slopeOrder());

        StdOut.println("Sorted:");
        for (Point p: points) {
            StdOut.print(p);
        }

        StdOut.println("\nExpected:");
        for (Point p: pointsBySlope) {
            StdOut.print(p);
        }



    }
}
