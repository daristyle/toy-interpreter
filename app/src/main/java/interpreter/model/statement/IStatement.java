package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.model.ProgramState;

public interface IStatement{
    ProgramState execute(ProgramState state) throws MyException;
}
