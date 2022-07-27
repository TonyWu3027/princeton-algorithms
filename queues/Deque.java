import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic Deque (double-ended queue) implemented with double-sided linked list
 * @param <Item> a generic type
 */
public class Deque<Item> implements Iterable<Item> {

    /**
     * A node in a generic linked list
     */
    private class Node {
        Node prev; // the previous node
        Node next; // the next node
        Item item; // the key at this node
    }

    private int n; // size of the deque
    private Node first; // the first node
    private Node last; // the last node

    /**
     * Construct an empty deque
     */
    public Deque() {
        this.n = 0;
        this.first = null;
        this.last = null;
    }

    /**
     * Is the deque empty?
     * @return true if empty, vice versa
     */
    public boolean isEmpty() {
        return this.n == 0;
    }

    /**
     * Return the number of items on the deque
     * @return the number of items on the deque
     */
    public int size() {
        return this.n;
    }

    // add the item to the front

    /**
     * Add the item to the front
     * @param item an item to be added
     * @throws IllegalArgumentException if item is null
     */
    public void addFirst(Item item) {
        // validate input
        if (item == null) throw new IllegalArgumentException("Item should not be null");

        // add item to the front
        Node oldFirst = this.first;
        this.first = new Node();
        this.first.item = item;
        this.first.next = oldFirst;
        this.first.prev = null;

        // increment item counter
        this.n++;



        // if there is only one item,
        // the first and the last node are the same node
        if (this.n == 1) {
            this.last = this.first;
        } else {
            /**
             * IMPORTANT:
             * since a two-sided linked list is used,
             * do not forget to set the prev of old first to the new first
             */
            oldFirst.prev = this.first;
        }


    }

    /**
     * Add the item to the back
     * @param item an item to be added
     * @throws IllegalArgumentException if item is null
     */
    public void addLast(Item item) {
        // validate input
        if (item == null) throw new IllegalArgumentException("Item should not be null");

        // add item to the back
        Node oldLast = this.last;
        this.last = new Node();
        this.last.item = item;
        this.last.prev = oldLast;
        this.last.next = null;

        // increment item counter
        this.n++;

        // if there is only one item,
        // the first and the last node are the same node
        if (this.n == 1) {
            this.first = this.last;
        } else {
            /**
             * IMPORTANT:
             * since a two-sided linked list is used,
             * do not forget to set the next of old last to the new last
             */
            oldLast.next = this.last;
        }
    }

    /**
     * Remove and return the item from the front
     * @return the first item
     * @throws NoSuchElementException if the deque is empty
     */
    public Item removeFirst() {
        // check for underflow
        if (this.isEmpty()) throw new NoSuchElementException("Deque underflow");

        // remove the first item
        Item item = this.first.item;
        this.first = this.first.next;
        this.n--;

        // if the deque is empty,
        // set both first and last as null
        if (this.isEmpty()) {
            this.last = null;
        } else {
            this.first.prev = null;
        }

        return item;
    }

    /**
     * Remove and return the item from the back
     * @return the last item
     * @throws NoSuchElementException if the deque is empty
     */
    public Item removeLast() {
        // check for underflow
        if (this.isEmpty()) throw new NoSuchElementException("Deque underflow");

        // remove the last item
        Item item = this.last.item;
        this.last = this.last.prev;
        this.n--;

        // if the deque is empty,
        // set both first and last as null
        if (this.isEmpty()) {
            this.first = null;
        } else {
            this.last.next = null;
        }

        return item;
    }

    /**
     * Return an iterator over items in order from front to back
     * @return an iterator over items in order from front to back
     */
    public Iterator<Item> iterator() {
        return new DequeueIterator();
    }

    private class DequeueIterator implements Iterator<Item> {

        private Node current = first; // the node in current iteration

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() { /* not supported */ }

    }

    // unit testing (required)
    public static void main(String[] args) {
        StdOut.println("Create a new Deque of String");
        Deque<String> dq = new Deque<>();

        StdOut.printf("Deque is empty=%b, expecting true\n", dq.isEmpty());
        StdOut.printf("Deque size is %d, expecting 0\n", dq.size());

        StdOut.println("\nAdding 'hello' to the front");
        dq.addFirst("Hello");
        StdOut.printf("Deque is empty=%b, expecting false\n", dq.isEmpty());
        StdOut.printf("Deque size is %d, expecting 1\n", dq.size());

        StdOut.println("\nRemoving 'Hello' to the front");
        String hello = dq.removeFirst();
        StdOut.printf("First item is '%s', expecting 'Hello'\n", hello);
        StdOut.printf("Deque size is %d, expecting 0\n", dq.size());

        StdOut.println("\nAdding 'world' to the back and removing");
        dq.addLast("world");
        String world = dq.removeLast();
        StdOut.printf("Last item is '%s', expecting 'world'\n", world);
        StdOut.printf("Deque size is %d, expecting 1\n", dq.size());

        StdOut.println("\nAdding 'Hello world' from the front and iterating");
        dq.addFirst("world");
        dq.addFirst("Hello");
        StdOut.printf("Deque size is %d, expecting 2\n", dq.size());
        for (String s: dq) {
            StdOut.println(s);
        }
        StdOut.println("Expecting 'Hello world'");

        StdOut.println("\nCleaning the deque");
        dq.removeFirst();
        dq.removeLast();
        StdOut.printf("Deque size is %d, expecting 0\n", dq.size());

        StdOut.println("\nAdding 'Hello world' from the back and iterating");
        dq.addLast("Hello");
        dq.addLast("world");
        StdOut.printf("Deque size is %d, expecting 2\n", dq.size());
        for (String s: dq) {
            StdOut.println(s);
        }
        StdOut.println("Expecting 'Hello world'");

        StdOut.println("\nCleaning the deque");
        dq.removeFirst();
        dq.removeLast();
        StdOut.printf("Deque size is %d, expecting 0\n", dq.size());

        StdOut.println("\nAdding 'Hello world' from the front then back and iterating");
        dq.addFirst("Hello");
        dq.addLast("world");
        StdOut.printf("Deque size is %d, expecting 2\n", dq.size());
        for (String s: dq) {
            StdOut.println(s);
        }
        StdOut.println("Expecting 'Hello world'");
    }

}
