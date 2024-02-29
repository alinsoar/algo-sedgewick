
public class Percolation {

    private boolean [] o, full, down;;
    private WeightedQuickUnionUF table;
    private boolean perc;
    private int N;

    public Percolation(int N) {
        // create N-by-N grid, with all sites blocked
        this.N = N;
        table = new WeightedQuickUnionUF(N*N);
        o = new boolean [N*N];
        full = new boolean [N*N];
        down = new boolean [N*N];
    }

    private void checkBounds(int i, int j) {
        // error for bad indices
        if ((i < 0) || (i > N-1) || (j < 0) || (j > N-1))
            throw new IndexOutOfBoundsException("bad bounds");
    }

    private void unify(int p, int q) {
        int root1 = table.find(p);
        int root2 = table.find(q);
        table.union(p, q);
        if (full[root1] || full[root2]) full[table.find(p)] = true;
        if (down[root1] || down[root2]) down[table.find(p)] = true;
    }

    public void open(int i0, int j0) {
        // open site (row i, column j) if it is not already
        int i, j;
        i = i0-1;
        j = j0-1;
        checkBounds(i, j);

        int p = N*i+j;
        int up = p-N;
        int right = p+1;
        int dow = p+N;
        int left = p-1;

        if (o[p]) return;
        o[p] = true;
        if (i == 0) full[p] = true;
        if (i == N-1) down[p] = true;

        if ((i > 0) && o[up]) unify(p, up);
        if ((i < N-1) && o[dow]) unify(p, dow);
        if ((j > 0) && o[left]) unify(p, left);
        if ((j < N-1) && o[right]) unify(p, right);

        if (!perc)
            perc = full[table.find(p)] && down[table.find(p)];
    }

    public boolean isOpen(int i0, int j0) {
        // is site (row i, column j) open?
        int i, j;
        i = i0-1;
        j = j0-1;
        checkBounds(i, j);
        return o[N*i+j];
    }

    public boolean isFull(int i0, int j0) {
        // is site (row i, column j) full?
        int i, j;
        i = i0-1;
        j = j0-1;
        checkBounds(i, j);
        int root = table.find(N*i+j);
        return full[root];
    }

    public boolean percolates() {
        // does the system percolate?
        return perc;
    }
}
