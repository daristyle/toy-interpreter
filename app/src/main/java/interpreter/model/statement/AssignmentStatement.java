package interpreter.model.statement;

import interpreter.exceptions.AssignmentStatementException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.UndefinedVariableException;
import interpreter.model.ProgramState;
import interpreter.model.expression.IExpression;
import interpreter.model.value.IValue;

public record AssignmentStatement(String name, IExpression expression) implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        var symTable = state.getSymTable();
        if(!symTable.containsKey(name)) throw new UndefinedVariableException("Variable " + name + " was not defined");

        IValue value = expression.evaluate(symTable);
        if(!value.getType().equals(symTable.get(name).getType()))
            throw new AssignmentStatementException("Declared type of variable " + name + " and type of the assigned expression do not match");

        symTable.put(name,value);
        return state;
    }

    @Override
    public String toString() {
        return name + " = " + expression;
    }
}
