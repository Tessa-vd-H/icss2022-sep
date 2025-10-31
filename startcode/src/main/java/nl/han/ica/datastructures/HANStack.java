package nl.han.ica.datastructures;

import java.util.LinkedList;
import java.util.EmptyStackException;

public class HANStack<T> implements IHANStack<T> {
    // Internal list used to store the stack elements
    private LinkedList<T> list = new LinkedList<>();

    // Pushes a new element onto the top of the stack
    public void push(T value) {
        list.add(value);
    }

    // Removes and returns the top element of the stack
    // Throws EmptyStackException if the stack is empty
    public T pop() {
        if (list.size() == 0) {
            throw new EmptyStackException();
        }
        return list.remove(list.size() - 1);
    }

    // Returns the top element of the stack without removing it
    // Throws EmptyStackException if the stack is empty
    public T peek() {
        if (list.size() == 0) {
            throw new EmptyStackException();
        }
        return list.get(list.size() - 1);
    }

    // Checks if the stack is empty
    @Override
    public boolean isEmpty() {
        // Should return true when list is empty
        return list.isEmpty();
    }
}
