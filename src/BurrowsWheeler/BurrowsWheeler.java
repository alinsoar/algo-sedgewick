import java.util.Arrays;


public class BurrowsWheeler {
  // apply Burrows-Wheeler encoding, reading from standard input and
  // writing to standard output
  public static void encode() {
      StringBuilder sb = new StringBuilder ();
      while (!BinaryStdIn.isEmpty()) {
          char c = BinaryStdIn.readChar();
          sb.append (c);
      }
      String s = sb.toString ();
      CircularSuffixArray csa = new CircularSuffixArray (s);
      int i;
      for (i = 0; csa.index (i) > 0; i++);
      BinaryStdOut.write(i, 32);
      for (i = 0; i < s.length (); i++) {
          int index = csa.index (i);
          int pos = index > 0 ? index-1 : s.length ()-1;
          char c = s.charAt (pos);
          BinaryStdOut.write(c, 8);
      }
      BinaryStdOut.close ();
  }

  // apply Burrows-Wheeler decoding, reading from standard input and
  // writing to standard output
  public static void decode() {
      int first = BinaryStdIn.readInt();
      StringBuilder sb = new StringBuilder ();
      while (!BinaryStdIn.isEmpty()) {
          char c = BinaryStdIn.readChar();
          sb.append (c);
      }
      // System.out.println (first);
      String s = sb.toString ();
      char[] array = (s.toCharArray());
      Arrays.sort (array);
      // System.out.println (sb);
      // System.out.println (array);

      // COMPUTE NEXT FUNCTION

      int len = array.length;
      int[] tmpoff = new int[256];
      int[] which = new int[len];

      int count = 0;
      while (count < len) {
          which[count] = tmpoff[array[count]]++;
          count ++;
      }

      int[][] next = new int[256][];
      for (int i = 0; i < 256; i++) {
          next[i] = new int[tmpoff[i]];
      }

      Arrays.fill (tmpoff, 0);

      count = 0;
      while (count < len) {
          char c = sb.charAt (count);
          next[c][tmpoff[c]] = count;
          tmpoff[c]++;
          count++;
      }

      // PRINT NEXT FUNCTION
      // for (int ch = 0; ch < 256; ch++) {
      //     for (int pos = 0; pos < next[ch].length; pos++)
      //         System.out.printf ("%s %s %s\n", (char) ch, pos, next[ch][pos]);
      // }

      // ******************************
      
      // DECODING
      int pos = first;
      count = 0;
      while (count < len) {
          char c = array[pos];
          //System.out.printf ("%d %c %d\n", pos, c, which[pos]);
          BinaryStdOut.write(c, 8);
          pos = next[c][which[pos]];
          count++;
      }
      BinaryStdOut.close ();
  }

  // if args[0] is '-', apply Burrows-Wheeler encoding
  // if args[0] is '+', apply Burrows-Wheeler decoding
  public static void main(String[] argv) {
      if (argv.length != 1)
          throw new java.lang.IllegalArgumentException ();
      else if (argv[0].equals ("-"))
          encode ();
      else if (argv[0].equals ("+"))
          decode ();
      else throw new java.lang.IllegalArgumentException ();
  }
}


