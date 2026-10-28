package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;

public record CompoundStatement(IStatement first, IStatement second) implements IStatement{
    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        state.getExecStack().push(second);
        state.getExecStack().push(first);
        return state;
    }

    @Override
    public String toString(){
        return "(" + first + "; " + second + ")";
    }
}
