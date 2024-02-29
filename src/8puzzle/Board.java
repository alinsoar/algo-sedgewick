
public class Board {
    private final char[][] tiles;
    private final int hamming;
    private final int manhattan;
    private final  int N;
    private final int ox, oy;
    public Board(int[][] blocks) {
        // construct a board from an N-by-N array of blocks
        // (where blocks[i][j] = block in row i, column j)
        int x = -1, y = -1;
        N = blocks.length;
        tiles = new char[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = (char) blocks[i][j];
                if (tiles[i][j] == 0) {
                    x = i;
                    y = j;
                }
            }
        }
        ox = x;
        oy = y;
        manhattan = computeManhattan();
        hamming = computeHamming();
    }
    public int dimension() {
        // board dimension N
        return N;
    }
    private int computeHamming() {
        // number of blocks out of place
        int hamming0 = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if ((tiles[i][j] > 0)
                    && ((tiles[i][j] - 1) != i*N+j))
                    hamming0++;
        return hamming0;
    }
    public int hamming() { return hamming; }
    private int abs(int x) {
        if (x >= 0) return x;
        else return -x;
    }
    private int computeManhattan() {
        // sum of Manhattan distances between blocks and goal
        int manhattan0 = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                int v = tiles[i][j] - 1;
                if (v > -1) manhattan0 += abs(i - v / N) + abs(j - v % N);
            }
        return manhattan0;
    }
    public int manhattan() { return manhattan; }
    public boolean isGoal() {
        // is this board the goal board?
        return (manhattan == 0);
    }

    private void sweep(int[] a, int j) {
        int x = a[j-1];
        a[j-1] = a[j];
        a[j] = x;
    };

    public Board twin() {
        // a board obtained by exchanging two adjacent blocks in the same row
        int[][] tmp = new int [N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                tmp[i][j] = (int) tiles[i][j];
        int mi = -1, mj = -1;
        int minDist = manhattan+hamming+N*N*N;
        for (int i = 0; i < N; i++) {
            for (int j = 1; j < N; j++) {
                if ((tmp[i][j] == 0) || (tmp[i][j-1] == 0)) continue;
                sweep(tmp[i], j);
                Board t = new Board(tmp);
                int MM = t.computeManhattan() + t.computeHamming();
                if (MM < minDist) {
                    mi = i;
                    mj = j;
                    minDist = MM;
                }
                sweep(tmp[i], j);
            }
        }
        sweep(tmp[mi], mj);
        return new Board(tmp);
    }

    public boolean equals(Object y) {
        // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        boolean ok = ((N == that.N)
                      && (manhattan == that.manhattan)
                      && (hamming == that.hamming));
        for (int i = 0; i < N; i++) {
            if (!ok) break;
            for (int j = 0; j < N; j++)
                if (tiles[i][j] != that.tiles[i][j]) { ok = false; break; }
        }
        return ok;
    }
    private void sweep(int [][]a, int dx, int dy) {
        int x = a[ox][oy];
        a[ox][oy] = a[ox+dx][oy+dy];
        a[ox+dx][oy+dy] = x;
    };
    public Iterable<Board> neighbors() {
        // all neighboring boards
        int[][] copy = new int[N][N];
        Stack<Board> n = new Stack<Board>();
        if (ox > 0) {
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    copy[i][j] = tiles[i][j];
            sweep(copy, -1, 0);
            Board up = new Board(copy);
            n.push(up);
        }
        if (ox < N-1) {
            copy = new int[N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    copy[i][j] = tiles[i][j];
            sweep(copy, 1, 0);
            Board down = new Board(copy);
            n.push(down);
        }
        if (oy > 0) {
            copy = new int[N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    copy[i][j] = tiles[i][j];
            sweep(copy, 0, -1);
            Board left = new Board(copy);
            n.push(left);
        }
        if (oy < N-1) {
            copy = new int[N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    copy[i][j] = tiles[i][j];
            sweep(copy, 0, 1);
            Board right = new Board(copy);
            n.push(right);
        }
        return n;
    }
    public String toString() {
        // string representation of the board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        // s.append("[" +ox + " / " + oy + "] : " + "// " +
        //          hamming + " + " + manhattan +
        //          " = "+(hamming + manhattan) +
        //          " // " + N + "\n");
        s.append(N+"\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                s.append(String.format("%2d ", (int) tiles[i][j]));
            s.append("\n");
        }
        return s.toString();
    }
    public static void main(String[] args) {
        // for each command-line argument
        for (String filename : args) {
            // read in the board specified in the filename
            In in = new In(filename);
            int N = in.readInt();
            int[][] tiles = new int[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    tiles[i][j] = in.readInt();
                }
            }
            // solve the slider puzzle
            Board initial = new Board(tiles);
            Board twin = initial.twin();
            StdOut.println("--" + initial.toString()
                           + "--" + initial.hamming()
                           + "--" + initial.manhattan()
                           + "\nTWIN--" + twin.toString()
                           + "--" + twin.hamming()
                           + "--" + twin.manhattan()
                           + "--\n" + initial.equals(twin));
            StdOut.println("~~~");
            // StdOut.println (""+ initial.neighbors ().size);
            for (Board x: initial.neighbors()) {
                StdOut.println(""+x.toString());
            }
        }
    }
}



