package nl.han.ica.datastructures;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class HANStack<T> implements IHANStack<T> {
    private ArrayList<T> elements;

    public HANStack() {
        this.elements = new ArrayList<>();
    }

    private boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public void push(T value) {
        elements.add(value);
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        return elements.remove(elements.size() - 1);
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elements.get(elements.size() - 1);
    }
}
