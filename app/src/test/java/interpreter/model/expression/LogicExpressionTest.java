package interpreter.model.expression;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.LogicException;
import interpreter.exceptions.UndefinedVariableException;
import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.adt.dictionary.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LogicExpressionTest{
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp(){
        symTable = new MyDictionary<>();
    }

    private static IExpression constant(boolean b){
        return new ValueExpression(new BoolValue(b));
    }

    private static IExpression constant(int n){
        return new ValueExpression(new IntValue(n));
    }

    @ParameterizedTest
    @CsvSource({
            "AND, true, true, true",
            "AND, true, false, false",
            "AND, false, true, false",
            "AND, false, false, false",
            "OR, true, true, true",
            "OR, true, false, true",
            "OR, false, true, true",
            "OR, false, false, false",
    })
    void evaluatesTruthTable(LogicOperation op, boolean left, boolean right, boolean expected) throws ExpressionException{
        assertEquals(new BoolValue(expected), new LogicExpression(op, constant(left), constant(right)).evaluate(symTable));
    }

    @Test
    void nonBoolFirstOperandThrows(){
        LogicException e = assertThrows(LogicException.class,
                () -> new LogicExpression(LogicOperation.AND, constant(1), constant(true)).evaluate(symTable));
        assertEquals("First operand must be boolean", e.getMessage());
    }

    @Test
    void nonBoolSecondOperandThrows(){
        LogicException e = assertThrows(LogicException.class,
                () -> new LogicExpression(LogicOperation.OR, constant(true), constant(1)).evaluate(symTable));
        assertEquals("Second operand must be boolean", e.getMessage());
    }

    @Test
    void firstOperandIsCheckedBeforeSecond(){
        LogicException e = assertThrows(LogicException.class,
                () -> new LogicExpression(LogicOperation.AND, constant(0), constant(1)).evaluate(symTable));
        assertEquals("First operand must be boolean", e.getMessage());
    }

    @Test
    void evaluatesVariables() throws ExpressionException{
        symTable.put("p", new BoolValue(true));
        symTable.put("q", new BoolValue(false));
        IExpression expr = new LogicExpression(LogicOperation.OR, new VariableExpression("p"), new VariableExpression("q"));
        assertEquals(new BoolValue(true), expr.evaluate(symTable));
    }

    @Test
    void undefinedVariableOperandPropagates(){
        IExpression expr = new LogicExpression(LogicOperation.AND, constant(true), new VariableExpression("p"));
        assertThrows(UndefinedVariableException.class, () -> expr.evaluate(symTable));
    }

    @Test
    void intVariableOperandThrows(){
        symTable.put("p", new IntValue(1));
        IExpression expr = new LogicExpression(LogicOperation.AND, new VariableExpression("p"), constant(true));
        assertThrows(LogicException.class, () -> expr.evaluate(symTable));
    }

    @Test
    void evaluatesNestedExpressions() throws ExpressionException{
        // (true and false) or (false or true) = true
        IExpression expr = new LogicExpression(LogicOperation.OR,
                new LogicExpression(LogicOperation.AND, constant(true), constant(false)),
                new LogicExpression(LogicOperation.OR, constant(false), constant(true)));
        assertEquals(new BoolValue(true), expr.evaluate(symTable));
    }

    @Test
    void arithmeticSubexpressionIsNotBoolean(){
        IExpression expr = new LogicExpression(LogicOperation.AND, constant(true),
                new ArithmeticExpression(ArithmeticOperation.ADD, constant(1), constant(2)));
        assertThrows(LogicException.class, () -> expr.evaluate(symTable));
    }

    @ParameterizedTest
    @CsvSource({"AND, and", "OR, or"})
    void operationToStringIsSymbol(LogicOperation op, String symbol){
        assertEquals(symbol, op.toString());
    }

    @Test
    void toStringIsParenthesizedInfix(){
        assertEquals("(true and false)", new LogicExpression(LogicOperation.AND, constant(true), constant(false)).toString());
    }

    @Test
    void toStringOfNestedExpression(){
        IExpression expr = new LogicExpression(LogicOperation.OR,
                new VariableExpression("p"),
                new LogicExpression(LogicOperation.AND, constant(true), new VariableExpression("q")));
        assertEquals("(p or (true and q))", expr.toString());
    }
}
