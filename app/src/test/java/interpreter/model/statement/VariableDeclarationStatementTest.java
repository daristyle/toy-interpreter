package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.exceptions.VariableAlreadyDeclaredException;
import interpreter.model.ProgramState;
import interpreter.model.expression.VariableExpression;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VariableDeclarationStatementTest{
    private ProgramState programState;

    @BeforeEach
    void setUp(){
        programState = new ProgramState();
    }

    @Test
    void returnsSameProgramState() throws MyException{
        assertSame(programState, new VariableDeclarationStatement(new IntType(), "x").execute(programState));
    }

    @Test
    void declaresIntWithDefaultZero() throws MyException{
        new VariableDeclarationStatement(new IntType(), "x").execute(programState);
        assertTrue(programState.getSymTable().containsKey("x"));
        assertEquals(new IntValue(0), programState.getSymTable().get("x"));
    }

    @Test
    void declaresBoolWithDefaultFalse() throws MyException{
        new VariableDeclarationStatement(new BoolType(), "b").execute(programState);
        assertEquals(new BoolValue(false), programState.getSymTable().get("b"));
    }

    @Test
    void declaresSeveralVariables() throws MyException{
        new VariableDeclarationStatement(new IntType(), "x").execute(programState);
        new VariableDeclarationStatement(new BoolType(), "b").execute(programState);
        assertEquals(new IntValue(0), programState.getSymTable().get("x"));
        assertEquals(new BoolValue(false), programState.getSymTable().get("b"));
    }

    @Test
    void redeclaringVariableThrows() throws MyException{
        new VariableDeclarationStatement(new IntType(), "x").execute(programState);
        VariableAlreadyDeclaredException e = assertThrows(VariableAlreadyDeclaredException.class,
                () -> new VariableDeclarationStatement(new IntType(), "x").execute(programState));
        assertEquals("Variable x was already declared", e.getMessage());
    }

    @Test
    void redeclaringWithDifferentTypeThrows() throws MyException{
        new VariableDeclarationStatement(new IntType(), "x").execute(programState);
        assertThrows(VariableAlreadyDeclaredException.class,
                () -> new VariableDeclarationStatement(new BoolType(), "x").execute(programState));
    }

    @Test
    void failedRedeclarationKeepsExistingValue() throws MyException{
        programState.getSymTable().put("x", new IntValue(7));
        assertThrows(VariableAlreadyDeclaredException.class,
                () -> new VariableDeclarationStatement(new IntType(), "x").execute(programState));
        assertEquals(new IntValue(7), programState.getSymTable().get("x"));
    }

    @Test
    void toStringOfIntDeclaration(){
        assertEquals("int x", new VariableDeclarationStatement(new IntType(), "x").toString());
    }

    @Test
    void toStringOfBoolDeclaration(){
        assertEquals("bool flag", new VariableDeclarationStatement(new BoolType(), "flag").toString());
    }
}
