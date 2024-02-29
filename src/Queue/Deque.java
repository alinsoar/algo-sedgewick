import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item info;
        private Node next;
        private Node prev;
        Node(Item item) {
            info = item;
        }
    }
    private Node first, last;
    private int size;

    public Deque() {
        // construct an empty deque
    }

    public boolean isEmpty() {
        // is the deque empty?
        return (size == 0);
    }

    public int size() {
        // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) {
        // insert the item at the front
        if (item == null) throw new java.lang.NullPointerException();
        Node old = first;
        first = new Node(item);
        if (old == null)
            last = first;
        else {
            first.next = old;
            old.prev = first;
        }
        size++;
    }

    public void addLast(Item item) {
        // insert the item at the end
        if (item == null) throw new java.lang.NullPointerException();
        Node old = last;
        last = new Node(item);
        if (old == null)
            first = last;
        else {
            last.prev = old;
            old.next = last;
        }
        size++;
    }

    public Item removeFirst() {
        // delete and return the item at the front
        if (first == null) throw new java.util.NoSuchElementException();
        size--;
        if (first == last) {
            Item info = first.info;
            first.next = null;
            first.prev = null;
            first = null;
            last = null;
            return info;
        }
        Item info = first.info;
        first.next.prev = null;
        first = first.next;
        return info;
    }

    public Item removeLast() {
        // delete and return the item at the end
        if (last == null) throw new java.util.NoSuchElementException();
        size--;
        if (first == last) {
            Item info = first.info;
            first = null;
            last = null;
            return info;
        }
        Item info = last.info;
        last.prev.next = null;
        last = last.prev;
        return info;
    }

    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() { return current != null; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.info;
            current = current.next; 
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<Integer> test = new Deque<Integer>();
        test.addFirst(10);
        test.addLast(20);
        test.addFirst(30);

        for (int t:test) {
            StdOut.println("++"+t);
        }

        // StdOut.println(""+test.isEmpty());
        // StdOut.println(""+test.removeLast());
        // StdOut.println(""+test.removeFirst());
        // StdOut.println(""+test.removeFirst());
        // StdOut.println(""+test.removeLast());
        StdOut.println(""+test.isEmpty());

        while (test.size() > 0) {
            StdOut.println(""+test.removeLast());
        }
        StdOut.println(""+test.isEmpty());
    }
}
