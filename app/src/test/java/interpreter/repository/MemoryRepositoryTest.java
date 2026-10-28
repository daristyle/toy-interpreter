package interpreter.repository;

import interpreter.exceptions.RepositoryException;
import interpreter.model.ProgramState;
import interpreter.model.TestProgramStates;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryRepositoryTest{
    private final ProgramState first = TestProgramStates.emptyProgramState();
    private final ProgramState second = TestProgramStates.emptyProgramState();

    @Test
    void returnsTheOnlyProgramState() throws RepositoryException{
        IRepository repository = new MemoryRepository(List.of(first));
        assertSame(first, repository.getCurrentProgramState());
    }

    @Test
    void returnsFirstProgramStateWhenThereAreSeveral() throws RepositoryException{
        IRepository repository = new MemoryRepository(List.of(first, second));
        assertSame(first, repository.getCurrentProgramState());
    }

    @Test
    void returnsSameProgramStateOnEveryCall() throws RepositoryException{
        IRepository repository = new MemoryRepository(List.of(first));
        assertSame(repository.getCurrentProgramState(), repository.getCurrentProgramState());
    }

    @Test
    void changesToCurrentProgramStateAreKept() throws RepositoryException{
        IRepository repository = new MemoryRepository(List.of(first));
        repository.getCurrentProgramState().getSymTable().put("x", new IntValue(5));
        assertEquals(new IntValue(5), repository.getCurrentProgramState().getSymTable().get("x"));
    }

    @Test
    void emptyRepositoryThrows(){
        IRepository repository = new MemoryRepository(List.of());
        RepositoryException e = assertThrows(RepositoryException.class, repository::getCurrentProgramState);
        assertEquals("No program states were found", e.getMessage());
    }

    @Test
    void constructorCopiesTheGivenList() throws RepositoryException{
        List<ProgramState> programStates = new ArrayList<>(List.of(first));
        IRepository repository = new MemoryRepository(programStates);

        programStates.addFirst(second);
        assertSame(first, repository.getCurrentProgramState());

        programStates.clear();
        assertSame(first, repository.getCurrentProgramState());
    }

    @Test
    void addProgramStateToEmptyRepositoryMakesItCurrent() throws RepositoryException{
        IRepository repository = new MemoryRepository(List.of());
        repository.addProgramState(first);
        assertSame(first, repository.getCurrentProgramState());
    }

    @Test
    void addProgramStateKeepsFirstAsCurrent() throws RepositoryException{
        IRepository repository = new MemoryRepository(List.of(first));
        repository.addProgramState(second);
        assertSame(first, repository.getCurrentProgramState());
    }

    @Test
    void addProgramStateWorksWhenBuiltFromImmutableList() throws RepositoryException{
        IRepository repository = new MemoryRepository(List.of());
        assertDoesNotThrow(() -> repository.addProgramState(first));
        assertSame(first, repository.getCurrentProgramState());
    }

    @Test
    void setProgramStateReplacesExistingProgramStates() throws RepositoryException{
        IRepository repository = new MemoryRepository(List.of(first));
        repository.setProgramStates(List.of(second));
        assertSame(second, repository.getCurrentProgramState());
    }

    @Test
    void setProgramStateWithSeveralMakesFirstCurrent() throws RepositoryException{
        IRepository repository = new MemoryRepository(List.of());
        repository.setProgramStates(List.of(second, first));
        assertSame(second, repository.getCurrentProgramState());
    }

    @Test
    void setProgramStateWithEmptyListEmptiesRepository(){
        IRepository repository = new MemoryRepository(List.of(first));
        repository.setProgramStates(List.of());
        assertThrows(RepositoryException.class, repository::getCurrentProgramState);
    }

    @Test
    void setProgramStateCopiesTheGivenList() throws RepositoryException{
        IRepository repository = new MemoryRepository(List.of());
        List<ProgramState> programStates = new ArrayList<>(List.of(first));
        repository.setProgramStates(programStates);

        programStates.clear();
        assertSame(first, repository.getCurrentProgramState());
    }

    @Test
    void addProgramStateAfterSetProgramStateKeepsFirstAsCurrent() throws RepositoryException{
        IRepository repository = new MemoryRepository(List.of());
        repository.setProgramStates(List.of(first));
        repository.addProgramState(second);
        assertSame(first, repository.getCurrentProgramState());
    }
}
