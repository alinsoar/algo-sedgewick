
public class SeamCarver {

  private Matrix m;

  private class Matrix {
    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;

    private int[][] COL;
    private int w, h;

    Matrix(Picture p) {
        w = p.width();
        h = p.height();
        // System.out.printf("MATRIX: WIDTH %d HEIGTH %d\n", w, h);
        COL = new int[w][h];
        // SET the RED GREEN BLUE values for this picture
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                java.awt.Color c = p.get(x, y);
                COL[x][y] = c.getRed() +(c.getGreen() << 8) +(c.getBlue() << 16);
            }
    }

    public int width() {
        return w;
    }

    public int height() {
        return h;
    }

    private int K(int x, int y, int color) {
        return (COL[x][y] & (255 << (color*8))) >> (color*8);
    }

    public double gX(int x, int y, int C) {
        if ((x == 0) || (y == 0) || (x == w-1) || (y == h-1))
            return 65025;
        int left = K(x-1, y, C);
        int right = K(x+1, y, C);
        int up = K(x, y-1, C);
        int down = K(x, y+1, C);
        int dx = left - right;
        int dy = up - down;
        return dx*dx + dy*dy;
    }

    public double gradient(int x, int y) {
        return gX(x, y, RED) + gX(x, y, GREEN) + gX(x, y, BLUE);
    }

    /// implements TOPOLOGICAL SHORTEST PATH.
    public int[] getHorizontalCarve() {
        double[] E = new double[h];
        double[] min = new double[h];
        double[] minPrev = new double[h];
        Byte[][] dir = new Byte [w][h];
        int x, y;
        for (y = 0; y < h; y++) {
            min[y] = 0.;
            minPrev[y] = 0.;
            dir[0][y] = -1;
            dir[w-1][y] = 1;
        }
        dir[0][h-1] = 0;
        dir[w-1][0] = 0;
        for (x = 1; x < w-1; x++) {
            for (y = 0; y < h; y++)
                E[y] = gradient(x, y);

            // first try LEFT
            for (y = 0; y < h; y++) {
                min[y] = minPrev[y] + E[y];
                dir[x][y] = 0;
            }
            // relaxation LEFT-UP
            for (y = 1; y < h; y++) {
                if (min[y] > minPrev[y-1] + E[y-1]) {
                    // System.out.printf("# RELAX LEFT %d %d\n", x, y);
                    dir[x][y] = -1;
                    min[y] = minPrev[y-1] + E[y-1];
                }
            }
            // relaxation
            for (y = 0; y < h-1; y++) {
                if (min[y] > minPrev[y+1] + E[y+1]) {
                    // System.out.printf("# RELAX RIGHT %d %d\n", x, y);
                    dir[x][y] = 1;
                    min[y] = minPrev[y+1] + E[y+1];
                }
            }
            for (y = 0; y < h; y++)
                minPrev[y] = min[y];
        }
        // Get the index of the minimum in the final total energy row
        double M0 = Double.MAX_VALUE;
        int p = -1;
        for (y = 0; y < h; y++)
            if (M0 > min[y]) {
                M0 = min[y];
                p = y;
            }

        int[] RES = new int[w];
        for (x = w-1; x > 0; x--) {
            RES[x] = p;
            p += dir[x-1][p];
        }
        RES[0] = p;
        return RES;
    }

    /// implements TOPOLOGICAL SHORTEST PATH.
    public int[] getVerticalCarve() {
        double[] E = new double[w];
        double[] min = new double[w];
        double[] minPrev = new double[w];
        Byte[][] dir = new Byte [w][h];
        int x, y;
        for (x = 0; x < w; x++) {
            min[x] = 0.;
            minPrev[x] = 0.;
            dir[x][0] = -1;
            dir[x][h-1] = 1;
        }
        dir[0][0] = 0;
        dir[w-1][h-1] = 0;
        for (y = 1; y < h-1; y++) {
            for (x = 0; x < w; x++)
                E[x] = gradient(x, y);

            // first try UP
            for (x = 0; x < w; x++) {
                min[x] = minPrev[x] + E[x];
                dir[x][y] = 0;
            }
            // relaxation UP-LEFT
            for (x = 1; x < w; x++) {
                if (min[x] > minPrev[x-1] + E[x-1]) {
                    // System.out.printf("# RELAX LEFT %d %d\n", x, y);
                    dir[x][y] = -1;
                    min[x] = minPrev[x-1] + E[x-1];
                }
            }
            // relaxation UP-RIGHT
            for (x = 0; x < w-1; x++) {
                if (min[x] > minPrev[x+1] + E[x+1]) {
                    // System.out.printf("# RELAX RIGHT %d %d\n", x, y);
                    dir[x][y] = 1;
                    min[x] = minPrev[x+1] + E[x+1];
                }
            }
            for (x = 0; x < w; x++)
                minPrev[x] = min[x];
        }
        // Get the index of the minimum in the final total energy row
        double M0 = Double.MAX_VALUE;
        int p = -1;
        for (x = 0; x < w; x++)
            if (M0 > min[x]) {
                M0 = min[x];
                p = x;
            }

        int[] RES = new int[h];

        for (y = h-1; y > 0; y--) {
            RES[y] = p;
            p += dir[p][y-1];
        }
        RES[0] = p;
        return RES;
    }

    public Picture picture() {
        Picture p = new Picture(w, h);
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int r = K(x, y, RED);
                int g = K(x, y, GREEN);
                int b = K(x, y, BLUE);
                java.awt.Color c = new java.awt.Color(r, g, b);
                p.set(x, y, c);
            }
        return p;
    }

    public void removeVerticalCarve(int[] a) {
        int diff = a[0];
        for (int x:a) {
            if (Math.abs(x-diff) > 1)
                throw new java.lang.IllegalArgumentException();
            diff = x;
        }
        w--;
        for (int y = 0; y < h; y++)
            for (int x = a[y]; x < w; x++)
                COL[x][y] = COL[x+1][y];
    }

    public void removeHorizontalCarve(int[] a) {
        int diff = a[0];
        for (int x:a) {
            if (Math.abs(x-diff) > 1)
                throw new java.lang.IllegalArgumentException();
            diff = x;
        }
        h--;
        for (int x = 0; x < w; x++)
            for (int y = a[x]; y < h; y++)
                COL[x][y] = COL[x][y+1];
    }

    public String toString() {
        StringBuilder s = new StringBuilder("ENERGY\n");
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                s.append(String.format("%10.0f;", gradient(col, row)));
            }
            s.append("\n");
        }
        s.append("COLORS\n");
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                s.append(String.format(" %02X%02X%02X;",
                                       K(col, row, RED),
                                       K(col, row, GREEN),
                                       K(col, row, BLUE)));
            }
            s.append("\n");
        }
        return s.toString();
    }
  }

  public SeamCarver(Picture picture) {
      // pic = picture;
      m = new Matrix(picture);
      // System.out.println(m);
  }

  // current picture
  public Picture picture() {
      return m.picture();
  }

  // width of current picture
  public int width() {
      return m.width();
  }

  // height of current picture
  public int height() {
      return m.height();
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
      if ((x >= width()) || (x < 0) || (y >= height()) || (y < 0))
          throw new java.lang.IndexOutOfBoundsException();
      return m.gradient(x, y);
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
      return m.getHorizontalCarve();
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
      return m.getVerticalCarve();
  }

  // remove horizontal seam from picture
  public void removeHorizontalSeam(int[] a) {
      if (a.length != width())
          throw new java.lang.IllegalArgumentException();
      m.removeHorizontalCarve(a);
  }

  // remove vertical seam from picture
  public void removeVerticalSeam(int[] a) {
      if (a.length != height())
          throw new java.lang.IllegalArgumentException();
      m.removeVerticalCarve(a);
  }

  public static void main(String[] args) {
      String filename = "src/SeamCarving/input/HJoceanSmall.png";
      if (args.length == 1) filename = args[0];
      System.out.printf("File %s\n", filename);

      Picture p = new Picture(filename);
      SeamCarver sc = new SeamCarver(p);
      sc.findVerticalSeam();
      sc.findHorizontalSeam();
  }
}
