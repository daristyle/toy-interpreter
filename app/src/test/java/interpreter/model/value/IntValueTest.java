package interpreter.model.value;

import interpreter.model.type.IntType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class IntValueTest{
    @ParameterizedTest
    @ValueSource(ints = {0, 1, -1, 42, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void storesValue(int n){
        assertEquals(n, new IntValue(n).value());
    }

    @Test
    void typeIsIntType(){
        assertEquals(new IntType(), new IntValue(7).getType());
    }

    @Test
    void equalWhenSameValue(){
        assertEquals(new IntValue(5), new IntValue(5));
        assertEquals(new IntValue(5).hashCode(), new IntValue(5).hashCode());
    }

    @Test
    void notEqualWhenDifferentValue(){
        assertNotEquals(new IntValue(5), new IntValue(6));
    }

    @Test
    void notEqualToBoolValue(){
        assertNotEquals(new IntValue(0), new BoolValue(false));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 42, -17})
    void toStringIsNumber(int n){
        assertEquals(Integer.toString(n), new IntValue(n).toString());
    }
}
