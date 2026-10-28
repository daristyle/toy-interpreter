package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;

public record NoOperationStatement() implements IStatement{
    @Override
    public ProgramState execute(ProgramState state){
        return state;
    }

    @Override
    public String toString(){
        return " ";
    }
}
