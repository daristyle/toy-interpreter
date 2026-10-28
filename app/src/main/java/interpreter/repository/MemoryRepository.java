package interpreter.repository;

import interpreter.exceptions.RepositoryException;
import interpreter.model.ProgramState;

import java.util.ArrayList;
import java.util.List;

public class MemoryRepository implements IRepository{
    private final List<ProgramState> programs;

    public MemoryRepository(List<ProgramState> programStates){
        programs = new ArrayList<>(programStates);
    }

    @Override
    public ProgramState getCurrentProgramState() throws RepositoryException{
        if(programs.isEmpty()) throw new RepositoryException("No program states were found");
        return programs.get(0);
    }

    @Override
    public void addProgramState(ProgramState programState){
        programs.add(programState);
    }

    @Override
    public void setProgramStates(List<ProgramState> programStates){
        programs.clear();
        programs.addAll(programStates);
    }
}
