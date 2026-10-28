package interpreter.model.statement;

import interpreter.exceptions.MyException;
import interpreter.exceptions.VariableAlreadyDeclaredException;
import interpreter.model.ProgramState;
import interpreter.model.type.IType;

public record VariableDeclarationStatement(IType type, String name) implements IStatement{
    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        var symTable = state.getSymTable();
        if(symTable.containsKey(name)) throw new VariableAlreadyDeclaredException("Variable " + name + " was already declared");

        symTable.put(name, type.defaultValue());
        return state;
    }

    @Override
    public String toString(){
        return type + " " + name;
    }
}
