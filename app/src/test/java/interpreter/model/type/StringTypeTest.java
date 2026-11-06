package interpreter.model.type;

import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringTypeTest{
    @Test
    void defaultValueIsEmptyString(){
        assertEquals(new StringValue(""), new StringType().defaultValue());
    }

    @Test
    void defaultValueHasStringType(){
        assertEquals(new StringType(), new StringType().defaultValue().getType());
    }

    @Test
    void allInstancesAreEqual(){
        assertEquals(new StringType(), new StringType());
    }

    @Test
    void toStringIsString(){
        assertEquals("string", new StringType().toString());
    }
}
