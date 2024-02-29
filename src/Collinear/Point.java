
import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        // StdOut.println ("... "+this.toString()+"-"+that.toString()+"  ");
        if ((that.x == x) && (that.y == y)) return Double.NEGATIVE_INFINITY;
        if ((that.x == x)) return Double.POSITIVE_INFINITY;
        if ((that.y == y)) return +0;
        return ((double) that.y - y)/(that.x - x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if ((x == that.x) && (y == that.y)) return 0;
        if ((y < that.y) || ((y == that.y) && (x < that.x))) return -1;
        return 1;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    private class SlopeOrder implements Comparator<Point> {
        // SlopeOrder -- an operator for THIS Point, which, given 2
        // other Points, will return which of the 2 points has the
        // greater slope, into a system centered at THIS Point.
        public int compare(Point q1, Point q2) {
            double s1 = Point.this.slopeTo(q1);
            double s2 = Point.this.slopeTo(q2);
            if (s1 > s2) return 1;
            if (s1 < s2) return -1;
            return 0;
        }
    }

    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        String filename = args[0];
        Point[] P, copy;
        boolean [][] seg;
        In file = new In(filename);
        int N = file.readInt();
        P = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = file.readInt()/1000;
            int y = file.readInt()/1000;
            P[i] = new Point(x, y);
        }

        for (int t = 0; t < N; t++)
            StdOut.println("::" + P[t].toString());

        int K = 2;

        Point x = P[K];
        P[K] = P[0];
        P[0] = x;

        Arrays.sort(P, 1, N, P[0].SLOPE_ORDER);

        StdOut.println("sorted about P[2]");
        for (int t = 0; t < N; t++)
            StdOut.println("  ::" + P[t].toString());

        // --------------------------------------------------------

        Point p = new Point(9, 2);
        Point q = new Point(9, 9);
        Point r = new Point(9, 0);

        StdOut.println(p.SLOPE_ORDER.compare(q, r));

        Point origin = new Point(0, 0);
        Point[] pts = new Point[]{
            new Point(1, 1),
            new Point(1, 0),
            new Point(1, -1),
            new Point(0, 1),
            origin,
            new Point(0, -1),
            new Point(-1, 1),
            new Point(-1, 0),
            new Point(-1, -1)
        };
        StdOut.println("before sort");
        for (int i = 0; i < pts.length; ++i)
            StdOut.println(pts[i] +": "+ origin.slopeTo(pts[i]));

        Arrays.sort(pts, origin.SLOPE_ORDER);

        StdOut.println("after sort");
        for (int i = 0; i < pts.length; ++i) {
            StdOut.println(pts[i] +" + "+ origin.slopeTo(pts[i]));
        }
    }
}

