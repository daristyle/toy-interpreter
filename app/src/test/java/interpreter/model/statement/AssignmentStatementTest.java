package interpreter.model.statement;

import interpreter.exceptions.ArithmeticException;
import interpreter.exceptions.AssignmentStatementException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.UndefinedVariableException;
import interpreter.model.ProgramState;
import interpreter.model.TestProgramStates;
import interpreter.model.expression.ArithmeticExpression;
import interpreter.model.expression.ArithmeticOperation;
import interpreter.model.expression.IExpression;
import interpreter.model.expression.ValueExpression;
import interpreter.model.expression.VariableExpression;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentStatementTest{
    private ProgramState programState;

    @BeforeEach
    void setUp(){
        programState = TestProgramStates.emptyProgramState();
    }

    private static IExpression constant(int n){
        return new ValueExpression(new IntValue(n));
    }

    private static IExpression constant(boolean b){
        return new ValueExpression(new BoolValue(b));
    }

    @Test
    void returnsSameProgramState() throws MyException{
        programState.getSymTable().put("x", new IntValue(0));
        assertSame(programState, new AssignmentStatement("x", constant(5)).execute(programState));
    }

    @Test
    void assignsIntValue() throws MyException{
        programState.getSymTable().put("x", new IntValue(0));
        new AssignmentStatement("x", constant(5)).execute(programState);
        assertEquals(new IntValue(5), programState.getSymTable().get("x"));
    }

    @Test
    void assignsBoolValue() throws MyException{
        programState.getSymTable().put("b", new BoolValue(false));
        new AssignmentStatement("b", constant(true)).execute(programState);
        assertEquals(new BoolValue(true), programState.getSymTable().get("b"));
    }

    @Test
    void reassignmentOverwritesValue() throws MyException{
        programState.getSymTable().put("x", new IntValue(0));
        new AssignmentStatement("x", constant(1)).execute(programState);
        new AssignmentStatement("x", constant(2)).execute(programState);
        assertEquals(new IntValue(2), programState.getSymTable().get("x"));
    }

    @Test
    void assignsValueOfOtherVariable() throws MyException{
        programState.getSymTable().put("x", new IntValue(0));
        programState.getSymTable().put("y", new IntValue(9));
        new AssignmentStatement("x", new VariableExpression("y")).execute(programState);
        assertEquals(new IntValue(9), programState.getSymTable().get("x"));
    }

    @Test
    void canReferenceItselfOnRightHandSide() throws MyException{
        // x = x + 1
        programState.getSymTable().put("x", new IntValue(4));
        new AssignmentStatement("x",
                new ArithmeticExpression(ArithmeticOperation.ADD, new VariableExpression("x"), constant(1))).execute(programState);
        assertEquals(new IntValue(5), programState.getSymTable().get("x"));
    }

    @Test
    void undeclaredVariableThrows(){
        UndefinedVariableException e = assertThrows(UndefinedVariableException.class,
                () -> new AssignmentStatement("x", constant(5)).execute(programState));
        assertEquals("Variable x was not defined", e.getMessage());
    }

    @Test
    void undeclaredTargetIsReportedBeforeExpressionErrors(){
        UndefinedVariableException e = assertThrows(UndefinedVariableException.class,
                () -> new AssignmentStatement("x", new VariableExpression("y")).execute(programState));
        assertEquals("Variable x was not defined", e.getMessage());
    }

    @Test
    void assigningBoolToIntVariableThrows(){
        programState.getSymTable().put("x", new IntValue(0));
        AssignmentStatementException e = assertThrows(AssignmentStatementException.class,
                () -> new AssignmentStatement("x", constant(true)).execute(programState));
        assertEquals("Declared type of variable x and type of the assigned expression do not match", e.getMessage());
    }

    @Test
    void assigningIntToBoolVariableThrows(){
        programState.getSymTable().put("b", new BoolValue(false));
        assertThrows(AssignmentStatementException.class,
                () -> new AssignmentStatement("b", constant(1)).execute(programState));
    }

    @Test
    void typeMismatchKeepsOldValue(){
        programState.getSymTable().put("x", new IntValue(3));
        assertThrows(AssignmentStatementException.class,
                () -> new AssignmentStatement("x", constant(true)).execute(programState));
        assertEquals(new IntValue(3), programState.getSymTable().get("x"));
    }

    @Test
    void expressionErrorPropagatesAndKeepsOldValue(){
        programState.getSymTable().put("x", new IntValue(3));
        assertThrows(ArithmeticException.class, () -> new AssignmentStatement("x",
                new ArithmeticExpression(ArithmeticOperation.DIV, constant(1), constant(0))).execute(programState));
        assertEquals(new IntValue(3), programState.getSymTable().get("x"));
    }
}
