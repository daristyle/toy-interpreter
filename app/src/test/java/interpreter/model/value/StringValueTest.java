package interpreter.model.value;

import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValueTest{
    @Test
    void storesValue(){
        assertEquals("hello", new StringValue("hello").value());
    }

    @Test
    void typeIsStringType(){
        assertEquals(new StringType(), new StringValue("hello").getType());
    }

    @Test
    void equalOnlyWhenSameText(){
        assertEquals(new StringValue("a"), new StringValue("a"));
        assertEquals(new StringValue("a").hashCode(), new StringValue("a").hashCode());
        assertNotEquals(new StringValue("a"), new StringValue("b"));
        assertNotEquals(new StringValue("a"), new StringValue("A"));
    }

    @Test
    void notEqualToOtherValueTypes(){
        assertNotEquals(new StringValue("0"), new IntValue(0));
        assertNotEquals(new StringValue("true"), new BoolValue(true));
    }

    @Test
    void toStringWrapsTextInQuotes(){
        assertEquals("\"hello\"", new StringValue("hello").toString());
        assertEquals("\"\"", new StringValue("").toString());
    }
}
