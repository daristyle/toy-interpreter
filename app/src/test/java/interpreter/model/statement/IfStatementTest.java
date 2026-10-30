package interpreter.model.statement;

import interpreter.exceptions.IfStatementException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.UndefinedVariableException;
import interpreter.model.ProgramState;
import interpreter.model.TestProgramStates;
import interpreter.model.expression.IExpression;
import interpreter.model.expression.LogicExpression;
import interpreter.model.expression.LogicOperation;
import interpreter.model.expression.ValueExpression;
import interpreter.model.expression.VariableExpression;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IfStatementTest{
    private ProgramState programState;
    private final IStatement thenS = new PrintStatement(new ValueExpression(new IntValue(1)));
    private final IStatement elseS = new PrintStatement(new ValueExpression(new IntValue(2)));

    @BeforeEach
    void setUp(){
        programState = TestProgramStates.emptyProgramState();
    }

    private static IExpression constant(boolean b){
        return new ValueExpression(new BoolValue(b));
    }

    @Test
    void returnsSameProgramState() throws MyException{
        assertSame(programState, new IfStatement(constant(true), thenS, elseS).execute(programState));
    }

    @Test
    void trueConditionPushesThenBranch() throws MyException{
        new IfStatement(constant(true), thenS, elseS).execute(programState);
        assertSame(thenS, programState.getExecStack().pop());
        assertTrue(programState.getExecStack().isEmpty());
    }

    @Test
    void falseConditionPushesElseBranch() throws MyException{
        new IfStatement(constant(false), thenS, elseS).execute(programState);
        assertSame(elseS, programState.getExecStack().pop());
        assertTrue(programState.getExecStack().isEmpty());
    }

    @Test
    void doesNotExecuteBranch() throws MyException{
        new IfStatement(constant(true), thenS, elseS).execute(programState);
        assertEquals(0, programState.getOutList().size());
    }

    @Test
    void evaluatesConditionWithVariables() throws MyException{
        programState.getSymTable().put("p", new BoolValue(true));
        programState.getSymTable().put("q", new BoolValue(false));
        new IfStatement(new LogicExpression(LogicOperation.AND,
                new VariableExpression("p"), new VariableExpression("q")), thenS, elseS).execute(programState);
        assertSame(elseS, programState.getExecStack().pop());
    }

    @Test
    void nonBoolConditionThrows(){
        IfStatementException e = assertThrows(IfStatementException.class,
                () -> new IfStatement(new ValueExpression(new IntValue(1)), thenS, elseS).execute(programState));
        assertEquals("Condition is not a boolean", e.getMessage());
        assertTrue(programState.getExecStack().isEmpty());
    }

    @Test
    void undefinedVariableInConditionThrows(){
        assertThrows(UndefinedVariableException.class,
                () -> new IfStatement(new VariableExpression("p"), thenS, elseS).execute(programState));
        assertTrue(programState.getExecStack().isEmpty());
    }

    @Test
    void toStringShowsAllParts(){
        assertEquals("(if p then print(1) else print(2))",
                new IfStatement(new VariableExpression("p"), thenS, elseS).toString());
    }
}
