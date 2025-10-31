package nl.han.ica.datastructures;

public class HANQueue<T> implements IHANQueue<T> {

    // Internal linked list used to store queue elements
    private final HANLinkedList<T> list = new HANLinkedList<>();

    // Removes all elements from the queue
    @Override
    public void clear() {
        list.clear();
    }

    // Returns true if the queue is empty
    @Override
    public boolean isEmpty() {
        return list.getSize() == 0;
    }

    // Adds a new element to the back of the queue
    @Override
    public void enqueue(T value) {
        if (list.getSize() == 0) {
            list.addFirst(value);
        } else {
            list.insert(list.getSize(), value);
        }
    }

    // Removes and returns the element at the front of the queue
    @Override
    public T dequeue() {
        T value = list.getFirst();
        list.removeFirst();
        return value;
    }

    // Returns the element at the front of the queue without removing it
    @Override
    public T peek() {
        return list.getFirst();
    }

    // Returns the number of elements currently in the queue
    @Override
    public int getSize() {
        return list.getSize();
    }
}
