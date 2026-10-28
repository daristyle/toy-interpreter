package interpreter.model.adt.list;

import interpreter.exceptions.ListException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest{
    private IList<String> list;

    @BeforeEach
    void setUp(){
        list = new MyList<>();
    }

    @Test
    void newListIsEmpty(){
        assertEquals(0, list.size());
    }

    @Test
    void addIncreasesSize(){
        list.add("a");
        assertEquals(1, list.size());
        list.add("b");
        assertEquals(2, list.size());
    }

    @Test
    void getReturnsElementsInInsertionOrder() throws ListException{
        list.add("a");
        list.add("b");
        list.add("c");
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    void getDoesNotRemoveElement() throws ListException{
        list.add("a");
        list.get(0);
        assertEquals(1, list.size());
        assertEquals("a", list.get(0));
    }

    @Test
    void allowsDuplicates() throws ListException{
        list.add("a");
        list.add("a");
        assertEquals(2, list.size());
        assertEquals("a", list.get(1));
    }

    @Test
    void getOnEmptyListThrows(){
        ListException e = assertThrows(ListException.class, () -> list.get(0));
        assertEquals("Index out of bounds", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 2, 3, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void getOutOfBoundsThrows(int index){
        list.add("a");
        list.add("b");
        assertThrows(ListException.class, () -> list.get(index));
    }

    @Test
    void clearEmptiesList(){
        list.add("a");
        list.add("b");
        list.clear();
        assertEquals(0, list.size());
        assertThrows(ListException.class, () -> list.get(0));
    }

    @Test
    void listIsUsableAfterClear() throws ListException{
        list.add("a");
        list.clear();
        list.add("b");
        assertEquals(1, list.size());
        assertEquals("b", list.get(0));
    }
}
