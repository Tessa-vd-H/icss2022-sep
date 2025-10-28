package nl.han.ica.datastructures;

public class HANQueue<T> implements IHANQueue<T> {

    private final HANLinkedList<T> list = new HANLinkedList<>();

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean isEmpty() {
        return list.getSize() == 0;
    }

    @Override
    public void enqueue(T value) {
        if (list.getSize() == 0) {
            list.addFirst(value);
        } else {
            list.insert(list.getSize(), value);
        }
    }

    @Override
    public T dequeue() {
        T value = list.getFirst();
        list.removeFirst();
        return value;
    }

    @Override
    public T peek() {
        return list.getFirst();
    }

    @Override
    public int getSize() {
        return list.getSize();
    }
}
