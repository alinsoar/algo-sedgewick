
public class Outcast {

  private WordNet wn;

  // constructor takes a WordNet object
  public Outcast(WordNet wordnet) {
      wn = wordnet;
  }

  // given an array of WordNet nouns, return an outcast
  public String outcast(String[] nouns) {
      String longest = nouns[0];
      int worse = -1;
      for (String s:nouns) {
          //System.out.println (s);
          int d = 0;
          for (String t:nouns) {
              // System.out.println ("\t");
              d += wn.distance(s, t);
              //System.out.printf ("\t%s -> %s -- %d\n", s, t, d);
          }
          // System.out.printf ("%s -- %d\n", s, d);
          if (d > worse) {
              worse = d;
              longest = s;
          }
      }
      return longest;
  }

  // for unit testing of this class (such as the one below
  public static void main(String[] args) {
      WordNet wordnet = new WordNet(args[0], args[1]);
      Outcast outcast = new Outcast(wordnet);
      for (int t = 2; t < args.length; t++) {
          String[] nouns = In.readStrings(args[t]);
          StdOut.println(args[t] + ": " + outcast.outcast(nouns));
      }
  }
}
