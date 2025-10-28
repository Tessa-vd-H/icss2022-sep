package nl.han.ica.Datastrucutres;

import nl.han.ica.datastructures.HANQueue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HANQueueTest {
    @Test
    public void testIsEmpty() {
        var queue = new HANQueue<String>();
        assertTrue(queue.isEmpty());
    }

    @Test
    void testIsEmpty_notEmpty() {
        var queue = new HANQueue<String>();
        queue.enqueue("first");
        assertFalse(queue.isEmpty());
    }

    @Test
    void testEnqueue() {
        var queue = new HANQueue<String>();
        queue.enqueue("first");
        assertEquals("first", queue.peek());
    }

    @Test
    void testDequeue() {
        var queue = new HANQueue<String>();
        queue.enqueue("first");
        queue.enqueue("second");

        assertEquals("first", queue.dequeue());
        assertEquals("second", queue.peek());
    }

    @Test
    void testDequeue_empty() {
        var queue = new HANQueue<String>();
        assertNull(queue.dequeue());
    }

    @Test
    void testClear() {
        var queue = new HANQueue<String>();
        queue.enqueue("first");
        queue.enqueue("second");

        queue.clear();
        assertTrue(queue.isEmpty());
    }

    @Test
    void testPeek() {
        var queue = new HANQueue<String>();
        queue.enqueue("first");
        queue.enqueue("second");

        assertEquals("first", queue.peek());
        queue.dequeue();
        assertEquals("second", queue.peek());
    }

    @Test
    void testPeek_empty() {
        var queue = new HANQueue<String>();
        assertNull(queue.peek());
    }

    @Test
    void testGetSize() {
        var queue = new HANQueue<String>();
        queue.enqueue("first");
        queue.enqueue("second");

        assertEquals(2, queue.getSize());
    }
}
