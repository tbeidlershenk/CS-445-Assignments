import java.util.Random;
import java.util.ArrayList;

public class RandIndexQueue<T> implements MyQ<T>, Indexable, Shufflable {

    private int sz;
    private T [] arr;
    private int moves = 0;
    private int size = 0;
    private int first = 0;

    /** Constructor creating a new RandIndexQueue<T> object
     * @param sz
     */
    public RandIndexQueue (int sz) {
        this.sz = sz;
        T [] temp = (T []) new Object[sz];
        arr = temp;
    }

    /** Constructor copying a new RandIndexQueue<T> object in a non-shallow way
     * @param sz
     */
    public RandIndexQueue (RandIndexQueue<T> old) {
        T [] temp = (T []) new Object[old.sz];
        arr = temp; sz = old.sz;
        for (int i = 0, count = 0; i < sz && count < old.size(); i++, count++) {
            arr[i] = old.get(i);
            size++;
        }
    }

    /** Adds a new entry to the logical back of the queue
     * @param newEntry
     */
    public void enqueue (T newEntry) {
        if (size < sz) {
            for (int i = first; i < sz; i++) {
                if (arr[i] == null) {
                    arr[i] = newEntry;
                    break;
                } else if (i == sz-1) i = -1;
            }
        } else {
            resizeArray();
            arr[first + size] = newEntry;
        }
        moves++; size++;
    }

    /** Removes the element at the logical front of the queue, shifting the front to the right by one
     * @return temp (removed object of type T)
     */
    public T dequeue () {
        if (arr[first] == null) throw new EmptyQueueException();
        T temp = arr[first];
        arr[first] = null;
        if (first < sz-1) first++;
        else first = 0;
        moves++; size--;
        return temp;
    }

    /** Return the element at the front of the queue
     * @return T object
     */
    public T getFront () {
        return arr[first];
    }

    /** Returns the array index representing the front of the queue
     * @return first
     */
    public int getFirst () {
        return first;
    }

    /** Returns true if the queue is empty, false otherwise
     * @return boolean
     */
    public boolean isEmpty () {
        return arr[first] == null;
    }

    /** Clears the queue of all elements
     */
    public void clear () {
        for (int i = 0; i < sz; i++) arr[i] = null;
        size = 0;
    }

    /** Returns the logical size of the queue
     * @return size
     */
    public int size () {
        return size;
    }

    /** Returns the physical size of the queue (array length)
     * @return sz
     */
    public int capacity () {
        return sz;
    }

    /** Returns the number of actions that have been taken on the queue (enqueue, dequeue)
     * @return moves
     */
    public int getMoves () {
        return moves;
    }

    /** Sets the moves of the queue to a value
     * @param moves
     */
    public void setMoves (int moves) {
        this.moves = moves;
    }
    
    // Indexable.java implementation 

    /** Gets the element at the logical index i in the queue, and returns it
     * @param i
     * @return
     */
    public T get (int i) {
        if (size < i+1) throw new IndexOutOfBoundsException();
        int temp = sz - first;
        if (i >= temp) return arr[i - temp];
        else return arr[first + i];
    }

    /** Sets the element at the logical index i as the parameter item
     * @param i
     * @param item
     */
    public void set (int i, Object item) {
        if (size < i+1) throw new IndexOutOfBoundsException();
        T obj = (T) item;
        int temp = sz - first;
        if (i >= temp) arr[i - temp] = obj;
        else arr[first + i] = obj;
    }

    // Shufflable.java implementation

        // Shuffles the queue
    public void shuffle () {
        RandIndexQueue<T> temp = new RandIndexQueue(this);
        ArrayList<Integer> positions = new ArrayList();
        Random rand = new Random();
        for (int i = 0, num; i < size; i++) {
            do num = rand.nextInt(size);
            while (positions.contains(num));
            positions.add(num);
        }
        for (int i = 0; i < size; i++) {
            int index = positions.get(i);
            if (index < sz - first) arr[first + index] = temp.getFront();
            else arr[index + first - sz] = temp.getFront();
            temp.dequeue();         
        }
    }

    /** Checks if the queue is equal to the parameter RandIndexQueue
     * @param rhs
     * @return
     */
    public boolean equals (RandIndexQueue<T> rhs) {
        return toString().equals(rhs.toString());
    }

    /** Stores the elements of the queue in a String variable data and returns it
     * @return data
     */
    public String toString () {
        String data = "Contents: ";
        for (int i = first, count = 0; i < sz && arr[i] != null && count < size; i++, count++) {
            data += arr[i].toString() + " ";
            if (i == sz-1) i = -1;
        }
        return data;
    }

    // Resizes the queue when it fills up (when logical size == physical size and enqueue() is called)
    private void resizeArray () {
        T [] temp = (T []) new Object[2 * sz];
        for (int i = first, count = 0; i < sz && count < size; i++, count++) {
            temp[count + first] = arr[i];
            if (i == sz-1) i = -1;
        }
        arr = temp;
        sz = arr.length;
    }
}