import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * A fast, sort-based solution to find all line segments containing 4 points
 * in a given array of points.
 */
public class FastCollinearPoints {

    private int segmentCount = 0;
    private LineSegment[] lineSegments;

    /**
     * Finds all line segments containing 4 points
     *
     * @param points an array of points to be examined
     * @throws IllegalArgumentException if points[] is null, or any Point in points[] is null,
     *                                  or there is a repeated Point
     */
    public FastCollinearPoints(Point[] points) {
        // check input is not null
        if (points == null) throw new IllegalArgumentException("points[] should not be null");

        // check every point is not null
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("any point should not be null");
        }

        // make a copy of points[]
        int n = points.length;
        Point[] pointsCopy = new Point[n];

        for (int i = 0; i < n; i++) {
            pointsCopy[i] = points[i];
        }

        // sort the array in natural order (bottom-left to top-right)
        Arrays.sort(pointsCopy);

        // check no repeated point
        // since points[] is sorted, equal (repeated) points will be neighbours
        for (int i = 0; i < n - 1; i++) {
            if (pointsCopy[i].compareTo(pointsCopy[i + 1]) == 0)
                throw new IllegalArgumentException("repeated point found");
        }

        this.lineSegments = new LineSegment[n * n];

        for (Point p : pointsCopy) {
            // sort the array by slope with Point p as the origin
            Arrays.sort(pointsCopy, p.slopeOrder());

            /**
             * traverse the sorted array with 2 pointers to find any collinear groups.
             * a "collinear group" is a set of adjacent equi-slope points and group size >= 1
             */
            int i = 1; // potential start of a group
            int j = 2; // potential end of a group
            while (j < n) {
                /**
                 * a collinear group terminates when:
                 * ...there is a change in slope
                 * ...the last point is being visited
                 *
                 * To avoid recording duplicated collinear groups, a valid collinear group must be:
                 * ...group size >= 3
                 * AND
                 * ...the origin is the min of this group by natural order
                 */
                if (p.slopeTo(pointsCopy[i]) != p.slopeTo(pointsCopy[j])) {
                    // there is a change in slope
                    if (j - i >= 3 && p.compareTo(pointsCopy[i]) < 0) {
                        // i and j-1 make a group with group size >= 3 AND the origin is the min of this group by natural order
                        this.lineSegments[this.segmentCount++] = new LineSegment(p,
                                                                                 pointsCopy[j - 1]);
                    }
                    // either it makes a valid group or not, consider the next possible group
                    i = j;
                }
                else if (j == n - 1 && j - i >= 2 && p.compareTo(pointsCopy[i]) < 0) {
                    // the last point being visited makes a group >= 3 AND the origin is the min of this group by natural order
                    this.lineSegments[this.segmentCount++] = new LineSegment(p, pointsCopy[j]);
                }
                j++;
            }
            /**
             * Note that:
             * Natural order is preserved in each group in each iteration since Arrays.sort() is a stable mergesort.
             * Therefore, if p < i by natural order, p < j as well given that p, i, and j are in the same group.
             */
            Arrays.sort(pointsCopy);
        }

    }

    /**
     * The number of line segments
     *
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return this.segmentCount;
    }

    /**
     * The line segments
     *
     * @return an array of the line segments
     */
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[segmentCount];

        for (int i = 0; i < segmentCount; i++) {
            segments[i] = this.lineSegments[i];
        }

        return segments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.RED);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        StdDraw.setPenRadius();
        StdDraw.setPenColor();
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
