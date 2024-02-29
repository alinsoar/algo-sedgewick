
import java.util.Iterator;

public class Brute {

    private static Bag<Point[]> segments;

    private static void sort(Point p1, Point p2, Point p3, Point p4) {
        Point[] P = { p1, p2, p3, p4 };
        for (int f = 0; f < P.length; f++) {
            int m = f;
            for (int e = f+1; e < P.length; e++)
                if (P[e].compareTo(P[m]) < 0) m = e;
            if (m != f) {
                Point x = P[f];
                P[f] = P[m];
                P[m] = x;
            }
        }
        boolean ok = true;
        Iterator it = segments.iterator();
        while (it.hasNext()) {
            Point[] Q = (Point[]) it.next();
            if (Q.length == P.length) {
                int e;
                for (e = 0; e < Q.length; e++)
                    if (Q[e] != P[e]) break;
                if (e == P.length) ok = false;
            }
        }
        if (ok) {
            segments.add(P);

            StdOut.println(P[0].toString()
                           + " -> " + P[1].toString()
                           + " -> " + P[2].toString()
                           + " -> " + P[3].toString());

            P[0].drawTo(P[3]);
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        Point[] P;
        In file = new In(filename);
        int N = file.readInt();
        P = new Point[N];
        segments = new Bag<Point[]>();
        for (int i = 0; i < N; i++) {
            int x = file.readInt();
            int y = file.readInt();
            P[i] = new Point(x, y);
        }
        StdDraw.setXscale(0, 32000);
        StdDraw.setYscale(0, 32000);

        int i, j, k, l;
        for (i = 0; i < N; i++)
            P[i].draw();

        for (i = 0; i < N; i++)
            for (j = 0; j < N; j++) {
                if (i == j) continue;
                double slopeij  = P[i].slopeTo(P[j]);
                for (k = 0; k < N; k++) {
                    if (P[i].SLOPE_ORDER.compare(P[j], P[k]) != 0) continue;
                    if ((i == k) || (j == k)) continue;
                    double slopejk  = P[j].slopeTo(P[k]);
                    if (slopeij != slopejk) continue;
                    for (l = 0; l < N; l++) {
                        if (P[l].SLOPE_ORDER.compare(P[j], P[k]) != 0) continue;
                        if ((l == k) || (l == j) || (l == i)) continue;
                        if ((slopejk == P[k].slopeTo(P[l])))
                            sort(P[i], P[j], P[k], P[l]);
                    }
                }
            }
        StdDraw.show(100);
    }
}
