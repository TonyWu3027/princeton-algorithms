import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    // numbers of open sites when the subject percolate
    private final double[] thresholds;

    // number of trials
    private final int numCount;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // validates arguments
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("Both n and trials should be >0");

        // initialise instance variables
        thresholds = new double[trials];
        // size of the grid
        this.numCount = trials;

        // perform independent trials
        for (int i = 0; i < trials; i++) {
            // initialise a single trial
            Percolation perc = new Percolation(n);

            // open sites until percolates
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                perc.open(row, col);
            }

            // records the number of open site when percolates
            thresholds[i] = perc.numberOfOpenSites()/((double) n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - (1.96*this.stddev()/Math.sqrt(this.numCount));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + (1.96*this.stddev()/Math.sqrt(this.numCount));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int numTrial = Integer.parseInt(args[1]);

        PercolationStats experiment = new PercolationStats(n, numTrial);
        StdOut.printf("mean                    = %f\n", experiment.mean());
        StdOut.printf("stddev                  = %f\n", experiment.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", experiment.confidenceLo(), experiment.confidenceHi());
    }

}
