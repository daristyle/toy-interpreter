package interpreter.model.expression;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.adt.dictionary.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueExpressionTest{
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    @Test
    void evaluatesToIntValue() throws ExpressionException{
        assertEquals(new IntValue(5), new ValueExpression(new IntValue(5)).evaluate(symTable));
    }

    @Test
    void evaluatesToBoolValue() throws ExpressionException{
        assertEquals(new BoolValue(true), new ValueExpression(new BoolValue(true)).evaluate(symTable));
    }

    @Test
    void ignoresSymbolTable() throws ExpressionException{
        symTable.put("x", new IntValue(10));
        assertEquals(new IntValue(5), new ValueExpression(new IntValue(5)).evaluate(symTable));
    }

    @Test
    void toStringIsValue(){
        assertEquals("5", new ValueExpression(new IntValue(5)).toString());
        assertEquals("false", new ValueExpression(new BoolValue(false)).toString());
    }
}
