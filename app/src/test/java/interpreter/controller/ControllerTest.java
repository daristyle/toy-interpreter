package interpreter.controller;

import interpreter.exceptions.ArithmeticException;
import interpreter.exceptions.ExecStackException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.RepositoryException;
import interpreter.model.ProgramState;
import interpreter.model.expression.ArithmeticExpression;
import interpreter.model.expression.ArithmeticOperation;
import interpreter.model.expression.IExpression;
import interpreter.model.expression.ValueExpression;
import interpreter.model.expression.VariableExpression;
import interpreter.model.statement.AssignmentStatement;
import interpreter.model.statement.CompoundStatement;
import interpreter.model.statement.IStatement;
import interpreter.model.statement.PrintStatement;
import interpreter.model.statement.VariableDeclarationStatement;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.IRepository;
import interpreter.repository.MemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest{
    private IRepository repository;
    private List<String> displayed;

    @BeforeEach
    void setUp(){
        repository = new MemoryRepository(List.of());
        displayed = new ArrayList<>();
    }

    /** The displayer records a snapshot, since it always receives the same (mutable) program state. */
    private Controller newController(boolean displayFlag){
        return new Controller(repository, displayFlag, programState -> displayed.add(programState.toString()));
    }

    private static IExpression constant(int n){
        return new ValueExpression(new IntValue(n));
    }

    private static IStatement print(int n){
        return new PrintStatement(constant(n));
    }

    private static IStatement compound(IStatement... statements){
        IStatement result = statements[statements.length - 1];
        for(int i = statements.length - 2; i >= 0; i--){
            result = new CompoundStatement(statements[i], result);
        }
        return result;
    }

    private ProgramState currentProgramState() throws RepositoryException{
        return repository.getCurrentProgramState();
    }

    @Test
    void loadProgramReplacesPreviousProgram() throws MyException{
        Controller controller = newController(false);
        controller.loadProgram(compound(new VariableDeclarationStatement(new IntType(), "v"), print(1)));
        controller.allSteps();

        IStatement program = print(2);
        controller.loadProgram(program);

        ProgramState programState = currentProgramState();
        assertSame(program, programState.getOriginalProgram());
        assertEquals("{print(2)}", programState.getExecStack().toString());
        assertFalse(programState.getSymTable().containsKey("v"));
        assertEquals(0, programState.getOutList().size());
    }

    @Test
    void oneStepRunsOneStatementAtATime() throws MyException{
        Controller controller = newController(true);
        controller.loadProgram(compound(print(1), print(2)));

        controller.oneStep();
        assertEquals("{}", currentProgramState().getOutList().toString());
        controller.oneStep();
        assertEquals("{1}", currentProgramState().getOutList().toString());
        controller.oneStep();
        assertEquals("{1; 2}", currentProgramState().getOutList().toString());

        ExecStackException e = assertThrows(ExecStackException.class, controller::oneStep);
        assertEquals("Empty Execution Stack", e.getMessage());
        assertTrue(displayed.isEmpty());
    }

    @Test
    void allStepsRunsProgramToCompletion() throws MyException{
        // int v; v = 2; print(v)
        Controller controller = newController(false);
        controller.loadProgram(compound(
                new VariableDeclarationStatement(new IntType(), "v"),
                new AssignmentStatement("v", constant(2)),
                new PrintStatement(new VariableExpression("v"))));

        controller.allSteps();

        ProgramState programState = currentProgramState();
        assertTrue(programState.getExecStack().isEmpty());
        assertEquals(new IntValue(2), programState.getSymTable().get("v"));
        assertEquals("{2}", programState.getOutList().toString());
    }

    @Test
    void allStepsStopsAtFirstErrorAndKeepsEarlierOutput() throws MyException{
        Controller controller = newController(false);
        controller.loadProgram(compound(print(1),
                new PrintStatement(new ArithmeticExpression(ArithmeticOperation.DIV, constant(1), constant(0))),
                print(3)));

        assertThrows(ArithmeticException.class, controller::allSteps);

        assertEquals("{1}", currentProgramState().getOutList().toString());
        assertEquals("{print(3)}", currentProgramState().getExecStack().toString());
    }

    @Test
    void allStepsDisplaysInitialProgramStateAndEveryStep() throws MyException{
        Controller controller = newController(true);
        controller.loadProgram(compound(print(1), print(2)));

        controller.allSteps();

        assertEquals(List.of(
                "ExecStack: {(print(1); print(2))}\nSymTable: {}\nOutList: {}",
                "ExecStack: {print(1)| print(2)}\nSymTable: {}\nOutList: {}",
                "ExecStack: {print(2)}\nSymTable: {}\nOutList: {1}",
                "ExecStack: {}\nSymTable: {}\nOutList: {1; 2}"
        ), displayed);
    }

    @Test
    void displayFlagControlsDisplay() throws MyException{
        Controller controller = newController(false);
        controller.loadProgram(print(1));
        controller.allSteps();
        assertTrue(displayed.isEmpty());

        controller.setDisplayFlag(true);
        controller.loadProgram(print(2));
        controller.allSteps();
        assertEquals(2, displayed.size());
    }
}
