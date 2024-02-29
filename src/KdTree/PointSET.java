
public class PointSET {

    private SET<Point2D> set;

    public PointSET() {
        // construct an empty set of points
        set = new SET ();
    }
    public boolean isEmpty() {
        // is the set empty?
        return set.isEmpty ();
    }
    public int size() {
        // number of points in the set
        return set.size ();
    }
    public void insert(Point2D p) {
        // add the point p to the set (if it is not already in the set)
        set.add (p);
    }
    public boolean contains(Point2D p) {
        // does the set contain the point p?
        return set.contains (p);
    }
    public void draw() {
        // draw all of the points to standard draw
        for (Point2D p:set) {
            p.draw ();
        }
    }
    public Iterable<Point2D> range(RectHV rect) {
        // all points in the set that are inside the rectangle
        Stack s = new Stack<Point2D>();
        for (Point2D p:set) {
            if (rect.contains (p)) s.push (p);
        }
        return s;
    }
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to p; null if set is empty
        double distmin = 3;
        Point2D x = null;
        for (Point2D i:set) {
            double d = p.distanceTo (i);
            if (d < distmin) {
                distmin = d;
                x = i;
            }
        }
        return x;
    }
}


