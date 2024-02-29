import java.util.Arrays;
// import java.util.Random;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Collections;

public class CircularSuffixArray {

  private final int len;
  private final LinkedList<Integer> index;

  // circular suffix array of s
  public CircularSuffixArray(String s) {
      len = s.length ();
      index = new LinkedList<Integer>();
      final char[] s2 = s.toCharArray();
      Comparator<Integer> com = new Comparator<Integer> () {
        public int compare (Integer p1, Integer p2) {
          int x1 = p1;
          int x2 = p2;
          for (int i = 0; i < len; i++, x1++, x2++) {
            if (x1 == len) x1 = 0;
            if (x2 == len) x2 = 0;
            if (s2[x1] == s2[x2])     continue ;
            else if (s2[x1] > s2[x2]) return  1;
            else if (s2[x1] < s2[x2]) return -1;
          }
          return 0;
        }
      };
      for (int i=0; i < len; i++) index.add(i);
      Collections.sort (index, com);
  }

  // length of s
  public int length() {
      return len;
  }
  // returns index of ith sorted suffix
  public int index(int i) {
      return index.get(i);
  }
  public static void main (String[] argv) {

      StringBuilder sb = new StringBuilder ();
      while (!BinaryStdIn.isEmpty()) {
          char c = BinaryStdIn.readChar();
          sb.append (c);
      }
      String s = sb.toString ();
      CircularSuffixArray test = new CircularSuffixArray (s);
      for (int i=0; i < test.length (); i++)
          System.out.printf ("%10d", test.index (i));
      // System.out.printf ("\n");
  }
}


