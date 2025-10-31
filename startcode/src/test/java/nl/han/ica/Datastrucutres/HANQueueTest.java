package nl.han.ica.Datastrucutres;

import nl.han.ica.datastructures.HANQueue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HANQueueTest {

    @Test
    public void testIsEmpty() {
        var queue = new HANQueue<String>();
        // Newly created queue should be empty
        assertTrue(queue.isEmpty());
    }

    @Test
    void testIsEmpty_notEmpty() {
        var queue = new HANQueue<String>();
        queue.enqueue("first"); // Add element
        // Queue is no longer empty
        assertFalse(queue.isEmpty());
    }

    @Test
    void testEnqueue() {
        var queue = new HANQueue<String>();
        queue.enqueue("first"); // Add element
        // Peek should return the first element enqueued
        assertEquals("first", queue.peek());
    }

    @Test
    void testDequeue() {
        var queue = new HANQueue<String>();
        queue.enqueue("first");  // Enqueue first element
        queue.enqueue("second"); // Enqueue second element

        // Dequeue should return the first element
        assertEquals("first", queue.dequeue());
        // Peek now returns the next element in queue
        assertEquals("second", queue.peek());
    }

    @Test
    void testDequeue_empty() {
        var queue = new HANQueue<String>();
        // Dequeue on empty queue should return null
        assertNull(queue.dequeue());
    }

    @Test
    void testClear() {
        var queue = new HANQueue<String>();
        queue.enqueue("first");
        queue.enqueue("second");

        queue.clear(); // Clear all elements
        // Queue should be empty after clear
        assertTrue(queue.isEmpty());
    }

    @Test
    void testPeek() {
        var queue = new HANQueue<String>();
        queue.enqueue("first");
        queue.enqueue("second");

        // Peek returns first element without removing it
        assertEquals("first", queue.peek());
        queue.dequeue(); // Remove first element
        // Peek now returns second element
        assertEquals("second", queue.peek());
    }

    @Test
    void testPeek_empty() {
        var queue = new HANQueue<String>();
        // Peek on empty queue should return null
        assertNull(queue.peek());
    }

    @Test
    void testGetSize() {
        var queue = new HANQueue<String>();
        queue.enqueue("first");
        queue.enqueue("second");

        // Size should match number of elements enqueued
        assertEquals(2, queue.getSize());
    }
}
