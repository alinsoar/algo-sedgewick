import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] el;
    private int size;

    public RandomizedQueue() {
        // construct an empty randomized queue
        el = (Item[]) new Object[1];
        size = 0;
    }

    private void resize(int cap) {
        Item[] copy = (Item[]) new Object[cap];
        for (int i = 0; i < size; i++)
            copy[i] = el[i];
        el = copy;
    }

    public boolean isEmpty() {
        // is the queue empty?
        return (size == 0);
    }

    public int size() {
        // return the number of items on the queue
        return size;
    }

    public void enqueue(Item item) {
        // add the item
        if (item == null) throw new java.lang.NullPointerException();
        if (el.length < ++size + 1)
            resize(2 * el.length);
        el[size-1] = item;
    }

    public Item dequeue() {
        // delete and return a random item
        if (size == 0) throw new java.util.NoSuchElementException();
        int ind = StdRandom.uniform(0, size);
        Item item = el[ind];
        el[ind] = el[size-1];
        el[--size] = null;
        if ((size > 0) && (size == (el.length / 4)))
            resize(el.length / 2);
        return item;
    }

    public Item sample() {
        // return (but do not delete) a random item
        if (size == 0) throw new java.util.NoSuchElementException();
        int ind = StdRandom.uniform(0, size);
        return el[ind];
    }

    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current;
        private int[] p;

        RandomizedQueueIterator() {
            p = new int[size];
            for (int i = 0; i < size; i++)
                p[i] = i;
            StdRandom.shuffle(p);
            current = 0;
        }
        public boolean hasNext() { return current < size; }
        public void remove() { throw new UnsupportedOperationException();  }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return el[p[current++]];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> test = new RandomizedQueue<Integer>();
        test.enqueue(10);
        test.enqueue(20);
        test.enqueue(30);
        test.enqueue(40);
        test.enqueue(50);
        test.enqueue(60);
        for (int t:test) StdOut.println("++" + t);
        StdOut.println("" + test.dequeue()+"-"+test.size());
        StdOut.println("" + test.dequeue()+"-"+test.size());
        StdOut.println("" + test.dequeue()+"-"+test.size());
        StdOut.println("" + test.dequeue()+"-"+test.size());
        StdOut.println("" + test.dequeue()+"-"+test.size());
        StdOut.println("" + test.dequeue()+"-"+test.size());
        StdOut.println("" + test.dequeue()+"-"+test.size());
        StdOut.println("" + test.dequeue()+"-"+test.size());
        StdOut.println("" + test.dequeue()+"-"+test.size());
        //StdOut.println("" + test.dequeue());
        for (int t:test) StdOut.println("++" + t);
    }
}
