import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class BoggleSolver
{
  private final int MAX_DIM = 100;
  private TernaryTrie[][] dictionaries;
  // private TernaryTrie prefixes;
  private boolean[][] mask;
  private int[][][] neighboursX;
  private int[][][] neighboursY;
  private int[][] countNeighbours;
  // Initializes the data structure using the given array of strings
  // as the dictionary. (You can assume each word in the dictionary
  // contains only the uppercase letters A through Z.)
  public
  BoggleSolver(String[] dictionary) {
      // prefixes = new TernaryTrie ();
      dictionaries = new TernaryTrie['Z'-'A'+1]['Z'-'A'+1];
      mask = new boolean[MAX_DIM][MAX_DIM];
      for (int i = 'A'; i <= 'Z'; i++)
          for (int j = 'A'; j <= 'Z'; j++)
              dictionaries[i-'A'][j-'A'] = new TernaryTrie();
      for (String word : dictionary) {
          if (word.length () < 3)
              continue;
          int i = word.charAt(0)-'A';
          int j = word.charAt(1)-'A';
          dictionaries[i][j].put (word, 2);
      }
      // for (String word : dictionary)
      //     for (int i = 0; i < word.length (); i++)
      //         prefixes.put (word.substring (i), 1);
  }

  private
  void printState(Node n, int x, int y, SET<String> s, BoggleBoard b,
                  String prefix, int k, String msg, TernaryTrie dict) {
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("\n---:%s:---------------------- %s [%d %d]\n",
                               prefix, msg, x, y));
      sb.append(String.format("---:%s:", n));
      for (int t = 0; t < k; t++)
          sb.append(String.format("__%d:%d__",
                                   neighboursX[x*b.cols()+y][t],
                                   neighboursY[x*b.cols()+y][t]));
      sb.append("\n");
      for (int i = 0; i < b.rows(); i++) {
          for (int j = 0; j < b.cols(); j++) {
              String c = Character.toString(b.getLetter(i, j));
              if (c.charAt(0) == 'Q')
                  c = "Qu";
              if ((i == x) &&(j == y))
                  sb.append(" >");
              else
                  sb.append("  ");
              if ((i == x) &&(j == y))
                  sb.append(c);
              else if (mask[i][j])
                  sb.append("-");
              else
                  sb.append(c);
              if ((i == x) &&(j == y))
                  sb.append("<.");
              else
                  sb.append("  ");
          }
          sb.append("\n");
      }
      if (dict != null)
          sb.append(String.format("\n<<..%s..>>\n", dict.getAllWords(n, 2)));
      sb.append("\n-->>");
      for (String word: s)
          sb.append(String.format(";%s;", word));
      sb.append("<<--\n");
      System.out.printf("%s\n:", sb.toString().trim());
  }

  private
  void getWords(Node x, int i, int j, BoggleBoard b, SET<String> s, char[] prefix, int pos,
                TernaryTrie dict) {
      // printState(x, i, j, s, b, String.valueOf (Arrays.copyOfRange (prefix, 0, pos)),
      //            0, "X", null);
      if (x == null) return;
      mask[i][j] = true;
      char c0 = b.getLetter(i, j);
      // String c_ = (c0 == 'Q') ? "QU" : Character.toString(c0);
      Node n = dict.get (x, c0);
      if ((c0 == 'Q') && (n != null))
          n = dict.get (n.mid, 'U');
      // Node n = dict.get(x, c_ , 0);
      // printState(n, i, j, s, b, String.valueOf (Arrays.copyOfRange (prefix, 0, pos)), 0, "N1", dict);
      //System.out.printf("%s %s %s\n:", dict.get(x, c_ , 0), n, dict.get(x, c_ , 0) == n);
      if (n == null) {
          mask[i][j] = false;
          return;
      }
      if (n.val == 2) {
          prefix[pos] = c0;
          char[] pre = Arrays.copyOfRange (prefix, 0, pos+1);
          String word = String.valueOf (pre);
          s.add(word);
      }
      n = n.mid;
      int k = countNeighbours[i][j];
      // printState(n, i, j, s, b, prefix, k, "N2", dict);
      for (int t = 0; t < k; t++) {
          int kx = neighboursX[i][j][t];
          int ky = neighboursY[i][j][t];
          if (!mask[kx][ky]) {
              prefix[pos++] = c0;
              if (c0 == 'Q')
                  prefix[pos++] = 'U';
              getWords(n, kx, ky, b, s, prefix, pos, dict);
              mask[kx][ky] = false;
              pos--;
              if (c0 == 'Q')
                  pos--;
          }
      }
      mask[i][j] = false;
  }

  private
  void neighbour(int x, int y, BoggleBoard b) {
      int n = 0;
      for (int i = x-1; i <= x+1; i++) {
          if ((i < 0) ||(i >= b.rows()))
              continue;
          for (int j = y-1; j <= y+1; j++ ) {
              if ((j < 0)  || (j >= b.cols()) || ((i == x) && (j == y)))
                  continue;
              neighboursX[x][y][n] = i;
              neighboursY[x][y][n] = j;
              n++;
          }
      } 
      countNeighbours[x][y] = n;
  }

  // Returns the set of all valid words in the given Boggle board, as
  // an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
      // System.out.printf("...<<%s>>", dict.root);
      countNeighbours = new int[board.rows ()][board.cols ()];
      neighboursX = new int[board.rows ()][board.cols ()][8];
      neighboursY = new int[board.rows ()][board.cols ()][8];
      char[] prefix = new char[1000];
      for (int i = 0; i < board.rows(); i++)
          for (int j = 0; j < board.cols(); j++)
              neighbour (i, j, board);
      SET<String> q = new SET<String>();
      for (int i = 0; i < board.rows(); i++)
          for (int j = 0; j < board.cols(); j++) {
              for (boolean[] row : mask)
                  Arrays.fill(row, false);
              mask[i][j] = true;
              char c0 = board.getLetter(i, j);
              // String c_ = (c0 == 'Q') ? "QU" : Character.toString(c0);
              int k = countNeighbours[i][j];
              for (int t = 0; t < k; t++) {
                  int x = neighboursX[i][j][t];
                  int y = neighboursY[i][j][t];
                  char c1 = board.getLetter(x, y);
                  if (c0 == 'Q')
                      c1 = 'U';
                  TernaryTrie dict = dictionaries[c0-'A'][c1-'A'];
                  // Node root_ij = dict.get(dict.root, c_ , 0);
                  Node root_ij = dict.get(dict.root, c0);
                  if  (root_ij == null)
                      continue;
                  if (c0 == 'Q')
                      root_ij = dict.get(root_ij.mid, 'U');
                  if  (root_ij == null)
                      continue;
                  prefix[0] = c0;
                  prefix[1] = c1;
                  getWords(root_ij.mid, x, y, board, q, prefix, (c0 == 'Q') ? 2:1, dict);
              }
          }
      return q;
  }

  // Returns the score of the given word if it is in the dictionary,
  // zero otherwise. (You can assume the word contains only the
  // uppercase letters A through Z.)
  public int scoreOf(String word) {
      int points;
      int length = word.length();
      if      (length < 3)                points = 0;
      else {
          int c0 = word.charAt(0)-'A';
          int c1 = word.charAt(1)-'A';
          TernaryTrie dict = dictionaries[c0][c1];
          if (dict.contains (word) != 2) points = 0;
          else if (length < 5)           points = 1;
          else if (length < 6)           points = 2;
          else if (length < 7)           points = 3;
          else if (length < 8)           points = 5;
          else                           points = 11;
      }
      return points;
  }

  public static void main(String[] args) {
      System.out.printf("Dictionary:%s\nBoard:%s\n", args[0], args[1]);
      // In in = new In(args[0]);
      // TernaryTrie tt = new TernaryTrie ();
      Queue<String> q = new Queue<String>();
      BufferedReader br = null;
      try {
          br = new BufferedReader(new FileReader(args[0]));
          String word = br.readLine();
          while (word != null) {
              q.enqueue (word);
              word = br.readLine();
          }
      } catch(IOException e) {
              System.out.printf ("---%s", e.getMessage ());
      } finally {
          try {
              br.close ();
          } catch (IOException e) {
              System.out.printf ("---%s", e.getMessage ());
          }
      }
      String[] dictionary = new String[q.size ()];
      int i = 0;
      while (true) {
              String word = q.dequeue ();
              dictionary[i++] = word;
              if (q.isEmpty ())
                  break;
      }
      BoggleSolver bs = new BoggleSolver(dictionary);
      BoggleBoard board = new BoggleBoard(args[1]);
      System.out.println(board);
      System.out.printf("\n<---\n%s\n--->\n", bs.getAllValidWords(board));
  }
}



