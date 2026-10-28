package interpreter.controller;

import interpreter.exceptions.ExecStackException;
import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;
import interpreter.model.adt.dictionary.MyDictionary;
import interpreter.model.adt.list.MyList;
import interpreter.model.adt.stack.MyStack;
import interpreter.model.statement.IStatement;
import interpreter.repository.IRepository;

import java.util.List;
import java.util.function.Consumer;

public class Controller implements IController{
    private final IRepository repo;
    private final Consumer<ProgramState> displayer;
    private boolean displayFlag;

    private void oneStep(ProgramState program) throws MyException{
        var execStack = program.getExecStack();
        if(execStack.isEmpty()) throw new ExecStackException("Empty Execution Stack");
        execStack.pop().execute(program);
    }

    private void display(ProgramState program){
        if(displayFlag){
            displayer.accept(program);
        }
    }

    public Controller(IRepository repo, boolean displayFlag, Consumer<ProgramState> displayer){
        this.repo = repo;
        this.displayFlag = displayFlag;
        this.displayer = displayer;
    }

    public void setDisplayFlag(boolean displayFlag){
        this.displayFlag = displayFlag;
    }

    @Override
    public void oneStep() throws MyException{
        oneStep(repo.getCurrentProgramState());
    }

    @Override
    public void allSteps() throws MyException{
        var program = repo.getCurrentProgramState();
        var execStack = program.getExecStack();
        display(program);
        while(!execStack.isEmpty()){
            execStack.pop().execute(program);
            display(program);
        }
    }

    @Override
    public void loadProgram(IStatement program){
        repo.setProgramStates(List.of(new ProgramState(
                new MyStack<>(), new MyDictionary<>(), new MyList<>(), program)));
    }
}
