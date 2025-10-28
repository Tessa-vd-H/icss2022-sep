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
        assertEquals("first", list.getFirst());
    }

    @Test
    void testAddAndGetFirst_NoHeader() {
        var list = new HANLinkedList<String>();
        assertNull(list.getFirst());
    }

    @Test
    void testAddAndGetFirst_LongerList() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.addFirst("second");

        assertEquals("second", list.getFirst());
        assertEquals("first", list.getFirst());
    }

    @Test
    void testClear() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(3, "second");

        list.clear();

        assertNull(list.getFirst());
        assertNull(list.getFirst());
    }

    @Test
    void testInsert() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");

        assertEquals("second", list.get(1));
    }

    @Test
    void testInsert_inBetween() {
      var list = new HANLinkedList<String>();
      list.addFirst("first");
      list.insert(1, "second");
      list.insert(1, "second again");

      assertEquals("second again", list.get(1));
      assertEquals("second", list.get(2));
    }

    @Test
    void testInsert_skippedIndex() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(3, "second");

        assertEquals("second", list.get(1));
    }

    @Test
    void testInsert_header() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(0, "second");

        assertEquals("second", list.getFirst());
    }

    @Test
    void testDelete() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");

        list.delete(0);

        assertEquals("second", list.get(0));
    }

    @Test
    void testDelete_deleteFirst() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");

        list.delete(0);

        assertEquals("second", list.get(0));
    }

    @Test
    void testDelete_outOfBounds() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");

        list.delete(4); // negatieve getallen verwijderen het eerste element
        assertEquals("second", list.get(1));
    }

    @Test
    void testGet() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");

        assertEquals("first", list.get(0));
    }

    @Test
    void testGet_outOfBounds() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");

        assertNull(list.get(4));
    }

    @Test
    void testRemoveFirst() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");

        list.removeFirst();

        assertEquals("second", list.getFirst());
    }

    @Test
    void testRemoveFirst_header() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.removeFirst();

        assertNull(list.getFirst());
    }

    @Test
    void testGetFirst() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");

        assertEquals("first", list.getFirst());
    }

    @Test
    void testGetFirst_noHeader() {
        var list = new HANLinkedList<String>();
        assertNull(list.getFirst());
    }

    @Test
    void testGetSize() {
        var list = new HANLinkedList<String>();
        list.addFirst("first");
        list.insert(1, "second");

        assertEquals(2, list.getSize());
    }
}
