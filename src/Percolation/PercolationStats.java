public class PercolationStats {

    private double[] estimate;
    private final int N;
    private final int T;

    public PercolationStats(int N, int T) {
        // perform T independent computational experiments on an N-by-N grid
        if ((N <= 0) || (T <= 0))
            throw new java.lang.IllegalArgumentException();
        this.N = N;
        this.T = T;
        int i, j, tries = T;
        estimate = new double [T];

        while (tries > 0) {
            Percolation percolation = new Percolation(N);

            int x = 0;
            while (!percolation.percolates()) {
                i = StdRandom.uniform(0, N);
                j = StdRandom.uniform(0, N);
                if (!percolation.isOpen(i+1, j+1)) {
                    percolation.open(i+1, j+1);
                    x++;
                }
            }
            estimate [T-tries] = ((double) x) / (N*N);
            tries--;
        }
    }

    public double mean() {
        // sample mean of percolation threshold
        return StdStats.mean(estimate);
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        return StdStats.stddev(estimate);
    }

    public double confidenceLo() {
        // lower bound of the 95% confidence interval
        double nu, sig;
        sig = StdStats.stddev(estimate);
        nu = StdStats.mean(estimate);
        return nu - 1.96*sig/Math.sqrt(T);
    }

    public double confidenceHi() {
        // upper bound of the 95% confidence interval
        double nu, sig;
        sig = StdStats.stddev(estimate);
        nu = StdStats.mean(estimate);
        return nu + 1.96*sig/Math.sqrt(T);
    }

    public static void main(String[] args) {
        // test client, described below

        if (args.length != 2)
            throw new RuntimeException("wrong no of arguments");

        int n = Integer.parseInt(args[0]);
        int tries = Integer.parseInt(args[1]);

        PercolationStats test = new PercolationStats(n, tries);

        double m = test.mean();
        double s = test.stddev();
        double intmin = m - (1.96 * s)/Math.sqrt(tries);
        double intmax = m + (1.96 * s)/Math.sqrt(tries);

        StdOut.printf("mean                    = %20.20f\n", m);
        StdOut.printf("stddev                  = %20.20f\n", s);
        StdOut.printf("95%% confidence interval = %20.20f, %20.20f\n",
                      intmin, intmax);
    }
}
