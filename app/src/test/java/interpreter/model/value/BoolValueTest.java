package interpreter.model.value;

import interpreter.model.type.BoolType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoolValueTest{
    @Test
    void storesValue(){
        assertTrue(new BoolValue(true).value());
        assertFalse(new BoolValue(false).value());
    }

    @Test
    void typeIsBoolType(){
        assertEquals(new BoolType(), new BoolValue(true).getType());
        assertEquals(new BoolType(), new BoolValue(false).getType());
    }

    @Test
    void equalWhenSameValue(){
        assertEquals(new BoolValue(true), new BoolValue(true));
        assertEquals(new BoolValue(true).hashCode(), new BoolValue(true).hashCode());
    }

    @Test
    void notEqualWhenDifferentValue(){
        assertNotEquals(new BoolValue(true), new BoolValue(false));
    }

    @Test
    void notEqualToIntValue(){
        assertNotEquals(new BoolValue(false), new IntValue(0));
    }

    @Test
    void toStringIsLowercaseLiteral(){
        assertEquals("true", new BoolValue(true).toString());
        assertEquals("false", new BoolValue(false).toString());
    }
}
