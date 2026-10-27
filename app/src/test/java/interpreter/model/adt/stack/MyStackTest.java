package interpreter.model.adt.stack;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStackTest{
    private IStack<Integer> stack;

    @BeforeEach
    void setUp(){
        stack = new MyStack<>();
    }

    @Test
    void newStackIsEmpty(){
        assertTrue(stack.isEmpty());
    }

    @Test
    void pushMakesStackNonEmpty(){
        stack.push(1);
        assertFalse(stack.isEmpty());
    }

    @Test
    void popReturnsElementsInLIFOOrder() throws ADTException{
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void peekReturnsTopWithoutRemovingIt() throws ADTException{
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.peek());
        assertEquals(2, stack.peek());
        assertEquals(2, stack.pop());
    }

    @Test
    void popOnEmptyStackThrows(){
        ADTException e = assertThrows(ADTException.class, () -> stack.pop());
        assertEquals("Stack is empty", e.getMessage());
    }

    @Test
    void peekOnEmptyStackThrows(){
        ADTException e = assertThrows(ADTException.class, () -> stack.peek());
        assertEquals("Stack is empty", e.getMessage());
    }

    @Test
    void popAfterAllElementsRemovedThrows() throws ADTException{
        stack.push(1);
        stack.pop();
        assertThrows(ADTException.class, () -> stack.pop());
    }

    @Test
    void clearEmptiesStack(){
        stack.push(1);
        stack.push(2);
        stack.clear();
        assertTrue(stack.isEmpty());
        assertThrows(ADTException.class, () -> stack.peek());
    }

    @Test
    void stackIsUsableAfterClear() throws ADTException{
        stack.push(1);
        stack.clear();
        stack.push(2);
        assertEquals(2, stack.pop());
    }
}
