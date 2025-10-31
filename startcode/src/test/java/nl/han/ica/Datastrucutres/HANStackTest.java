package nl.han.ica.Datastrucutres;

import nl.han.ica.datastructures.HANStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HANStackTest {

    @Test
    void testPushAndPeek() {
        var stack = new HANStack<String>();
        stack.push("first"); // Push first element
        assertEquals("first", stack.peek()); // Peek should return "first"
    }

    @Test
    void testPushAndPeek_multiple() {
        var stack = new HANStack<String>();
        stack.push("first");  // Push first element
        stack.push("second"); // Push second element on top

        assertEquals("second", stack.peek()); // Peek should return top: "second"

        stack.push("third"); // Push third element
        assertEquals("third", stack.peek()); // Peek should return top: "third"
    }

    @Test
    void testPopAndPeek() {
        var stack = new HANStack<String>();
        stack.push("first");  // Push first element
        stack.push("second"); // Push second element

        stack.pop(); // Remove top element ("second")
        assertEquals("first", stack.peek()); // Peek should now return "first"
    }
}
