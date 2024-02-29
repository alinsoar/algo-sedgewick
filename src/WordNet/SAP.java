
public class SAP {

  private Digraph g;

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
      g = new Digraph(G);
  }

  // length of shortest ancestral path between v and w; -1 if no such
  // path
  public int length(int v, int w) {
      Stack<Integer> from = new Stack<Integer>();
      Stack<Integer> to = new Stack<Integer>();
      from.push(v);
      to.push(w);
      return length(from, to);
  }

  // a common ancestor of v and w that participates in a shortest
  // ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
      Stack<Integer> from = new Stack<Integer>();
      Stack<Integer> to = new Stack<Integer>();
      from.push(v);
      to.push(w);
      return ancestor(from, to);
  }

  // length of shortest ancestral path between any vertex in v and any
  // vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
      // for (int x: v) System.out.printf ("\\/%4d", x);
      // System.out.println ();
      // for (int x: w) System.out.printf ("/\\%4d", x);
      // System.out.println ();
      if ((v == null) || (w == null))
          return -1;
      BreadthFirstDirectedPaths from = new BreadthFirstDirectedPaths(g, v);
      BreadthFirstDirectedPaths to = new BreadthFirstDirectedPaths(g, w);
      int min = g.V();
      int r = -1;
      for (int node = 0; node < g.V(); node++) {
          // if (from.distTo (node) == 0) {
          //     System.out.printf ("OK-OK\n",node);
          //     System.out.println (from.hasPathTo(node));
          //     System.out.println (to.hasPathTo(node));
          //     System.out.println (from.distTo (node));
          //     System.out.println (to.distTo (node));
          // }
          if ((from.hasPathTo(node)) && (to.hasPathTo(node))) {
              int m = from.distTo(node) + to.distTo(node);
              // System.out.println (m);
              if (min > m) {
                  min = m;
                  r = min;
              }
          }
      }
      // System.out.printf ("+++ %d\n",r);
      return r;
  }

  // a common ancestor that participates in shortest ancestral path;
  // -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
      BreadthFirstDirectedPaths from = new BreadthFirstDirectedPaths(g, v);
      BreadthFirstDirectedPaths to = new BreadthFirstDirectedPaths(g, w);
      int min = g.V();
      int a = -1;
      for (int node = 0; node < g.V(); node++) {
          if ((from.hasPathTo(node)) && (to.hasPathTo(node))) {
              int m = from.distTo(node) + to.distTo(node);
              // System.out.println (m);
              if (min > m) {
                  min = m;
                  a = node;
              }
          }
      }
      return a;
  }

  // for unit testing of this class (such as the one below)
  public static void main(String[] args) {
      In in = new In(args[0]);
      Digraph G = new Digraph(in);
      SAP sap = new SAP(G);
      while (!StdIn.isEmpty()) {
          int v = StdIn.readInt();
          int w = StdIn.readInt();
          int length   = sap.length(v, w);
          int ancestor = sap.ancestor(v, w);
          StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
      }
  }

  // public static void main(String[] args) {
  //     String graph_file = new String ("digraph1.txt");
  //     int from = 3;
  //     int to = 11;
  //     if (args.length > 0) {
  //         graph_file = args[0];
  //         from = Integer.parseInt (args[1]);
  //         to = Integer.parseInt (args[2]);
  //     }
  //     In in = new In(graph_file);
  //     Digraph g = new Digraph (in);
  //     SAP sap = new SAP (g);
  //     System.out.println ();
  //     System.out.println(sap.length (from, to));
  //     System.out.println(sap.ancestor (from, to));
  // }

}
