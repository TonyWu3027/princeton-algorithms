import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] arr; // a resizing array of items on Randomized Queue
    private int n; // size of the RQ (and the index of the next available entry in arr)

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        this.arr = (Item[]) new Object[1];
        this.n = 0;
    }

    /**
     * Is the randomized queue empty?
     * @return true if empty, vice versa
     */
    public boolean isEmpty() {
        return this.n == 0;
    }

    /**
     * Return the number of items on the randomized queue
     * @return the number of items on the randomized queue
     */
    public int size() {
        return this.n;
    }

    /**
     * Add the item to the randomized queue
     * @param item an item of the generic type
     * @throws IllegalArgumentException if item is null
     */
    public void enqueue(Item item) {
        // validate input
        if (item == null) throw new IllegalArgumentException("Item should not be null");

        // double the array if full
        if (this.n == this.arr.length) resizing(2*this.arr.length);
        this.arr[this.n++] = item;
    }

    /**
     * Resize the item array to a given capacity
     * @param capacity capacity of the array after resizing
     */
    private void resizing(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];

        for (int i = 0; i < this.n; i++) {
            temp[i] = this.arr[i];
        }

        this.arr = temp;
    }

    /**
     * Remove and return a random item
     * @return a random item
     * @throws NoSuchElementException if the RQ is empty
     */
    public Item dequeue() {
        // check for underflow
        if (this.isEmpty()) throw new NoSuchElementException("Randomized queue underflow");

        // generate a random array index
        int index = StdRandom.uniform(this.n);

        // retrieve the item
        Item item = this.arr[index];

        // fill the blank in the array
        for (int i = index; i < this.n-1; i++) {
            this.arr[i] = this.arr[i+1];
        }

        // decrement queue size and avoid loitering
        this.arr[--n] = null;

        // half the array if n = 1/4 array length and the array is not empty
        if (this.n > 0 && this.arr.length / 4 == this.n) resizing(this.arr.length / 2);

        return item;
    }

    /**
     * Return a random item (but do not remove it)
     * @return a random item
     * @throws NoSuchElementException if the RQ is empty
     */
    public Item sample() {
        // check for underflow
        if (this.isEmpty()) throw new NoSuchElementException("Randomized queue underflow");

        int index = StdRandom.uniform(this.n);
        return this.arr[index];
    }

    /**
     * Return an independent iterator over items in random order
     * @return an independent iterator over items in random order
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Item[] shuffledArr; // a shuffled copy of the RQ
        private int index; // the index of the current item

        public RandomizedQueueIterator() {
            this.shuffledArr = (Item[]) new Object[n];

            // copying the entries in the arr to the new arr
            for (int i = 0; i < n; i++) {
                this.shuffledArr[i] = arr[i];
            }

            // shuffle the new arr
            StdRandom.shuffle(this.shuffledArr);

            this.index = n;
        }

        public boolean hasNext() {
            return this.index > 0;
        }

        public Item next() {
            if (!this.hasNext()) throw new NoSuchElementException("No next item");

            return this.shuffledArr[--this.index];
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove() is not supported");
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();

        StdOut.printf("Queue is empty=%b, expecting true\n", queue.isEmpty());
        StdOut.printf("Queue size is %d, expecting 0\n", queue.size());

        StdOut.println("\nEnqueuing 1 2 3 4 5");
        for (int i = 0; i < n; i++)
            queue.enqueue(i);

        StdOut.printf("Queue is empty=%b, expecting false\n", queue.isEmpty());
        StdOut.printf("Queue size is %d, expecting 5\n", queue.size());

        StdOut.println("\nTesting iterator independency");
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
        StdOut.println("Expecting the most of the pairs consist of different integers");

        StdOut.println("\nTesting dequeue()");
        int num1 = queue.dequeue();
        StdOut.printf("Dequeued item: %d\n", num1);
        StdOut.printf("Queue size is %d, expecting 4\n", queue.size());

        StdOut.println("\nTesting sample()");
        int num2 = queue.sample();
        StdOut.printf("Sampled item: %d, expecting %d != %d\n", num2, num2, num1);
        StdOut.printf("Queue size is %d, expecting 4\n", queue.size());

        StdOut.println("\nTesting dequeue() underflow prevention");
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        try {
            queue.dequeue();
        } catch (NoSuchElementException e) {
            StdOut.println("NoSuchElementException thrown as expected");
        }

        StdOut.println("\nTesting sample() underflow prevention");
        try {
            queue.sample();
        } catch (NoSuchElementException e) {
            StdOut.println("NoSuchElementException thrown as expected");
        }

        StdOut.println("\nTesting enqueue() input validation");
        try {
            queue.enqueue(null);
        } catch (IllegalArgumentException e) {
            StdOut.println("IllegalArgumentException thrown as expected");
        }
    }

}
