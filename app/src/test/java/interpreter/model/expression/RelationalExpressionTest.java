package interpreter.model.expression;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.RelationalException;
import interpreter.exceptions.UndefinedVariableException;
import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.adt.dictionary.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class RelationalExpressionTest{
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

    private static IExpression constant(String s){
        return new ValueExpression(new StringValue(s));
    }

    @ParameterizedTest
    @CsvSource({
            "LT, true, false, false",
            "LE, true, true, false",
            "EQ, false, true, false",
            "NE, true, false, true",
            "GT, false, false, true",
            "GE, false, true, true",
    })
    void comparesIntegers(RelationalOperator op, boolean whenLess, boolean whenEqual, boolean whenGreater) throws ExpressionException{
        assertEquals(new BoolValue(whenLess), new RelationalExpression(op, constant(1), constant(2)).evaluate(symTable));
        assertEquals(new BoolValue(whenEqual), new RelationalExpression(op, constant(2), constant(2)).evaluate(symTable));
        assertEquals(new BoolValue(whenGreater), new RelationalExpression(op, constant(3), constant(2)).evaluate(symTable));
    }

    @Test
    void comparesNegativeAndExtremeNumbers() throws ExpressionException{
        assertEquals(new BoolValue(true),
                new RelationalExpression(RelationalOperator.LT, constant(-5), constant(3)).evaluate(symTable));
        assertEquals(new BoolValue(true),
                new RelationalExpression(RelationalOperator.LT, constant(Integer.MIN_VALUE), constant(Integer.MAX_VALUE)).evaluate(symTable));
    }

    @Test
    void nonIntOperandsThrow(){
        RelationalException first = assertThrows(RelationalException.class,
                () -> new RelationalExpression(RelationalOperator.EQ, constant(true), constant(1)).evaluate(symTable));
        assertEquals("First operand must be an integer", first.getMessage());

        RelationalException second = assertThrows(RelationalException.class,
                () -> new RelationalExpression(RelationalOperator.EQ, constant(1), constant("a")).evaluate(symTable));
        assertEquals("Second operand must be an integer", second.getMessage());

        RelationalException both = assertThrows(RelationalException.class,
                () -> new RelationalExpression(RelationalOperator.EQ, constant(true), constant("a")).evaluate(symTable));
        assertEquals("First operand must be an integer", both.getMessage());
    }

    @Test
    void evaluatesVariablesAndNestedExpressions() throws ExpressionException{
        // (a + 1) == b
        symTable.put("a", new IntValue(2));
        symTable.put("b", new IntValue(3));
        IExpression expr = new RelationalExpression(RelationalOperator.EQ,
                new ArithmeticExpression(ArithmeticOperator.ADD, new VariableExpression("a"), constant(1)),
                new VariableExpression("b"));
        assertEquals(new BoolValue(true), expr.evaluate(symTable));
    }

    @Test
    void undefinedVariableOperandPropagates(){
        IExpression expr = new RelationalExpression(RelationalOperator.LT, constant(1), new VariableExpression("a"));
        assertThrows(UndefinedVariableException.class, () -> expr.evaluate(symTable));
    }

    @Test
    void resultIsABooleanNotAnInteger() throws ExpressionException{
        IExpression oneLessThanTwo = new RelationalExpression(RelationalOperator.LT, constant(1), constant(2));

        IExpression logic = new LogicExpression(LogicOperator.AND, oneLessThanTwo, constant(true));
        assertEquals(new BoolValue(true), logic.evaluate(symTable));

        IExpression chained = new RelationalExpression(RelationalOperator.LT, oneLessThanTwo, constant(3));
        assertThrows(RelationalException.class, () -> chained.evaluate(symTable));
    }

    @ParameterizedTest
    @CsvSource({"LT, <", "LE, <=", "EQ, ==", "NE, !=", "GT, >", "GE, >="})
    void toStringIsParenthesizedInfix(RelationalOperator op, String symbol){
        assertEquals("(a " + symbol + " 2)", new RelationalExpression(op, new VariableExpression("a"), constant(2)).toString());
    }
}
