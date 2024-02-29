import java.util.Arrays;
import java.io.BufferedInputStream;

import java.io.IOException;


public
class RBT {
    private static final boolean RED   = true;
    private static final boolean BLACK = false;
    private static final boolean LEFT  = false;
    private static final boolean RIGHT = true;
    final Node nil = new Node (null, null, null, "#", BLACK, 0);
    Node root = nil;
    // static public long op = 0;
    int treeHeight;
    int treeHeightError;
    int treeColorError;
    int treeWeightError;
    boolean[] stack = new boolean[128]; // max heigh = 2**64

    /****************************************************************/
    // CLASS NODE

    private class Node {
        Node parent;
        Node left;
        Node right;
        int weight;
        boolean color;
        String val;

        Node (Node parent,
              Node left,
              Node right,
              String value,
              boolean color,
              int weight) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.weight = weight;
            this.color = color;
            this.val = value;
        }
        // 0-operators
        boolean isRed     () {return color == RED;}
        boolean isBlack   () {return color == BLACK;}
        boolean hasRight  () {return right != nil;}
        boolean hasLeft   () {return left != nil;}
        boolean isRoot    () {return parent == nil;}
        boolean isLeaf    () {return this == nil;} // leaf of root`s parent
        // 1-operators
        boolean equal(Node that) {return this == that;}

        // operators useful for INSERT
        boolean is3         () {return left.isRed ();} // valid 3
        boolean is3BlackRed () {return left.isBlack() && right.isRed ();}
        boolean is4LeftLeft () {return left.isRed() && left.left.isRed();}
        boolean is4LeftRight(){return left.isRed() && right.isRed();}

        Node flipColors () {;
            print ("flip colors " + val);
            // print (this);
            // assert isRoot () || (color == RED);
            // RBT.op++;
            color = !color;
            left.color = !left.color;
            right.color = !right.color;
            return this;
        }

        private void transplant (Node x) {
            if (equal(parent.left)) parent.left  = x;
            else                    parent.right = x;
            x.parent = parent;
        }

        // COLORS: C0, RED
        // ORDER:  (A X B) THIS (C RIGHT D)  | A X (B THIS (C RIGHT D))
        // ----------------------------------|-------------------------
        //                P                  |           P
        //                |                  |           |
        //              THIS(C0)             |           X(C0)
        //             /    \                |          / \
        //      X=LEFT(RED)  RIGHT           |         A   THIS(RED)
        //        /   \      /  \            |            /   \
        //       A     B    C    D           |           B     RIGHT
        //                                   |                 /  \
        //                                   |                C    D
        Node rotateRight () {
            print ("rotate right " + val);
            assert left.color == RED;
            // RBT.op++;
            Node x = left;
            left = x.right;
            left.parent = this;
            x.right = this;
            x.color = color;
            color = RED;
            transplant (x);
            parent = x;
	    // setSize ();
	    // x.setSize ();
	    if (x.isRoot ()) root = x;
            return x;
        }

        // COLORS: C0, RED
        // ORDER:  (A LEFT B) THIS (C X D)   | ((A LEFT B) THIS C) X D
        // ----------------------------------|-------------------------
        //                P                  |                   P
        //                |                  |                   |
        //              THIS(C0)             |                   X(C0)
        //             /    \                |                  /  \
        //        LEFT     X=RIGHT(RED)      |          THIS(RED)   D
        //        /  \       /  \            |          /   \
        //       A    B     C    D           |       LEFT    C
        //                                   |       /  \
        //                                   |      A    B
        Node rotateLeft () {
            print ("rotate left " + val);
	    assert right.color == RED;
            // RBT.op++;
            Node x = right;
            right = x.left;
            right.parent = this;
            x.left = this;
            x.color = color;
            color = RED;
            transplant (x);
            parent = x;
	    // setSize ();
	    // x.setSize ();
            if (x.isRoot ()) root = x;
            return x;
        }

        // clean the 4 nodes. goes up to parent node as time as the
        // current node is RED.
        Node clean4 () {
            Node w = this;
            for (;!w.isLeaf (); w = w.parent) {
                w = w.balance ();
                if (w.isRoot ()) root = w;
                if (w.isBlack ()) return w;
            }
            return w;
        }

        Node balance () {
            print ("balance " + val);
            Node w = this;
            if (w.is3BlackRed ()) w = w.rotateLeft ();
            if (w.is4LeftLeft ()) w = w.rotateRight();
            if (w.is4LeftRight()) w = w.flipColors ();
            return w;
        }

        Node insert (Node t, boolean dir) {
            if (dir == LEFT) left  = t;
            else             right = t;
            Node w = clean4 ();
            for (; w != nil; w = w.parent) w.setWeight ();
            return w;
        }

        Node rank (int n) {
            int k = left.weight;
            if (k == n) return this;
            if (k > n)  return left.rank (n);
            else        return right.rank (n-k-1);
        }

        Node min () {
            if (left.isLeaf ()) return this;
            else return left.min ();
        }

        void setWeight () { weight = right.weight + left.weight + 1; }

	Node rollRedLeft12 () {
	    // case A and B
	    // ---------------------------------------------
	    //        u            |           a
	    //      /   \          |         /   \
	    //     k    a b        |       k u     b
	    //    /\    /|\        |       /|\    / \
	    //   1  2  3 4 5       |      1 2 3  4   5
	    //		           |
	    //          u(c)       |            a(c)
	    //        /   \        |           /   \
	    //      k      b       |         u      b
	    //     /\     / \      |        / \    / \
	    //    1  2   a@  5     |       k@  3  4   5
	    //          / \        |      / \
	    //         3   4       |     1  2
	    //                     |
	    boolean ww = parent.isRoot ();
	    print ("...>", ww);
	    Node a = parent.right.left;
	    Node n3 = a.left;
	    Node n4 = a.right;

	    color = RED;
	    a.color = parent.color;
	    parent.color = BLACK;

	    a.left = parent;
	    a.right = parent.right;
	    parent.transplant (a);
	    parent.right.parent = a;
	    parent.parent = a;
	    parent.right.left = n4;
	    n4.parent = parent.right;
	    parent.right = n3;
	    n3.parent = parent;
	    if (ww) root = a;
	    return a;
	}

	Node rollRedRight21x () {
	    // case F              |
	    // ---------------------------------------------
	    //        u            |           b
	    //      /   \          |         /   \
	    //    a b    k         |        a     u k
	    //    /|\  	 /\        |       / \	  /|\
	    //   1 2 3  4  5       |      1   2	 3 4 5
	    //		           |
	    //          u          |              b
	    //        /   \        |           /     \
	    //      b      k       |          a       u
	    //     /\      /\      |         / \     / \
	    //    A* 3    4  5     |        1   2   3   K@
	    //   / \               |                   / \
	    //   1  2              |                  4   5
	    //                     |
	    Node x;
	    parent.left.color = RED;
	    x = parent.rotateRight ();
	    x.left.color = BLACK;
	    x.right.color = BLACK;
	    x.right.right.color = RED;
	    // x.right.right.setSize ();
	    // x.right.setSize ();
	    // x.setSize ();
	    return x;
	}

	Node rollRedRight22 () {
	    // case E
	    Node a = parent.left.right.left;
	    Node b = a.parent;
	    Node n5 = b.right;
	    a.color = BLACK;
	    color = RED;
	    a.parent = parent.left;
	    parent.left.right = a;

	    b.left = parent.left;
	    b.right = parent;
	    parent.transplant (b);
	    if (b.isRoot ()) root = b;
	    parent.left.parent = b;
	    parent.parent = b;
	    parent.left = n5;
	    n5.parent = parent;
	    return b;
	}

        // invariant: `THIS.RIGHT.COLOR = RED`, at the finish of this
        // procedure, keeping in the same time a valid RBT.
        void rollRedDownToRight () {
            print ("roll red to right " + val);
            assert right.color == RED;
            print ("{<<}", root);
        }

        // invariant: `THIS.LEFT.COLOR = RED`, when this procedure
        // finishes, keeping in the same time a valid RBT.
        void rollRedDownToLeft  () {
            print ("roll red to left " + val);
            assert left.color == RED;
            print ("{<<}", root);
        }

        void rollRedDown () {
            print ("roll red down " + val);
            if (left.color == RED) rotateRight ();
            else if (isRoot ()) color = RED;
            else if (parent.left == this) parent.rollRedDownToLeft ();
            else parent.rollRedDownToRight ();
        }

	//                                k-17-0                     |
	//                                ·                          |
	//                :´´´´´´´´´´´´´´´°¯¯¯¯¯¯¯¦                  |
	//                B-12-0-(*)              r-4-1              |
	//                ·                       ·                  |
	//        ¦¯¯¯¯¯¯¯°¯¯¯¯¯¯¯¦         ¦¯¯¯¯¯°¯¯¯¯¯¯¯¯¯¯¯¦      |
	//        e-4-1           a-4-1     T-1-2             e-2-2  |
	//        ·               ·         ·                 ·      |
	//  ¦¯¯¯¯¯°¯¦       ¦¯¯¯¯¯°¯¦      ¦°¦      :´´´´´´´´´°¦     |
	//  R-1-2   d-1-2   l-1-2   c-1-2  # #      e-1-2-(*)  #     |
	//  ·       ·       ·       ·               ·                |
	// ¦°¦     ¦°¦     ¦°¦     ¦°¦             ¦°¦               |
	// # #     # #     # #     # #             # #               |
        private class Visual {
            char [][] data;
            int point;
            boolean color;
            Visual (char[][] data, int point) { this.data = data; this.point = point; }
            Visual (String str) { data = new char[][]{str.toCharArray() }; point = 0; }
            Visual (int rows, int cols, boolean color) {
                data = new char[rows][cols]; point = 0;
                for (char[] k: data) Arrays.fill (k, ' ');
                this.color = color;
            }
            public String toString () {
                StringBuilder sb = new StringBuilder ();
                for (char[] arr: data ) {
                    sb.append (new String (arr));
                    sb.append ('\n');
                }
                sb.append ("\n");
                return sb.toString ();
            }
            }

        private Visual strRec (int h) {
            assert ((nil == this)
                    || (nil == parent)
                    || (parent.left == this)
                    || (parent.right == this));
            if (equal (nil)) {
		assert weight == 0;
                if (treeHeight < 0) treeHeight = h;
                else if (treeHeight != h) treeHeightError ++;
                return new Visual (val);
            }
            else {
		if (weight != left.weight + right.weight + 1) treeWeightError++;
                nil.color = BLACK;
                if ((color == RED) && (left.color == RED)) treeColorError ++;

                Visual left = this.left.strRec(this.left.color == RED ? h:h+1);
                Visual right = this.right.strRec(this.right.color == RED ? h:h+1);
                char[] val = (this.val + "-" + weight + '-' + h
                              + (color ? "-(*)":"")).toCharArray();
                int leftCols = left.data[0].length;
                int leftRows = left.data.length;
                int rightCols = right.data[0].length;
                int rightRows = right.data.length;
                int valLen = val.length;
                int gap = 1;
                int offset = leftCols + gap;
                int height = 3 + Math.max (leftRows, rightRows);
                int width = offset + Math.max(valLen, rightCols);
                int p1 = left.point;
                int p2 = offset + right.point;
                Visual result = new Visual (height, width, color);
                for (int row = 0; row < leftRows; row++ )
                    for (int col = 0; col < leftCols; col++ )
                        result.data[row+3][col] = left.data[row][col];
                for (int row = 0; row < rightRows; row++ )
                    for (int col = 0; col < rightCols; col++ )
                        result.data[row+3][col+offset] = right.data[row][col];
                for (int i = 0; i < valLen; i++ ) result.data[0][i+leftCols] = val[i];
                // 161  A1    ¡    INVERTED EXCLAMATION MARK
                // 164  A4    ¤    CURRENCY SIGN
                // 166  A6    ¦    BROKEN BAR
                // 168  A8    ¨    DIAERESIS
                // 175  AF    ¯    MACRON
                // 176  B0    °    DEGREE SIGN
                // 180  B4    ´    ACUTE ACCENT
                // 183  B7    ·    MIDDLE DOT
                char leftNode = (left.color==RED ? (char) 180:175);
                char rightNode = (right.color==RED ? (char) 168:175);
                for (int i = p1+1; i < leftCols; i++ ) result.data[2][i] = leftNode ;
                for (int i = leftCols+1; i < p2; i++ ) result.data[2][i] = rightNode;
                result.data[2][p1] = (left.color == RED) ? ':':166;
                // In right side, it should never be any red link
                if (right.color == RED) treeColorError ++;
                result.data[2][p2] = (right.color == RED) ? (char) 'X':166; // error RED
                result.data[1][leftCols] = 183;
                result.data[2][leftCols] = 176;
                result.point = leftCols;
                return result;
            }
        }

        public String toString () {
            nil.color = RED;
            treeHeight = -1;
            treeHeightError = treeColorError = treeWeightError = 0;
            String tree = strRec(0).toString ();
            tree = tree.replaceAll ("\n", " |\n ");
            tree = tree.replaceAll ("^", "\n ");
            tree = tree.replaceAll ("  [|]\n $", "");
            if (treeHeightError > 0) print ("tree height error ! " + treeHeightError);
            if (treeColorError > 0) print ("tree color error ! " + treeColorError);
            if (treeWeightError > 0) print ("tree weight error ! " + treeWeightError);
	    nil.color = BLACK;
            return tree;
        }

        public String toString2 () {
            if (equal (nil)) return "";
            StringBuilder sb = new StringBuilder ();
            sb.append (left.toString2 ());
            sb.append (val);
            sb.append (right.toString2 ());
            return sb.toString ();
        }

        public void toString3 () {
            String s = toString2 ();
            s = s.replaceAll ("(.{1000})", "$1\n");
            print (s);
        }
        }

/********************************************************************/
// INSERTION

    Node setRoot (String value) {
	return this.root = new Node (nil, nil, nil, value, BLACK, 1);
    }

    Node insert (String value, Node where, boolean dir) {
	assert nil.isBlack ();
        Node newNode = new Node (where, nil, nil, value, RED, 1);
	Node n;
        if (dir == RIGHT) where.right = newNode;
	else              where.left  = newNode;
	n = where;
	for (; n != nil; n = n.parent) {
	    if (n.left.isBlack() && n.right.isRed    ()) n = n.rotateLeft ();
	    if (n.left.isRed  () && n.left.left.isRed()) n = n.rotateRight();
	    if (n.left.isRed  () && n.right.isRed    ()) n = n.flipColors ();
	    if (n.color == BLACK) break;
	}
	for (; n != nil; n = n.parent) n.setWeight ();
        root.color = BLACK;
        // print ("<<--", root);
        return newNode;
    }

/********************************************************************/
// DELETE

    void rollRedDown (Node n) {
	print ("___________________________________________"+
	       "___________________________________________" );
	print ("```````````````````````````````````````````"+
	       "```````````````````````````````````````````" );
	print ("roll red " + n.val);
	print (root);
	int p = 0;
	Node k;

	if (   root.left.isBlack      () && root.right.isBlack ()
	    && root.left.left.isBlack () && root.right.left.isBlack ())
	    root.color = RED;

	assert (n.left == nil) && (n.right == nil);
	assert root.parent == nil;

	for(k = n; k.isBlack (); k = k.parent) {
	    //print ("()", k.val);
	    Node parent = k.parent;
	    boolean direction = parent.left == k ? LEFT:RIGHT;
	    stack [p++] = direction;
	    print ("direction", direction == LEFT ? "LEFT" : "RIGHT",
		   k.val);
	    if (LEFT == direction) {
		if (parent.right.left.isRed ()) {
		    if (parent.right.left.color == RED) {
			print ("case A&B", parent.val);
			print ("<>", k);
			k.rollRedLeft12 ();
			k = k.left;
			p--;
			break;
		    }
		}
		else if (parent.isRed ()) {
		    print ("case C", parent.val);
		    break;
		}
	    }
	    else { // direction: RIGHT
		if (parent.color == RED) {
		    if (parent.left.left.isRed ()) {
			print ("case H", parent.val);
			parent.flipColors ();
			parent.rotateRight ();
			parent.parent.flipColors ();
			k = k.left;
			p--;
		    }
		    else {
			print ("case G", parent.val);
		    }
		    break;
		}
		else if (parent.left.color == RED) {
		    if (parent.left.right.left.color == RED) {
			print ("case E", parent.val);
			k. rollRedRight22 ();
			k = k.right;
			p--;
			break;
		    }
		    else {
			print ("case D", parent.val);
			parent.rotateRight ();
			break;
		    }
		}
		else {
		    if ((parent.left.left.color == RED)) {
			print ("case F");
			k.rollRedRight21x ();
			k = k.right;
			p--;
			break;
		    }
		}
	    }
	}

	//print ("<:>", root);
	k = k.parent;
	int i;
	Node w;
	for (i = p-1; i >= 0; i--) {
	    boolean direction = stack[i];
	    //print ("%%", i, direction == LEFT ? "L":"R" , k.val);
	    Node q = k;
	    k.flipColors ();
	    if (LEFT == direction) {
		w = k.rotateLeft ();
		k = k.left;
	    }
	    else {
		k = k.right;
	    }
            if (q.is4LeftLeft ()) q.rotateRight().flipColors ();
	}

	print ("...", root);
	k = n.parent;
	n.transplant(nil);

	if (root.isRed ()) root.color = BLACK;
	print (">>>", root);
    }

/********************************************************************/
// HELPER FUNCTIONS

    static void print (Object...args) {
        for (Object x: args) System.out.printf ("%s ", x);
        System.out.println ();
    }

/********************************************************************/
// TEST UNIT

    public static void main (String[] argv) {
        long start = System.nanoTime();
        RBT tab = new RBT ();
	Node x;

	// BufferedInputStream in = new BufferedInputStream(System.in);
	// byte[] data = null;
	// try {
	//     data = new byte[in.available ()];
	//     in.read(data, 0, data.length);
	// }
	// catch (IOException ioe) {System.err.println ("input error");}
        // // // TEST1
        // String c = Character.toString((char) data[0]);
        // Node n = tab.setRoot (c);
	// int i = 0;
        // while (++i < data.length) {
        //     c = Character.toString((char) data[i]);
	//     print (c);
        //     n = tab.insert (c, n, tab.RIGHT);
	//     // print (tab.root);
        // }
        // // RBT.print (tab.root.rank (888));
        // // tab.root.toString3 ();
        // print ("INITIAL", tab.root);
	// // tab.rollRedDown (tab.root.right.right.right);
	// // tab.rollRedDown (tab.root.left.right.right.right);
	// // tab.rollRedDown (tab.root.left.left.left.left);
	// tab.rollRedDown (tab.root.left.right.left.left);
	// tab.rollRedDown (tab.root.left.left.right.right);
	// print ("...>>", tab.root);

        // TEST 2
	String test = "RED-BLACK-TREEabcde";
	Node n = tab.root;
	for (char w: test.toCharArray()) {
	    n = tab.insert (Character.toString (w), n, tab.LEFT);
	}
	print ("INITIAL", tab.root);

	Node o = tab.root;
	tab.rollRedDown (o.right.right.right);

	o = tab.root;
	tab.rollRedDown (o.right.left.right);

	o = tab.root;
	tab.rollRedDown (o.right.right.left);

	o = tab.root;
	tab.rollRedDown (o.left.right.right);

	o = tab.root;
	tab.rollRedDown (o.left.right.right);

	o = tab.root;
	tab.rollRedDown (o.right.left);

	o = tab.root;
	tab.rollRedDown (o.right.right.left);

	o = tab.root;
	tab.rollRedDown (o.left.left.right);

	o = tab.root;
	tab.rollRedDown (o.right.right);

	o = tab.root;
	tab.rollRedDown (o.right.left);

	o = tab.root;
	tab.rollRedDown (o.right.left);

	o = tab.root;
	tab.rollRedDown (o.left.left.left);

	o = tab.root;
	tab.rollRedDown (o.left.left);

	o = tab.root;
	tab.rollRedDown (o.left.right);

	o = tab.root;
	tab.rollRedDown (o.left.left);

	o = tab.root;
	tab.rollRedDown (o.left);

	o = tab.root;
	tab.rollRedDown (o.left);

	o = tab.root;
	tab.rollRedDown (o.left);

        print ("FINAL", tab.root);
        long elapsedTime = System.nanoTime() - start;
        RBT.print (elapsedTime / 10e8);
    }
    }
