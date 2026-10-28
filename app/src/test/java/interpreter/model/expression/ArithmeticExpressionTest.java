package interpreter.model.expression;

import interpreter.exceptions.ArithmeticException;
import interpreter.exceptions.ExpressionException;
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

class ArithmeticExpressionTest{
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp(){
        symTable = new MyDictionary<>();
    }

    private static IExpression constant(int n){
        return new ValueExpression(new IntValue(n));
    }

    private static IExpression constant(boolean b){
        return new ValueExpression(new BoolValue(b));
    }

    @ParameterizedTest
    @CsvSource({
            "ADD, 2, 3, 5",
            "ADD, -2, 3, 1",
            "SUB, 2, 3, -1",
            "SUB, 10, 4, 6",
            "MUL, 4, 5, 20",
            "MUL, -4, 5, -20",
            "MUL, 7, 0, 0",
            "DIV, 10, 2, 5",
            "DIV, 7, 2, 3",
            "DIV, -7, 2, -3",
            "DIV, 0, 5, 0",
    })
    void evaluatesOperation(ArithmeticOperation op, int left, int right, int expected) throws ExpressionException{
        assertEquals(new IntValue(expected), new ArithmeticExpression(op, constant(left), constant(right)).evaluate(symTable));
    }

    @Test
    void divisionByZeroThrows(){
        ArithmeticException e = assertThrows(ArithmeticException.class,
                () -> new ArithmeticExpression(ArithmeticOperation.DIV, constant(1), constant(0)).evaluate(symTable));
        assertEquals("Division by zero", e.getMessage());
    }

    @Test
    void nonIntFirstOperandThrows(){
        ArithmeticException e = assertThrows(ArithmeticException.class,
                () -> new ArithmeticExpression(ArithmeticOperation.ADD, constant(true), constant(1)).evaluate(symTable));
        assertEquals("First operand must be an integer", e.getMessage());
    }

    @Test
    void nonIntSecondOperandThrows(){
        ArithmeticException e = assertThrows(ArithmeticException.class,
                () -> new ArithmeticExpression(ArithmeticOperation.ADD, constant(1), constant(true)).evaluate(symTable));
        assertEquals("Second operand must be an integer", e.getMessage());
    }

    @Test
    void firstOperandIsCheckedBeforeSecond(){
        ArithmeticException e = assertThrows(ArithmeticException.class,
                () -> new ArithmeticExpression(ArithmeticOperation.ADD, constant(true), constant(false)).evaluate(symTable));
        assertEquals("First operand must be an integer", e.getMessage());
    }

    @Test
    void typeErrorIsReportedForDivisionByZeroWithBoolOperand(){
        ArithmeticException e = assertThrows(ArithmeticException.class,
                () -> new ArithmeticExpression(ArithmeticOperation.DIV, constant(true), constant(0)).evaluate(symTable));
        assertEquals("First operand must be an integer", e.getMessage());
    }

    @Test
    void evaluatesVariables() throws ExpressionException{
        symTable.put("a", new IntValue(6));
        symTable.put("b", new IntValue(4));
        IExpression expr = new ArithmeticExpression(ArithmeticOperation.SUB,
                new VariableExpression("a"), new VariableExpression("b"));
        assertEquals(new IntValue(2), expr.evaluate(symTable));
    }

    @Test
    void undefinedVariableOperandPropagates(){
        IExpression expr = new ArithmeticExpression(ArithmeticOperation.ADD, new VariableExpression("a"), constant(1));
        assertThrows(UndefinedVariableException.class, () -> expr.evaluate(symTable));
    }

    @Test
    void boolVariableOperandThrows(){
        symTable.put("a", new BoolValue(true));
        IExpression expr = new ArithmeticExpression(ArithmeticOperation.ADD, constant(1), new VariableExpression("a"));
        assertThrows(ArithmeticException.class, () -> expr.evaluate(symTable));
    }

    @Test
    void evaluatesNestedExpressions() throws ExpressionException{
        // (2 + 3) * (10 / 2) = 25
        IExpression expr = new ArithmeticExpression(ArithmeticOperation.MUL,
                new ArithmeticExpression(ArithmeticOperation.ADD, constant(2), constant(3)),
                new ArithmeticExpression(ArithmeticOperation.DIV, constant(10), constant(2)));
        assertEquals(new IntValue(25), expr.evaluate(symTable));
    }

    @Test
    void divisionByZeroInNestedExpressionThrows(){
        // 1 / (2 - 2)
        IExpression expr = new ArithmeticExpression(ArithmeticOperation.DIV, constant(1),
                new ArithmeticExpression(ArithmeticOperation.SUB, constant(2), constant(2)));
        assertThrows(ArithmeticException.class, () -> expr.evaluate(symTable));
    }

    @Test
    void additionOverflowWrapsAround() throws ExpressionException{
        IExpression expr = new ArithmeticExpression(ArithmeticOperation.ADD, constant(Integer.MAX_VALUE), constant(1));
        assertEquals(new IntValue(Integer.MIN_VALUE), expr.evaluate(symTable));
    }

    @ParameterizedTest
    @CsvSource({"ADD, +", "SUB, -", "MUL, *", "DIV, /"})
    void operationToStringIsSymbol(ArithmeticOperation op, String symbol){
        assertEquals(symbol, op.toString());
    }

    @Test
    void toStringIsParenthesizedInfix(){
        assertEquals("(1 + 2)", new ArithmeticExpression(ArithmeticOperation.ADD, constant(1), constant(2)).toString());
    }

    @Test
    void toStringOfNestedExpression(){
        IExpression expr = new ArithmeticExpression(ArithmeticOperation.MUL,
                new ArithmeticExpression(ArithmeticOperation.SUB, new VariableExpression("a"), constant(2)),
                constant(3));
        assertEquals("((a - 2) * 3)", expr.toString());
    }
}
