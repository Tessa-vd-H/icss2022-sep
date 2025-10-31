package nl.han.ica.datastructures;

import nl.han.ica.datastructures.ListNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListNodeTest {

    // Create a node with initial value 5
    private final ListNode<Integer> node = new ListNode<>(5);

    @Test
    void testGetValue() {
        // Verify that getValue() returns the value passed in constructor
        assertEquals(5, node.getValue());
    }

    @Test
    void testSetValue() {
        // Change the node's value and verify it updated correctly
        node.setValue(10);
        assertEquals(10, node.getValue());
    }

    @Test
    void testSetAndGetNext() {
        // Create a new node and set it as next
        var nextNode = new ListNode<>(10);
        node.setNext(nextNode);

        // Verify that getNext() returns the correct node
        assertEquals(nextNode, node.getNext());

        // Verify that the value of the next node is correct
        assertEquals(10, node.getNext().getValue());
    }
}
