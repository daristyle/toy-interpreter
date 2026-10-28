package interpreter.repository;

import interpreter.model.ProgramState;

import java.util.ArrayList;
import java.util.List;

public class MemoryRepository implements IRepository{
    private final List<ProgramState> programStates;

    public MemoryRepository(List<ProgramState> programStates){
        this.programStates = programStates;
    }

    @Override
    public ProgramState getCurrentProgramState() throws IndexOutOfBoundsException{
        return programStates.get(0);
    }
}
