package interpreter.model;

import interpreter.exceptions.MyException;
import interpreter.model.adt.dictionary.IDictionary;
import interpreter.model.adt.dictionary.MyDictionary;
import interpreter.model.adt.list.IList;
import interpreter.model.adt.list.MyList;
import interpreter.model.adt.stack.IStack;
import interpreter.model.adt.stack.MyStack;
import interpreter.model.expression.ValueExpression;
import interpreter.model.expression.VariableExpression;
import interpreter.model.statement.CompoundStatement;
import interpreter.model.statement.IStatement;
import interpreter.model.statement.NoOperationStatement;
import interpreter.model.statement.PrintStatement;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProgramStateTest{
    private IStack<IStatement> execStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> outList;

    private final IStatement printOne = new PrintStatement(new ValueExpression(new IntValue(1)));
    private final IStatement printTwo = new PrintStatement(new ValueExpression(new IntValue(2)));

    @BeforeEach
    void setUp(){
        execStack = new MyStack<>();
        symTable = new MyDictionary<>();
        outList = new MyList<>();
    }

    private ProgramState newProgramState(IStatement originalProgram){
        return new ProgramState(execStack, symTable, outList, originalProgram);
    }

    @Test
    void constructorPushesOriginalProgramOnExecStack() throws MyException{
        ProgramState programState = newProgramState(printOne);
        assertSame(printOne, programState.getExecStack().pop());
        assertTrue(programState.getExecStack().isEmpty());
    }

    @Test
    void originalProgramIsPushedAboveExistingStackContents() throws MyException{
        execStack.push(printTwo);
        ProgramState programState = newProgramState(printOne);
        assertSame(printOne, programState.getExecStack().pop());
        assertSame(printTwo, programState.getExecStack().pop());
    }

    @Test
    void constructorLeavesSymTableAndOutListEmpty() throws MyException{
        ProgramState programState = newProgramState(printOne);
        assertFalse(programState.getSymTable().containsKey("x"));
        assertEquals(0, programState.getOutList().size());
    }

    @Test
    void keepsExistingSymTableAndOutListContents() throws MyException{
        symTable.put("x", new IntValue(1));
        outList.add(new IntValue(2));
        ProgramState programState = newProgramState(printOne);
        assertEquals(new IntValue(1), programState.getSymTable().get("x"));
        assertEquals(new IntValue(2), programState.getOutList().get(0));
    }

    @Test
    void gettersReturnTheGivenADTs(){
        ProgramState programState = newProgramState(printOne);
        assertSame(execStack, programState.getExecStack());
        assertSame(symTable, programState.getSymTable());
        assertSame(outList, programState.getOutList());
    }

    @Test
    void getOriginalProgramReturnsProgram(){
        assertSame(printOne, newProgramState(printOne).getOriginalProgram());
    }

    @Test
    void originalProgramIsKeptWhileExecuting() throws MyException{
        IStatement program = new CompoundStatement(printOne, printTwo);
        ProgramState programState = newProgramState(program);

        programState.getExecStack().pop().execute(programState);

        assertSame(program, programState.getOriginalProgram());
        assertSame(printOne, programState.getExecStack().peek());
    }

    @Test
    void originalProgramIsKeptAfterExecStackIsEmptied() throws MyException{
        ProgramState programState = newProgramState(printOne);
        programState.getExecStack().pop();
        assertSame(printOne, programState.getOriginalProgram());
    }

    @Test
    void toStringOfNewProgramState(){
        assertEquals("ExecStack: {print(1)}\nSymTable: {}\nOutList: {}", newProgramState(printOne).toString());
    }

    @Test
    void toStringShowsSymTableAndOutList(){
        symTable.put("x", new IntValue(1));
        outList.add(new IntValue(1));
        outList.add(new BoolValue(true));
        ProgramState programState = newProgramState(new PrintStatement(new VariableExpression("x")));
        assertEquals("ExecStack: {print(x)}\nSymTable: {x -> 1}\nOutList: {1; true}", programState.toString());
    }

    @Test
    void toStringShowsExecStackTopFirst() throws MyException{
        ProgramState programState = newProgramState(new CompoundStatement(printOne, printTwo));
        programState.getExecStack().pop().execute(programState);
        assertEquals("ExecStack: {print(1)| print(2)}\nSymTable: {}\nOutList: {}", programState.toString());
    }

    @Test
    void toStringWithEmptyExecStack() throws MyException{
        ProgramState programState = newProgramState(new NoOperationStatement());
        programState.getExecStack().pop();
        assertEquals("ExecStack: {}\nSymTable: {}\nOutList: {}", programState.toString());
    }
}
