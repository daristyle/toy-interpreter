package interpreter.model.statement;

import interpreter.exceptions.ArithmeticException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.UndefinedVariableException;
import interpreter.model.ProgramState;
import interpreter.model.expression.ArithmeticExpression;
import interpreter.model.expression.ArithmeticOperation;
import interpreter.model.expression.ValueExpression;
import interpreter.model.expression.VariableExpression;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrintStatementTest{
    private ProgramState programState;

    @BeforeEach
    void setUp(){
        programState = new ProgramState();
    }

    @Test
    void returnsSameProgramState() throws MyException{
        assertSame(programState, new PrintStatement(new ValueExpression(new IntValue(1))).execute(programState));
    }

    @Test
    void appendsValueToOutput() throws MyException{
        new PrintStatement(new ValueExpression(new IntValue(42))).execute(programState);
        assertEquals(1, programState.getOutList().size());
        assertEquals(new IntValue(42), programState.getOutList().get(0));
    }

    @Test
    void printsBoolValue() throws MyException{
        new PrintStatement(new ValueExpression(new BoolValue(true))).execute(programState);
        assertEquals(new BoolValue(true), programState.getOutList().get(0));
    }

    @Test
    void printsVariableValue() throws MyException{
        programState.getSymTable().put("x", new IntValue(7));
        new PrintStatement(new VariableExpression("x")).execute(programState);
        assertEquals(new IntValue(7), programState.getOutList().get(0));
    }

    @Test
    void printsEvaluatedExpression() throws MyException{
        new PrintStatement(new ArithmeticExpression(ArithmeticOperation.MUL,
                new ValueExpression(new IntValue(6)), new ValueExpression(new IntValue(7)))).execute(programState);
        assertEquals(new IntValue(42), programState.getOutList().get(0));
    }

    @Test
    void outputKeepsPrintOrder() throws MyException{
        new PrintStatement(new ValueExpression(new IntValue(1))).execute(programState);
        new PrintStatement(new ValueExpression(new IntValue(2))).execute(programState);
        new PrintStatement(new ValueExpression(new IntValue(3))).execute(programState);
        assertEquals(3, programState.getOutList().size());
        assertEquals(new IntValue(1), programState.getOutList().get(0));
        assertEquals(new IntValue(2), programState.getOutList().get(1));
        assertEquals(new IntValue(3), programState.getOutList().get(2));
    }

    @Test
    void doesNotChangeSymbolTable() throws MyException{
        programState.getSymTable().put("x", new IntValue(7));
        new PrintStatement(new VariableExpression("x")).execute(programState);
        assertEquals(new IntValue(7), programState.getSymTable().get("x"));
    }

    @Test
    void undefinedVariableThrowsAndPrintsNothing(){
        assertThrows(UndefinedVariableException.class,
                () -> new PrintStatement(new VariableExpression("x")).execute(programState));
        assertEquals(0, programState.getOutList().size());
    }

    @Test
    void expressionErrorThrowsAndPrintsNothing(){
        assertThrows(ArithmeticException.class, () -> new PrintStatement(new ArithmeticExpression(ArithmeticOperation.DIV,
                new ValueExpression(new IntValue(1)), new ValueExpression(new IntValue(0)))).execute(programState));
        assertEquals(0, programState.getOutList().size());
    }

    @Test
    void toStringWrapsExpression(){
        assertEquals("print(x)", new PrintStatement(new VariableExpression("x")).toString());
        assertEquals("print(5)", new PrintStatement(new ValueExpression(new IntValue(5))).toString());
    }
}
