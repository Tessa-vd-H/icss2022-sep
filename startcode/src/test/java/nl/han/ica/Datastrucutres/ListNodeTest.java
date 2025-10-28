package nl.han.ica.Datastrucutres;

import nl.han.ica.datastructures.ListNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListNodeTest {
    private final ListNode<Integer> node = new ListNode<>(5);

    @Test
    void testGetValue() {
        assertEquals(5, node.getValue());
    }

    @Test
    void testSetValue() {
        node.setValue(10);
        assertEquals(10, node.getValue());
    }

    @Test
    void testSetAndGetNext() {
        var nextNode = new ListNode<>(10);
        node.setNext(nextNode);

        assertEquals(nextNode, node.getNext());
        assertEquals(10, node.getNext().getValue());
    }
}
