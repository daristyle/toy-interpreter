package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoOperationStatementTest{
    @Test
    void returnsSameProgramState(){
        ProgramState programState = new ProgramState();
        assertSame(programState, new NoOperationStatement().execute(programState));
    }

    @Test
    void leavesProgramStateUnchanged() throws MyException{
        ProgramState programState = new ProgramState();
        programState.getSymTable().put("x", new IntValue(1));

        new NoOperationStatement().execute(programState);

        assertEquals(new IntValue(1), programState.getSymTable().get("x"));
        assertTrue(programState.getExecStack().isEmpty());
        assertEquals(0, programState.getOutList().size());
    }
}
