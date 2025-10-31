package nl.han.ica.datastructures;

public class ListNode<T> {
    // The value stored in this node
    private T value;

    // Reference to the next node in the list
    private ListNode<T> next;

    // Constructor to create a new node with a given value
    public ListNode(T value) {
        this.value = value;
        this.next = null;
    }

    // Returns the value stored in this node
    public T getValue() {
        return value;
    }

    // Updates the value of this node
    public void setValue(T value) {
        this.value = value;
    }

    // Returns the next node in the list
    public ListNode<T> getNext() {
        return next;
    }

    // Sets the next node in the list
    public void setNext(ListNode<T> next) {
        this.next = next;
    }
}
