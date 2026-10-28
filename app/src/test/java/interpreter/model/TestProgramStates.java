package interpreter.model;

import interpreter.model.adt.dictionary.MyDictionary;
import interpreter.model.adt.list.MyList;
import interpreter.model.adt.stack.MyStack;
import interpreter.model.statement.NoOperationStatement;

public final class TestProgramStates{
    private TestProgramStates(){
    }

    public static ProgramState emptyProgramState(){
        ProgramState programState = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(),
                new NoOperationStatement());
        programState.getExecStack().clear();
        return programState;
    }
}
