
public class KdTree {

    private Node root;
    private int size;

    private class Node {
        final RectHV rect;
        final int type;
        final Point2D p;
        Node left, right, parent;

        Node(Point2D q, Node parent, RectHV r) {
            rect = r;
            if (parent == null) type = 0;
            else if (parent.type == 0) type = 1;
            else type = 0;
            p = q;
            left = null;
            right = null;
        }
    }

    public KdTree() {
    }

    public boolean isEmpty() {
        // is the set empty?
        return (root == null);
    }

    public int size() {
        return size;
    }

    private double abs(double a) {
        if (a < 0) return -a;
        else return a;
    }

    private boolean equalPoint(Point2D p1, Point2D p2) {
        double dist = 1.0/100000000;
        if (p1.equals(p2)) return true;
        else if ((abs(p1.x() - p2.x()) < dist) &&
                 (abs(p1.y() - p2.y()) < dist) )
            return true;
        else
            return false;
    }

    private boolean find(Point2D p, Node n) {
        if (n == null) return false;
        if (equalPoint(p,n.p)) return true;
        else if (n.type == 0)
            if (p.x()  <= n.p.x()) return find(p, n.left);
            else return  find(p, n.right);
        else
            if (p.y()  <= n.p.y()) return find(p, n.left);
            else return find(p, n.right);
    }

    public boolean contains(Point2D p) {
        // does the set contain the point p?
        return find(p, root);
    }

    private void drawnext(Node n) {
      if (n == null) return;
      Point2D p = n.p;
      StdDraw.setPenColor(StdDraw.GREEN);
      StdDraw.setPenRadius(.03);
      p.draw ();
      RectHV rect = n.rect;
      double xmin = rect.xmin();
      double ymin = rect.ymin();
      double xmax = rect.xmax();
      double ymax = rect.ymax();
      double x = p.x();
      double y = p.y();
      // System.out.printf ("%d | %.3f  %.3f | %.3f  %.3f | %.3f  %.3f\n",
      //                    n.type, x,y,xmin,ymin,xmax, ymax);
      if (n.type == 0) {
          StdDraw.setPenColor(StdDraw.RED);
          StdDraw.setPenRadius(.005);
          StdDraw.line(x, ymin, x, ymax);
      }
      else {
          StdDraw.setPenColor(StdDraw.BLACK);
          StdDraw.setPenRadius(.005);
          StdDraw.line(xmin, y, xmax, y);
      }
      drawnext(n.left);
      drawnext(n.right);
  }

    public void draw() {
        StdDraw.clear();
        drawnext (root);
        StdDraw.show (200);
    }

    private void FindPoints(RectHV r, Node n, Stack<Point2D> s) {
        if (n == null) return;
        else {
            // inside the rectangle
            if ((r.xmin() <= n.p.x())
                && (r.xmax() >= n.p.x())
                && (r.ymin() <= n.p.y())
                && (r.ymax() >= n.p.y())) {
                s.push(n.p);
                FindPoints(r, n.left, s);
                FindPoints(r, n.right, s);
            }
            else if (n.type == 0) { // type = 0  --  inside the limits
                if ((n.p.x() <= r.xmax() )
                    && (n.p.x() >= r.xmin() ) ) {
                    FindPoints(r, n.left, s);
                    FindPoints(r, n.right, s);
                }
                else if (n.p.x() >= r.xmax()) { // rectangle to left
                    FindPoints(r, n.left, s);
                }
                else if (n.p.x() <= r.xmin()) { // rectangle to left
                    FindPoints(r, n.right, s);
                }
            }
            else { // type = 1  --  inside the limits
                if ((n.p.y() <= r.ymax() )
                    && (n.p.y() >= r.ymin() ) ) {
                    FindPoints(r, n.left, s);
                    FindPoints(r, n.right, s);
                }
                else if (n.p.y() >= r.ymax()) { // rectangle up
                    FindPoints(r, n.left, s);
                }
                else  if (n.p.y() <= r.ymin()) { // rectangle down
                    FindPoints(r, n.right, s);
                }
            }
        }
   }

    public Iterable<Point2D> range(RectHV rect) {
        // all points in the set that are inside the rectangle
        Stack<Point2D> s = new Stack<Point2D>();
        FindPoints(rect, root, s);
        return s;
    }

    private double minDist;
    private Point2D minPoint;
    private void closest(Point2D p, Node n) {
	// here one can still optimize by checking the position of P
	// against the vertical/horizontal rectangles on the node N
	// before of calling RECT.distanceSquaredTo(POINT). Some cases
	// can be eliminated by checking so instead of squaredDist.

	double d = p.distanceSquaredTo(n.p);

	if (d <= minDist) {
	    minPoint = n.p;
	    minDist = d;
	}

        if ((n.left != null) &&
	    (n.left.rect.distanceSquaredTo(p) <= minDist))
	    closest(p, n.left);

        if ((n.right != null) &&
	    (n.right.rect.distanceSquaredTo(p) <= minDist))
	    closest(p, n.right);
    }

    public Point2D nearest(Point2D p) {
        if (root == null) return null;
        minPoint = root.p;
        minDist = p.distanceSquaredTo(minPoint);
        closest(p, root);
        return minPoint;
    }

    private Node ins(Point2D p, Node n) {
        //StdOut.println("" + n.p + " -> " + p + " : " + n.rect);
        if (equalPoint(p,n.p)) return n;
        else if (n.type == 0) { // type 0
            if (p.x()  <= n.p.x()) {
                if (n.left == null) {
                    RectHV r = new RectHV(n.rect.xmin(),
                                           n.rect.ymin(),
                                           n.p.x(),
                                           n.rect.ymax());
                    Node w = new Node(p, n, r);
                    size++;
                    n.left = w;
                    return w;
                }
                else return ins(p, n.left);
            }
            else {
                if (n.right == null) {
                    RectHV r = new RectHV(n.p.x(),
                                           n.rect.ymin(),
                                           n.rect.xmax(),
                                           n.rect.ymax());
                    Node w = new Node(p, n, r);
                    size++;
                    n.right = w;
                    return w;
                }
                else return ins(p, n.right);
            }
        }
        else if (n.type == 1) { // type 1
            if (p.y() <= n.p.y()) {
                if (n.left == null) {
                    RectHV r = new RectHV(n.rect.xmin(),
                                           n.rect.ymin(),
                                           n.rect.xmax(),
                                           n.p.y());
                    Node w = new Node(p, n, r);
                    size++;
                    n.left = w;
                    return w;
                }
                else return ins(p, n.left);
            }
            else {
                if (n.right == null) {
                    RectHV r = new RectHV(n.rect.xmin(),
                                           n.p.y(),
                                           n.rect.xmax(),
                                           n.rect.ymax());
                    Node w = new Node(p, n, r);
                    size++;
                    n.right = w;
                    return w;
                }
                else return ins(p, n.right);
            }
        }
        assert(false);
        return null;
    }

    public void insert(Point2D p) {
        if (root == null) {
            root = new Node(p, null, new RectHV(0,0,1,1));
            size = 1;
        }
        else ins(p, root);
    }

    public static void main(String[] args) {

        KdTree k = new KdTree();

        String filename = args[0];
        In in = new In(filename);

        //StdDraw.show(0);

        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            k.insert(p);
            //brute.insert(p);
        }

        // for(double i=0.1;i<1;i+=0.1) {
        //     Point2D p = new Point2D(i,i);
        //     k.insert(p);
        // }

        //Point2D p1 = new Point2D(0.2441, 0.1694);
        // Point2D p2 = new Point2D(0.2, 0.2);
        // Point2D p3 = new Point2D(0.3, 0.3);

        //k.insert(p1);
        // k.insert(p2);
        // k.insert(p3);

        // StdOut.println("iterate - - - - - " + k.size());
        // //RectHV rect = new RectHV(0.226, 0.8141, 0.0984, 0.1729);
        // RectHV rect = new RectHV(0, 0, 1, 1);
        // for(Point2D i: k.range(rect)) {
        //     StdOut.println(""+ i);
        // }
        StdOut.println();
        StdOut.println(":" + k.nearest(new Point2D(0.35, 1)));
    }
}


