package interpreter.repository;

import interpreter.model.ProgramState;

public interface IRepository{
    ProgramState getCurrentProgramState() throws Exception;
}
