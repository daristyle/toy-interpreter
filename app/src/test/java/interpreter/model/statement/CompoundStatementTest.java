package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;
import interpreter.model.TestProgramStates;
import interpreter.model.expression.ValueExpression;
import interpreter.model.expression.VariableExpression;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompoundStatementTest{
    private ProgramState programState;
    private final IStatement first = new PrintStatement(new ValueExpression(new IntValue(1)));
    private final IStatement second = new PrintStatement(new ValueExpression(new IntValue(2)));

    @BeforeEach
    void setUp(){
        programState = TestProgramStates.emptyProgramState();
    }

    @Test
    void returnsSameProgramState() throws MyException{
        assertSame(programState, new CompoundStatement(first, second).execute(programState));
    }

    @Test
    void pushesFirstOnTopOfSecond() throws MyException{
        new CompoundStatement(first, second).execute(programState);
        assertSame(first, programState.getExecStack().pop());
        assertSame(second, programState.getExecStack().pop());
        assertTrue(programState.getExecStack().isEmpty());
    }

    @Test
    void doesNotExecuteEitherStatement() throws MyException{
        new CompoundStatement(first, second).execute(programState);
        assertEquals(0, programState.getOutList().size());
    }

    @Test
    void pushesAboveExistingStackContents() throws MyException{
        IStatement existing = new NoOperationStatement();
        programState.getExecStack().push(existing);

        new CompoundStatement(first, second).execute(programState);

        assertSame(first, programState.getExecStack().pop());
        assertSame(second, programState.getExecStack().pop());
        assertSame(existing, programState.getExecStack().pop());
    }

    @Test
    void doesNotUnfoldNestedCompound() throws MyException{
        IStatement inner = new CompoundStatement(first, second);
        new CompoundStatement(inner, new NoOperationStatement()).execute(programState);
        assertSame(inner, programState.getExecStack().pop());
    }

    @Test
    void toStringJoinsWithSemicolon(){
        IStatement stmt = new CompoundStatement(new PrintStatement(new VariableExpression("x")),
                new PrintStatement(new VariableExpression("y")));
        assertEquals("(print(x); print(y))", stmt.toString());
    }
}
