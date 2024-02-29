
public class WordNet {

  private SAP sap;
  private ST<String, SET<Integer>> st;
  private ST<Integer, String> synsetString;

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
      In in;
      Digraph g;

      System.out.printf("Vertices file:%s\nEdges file:%s\n",
                        synsets, hypernyms);
      st = new ST<String, SET<Integer>>();
      synsetString = new ST<Integer, String>();
      in = new In(synsets);
      int maxId = -1;
      while (in.hasNextLine()) {
          String line = in.readLine();
          String[] tokens = line.split(",");
          int id = Integer.parseInt(tokens [0]);
          if (id > maxId) maxId = id;
          synsetString.put(id, tokens[1]);
          String[] synset = tokens[1].split(" ");
          for (String s: synset) {
              SET<Integer> ID = st.get(s);
              if (ID == null)
                  st.put(s, new SET<Integer>());
              st.get(s).add(id);
          }
      }
      in.close();

      // now read the graph
      in = new In(hypernyms);
      g = new Digraph(maxId+1);
      boolean[] marked = new boolean[g.V()];
      java.util.Arrays.fill(marked, Boolean.FALSE);
      while (in.hasNextLine()) {
          String line = in.readLine();
          String[] tokens = line.split(",");
          int from = Integer.parseInt(tokens[0]);
          marked[from] = true;
          for (int i = 1; i < tokens.length; i++) {
              int to = Integer.parseInt(tokens[i]);
              g.addEdge(from, to);
          }
      }
      in.close();

      System.out.print("Test graph...");
      int numberRoots = 0;
      for (boolean b:marked) {
          if (!b) numberRoots++;
      }
      if (numberRoots > 1) {
          System.out.printf("too many roots [%d]\n", numberRoots);
          throw new java.lang.IllegalArgumentException();
      }
      DirectedCycle testDag = new DirectedCycle(g);
      if (testDag.hasCycle()) {
          System.out.printf("found a cycle\n");
          throw new java.lang.IllegalArgumentException();
      }
      System.out.println("Okay.");

      sap = new SAP(g);
      // report input sizes
      System.out.printf("%s nouns ; %d vertices (IDs) ; %d edges\n",
                         st.size(), g.V(), g.E());
      // for(String n: nouns()) {
      //     System.out.printf("%s -> {", n);
      //     for(int k: st.get(n))
      //         System.out.printf("%10d", k);
      //     System.out.printf("}\n");
      // }
  }

  // the set of nouns (no duplicates), returned as an Iterable
  public Iterable<String> nouns() {
      return st.keys();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
      return st.contains(word);
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) { 
      SET<Integer> idA = st.get(nounA);
      SET<Integer> idB = st.get(nounB);
      // System.out.printf ("..%d -- %d\n", idA, idB);
      if ((idA == null) || (idB == null))
          return -1;
      return sap.length(idA, idB);
  }

  // a synset (second field of synsets.txt) that is the common
  // ancestor of nounA and nounB in a shortest ancestral path (defined
  // below)
  public String sap(String nounA, String nounB) {
      SET<Integer> idA = st.get(nounA);
      SET<Integer> idB = st.get(nounB);
      if ((idA == null) || (idB == null))
          throw new java.lang.IllegalArgumentException();
      int ancestor = sap.ancestor(idA, idB);
      // String res = "";
      // for (String s: st)
      //     if (st.get(s).contains(ancestor)) {
      //         if (res.length() > 0)
      //             res += " "+s;
      //         else
      //             res += s;
      //         //System.out.printf(":: %s\n", res);
      //     }
      // return res;
      return synsetString.get(ancestor);
  }

  // for unit testing of this class
  public static void main(String[] args) {
      String verticesFile = "input/synsets.txt";
      String edgesFile = "input/hypernyms.txt";
      if (args.length == 2) {
          verticesFile = args[0];
          edgesFile = args[1];
      }
      WordNet problem = new WordNet(verticesFile, edgesFile);
      System.out.printf("-> %d\n", problem.distance("lanthanum",
                                                    "glaciation"));
      System.out.printf("-> %d\n", problem.distance("compass_point",
                                                    "southeastward"));
      System.out.printf("-> %s\n", problem.sap("compass_point",
                                               "southeastward"));
  }
}
