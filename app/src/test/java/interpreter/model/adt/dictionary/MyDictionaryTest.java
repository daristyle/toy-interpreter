package interpreter.model.adt.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest{
    private IDictionary<String, Integer> dictionary;

    @BeforeEach
    void setUp(){
        dictionary = new MyDictionary<>();
    }

    @Test
    void newDictionaryContainsNoKeys(){
        assertFalse(dictionary.containsKey("a"));
    }

    @Test
    void putThenGetReturnsValue(){
        dictionary.put("a", 1);
        assertTrue(dictionary.containsKey("a"));
        assertEquals(1, dictionary.get("a"));
    }

    @Test
    void putOverwritesExistingKey(){
        dictionary.put("a", 1);
        dictionary.put("a", 2);
        assertEquals(2, dictionary.get("a"));
    }

    @Test
    void keysAreIndependent(){
        dictionary.put("a", 1);
        dictionary.put("b", 2);
        assertEquals(1, dictionary.get("a"));
        assertEquals(2, dictionary.get("b"));
    }

    @Test
    void getMissingKeyReturnsNull(){
        assertNull(dictionary.get("missing"));
    }

    @Test
    void removeDeletesKey(){
        dictionary.put("a", 1);
        dictionary.put("b", 2);
        dictionary.remove("a");
        assertFalse(dictionary.containsKey("a"));
        assertTrue(dictionary.containsKey("b"));
    }

    @Test
    void removeMissingKeyDoesNothing(){
        dictionary.put("a", 1);
        dictionary.remove("missing");
        assertTrue(dictionary.containsKey("a"));
    }

    @Test
    void clearRemovesAllKeys(){
        dictionary.put("a", 1);
        dictionary.put("b", 2);
        dictionary.clear();
        assertFalse(dictionary.containsKey("a"));
        assertFalse(dictionary.containsKey("b"));
    }

    @Test
    void dictionaryIsUsableAfterClear(){
        dictionary.put("a", 1);
        dictionary.clear();
        dictionary.put("a", 2);
        assertEquals(2, dictionary.get("a"));
    }

    @Test
    void toStringOfEmptyDictionary(){
        assertEquals("{}", dictionary.toString());
    }

    @Test
    void toStringOfSingleEntry(){
        dictionary.put("a", 1);
        assertEquals("{a -> 1}", dictionary.toString());
    }

    @Test
    void toStringShowsAllEntries(){
        dictionary.put("a", 1);
        dictionary.put("b", 2);
        dictionary.put("c", 3);
        String text = dictionary.toString();//no order is guaranteed
        assertTrue(text.startsWith("{") && text.endsWith("}"));
        assertEquals(Set.of("a -> 1", "b -> 2", "c -> 3"),
                Set.of(text.substring(1, text.length() - 1).split("; ")));
    }

    @Test
    void toStringShowsOverwrittenValue(){
        dictionary.put("a", 1);
        dictionary.put("a", 2);
        assertEquals("{a -> 2}", dictionary.toString());
    }

    @Test
    void toStringReflectsRemove(){
        dictionary.put("a", 1);
        dictionary.put("b", 2);
        dictionary.remove("a");
        assertEquals("{b -> 2}", dictionary.toString());
    }

    @Test
    void toStringAfterClear(){
        dictionary.put("a", 1);
        dictionary.clear();
        assertEquals("{}", dictionary.toString());
    }
}
