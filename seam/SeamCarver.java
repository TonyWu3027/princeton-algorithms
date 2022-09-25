import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {

    private Picture picture;

    /**
     * Create a seam carver object based on the given picture
     *
     * @param picture a given picture
     * @throws IllegalArgumentException if the given {@code Picture} is null
     */
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Picture should not be null");
        // perform deep-copy on the original picture
        this.picture = new Picture(picture);
    }

    /**
     * Current picture
     *
     * @return current picture
     */
    public Picture picture() {
        return new Picture(this.picture);
    }

    /**
     * Width of current picture
     *
     * @return width of current picture
     */
    public int width() {
        return this.picture.width();
    }

    /**
     * Height of current picture
     *
     * @return height of current picture
     */
    public int height() {
        return this.picture.height();
    }

    /**
     * Energy of pixel at column x and row y,
     * which is the square root of the sum of the x-gradient and y-gradient of a pixel.
     *
     * @param x the column index of a given pixel
     * @param y the row index of a given pixel
     * @return energy of pixel (x, y)
     */
    public double energy(int x, int y) {
        validateX(x);
        validateY(y);

        // the energy of a pixel at the border of the image is pre-defined to be 1000
        // so that it is strictly larger than the energy of any interior pixel
        if (isOnBorder(x, y)) return 1000;

        return Math.sqrt(xGradient(x, y) + yGradient(x, y));
    }

    //  x-gradient of a pixel
    private double xGradient(int x, int y) {
        int leftRGB = this.picture.getRGB(x - 1, y);
        int rightRGB = this.picture.getRGB(x + 1, y);

        return Math.pow(r(leftRGB) - r(rightRGB), 2) + Math.pow(g(leftRGB) - g(rightRGB), 2)
                + Math.pow(b(leftRGB) - b(rightRGB), 2);
    }

    //  y-gradient of a pixel
    private double yGradient(int x, int y) {
        int upRGB = this.picture.getRGB(x, y - 1);
        int downRGB = this.picture.getRGB(x, y + 1);

        return Math.pow(r(upRGB) - r(downRGB), 2) + Math.pow(g(upRGB) - g(downRGB), 2) + Math.pow(
                b(upRGB) - b(downRGB), 2);
    }

    // parses the R value in an RGB integer
    private int r(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    // parses the G value in an RGB integer
    private int g(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    // parses the B value in an RGB integer
    private int b(int rgb) {
        return rgb & 0xFF;
    }

    /**
     * Is a pixel (x,y) on the border?
     *
     * @param x the column of the pixel
     * @param y the row of the pixel
     * @return {@code true} if is on the border, vice versa
     */
    private boolean isOnBorder(int x, int y) {
        return x == 0 || x == this.width() - 1 || y == 0 || y == this.height() - 1;
    }

    /**
     * Validates if {@code x} is within prescribed range
     *
     * @param x column index x
     * @throws IllegalArgumentException unless {@code 0<=x<this.width()}
     */
    private void validateX(int x) {
        if (x < 0 || x >= this.width()) {
            String msg = String.format("x should be 0<=x<%d, but x=%d", this.width(), x);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Validates if {@code y} is within prescribed range
     *
     * @param y row index y
     * @throws IllegalArgumentException unless {@code 0<=y<this.height()}
     */
    private void validateY(int y) {
        if (y < 0 || y >= this.height()) {
            String msg = String.format("y should be 0<=y<%d, but y=%d", this.height(), y);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Sequence of indices for horizontal seam
     *
     * @return sequence of indices for horizontal seam
     */
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    /**
     * Transpose {@code this.picture} from a Picture of {@code (width, height)} to a Picture of
     * {@code (height, width)}
     */
    private void transpose() {
        Picture newPicture = new Picture(height(), width());
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                newPicture.setRGB(y, x, this.picture.getRGB(x, y));
            }
        }

        this.picture = newPicture;
    }

    /**
     * Sequence of indices for vertical seam
     *
     * @return sequence of indices for vertical seam
     */
    public int[] findVerticalSeam() {
        // compute and cache the energy
        // otherwise, the energy for a pixel will be recomputed for a constant time
        double[][] energy = new double[width()][height()];

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energy[i][j] = energy(i, j);
            }
        }

        int[][] edgeTo = new int[width()][height()];
        // edgeTo[x][y] is the col index of the precedence pixel on the shortest path from the virtual top to pixel (x, y)

        double[][] distTo = new double[width()][height()];
        // distTo[x][y] is the distance from the virtual top to pixel (x, y)

        // initialise distTo[][] by setting the distance to the first-row pixels as their energy
        for (int i = 0; i < width(); i++) {
            distTo[i][0] = energy[i][0];
        }

        // initialise the rest of distTo[][] by setting the distance as infinity
        for (int i = 0; i < width(); i++) {
            for (int j = 1; j < height(); j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        // topological sort
        // an edge is (x, y) -> (i, y+1) where (i, y+1) is a reachable pixel
        // thus, traversing the picture from top to bottom is, naturally, in topological order
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                for (int i : reachable(x, y)) {
                    // relax
                    if (distTo[i][y + 1] > distTo[x][y] + energy[i][y + 1]) {
                        distTo[i][y + 1] = distTo[x][y] + energy[i][y + 1];
                        edgeTo[i][y + 1] = x;
                    }
                }
            }
        }

        // find the column index of the pixel in the bottom row with the smallest distance to the source
        double minDist = Double.POSITIVE_INFINITY;
        int minDistPixelX = 0;
        for (int x = 0; x < width(); x++) {
            if (distTo[x][height() - 1] < minDist) {
                minDist = distTo[x][height() - 1];
                minDistPixelX = x;
            }
        }

        // backtrack the pixels on the shortest path
        int[] seam = new int[height()];

        seam[height() - 1] = minDistPixelX;
        for (int y = height() - 1; y > 0; y--) {
            minDistPixelX = edgeTo[minDistPixelX][y];
            seam[y - 1] = minDistPixelX;
        }

        return seam;
    }

    /**
     * Compute column indices of the reachable pixels for a given pixel.
     * The reachable for a pixel {@code (x, y)} are {@code (x-1, y+1), (x, y+1), (x+1, y+1)} where
     * appropriate.
     *
     * @param x the column index of the pixel
     * @param y the row index of the pixel
     * @return an iterable of the column indices of the reachable, which is a subset of
     * {@code {x-1, x, x+1}}
     */
    private Iterable<Integer> reachable(int x, int y) {
        Stack<Integer> reachables = new Stack<>();

        // bottom row, no reachable
        if (y == height() - 1) {
            return reachables;
        }

        // there is a left-bottom reachable
        if (x > 0) {
            reachables.push(x - 1);
        }

        // there is a right-bottom reachable
        if (x < width() - 1) {
            reachables.push(x + 1);
        }

        reachables.push(x);

        return reachables;
    }

    /**
     * Remove vertical seam from current picture
     *
     * @param seam an integer array of length height
     */
    public void removeHorizontalSeam(int[] seam) {
        if (height() <= 1) throw new IllegalArgumentException("Picture height is <= 1");

        validateSeamArray(seam, false);

        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    /**
     * Validate whether the given seam is valid or not.
     *
     * @param seam       the seam array
     * @param isVertical true if the invoking method is for vertical seam, false for horizontal seam
     * @throws IllegalArgumentException when seam[] is null or the length is not equaled to width
     *                                  (horizontal) or height (vertical),
     *                                  or when an element of i is not {@code 0<=i<width} (vertical)
     *                                  or {@code 0<=i<height} (horizontal),
     *                                  or when two adjacent elements differ by >1
     */
    private void validateSeamArray(int[] seam, boolean isVertical) {
        if (seam == null) throw new IllegalArgumentException("seam[] should not be null");

        int bound, range;

        if (isVertical) {
            bound = height();
            range = width();
        }
        else {
            bound = width();
            range = height();
        }

        String msg;

        if (seam.length != bound) {
            msg = String.format("seam[] should be of length %d", bound);
            throw new IllegalArgumentException(msg);
        }

        for (int i = 0; i < bound; i++) {
            if (seam[i] < 0 || seam[i] >= range) {
                msg = String.format("seam[%d] should be 0<=seam[%d]<%d, but %d is found", i, i,
                                    range, seam[i]);
                throw new IllegalArgumentException(msg);
            }
            // check adjacent entries
            if (i < bound - 1) {
                if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                    msg = String.format("seam[%d] and seam[%d] differ by more than 1", i, i + 1);
                    throw new IllegalArgumentException(msg);
                }
            }
        }
    }

    /**
     * Remove vertical seam from current picture
     *
     * @param seam an integer array of length height
     */
    public void removeVerticalSeam(int[] seam) {
        if (width() <= 1) throw new IllegalArgumentException("Picture width is <= 1");

        validateSeamArray(seam, true);

        // the bitmap of the original picture, where bitmap[y][x] is the RGB value of pixel (x,y)
        int[][] bitmap = new int[height()][width()];
        // the bitmap of the new picture
        int[][] newBitmap = new int[height()][width() - 1];

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                bitmap[y][x] = this.picture.getRGB(x, y);
            }
        }

        for (int y = 0; y < height(); y++) {
            // assume a row: [ 1 1 x 1 1 1] where 1 is the pixel to keep and x is the pixel to remove
            // copy bitmap[y][0, seam[y]-1] to newBitmap[y][0, seam[y]-1]
            // escape seam[y], copy bitmap[y][seam[y]+1, width] to newBitMap[y][seam[y], width-1]
            System.arraycopy(bitmap[y], 0, newBitmap[y], 0, seam[y]);
            System.arraycopy(bitmap[y], seam[y] + 1, newBitmap[y], seam[y], width() - seam[y] - 1);
        }

        Picture newPicture = new Picture(width() - 1, height());

        for (int x = 0; x < width() - 1; x++) {
            for (int y = 0; y < height(); y++) {
                newPicture.setRGB(x, y, newBitmap[y][x]);
            }
        }

        this.picture = newPicture;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        // not implemented
    }

}
