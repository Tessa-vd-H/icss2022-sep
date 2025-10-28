package nl.han.ica.datastructures;

public class HANLinkedList<T> implements IHANLinkedList<T> {

    private ListNode<T> header;

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

    @Override
    public void clear() {
        header = null;
    }

    @Override
    public void insert(int index, T value) {
        if (index == 0) {
            addFirst(value);
            return;
        }

        var current = header;
        ListNode<T> previous = null;
        int i = 0;

        while (i < index && current != null) {
            previous = current;
            current = current.getNext();
            i++;
        }

        var newNode = new ListNode<>(value);

        if (current != null) {
            // Node invoegen tussen bestaande nodes
            newNode.setNext(current);
            if (previous != null) {
                previous.setNext(newNode);
            }
        } else if (previous != null) {
            // Node toevoegen aan het einde
            previous.setNext(newNode);
        }
    }

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

        while (i < pos && current != null) {
            previous = current;
            current = current.getNext();
            i++;
        }

        if (current == null || previous == null) {
            return;
        }

        // Node verwijderen
        previous.setNext(current.getNext());
    }

    @Override
    public T get(int pos) {
        var current = header;
        int i = 0;

        while (i < pos && current != null) {
            current = current.getNext();
            i++;
        }

        return (current != null) ? current.getValue() : null;
    }

    @Override
    public void removeFirst() {
        if (header != null) {
            header = header.getNext();
        }
    }

    @Override
    public T getFirst() {
        return (header != null) ? header.getValue() : null;
    }

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
