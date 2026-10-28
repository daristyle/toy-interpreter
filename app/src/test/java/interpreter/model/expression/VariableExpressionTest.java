package interpreter.model.expression;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.UndefinedVariableException;
import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.adt.dictionary.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VariableExpressionTest{
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp(){
        symTable = new MyDictionary<>();
    }

    @Test
    void evaluatesToIntVariableValue() throws ExpressionException{
        symTable.put("x", new IntValue(3));
        assertEquals(new IntValue(3), new VariableExpression("x").evaluate(symTable));
    }

    @Test
    void evaluatesToBoolVariableValue() throws ExpressionException{
        symTable.put("flag", new BoolValue(true));
        assertEquals(new BoolValue(true), new VariableExpression("flag").evaluate(symTable));
    }

    @Test
    void seesLatestValueOfVariable() throws ExpressionException{
        VariableExpression x = new VariableExpression("x");
        symTable.put("x", new IntValue(1));
        assertEquals(new IntValue(1), x.evaluate(symTable));
        symTable.put("x", new IntValue(2));
        assertEquals(new IntValue(2), x.evaluate(symTable));
    }

    @Test
    void undefinedVariableThrows(){
        UndefinedVariableException e = assertThrows(UndefinedVariableException.class,
                () -> new VariableExpression("x").evaluate(symTable));
        assertEquals("Variable x was not defined", e.getMessage());
    }

    @Test
    void namesAreCaseSensitive(){
        symTable.put("x", new IntValue(1));
        assertThrows(UndefinedVariableException.class, () -> new VariableExpression("X").evaluate(symTable));
    }

    @Test
    void removedVariableIsUndefined(){
        symTable.put("x", new IntValue(1));
        symTable.remove("x");
        assertThrows(UndefinedVariableException.class, () -> new VariableExpression("x").evaluate(symTable));
    }

    @Test
    void toStringIsName(){
        assertEquals("x", new VariableExpression("x").toString());
    }
}
