package nl.han.ica.Datastrucutres;

import nl.han.ica.datastructures.HANStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HANStackTest {
    @Test
    void testPushAndPeek() {
        var stack = new HANStack<String>();
        stack.push("first");
        assertEquals("first", stack.peek());
    }

    @Test
    void testPushAndPeek_multiple() {
        var stack = new HANStack<String>();
        stack.push("first");
        stack.push("second");

        assertEquals("second", stack.peek());

        stack.push("third");
        assertEquals("third", stack.peek());
    }

    @Test
    void testPopAndPeek() {
        var stack = new HANStack<String>();
        stack.push("first");
        stack.push("second");

        stack.pop();
        assertEquals("first", stack.peek());
    }

    @Test
    void testPopAndPeek_emptyStack() {
        var stack = new HANStack<String>();
        stack.pop();
        assertNull(stack.peek());
    }

    @Test
    void testPopAndPeek_multiple() {
        var stack = new HANStack<String>();
        stack.push("first");
        stack.push("second");

        stack.pop();
        assertEquals("first", stack.peek());

        stack.pop();
        assertNull(stack.peek());
    }
}
