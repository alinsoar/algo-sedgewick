
class Node {
  public char c;
  public Node left, mid, right;
  public int val;
  public String toString () {
      StringBuilder sb = new StringBuilder("");
      sb.append(c);
      sb.append((left == null) ? "-":left.c);
      sb.append((mid == null) ? "-":mid.c);
      sb.append((right == null) ? "-":right.c);
      sb.append(val);
      return sb.toString ();
  }
}

class TernaryTrie {
  public Node root;

  TernaryTrie () {
      root = null;
  }

  public int contains (String word) {
      Node x = get (root, word, 0);
      if (x == null) return 0;
      return x.val;
  }

  public Node get (Node x, String word, int i) {
      if (x == null) return null;
      char c = word.charAt (i);
      if      (c < x.c) return get (x.left, word, i);
      else if (c > x.c) return get (x.right, word, i);
      else if (i < word.length () - 1) return get (x.mid, word, i+1);
      else return x;
  }

  public Node get (Node x, char c) {
      if (x == null)    return null;
      else if (c < x.c) return get (x.left, c);
      else if (c > x.c) return get (x.right, c);
      else              return x;
  }


  public void put (String word, int value) { root = put (root, word, 0, value); }

  public Node put(Node x, String word, int i, int value) {
      // System.out.printf ("::%s::%s::%d\n", x, word, i);
      char c = word.charAt(i);
      if (x == null) {
          x = new Node ();
          x.c = c;
          x.val = 0;
      }
      if      (c < x.c)                x.left  = put(x.left,  word, i, value);
      else if (c > x.c)                x.right = put(x.right, word, i, value);
      else if (i < word.length () - 1) {
          x.mid = put(x.mid, word, i+1, value);
      }
      else x.val = value;
      return x;
  }

  public Iterable<String> getAllWords(Node n, int value) {
      Queue<String> q = new Queue<String> ();
      collect (n, "", q, value);
      return q;
  }

  public void collect (Node x, String prefix, Queue<String> q, int val) {
      if (x == null) return;
      collect (x.left, prefix, q, val);
      if (x.val == val) q.enqueue (prefix + x.c);
      collect (x.mid, prefix + x.c, q, val);
      collect (x.right, prefix, q, val);
  }

  public static void main(String[] args) {
      In in = new In(args[0]);
      String[] dictionary = in.readAllStrings ();
      TernaryTrie tt = new TernaryTrie ();
      for (String word : dictionary)
          tt.put (word, 1);

      System.out.printf ("\n");
      System.out.printf ("-->%s<<\n", tt.root);
      System.out.printf ("-->%s<<\n", tt.root.left);
      System.out.printf ("-->%s<<\n", tt.root.mid.mid);
      System.out.printf ("-->%s<<\n", tt.root.mid.mid.mid);
  }
}


