package nl.han.ica.datastructures;

public class HANStack<T> implements IHANStack<T> {

    private final HANLinkedList<T> list = new HANLinkedList<>();

    @Override
    public void push(T value) {
        list.insert(list.getSize(), value);
    }

    @Override
    public T pop() {
        if (list.getSize() == 0) {
            return null;
        }

        int lastIndex = list.getSize() - 1;
        T value = list.get(lastIndex);
        list.delete(lastIndex);
        return value;
    }

    @Override
    public T peek() {
        if (list.getSize() == 0) {
            return null;
        }

        return list.get(list.getSize() - 1);
    }
}
