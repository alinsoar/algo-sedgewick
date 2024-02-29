
public
class MoveToFront {

  public MoveToFront() {}

  // apply move-to-front encoding, reading from standard input and
  // writing to standard output
  public
  static void encode() {
      RBT<Long, Character> tab = new RBT<Long, Character>();
      long[] keys = new long[256];
      long minkey = 0;
      for (long i = 0; i<256;i++) {
          tab.put (i, (char) i);
          keys[(int) i] = i;
      }
      while (!BinaryStdIn.isEmpty()) {
          char c = BinaryStdIn.readChar();
          long key = keys[(int) c];
          int pos = tab.rank (key);
          tab.delete (key);
          keys[(int) c] = --minkey;
          tab.put (minkey, c);
          BinaryStdOut.write(pos, 8);
      }
      BinaryStdOut.close ();
  }

  // apply move-to-front decoding, reading from standard input and
  // writing to standard output
  public
  static void decode() {
      RBT<Long, Character> tab = new RBT<Long, Character>();
      long[] keys = new long[256];
      long minkey = 0;
      for (long i = 0; i<256;i++) {
          tab.put (i, (char) i);
          keys[(int) i] = i;
      }
      while (!BinaryStdIn.isEmpty()) {
          int pos = BinaryStdIn.readChar();
          long key = tab.select (pos);
          char c = tab.get (key);
          tab.delete (key);
          keys[(int) c] = --minkey;
          tab.put (minkey, c);
          BinaryStdOut.write(c, 8);
      }
      BinaryStdOut.close ();
  }

  // if args[0] is '-', apply move-to-front encoding
  // if args[0] is '+', apply move-to-front decoding
  public static void main(String[] args) {
      if (args.length != 1)
          throw new java.util.NoSuchElementException ();
      if (args[0].charAt (0) == '-')
          encode ();
      else if (args[0].charAt (0) == '+')
          decode ();
      else
          throw new java.util.NoSuchElementException ();
  }
}


