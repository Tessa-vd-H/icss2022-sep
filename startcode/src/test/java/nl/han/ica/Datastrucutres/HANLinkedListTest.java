package nl.han.ica.Datastrucutres;

import nl.han.ica.datastructures.HANLinkedList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HANLinkedListTest {

    @Test
    void testAddAndGetFirst() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        // First element added should be retrievable as first
        assertEquals("first", list.getFirst());
    }

    @Test
    void testAddAndGetFirst_NoHeader() {
        var list = new HANLinkedList<String>();
        // Empty list should return null for getFirst
        assertNull(list.getFirst());
    }

    @Test
    void testAddAndGetFirst_LongerList() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.addFirst("second");
        // Newest added element becomes first
        assertEquals("second", list.getFirst());
    }

    @Test
    void testClear() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(3, "second");

        list.clear();
        // After clear, list should be empty
        assertNull(list.getFirst());
    }

    @Test
    void testInsert() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");
        // Insert at end works
        assertEquals("second", list.get(1));
    }

    @Test
    void testInsert_inBetween() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");
        list.insert(1, "second again");
        // Insertion in between shifts elements
        assertEquals("second again", list.get(1));
        assertEquals("second", list.get(2));
    }

    @Test
    void testInsert_skippedIndex() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(3, "second");
        // Skipping index inserts at end
        assertEquals("second", list.get(1));
    }

    @Test
    void testInsert_header() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(0, "second");
        // Insertion at index 0 updates header
        assertEquals("second", list.getFirst());
    }

    @Test
    void testDelete() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");

        list.delete(0);
        // Deleting first element updates header
        assertEquals("second", list.get(0));
    }

    @Test
    void testDelete_deleteFirst() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");

        list.delete(0);
        // Deleting first element works
        assertEquals("second", list.get(0));
    }

    @Test
    void testDelete_outOfBounds() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");

        list.delete(4);
        // Out-of-bounds deletion does not break list
        assertEquals("second", list.get(1));
    }

    @Test
    void testGet() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        // Get returns correct element
        assertEquals("first", list.get(0));
    }

    @Test
    void testGet_outOfBounds() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        // Get out of bounds returns null
        assertNull(list.get(4));
    }

    @Test
    void testRemoveFirst() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");

        list.removeFirst();
        // Removing first shifts header
        assertEquals("second", list.getFirst());
    }

    @Test
    void testRemoveFirst_header() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.removeFirst();
        // Removing only element leaves list empty
        assertNull(list.getFirst());
    }

    @Test
    void testGetFirst() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        // getFirst returns header value
        assertEquals("first", list.getFirst());
    }

    @Test
    void testGetFirst_noHeader() {
        var list = new HANLinkedList<String>();
        // Empty list returns null
        assertNull(list.getFirst());
    }

    @Test
    void testGetSize() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");
        // getSize returns number of elements
        assertEquals(2, list.getSize());
    }
}
