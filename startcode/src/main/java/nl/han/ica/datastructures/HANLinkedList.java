package nl.han.ica.datastructures;

public class HANLinkedList<T> implements IHANLinkedList<T> {

    // Reference to the first node in the list
    private ListNode<T> header;

    // Adds a new element to the beginning of the list
    @Override
    public void addFirst(T value) {
        if (header == null) {
            header = new ListNode<>(value);
        } else {
            var newNode = new ListNode<>(value);
            newNode.setNext(header);
            header = newNode;
        }
    }

    // Removes all elements from the list
    @Override
    public void clear() {
        header = null;
    }

    // Inserts a new element at a given position in the list
    @Override
    public void insert(int index, T value) {
        if (index == 0) {
            addFirst(value);
            return;
        }

        var current = header;
        ListNode<T> previous = null;
        int i = 0;

        // Traverse until the desired index or the end of the list
        while (i < index && current != null) {
            previous = current;
            current = current.getNext();
            i++;
        }

        var newNode = new ListNode<>(value);

        if (current != null) {
            // Insert node between existing nodes
            newNode.setNext(current);
            if (previous != null) {
                previous.setNext(newNode);
            }
        } else if (previous != null) {
            // Add node at the end if index is out of range
            previous.setNext(newNode);
        }
    }

    // Deletes a node at a specific position in the list
    @Override
    public void delete(int pos) {
        if (header == null) {
            return;
        }

        if (pos <= 0) {
            removeFirst();
            return;
        }

        var current = header;
        ListNode<T> previous = null;
        int i = 0;

        // Traverse until the target position or end of the list
        while (i < pos && current != null) {
            previous = current;
            current = current.getNext();
            i++;
        }

        // If position is valid, remove the node
        if (current == null || previous == null) {
            return;
        }

        previous.setNext(current.getNext());
    }

    // Returns the element at a specific position
    @Override
    public T get(int pos) {
        var current = header;
        int i = 0;

        // Traverse until the target position
        while (i < pos && current != null) {
            current = current.getNext();
            i++;
        }

        return (current != null) ? current.getValue() : null;
    }

    // Removes the first element from the list
    @Override
    public void removeFirst() {
        if (header != null) {
            header = header.getNext();
        }
    }

    // Returns the first element without removing it
    @Override
    public T getFirst() {
        return (header != null) ? header.getValue() : null;
    }

    // Returns the total number of elements in the list
    @Override
    public int getSize() {
        int count = 0;
        var current = header;

        while (current != null) {
            count++;
            current = current.getNext();
        }

        return count;
    }
}
