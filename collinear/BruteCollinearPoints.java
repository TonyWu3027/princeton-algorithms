import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * A brute-force solution to find all line segments containing 4 points
 * in a given array of points.
 */
public class BruteCollinearPoints {

    private int segmentCount = 0;
    private LineSegment[] lineSegments;

    /**
     * Finds all line segments containing 4 points
     *
     * @param points an array of points to be examined
     * @throws IllegalArgumentException if points[] is null, or any Point in points[] is null,
     *                                  or there is a repeated Point
     */
    public BruteCollinearPoints(Point[] points) {
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

        this.lineSegments = new LineSegment[n];

        // traverse all 4-point subsets
        // form a line segment if the subset is collinear
        for (int p = 0; p < n - 3; p++) {
            for (int q = p + 1; q < n - 2; q++) {
                for (int r = q + 1; r < n - 1; r++) {
                    if (pointsCopy[p].slopeTo(pointsCopy[q]) == pointsCopy[p].slopeTo(
                            pointsCopy[r])) {
                        for (int s = r + 1; s < n; s++) {
                            if (pointsCopy[p].slopeTo(pointsCopy[r]) == pointsCopy[p].slopeTo(
                                    pointsCopy[s])) {
                                this.lineSegments[this.segmentCount++] = new LineSegment(
                                        pointsCopy[p], pointsCopy[s]);
                            }
                        }
                    }
                }
            }
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

        // print and draw the line segments
        StdDraw.setPenRadius();
        StdDraw.setPenColor();
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
