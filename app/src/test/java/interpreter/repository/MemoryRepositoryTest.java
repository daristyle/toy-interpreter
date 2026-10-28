package interpreter.repository;

import interpreter.model.ProgramState;
import interpreter.model.TestProgramStates;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryRepositoryTest{
    @Test
    void returnsTheOnlyProgramState(){
        ProgramState programState = TestProgramStates.emptyProgramState();
        IRepository repository = new MemoryRepository(List.of(programState));
        assertSame(programState, repository.getCurrentProgramState());
    }

    @Test
    void returnsFirstProgramStateWhenThereAreSeveral(){
        ProgramState first = TestProgramStates.emptyProgramState();
        ProgramState second = TestProgramStates.emptyProgramState();
        IRepository repository = new MemoryRepository(List.of(first, second));
        assertSame(first, repository.getCurrentProgramState());
    }

    @Test
    void returnsSameProgramStateOnEveryCall(){
        IRepository repository = new MemoryRepository(List.of(TestProgramStates.emptyProgramState()));
        assertSame(repository.getCurrentProgramState(), repository.getCurrentProgramState());
    }

    @Test
    void changesToCurrentProgramStateAreKept(){
        IRepository repository = new MemoryRepository(List.of(TestProgramStates.emptyProgramState()));
        repository.getCurrentProgramState().getSymTable().put("x", new IntValue(5));
        assertEquals(new IntValue(5), repository.getCurrentProgramState().getSymTable().get("x"));
    }

    @Test
    void worksWithMutableList(){
        ProgramState programState = TestProgramStates.emptyProgramState();
        List<ProgramState> programStates = new ArrayList<>();
        programStates.add(programState);
        IRepository repository = new MemoryRepository(programStates);
        assertSame(programState, repository.getCurrentProgramState());
    }

    @Test
    void emptyRepositoryThrows(){
        IRepository repository = new MemoryRepository(new ArrayList<>());
        assertThrows(IndexOutOfBoundsException.class, repository::getCurrentProgramState);
    }
}
