
import java.util.Arrays;
// import java.util.Comparator;
// import java.util.Iterator;

public class Fast {

    // private static void display_array (Point [] P, String msg) {
    //     for (int e = 0; e < P.length-1; e++)
    //         StdOut.printf(msg + "["+e+"]" + P[e].toString ());
    //     StdOut.println (msg + "[" + (P.length - 1)
    //                     + "]" + P [P.length - 1].toString ()
    //                     + " ");
    // }

    // private static void display_array (int [] P, String msg) {
    //     for (int e = 0; e < P.length-1; e++)
    //         StdOut.printf(msg + P[e] + " -> ");
    //     StdOut.println (P [P.length - 1] + " ");
    // }

    public static void main(String[] args) {
        String filename = args[0];
        Point[] P, copy;
        int [] freq;
        In file = new In(filename);
        int N = file.readInt();
        P = new Point[N];
        copy = new Point[N];
        freq = new int[N];
        //segments = new Bag<Point[]>();
        // init graphics
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (int i = 0; i < N; i++) {
            int x = file.readInt();
            int y = file.readInt();
            copy[i] = new Point(x, y);
            copy[i].draw();
        }
        // sort the initial array with compareTo ()
        for (int f = 0; f < copy.length; f++) {
            int m = f;
            for (int e = f+1; e < copy.length; e++)
                if (copy[e].compareTo(copy[m]) < 0) m = e;
            if (m != f) {
                Point x = copy[f];
                copy[f] = copy[m];
                copy[m] = x;
            }
        }
        //display_array (copy, ">> ");
        for (int i = 0; i < N; i++) {
            for (int t = 0; t < N; t++)
                P[t] = copy[t];
            Arrays.sort(P, P[i].SLOPE_ORDER);
            // StdOut.println("");
            // display_array (P, "# ");
            // compute the frequencies of each slope...
            freq [0] = 0;
            for (int t = 1; t < N; t++)
                if (copy[i].slopeTo(P[t-1]) == copy[i].slopeTo(P[t])) {
                    freq [t] = freq [t-1] + 1;
                    if ((t == N-1) && (freq [t] > 1)) {
                        int end = t;
                        int beg = t-freq[t-1]-1;

                        // display_array (P, "# ");
                        // display_array (freq, "%% ");
                        // StdOut.println("!+! "
                        //                + "[" + end
                        //                + "][" + beg + "] "
                        //                + P[0].compareTo (P[end])
                        //                + P[0].compareTo (P[beg])
                        //                + " | "
                        //                + P[end].toString ()
                        //                + P[beg].toString ());

                        if ((P[0].compareTo(P[end]) < 0)
                            && P[0].compareTo(P[beg]) < 0) {
                            StdOut.printf(P [0].toString());
                            for (int e = beg; e <= end; e++)
                                StdOut.printf(" -> " + P[e].toString());
                            StdOut.println();
                            P[0].drawTo(P [end]);
                        }
                    }
                }
                else {
                    freq [t] = 0;
                    if (freq[t-1] > 1) {
                        int end = t-1;
                        int beg = t-freq[t-1]-1;

                        // StdOut.println("!-! "
                        //                + "[" + (beg)
                        //                + "]["  + (end)
                        //                + "] "
                        //                + P[0].compareTo (P[beg])
                        //                + P[0].compareTo (P[end])
                        //                + " | "
                        //                + P[beg].toString ()
                        //                + P[end].toString ());

                        if ((P[0].compareTo(P[beg]) < 0)
                            && P[0].compareTo(P[end]) < 0) {
                            StdOut.printf(P [0].toString());
                            for (int e = beg; e <= end; e++)
                                StdOut.printf(" -> " + P[e].toString());
                            StdOut.println();
                            P[0].drawTo(P [end]);
                        }
                    }
                }
            //display_array (freq, "%% ");
        }
        StdDraw.show(0);
    }
}


