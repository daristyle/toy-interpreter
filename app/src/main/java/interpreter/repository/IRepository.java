package interpreter.repository;

import interpreter.exceptions.RepositoryException;
import interpreter.model.ProgramState;

import java.util.List;

public interface IRepository{
    ProgramState getCurrentProgramState() throws RepositoryException;
    void addProgramState(ProgramState programState);
    void setProgramStates(List<ProgramState> programStates);
}
