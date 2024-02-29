
public class Solver {

    private MinPQ<Node> open;
    private MinPQ<Node> openTwin;
    private Node goal;

    private class Node implements Comparable {
        private final Board board;
        private final int moves;
        private final Node parent;
        public Node(Board b, Node parent) {
            board = b;
            this.parent = parent;
            if (parent == null) moves = 0;
            else moves = parent.moves + 1;
        }
        public int compareTo(Object that) {
            Node t = (Node) that;
            if (board.manhattan() + moves == t.board.manhattan() + t.moves) return 0;
            if (board.manhattan() + moves < t.board.manhattan() + t.moves) return -1;
            return 1;
        }
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("PQ: == "+" \n"+board.toString());
            s.append("\n");
            return s.toString();
        }
    }

    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        Board twin = initial.twin();
        open = new MinPQ<Node>();
        openTwin = new MinPQ<Node>();
        Node init = new Node(initial, null);
        Node initTwin = new Node(twin, null);
        open.insert(init);
        openTwin.insert(initTwin);
        goal = null;
        // A STAR IMPLEMENTATION
        while ((!open.isEmpty()) && (!openTwin.isEmpty())) {
            Node current = open.delMin();
            Node currentTwin = openTwin.delMin();
            if (current.board.isGoal()) {
                // build solution
                goal = current;
                return;
            }
            else if (currentTwin.board.isGoal()) {
                // build NON-solution
                return;
            }
            for (Board o: current.board.neighbors()) {
                boolean ins = true;
                Node K = current.parent;
                while (K != null) {
                    if (o.equals(K.board)) ins = false;
                    K = K.parent;
                }
                if (!ins) continue;
                Node n = new Node(o, current);
                open.insert(n);
            }
            for (Board ot: currentTwin.board.neighbors()) {
                boolean ins = true;
                Node K = currentTwin.parent;
                while (K != null) {
                    if (ot.equals(K.board)) ins = false;
                    K = K.parent;
                }
                if (!ins) continue;
                Node nt = new Node(ot, currentTwin);
                openTwin.insert(nt);
            }
        }
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return goal != null;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if no solution
        if (goal != null) {
            int d = -1;
            Node n = goal;
            while (n != null) {
                d++;
                n = n.parent;
            }
            return d;
            // return goal.dist;
        }
        else return -1;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if no solution
        if (goal == null) return null;
        Stack<Board> sol = new Stack<Board>();
        Node n = goal;
        while (n != null) {
            sol.push(n.board);
            n = n.parent;
        }
        return sol;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        //System.out.println (initial.toString () + initial.manhattan ()+ " " + initial.hamming ());
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}


